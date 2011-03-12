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

import com.google.gdata.data.Link;
import com.google.gdata.data.docs.DocumentListEntry;

import javax.management.monitor.Monitor;
import java.util.*;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class RemoteMonitoringService extends Thread
{
   private GoogleDocClient googleDocClient;
   private int delay;
   private List<ServerListener> listeners;
   private MonitorContext context;

   public RemoteMonitoringService(final GoogleDocClient googleDocClient)
   {
      this(googleDocClient, 10);
      listeners = new LinkedList<ServerListener>();
   }

   public RemoteMonitoringService(final GoogleDocClient googleDocClient, final int delay)
   {
      if (googleDocClient == null)
      {
         throw new IllegalArgumentException("googleDocClient is null");
      }
      
      this.googleDocClient = googleDocClient;
      this.delay = delay;
   }

   @Override
   public void run()
   {
      while(true)
      {
         // TODO : first check the DateTime
         context = new MonitorContext();

         // Doc
         // TODO : get new folders only
         for (DocumentListEntry documentListEntry : googleDocClient.getFolders()
                 .getEntries())
         {
            context.add(documentListEntry.getDocId(), MonitorContext.DocType.FOLDER);
            fireEvent(new ServerEvent(
                 documentListEntry.getDocId(),
                 documentListEntry.getEtag(),
                 documentListEntry.getTitle().getPlainText(),
                 parentId(documentListEntry.getParentLinks()),
                 context
            ),
                 Event.UPDATE_DIRECTORY
            );
         }

         // Files
         // TODO : get new files only
         for (DocumentListEntry documentListEntry : googleDocClient.getFiles().getEntries())
         {
            context.add(documentListEntry.getDocId(), MonitorContext.DocType.FILE);
            fireEvent(new ServerEvent(
                 documentListEntry.getDocId(),
                 documentListEntry.getEtag(),
                 documentListEntry.getTitle().getPlainText(),
                 parentId(documentListEntry.getParentLinks()),
                 context
            ),
                 Event.UPDATE_FILE
            );
         }

         //
         fireEvent(new ServerEvent(
               null,
               null,
               null,
               null,
               context
         ),
               Event.END_UPDATE_DIRECTORY
         );

         //
         fireEvent(new ServerEvent(
               null,
               null,
               null,
               null,
               context
            ),
                  Event.END_UPDATE_FILE
         );
         
         try
         {
            Thread.sleep(delay * 1000);
         }
         catch (InterruptedException e)
         {
            e.printStackTrace(); // TODO : log
         }
      }
   }

   private void fireEvent(ServerEvent se, Event event)
   {
      for(ServerListener serverListener : listeners)
      {
         switch (event)
         {
            case UPDATE_DIRECTORY:
               serverListener.onDirectorySync(se);
               break;
            case UPDATE_FILE:
               serverListener.onFileSync(se);
               break;
            case END_UPDATE_DIRECTORY:
               serverListener.onDirectoryEndUpdate(se);
               break;
            case END_UPDATE_FILE:
               serverListener.onFileEndUpdate(se);
               break;
         }
      }
   }

   private List<String> parentId(List<Link> links)
   {
      List<String> parents = new ArrayList<String>();
      for (Link link : links)
      {
         String httpLink = link.getHref();
         parents.add(httpLink.substring(httpLink.indexOf("%3A")+3));
      }
      return parents;
   }

   public void addListener(ServerListener serverListener)
   {
      listeners.add(serverListener);
   }

   enum Event
   {
      UPDATE_FILE,
      END_UPDATE_FILE,
      UPDATE_DIRECTORY,
      END_UPDATE_DIRECTORY;
   }
}
