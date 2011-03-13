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

package org.gds.fs.listener;

import org.gds.fs.GDSFSManager;
import org.gds.fs.event.IndexEvent;
import org.gds.fs.event.IndexListener;
import org.gds.fs.object.GDSObjectType;
import org.gds.fs.object.GDSPath;

import java.io.File;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FSIndexListener implements IndexListener
{
   private GDSFSManager fsManager;

   public FSIndexListener(final GDSFSManager fsManager)
   {
      if (fsManager == null)
      {
         throw new IllegalArgumentException("fsManager is null");
      }
      this.fsManager = fsManager;
   }

   public void onCreateFile(final IndexEvent ie)
   {
      System.out.println("CREATE file " + ie.getGdsObject().getDocId());
   }

   public void onCreateFolder(final IndexEvent ie)
   {
      System.out.println("CREATE folder " + ie.getGdsObject().getDocId());
   }

   public void onCreatePath(final IndexEvent ie)
   {
      // TODO : avoid cast
      GDSPath path = (GDSPath) ie.getGdsObject();
      
      for (String currentPath : ie.getIndex().getPath(path.getDocId()).getPaths())
      {
         if (GDSObjectType.FOLDER.equals(path.getType()))
         {
            fsManager.writeDataFolder(currentPath);
         }
      }
   }

   public void onUpdateFile(final IndexEvent ie)
   {
      System.out.println("UPDATE file " + ie.getGdsObject().getDocId());
   }

   public void onUpdateFolder(final IndexEvent ie)
   {
      System.out.println("UPDATE folder " + ie.getGdsObject().getDocId());
   }

   public void onUpdatePath(final IndexEvent ie)
   {
      System.out.println("UPDATE path " + ie.getGdsObject().getDocId());
   }

   public void onDeleteFile(final IndexEvent ie)
   {
      System.out.println("DELETE file " + ie.getGdsObject().getDocId());
   }

   public void onDeleteFolder(final IndexEvent ie)
   {
      System.out.println("DELETE folder " + ie.getGdsObject().getDocId());
   }

   public void onDeletePath(final IndexEvent ie)
   {
      System.out.println("DELETE path " + ie.getGdsObject().getDocId());
   }
}
