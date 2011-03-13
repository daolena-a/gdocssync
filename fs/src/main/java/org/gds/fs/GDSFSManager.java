/*
* Copyright (C) 2003-2009 eXo Platform SAS.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.gds.fs;

import org.gds.fs.listener.FSIndexListener;
import org.gds.fs.mapping.FlatMapping;
import org.gds.fs.object.GDSDir;
import org.gds.fs.object.GDSFile;
import org.gds.fs.object.GDSObjectType;
import org.gds.fs.object.GDSPath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public abstract class GDSFSManager
{
   protected File monitoredFile;
   private File indexFileDirectory;
   private File indexDirDirectory;
   private File indexPathDirectory;
   private File sysDirectory;
   private FlatMapping mapping;
   private Index index;

   protected GDSFSManager(final File monitoredFile)
   {
      if (monitoredFile == null)
      {
         throw new IllegalArgumentException("monitoredFile is null");
      }
      this.monitoredFile = monitoredFile;
      this.mapping = new FlatMapping();
      this.index = new Index();
      index.addListener(new FSIndexListener(this));
   }

   public void init()
   {
      //
      sysDirectory = getSysDirectory();
      if (!sysDirectory.exists())
      {
         sysDirectory.mkdir();
      }

      //
      indexFileDirectory = new File(sysDirectory, "files");
      if (!indexFileDirectory.exists())
      {
         indexFileDirectory.mkdir();
      }

      //
      indexDirDirectory = new File(sysDirectory, "folders");
      if (!indexDirDirectory.exists())
      {
         indexDirDirectory.mkdir();
      }

      //
      indexPathDirectory = new File(sysDirectory, "paths");
      if (!indexPathDirectory.exists())
      {
         indexPathDirectory.mkdir();
      }

      //
      index.init(getSysDirectory(), mapping);
   }

   /**
    *
    * @param file
    * @return updated
    */
   public boolean updateFileIndex(GDSFile file)
   {
      File indexedFile = new File(indexFileDirectory, file.getDocId());
      GDSFile gdsFile  = index.getFile(file.getDocId());
      String eTag = (gdsFile == null ? "" : gdsFile.getEtag());
      if (!eTag.equals(file.getEtag()))
      {
         write(indexedFile, mapping.toString(file));
         index.addFile(file);
         return true;
      }
      return false;
   }

   /**
    *
    * @param dir
    * @return updated
    */
   public boolean updateDirIndex(GDSDir dir)
   {
      File indexedFile = new File(indexDirDirectory, dir.getDocId());
      GDSDir gdsDir = index.getFolder(dir.getDocId());
      String eTag = (gdsDir == null ? "" : gdsDir.getEtag());
      if (!eTag.equals(dir.getEtag()))
      {
         write(indexedFile, mapping.toString(dir));
         index.addFolder(dir);
         return true;
      }
      return false;
   }

   public void updatePathDirIndex(String dirName)
   {
      GDSDir dir = index.getFolder(dirName);
      GDSPath path = new GDSPath();
      path.setType(GDSObjectType.FOLDER);
      path.setDocId(dir.getDocId());
      path.setPaths(getPathDir(dir));

      File indexedPath = new File(indexPathDirectory, path.getDocId());
      write(indexedPath, mapping.toString(path));

      index.addPath(path);
   }

   public void updatePathFileIndex(String fileName)
   {
      GDSFile file = index.getFile(fileName);
      GDSPath path = new GDSPath();
      path.setType(GDSObjectType.FILE);
      path.setDocId(file.getDocId());
      List<String> paths = new ArrayList<String>();
      if (file.getParents().size() == 0)
      {
         paths.add(file.getTitle());
      }
      else
      {
         for (String parentId : file.getParents())
         {
            for (String parentPath : index.getPath(parentId).getPaths())
            {
               paths.add(parentPath + File.separator + file.getTitle());
            }
         }
      }
      path.setPaths(paths);

      File indexedPath = new File(indexPathDirectory, path.getDocId());
      write(indexedPath, mapping.toString(path));

      index.addPath(path);
   }

   public void deleteFileIndex(String id)
   {
      File indexedFile = new File(indexFileDirectory, id);
      File indexedPath = new File(indexPathDirectory, id);
      indexedFile.delete();
      indexedPath.delete();
      index.removeFile(id);
      index.removePath(id);
   }

   public void deleteDirIndex(String id)
   {
      File indexedDir = new File(indexDirDirectory, id);
      File indexedPath = new File(indexPathDirectory, id);
      indexedDir.delete();
      indexedPath.delete();
      index.removeFolder(id);
      index.removePath(id);
   }

   public void writeDataFolder(String path)
   {
      new File(monitoredFile, path).mkdirs();
   }

   public Set<String> getFilesName()
   {
      return index.getFilesName();
   }

   public Set<String> getDirectoriesName()
   {
      return index.getDirectoriesName();
   }

   public void write(File file, String content)
   {
      try
      {
         FileWriter fw = new FileWriter(file, false);
         fw.append(content);
         fw.flush();
      }
      catch (IOException e)
      {
         e.printStackTrace(); // TODO : manage
      }
   }

   public List<String> getPathDir(GDSDir dir)
   {
      List<String> paths = new ArrayList<String>();
      if (dir.getParents().size() == 0)
      {
         paths.add(dir.getTitle());
      }
      else
      {
         for (String parentId : dir.getParents())
         {
            for (String parentPath : getPathDir(index.getFolder(parentId)))
            {
               paths.add(parentPath + "/" + dir.getTitle());
            }
         }
      }
      return paths;
   }

   abstract protected File getSysDirectory();
}
