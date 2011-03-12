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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class Index
{
   private Map<String, GDSFile> files = new HashMap<String, GDSFile>();
   private Map<String, GDSDir> folders = new HashMap<String, GDSDir>();
   private Map<String, GDSPath> paths = new HashMap<String, GDSPath>();
   private boolean initialized = false;

   public void addFile(GDSFile file)
   {
      files.put(file.getDocId(), file);
   }

   public void addFolder(GDSDir folder)
   {
      folders.put(folder.getDocId(), folder);
   }

   public void addPath(GDSPath path)
   {
      paths.put(path.getDocId(), path);
   }

   public GDSFile getFile(String id)
   {
      return files.get(id);
   }

   public GDSDir getFolder(String id)
   {
      return folders.get(id);
   }

   public GDSPath getPath(String id)
   {
      return paths.get(id);
   }

   public void init(File sysDir, FlatMapping mapping)
   {
      if (initialized)
      {
         throw new RuntimeException("IndexCache is already initialized");
      }

      //
      File indexFiles = new File(sysDir, "files");
      for (File file : indexFiles.listFiles())
      {
         GDSFile f = mapping.toObject(readFile(file), GDSFile.class);
         f.setDocId(file.getName());
         files.put(file.getName(), f);
      }

      //
      File indexFolders = new File(sysDir, "folders");
      for (File folder : indexFolders.listFiles())
      {
         GDSDir f = mapping.toObject(readFile(folder), GDSDir.class);
         f.setDocId(folder.getName());
         folders.put(folder.getName(), f);
      }

      //
      File indexPaths = new File(sysDir, "paths");
      for (File path : indexPaths.listFiles())
      {
         GDSPath p = mapping.toObject(readFile(path), GDSPath.class);
         p.setDocId(path.getName());
         paths.put(path.getName(), p);
      }
      
      initialized = true;
   }

   private String readFile(File file)
   {
      try
      {
         Scanner sc = new Scanner(new FileReader(file));
         StringBuffer sb = new StringBuffer();
         while (sc.hasNextLine())
         {
            sb.append(sc.nextLine() + System.getProperty("line.separator"));
         }
         return sb.toString();
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   public void removeFile(String id)
   {
      files.remove(id);
   }

   public void removeFolder(String id)
   {
      folders.remove(id);
   }

   public void removePath(String id)
   {
      paths.remove(id);
   }

   public Set<String> getFilesName()
   {
      return files.keySet();
   }

   public Set<String> getDirectoriesName()
   {
      return folders.keySet();
   }
}
