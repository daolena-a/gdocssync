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

import org.gds.fs.event.IndexEvent;
import org.gds.fs.event.IndexListener;
import org.gds.fs.mapping.FlatMapping;
import org.gds.fs.object.GDSDir;
import org.gds.fs.object.GDSFile;
import org.gds.fs.object.GDSPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class Index
{
   private Map<String, GDSFile> files;
   private Map<String, GDSDir> folders;
   private Map<String, GDSPath> paths;
   private boolean initialized;
   private List<IndexListener> listeners;

   public Index()
   {
      files = new HashMap<String, GDSFile>();
      folders = new HashMap<String, GDSDir>();
      paths = new HashMap<String, GDSPath>();
      initialized = false;
      listeners = new ArrayList<IndexListener>();
   }

   public void addFile(GDSFile file)
   {
      boolean isNew = !files.containsKey(file.getDocId());
      
      files.put(file.getDocId(), file);

      fireEvent(
         new IndexEvent(file, this),
         (isNew ? EventType.ON_CREATE_FILE : EventType.ON_UPDATE_FILE)
      );
   }

   public void addFolder(GDSDir folder)
   {
      boolean isNew = !folders.containsKey(folder.getDocId());

      folders.put(folder.getDocId(), folder);

      fireEvent(
              new IndexEvent(folder, this),
              (isNew ? EventType.ON_CREATE_FOLDER : EventType.ON_UPDATE_FOLDER)
      );
   }

   public void addPath(GDSPath path)
   {
      boolean isNew = !paths.containsKey(path.getDocId()) ;

      paths.put(path.getDocId(), path);

      fireEvent(
              new IndexEvent(path, this),
              (isNew ? EventType.ON_CREATE_PATH : EventType.ON_UPDATE_PATH)
      );
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
         e.printStackTrace(); // TODO : manage
         return null;
      }
   }

   public void removeFile(String id)
   {
      fireEvent(
         new IndexEvent(files.remove(id), this),
         EventType.ON_DELETE_FILE
      );
   }

   public void removeFolder(String id)
   {
      fireEvent(
              new IndexEvent(folders.remove(id), this),
              EventType.ON_DELETE_FOLDER
      );
   }

   public void removePath(String id)
   {
      fireEvent(
              new IndexEvent(paths.remove(id), this),
              EventType.ON_DELETE_PATH
      );
   }

   public Set<String> getFilesName()
   {
      return files.keySet();
   }

   public Set<String> getDirectoriesName()
   {
      return folders.keySet();
   }

   public void addListener(IndexListener el)
   {
      listeners.add(el);
   }

   private void fireEvent(IndexEvent ie, EventType type)
   {
      for (IndexListener listener : listeners)
      {
         switch(type)
         {
            case ON_CREATE_FILE:
               listener.onCreateFile(ie);
               break;
            case ON_CREATE_FOLDER:
               listener.onCreateFolder(ie);
               break;
            case ON_CREATE_PATH:
               listener.onCreatePath(ie);
               break;
            case ON_UPDATE_FILE:
               listener.onUpdateFile(ie);
               break;
            case ON_UPDATE_FOLDER:
               listener.onUpdateFolder(ie);
               break;
            case ON_UPDATE_PATH:
               listener.onUpdatePath(ie);
               break;
            case ON_DELETE_FILE:
               listener.onDeleteFile(ie);
               break;
            case ON_DELETE_FOLDER:
               listener.onDeleteFolder(ie);
               break;
            case ON_DELETE_PATH:
               listener.onDeletePath(ie);
               break;
         }
      }
   }

   enum EventType
   {
      ON_CREATE_FILE,
      ON_CREATE_FOLDER,
      ON_CREATE_PATH,
      ON_UPDATE_FILE,
      ON_UPDATE_FOLDER,
      ON_UPDATE_PATH,
      ON_DELETE_FILE,
      ON_DELETE_FOLDER,
      ON_DELETE_PATH
   }
}
