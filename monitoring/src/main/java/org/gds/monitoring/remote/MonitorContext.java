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

package org.gds.monitoring.remote;

import javax.swing.plaf.metal.MetalIconFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MonitorContext
{
   private List<String> allFiles;
   private List<String> allFolders;
   private List<String> updatedFiles;
   private List<String> updatedFolders;

   public MonitorContext()
   {
      allFiles = new ArrayList<String>();
      allFolders = new ArrayList<String>();
      updatedFiles = new ArrayList<String>();
      updatedFolders = new ArrayList<String>();
   }

   public void setUpdated(String docId, DocType docType)
   {
      switch(docType)
      {
         case FILE:
            updatedFiles.add(docId);
            break;
         case FOLDER:
            updatedFolders.add(docId);
            break;
      }
   }

   public void add(String docId, DocType docType)
   {

      switch(docType)
      {
         case FILE:
            allFiles.add(docId);
            break;
         case FOLDER:
            allFolders.add(docId);
            break;
      }
   }

   public List<String> getUpdatedFiles()
   {
      return updatedFiles;
   }

   public List<String> getUpdatedFolders()
   {
      return updatedFolders;
   }

   public List<String> getAllFiles()
   {
      return allFiles;
   }

   public List<String> getAllFolders()
   {
      return allFolders;
   }

   public enum DocType
   {
      FILE, FOLDER;
   }
}
