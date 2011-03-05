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

import com.google.gdata.data.docs.DocumentListEntry;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class RemoteWatcherService extends Thread
{
   private GoogleDocClient googleDocClient;
   private int delay;

   public RemoteWatcherService(final GoogleDocClient googleDocClient)
   {
      this(googleDocClient, 10);
   }

   public RemoteWatcherService(final GoogleDocClient googleDocClient, final int delay)
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
         for (DocumentListEntry documentListEntry : googleDocClient.getFiles().getEntries())
         {
            System.out.println("Title " + documentListEntry.getTitle().getPlainText());
            System.out.println("Id " + documentListEntry.getId());
            System.out.println("tag " + documentListEntry.getEtag());
            System.out.println();
         }
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
}
