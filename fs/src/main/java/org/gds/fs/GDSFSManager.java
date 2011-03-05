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

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public abstract class GDSFSManager
{
   protected File monitoredFile;
   private File indexFileDirectory;
   private File sysDirectory;

   protected GDSFSManager(final File monitoredFile)
   {
      if (monitoredFile == null)
      {
         throw new IllegalArgumentException("monitoredFile is null");
      }
      this.monitoredFile = monitoredFile;
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
   }

   public void indexFile(File file)
   {
      try
      {
         new File(indexFileDirectory, file.getPath()).createNewFile();
      }
      catch (IOException e)
      {
         e.printStackTrace();  // TODO : manage
      }
   }

   abstract protected File getSysDirectory();
}
