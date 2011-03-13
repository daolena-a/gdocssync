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

package org.gds.monitoring.local;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class LocalListener implements JNotifyListener
{

   public static final int mask =
         JNotify.FILE_CREATED |
         JNotify.FILE_DELETED |
         JNotify.FILE_MODIFIED |
         JNotify.FILE_RENAMED;

   public void fileCreated(final int i, final String s, final String s1)
   {
      System.out.println("created : " + s + " " + s1);
   }

   public void fileDeleted(final int i, final String s, final String s1)
   {
      System.out.println("removed : " + s + " " + s1);
   }

   public void fileModified(final int i, final String s, final String s1)
   {
      System.out.println("updated : " + s + " " + s1);
   }

   public void fileRenamed(final int i, final String s, final String s1, final String s2)
   {
      System.out.println("renamed : " + s + " " + s1);
   }
}
