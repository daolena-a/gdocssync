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

import org.gds.fs.mapping.FlatMapping;

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
   private IndexCache cache;

   protected GDSFSManager(final File monitoredFile)
   {
      if (monitoredFile == null)
      {
         throw new IllegalArgumentException("monitoredFile is null");
      }
      this.monitoredFile = monitoredFile;
      this.mapping = new FlatMapping();
      this.cache = new IndexCache();
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
      indexFileDirectory = new File(getSysDirectory(), "files");
      if (!indexFileDirectory.exists())
      {
         indexFileDirectory.mkdir();
      }

      //
      indexDirDirectory = new File(getSysDirectory(), "folders");
      if (!indexDirDirectory.exists())
      {
         indexDirDirectory.mkdir();
      }

      //
      indexPathDirectory = new File(getSysDirectory(), "paths");
      if (!indexPathDirectory.exists())
      {
         indexPathDirectory.mkdir();
      }

      //
      cache.init(getSysDirectory(), mapping);
   }

   public void updateFileIndex(GDSFile file)
   {
      File indexedFile = new File(indexFileDirectory, file.getDocId());
      GDSFile gdsFile  = cache.getFile(file.getDocId());
      String eTag = (gdsFile == null ? "" : gdsFile.getEtag());
      if (!eTag.equals(file.getEtag()))
      {
         write(indexedFile, mapping.toString(file));
         cache.addFile(file);
      }
   }

   public void updateDirIndex(GDSDir dir)
   {
      File indexedFile = new File(indexDirDirectory, dir.getDocId());
      GDSDir gdsDir = cache.getFolder(dir.getDocId());
      String eTag = (gdsDir == null ? "" : gdsDir.getEtag());
      if (!eTag.equals(dir.getEtag()))
      {
         write(indexedFile, mapping.toString(dir));
         cache.addFolder(dir);
      }
   }

   public void resetPathDirIndex()
   {
      for (String dirName : cache.getDirectoriesName())
      {
         GDSDir dir = cache.getFolder(dirName);
         GDSPath path = new GDSPath();
         path.setDocId(dir.getDocId());
         path.setPaths(getPathDir(dir));

         File indexedPath = new File(indexPathDirectory, path.getDocId());
         write(indexedPath, mapping.toString(path));

         cache.addPath(path);
      }
   }

   public void resetPathFileIndex()
   {
      for (String fileName : cache.getFilesName())
      {
         GDSFile file = cache.getFile(fileName);
         GDSPath path = new GDSPath();
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
               for (String parentPath : cache.getPath(parentId).getPaths())
               {
                  paths.add(parentPath + "/" + file.getTitle());
               }
            }
         }
         path.setPaths(paths);

         File indexedPath = new File(indexPathDirectory, path.getDocId());
         write(indexedPath, mapping.toString(path));

         cache.addPath(path);
      }
   }

   public void deleteFileIndex(String id)
   {
      File indexedFile = new File(indexFileDirectory, id);
      indexedFile.delete();
      cache.removeFile(id);
      cache.removePath(id);
   }

   public void deleteDirIndex(String id)
   {
      File indexedDir = new File(indexDirDirectory, id);
      indexedDir.delete();
      cache.removeFolder(id);
      cache.removePath(id);
   }

   public Set<String> getFilesName()
   {
      return cache.getFilesName();
   }

   public Set<String> getDirectoriesName()
   {
      return cache.getDirectoriesName();
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
            for (String parentPath : getPathDir(cache.getFolder(parentId)))
            {
               paths.add(parentPath + "/" + dir.getTitle());
            }
         }
      }
      return paths;
   }

   abstract protected File getSysDirectory();
}
