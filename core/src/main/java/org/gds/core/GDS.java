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

package org.gds.core;

import net.contentobjects.jnotify.JNotify;
import org.gds.fs.GDSFSManager;
import org.gds.fs.unix.GDSFSUnixManager;
import org.gds.gui.GDSTray;
import org.gds.monitoring.index.IndexListener;
import org.gds.monitoring.local.LocalListener;
import org.gds.monitoring.remote.GoogleDocClient;
import org.gds.monitoring.remote.listener.RemoteListener;
import org.gds.monitoring.remote.RemoteMonitoringService;

import java.io.File;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GDS
{
   private File monitoredFile;
   private GDSFSManager fsManager;
   private GDSTray tray;
   private GoogleDocClient googleDocClient;
   private RemoteMonitoringService remoteMonitor;

   private GDS(String[] argv) // TODO : clean this exception
   {
      // TODO : clean this config
      monitoredFile = new File(argv[0]);
      googleDocClient = new GoogleDocClient(argv[1], argv[2]);
      fsManager = new GDSFSUnixManager(monitoredFile);
      tray = new GDSTray();
      remoteMonitor = new RemoteMonitoringService(googleDocClient);
   }

   public void start()
   {
      fsManager.init();
      startTray();
      startLocalMonitoring(monitoredFile);
      startRemoteMonitoring();
   }

   private void startTray()
   {
      tray.show();
   }

   private void startLocalMonitoring(File monitoredFile)
   {

      try
      {
         JNotify.addWatch(monitoredFile.getAbsolutePath(), LocalListener.mask, false, new LocalListener(googleDocClient));
       //  JNotify.addWatch(fsManager.getSysDirectory().getAbsolutePath(), LocalListener.mask, true, new IndexListener());

      }
      catch (Throwable e)
      {
         e.printStackTrace();  // TODO : manage
      }
   }
   private void startRemoteMonitoring()
   {
      remoteMonitor.addListener(new RemoteListener(fsManager));
      remoteMonitor.start();
   }

   public static void main(String[] argv) throws InterruptedException
   {
      new GDS(argv).start();
   }
}
