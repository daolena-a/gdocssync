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

import com.google.gdata.data.docs.DocumentListFeed;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public enum Urls
{
   ALL_FILES("http://docs.google.com/feeds/default/private/full", DocumentListFeed.class),
   ALL_FOLDERS("http://docs.google.com/feeds/default/private/full/-/folder?showfolders=true", DocumentListFeed.class)
   ;

   private static Map<String, UrlData> urls = new HashMap<String, UrlData>();

   private String url;
   private Class clazz;

   private Urls(String url, Class clazz)
   {
      this.url = url;
      this.clazz = clazz;
   }

   public UrlData get()
   {
      if (!urls.containsKey(url))
      {
         try
         {
            UrlData newData = new UrlData(new URL(url), clazz);
            urls.put(url, newData);
            return newData;
         }
         catch (MalformedURLException e)
         {
            e.printStackTrace(); // TODO : log
         }
      }
      return urls.get(url);
   }

   class UrlData
   {
      private URL url;
      private Class clazz;

      UrlData(final URL url, final Class clazz)
      {
         this.url = url;
         this.clazz = clazz;
      }

      public URL getUrl()
      {
         return url;
      }

      public void setUrl(final URL url)
      {
         this.url = url;
      }

      public Class getClazz()
      {
         return clazz;
      }

      public void setClazz(final Class clazz)
      {
         this.clazz = clazz;
      }
   }
}
