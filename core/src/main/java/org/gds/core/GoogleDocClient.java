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

import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GoogleDocClient
{
   public DocumentListFeed getDocuments()
   {
      try
      {
         DocsService client = new DocsService("yourCo-yourAppName-v1");
         client.setUserCredentials("***", "***");
         URL feedUri = new URL("https://docs.google.com/feeds/documents/private/full/-/MyFolder");
         return (DocumentListFeed) client.getFeed(feedUri, DocumentListFeed.class);
      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
      }
      catch (AuthenticationException e)
      {
         e.printStackTrace();
      }
      catch (ServiceException e)
      {
         e.printStackTrace();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return null;
   }
}
