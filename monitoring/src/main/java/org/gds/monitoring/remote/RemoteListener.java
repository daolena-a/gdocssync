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

import org.gds.fs.GDSDir;
import org.gds.fs.GDSFSManager;
import org.gds.fs.GDSFile;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class RemoteListener implements ServerListener
{
   private GDSFSManager fsManager;

   public RemoteListener(final GDSFSManager fsManager)
   {
      if (fsManager == null)
      {
         throw new IllegalArgumentException("fsManager is null");
      }
      this.fsManager = fsManager;
   }

   public void onFileSync(final ServerEvent se)
   {
      GDSFile gdsFile = new GDSFile();
      gdsFile.setDocId(se.getFileId());
      gdsFile.setEtag(se.getEtag());
      gdsFile.setTitle(se.getTitle());
      fsManager.updateFileIndex(gdsFile);
   }

   public void onDirectorySync(final ServerEvent se)
   {
      GDSDir gdsDir = new GDSDir();
      gdsDir.setDocId(se.getFileId());
      gdsDir.setEtag(se.getEtag());
      gdsDir.setTitle(se.getTitle());
      gdsDir.setParent(se.getParents());
      fsManager.updateDirIndex(gdsDir);
   }
}
