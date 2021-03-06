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

package org.gds.monitoring.remote.event;

import org.gds.monitoring.remote.MonitorContext;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ServerEvent
{
   private String fileId;
   private String etag;
   private String title;
   private List<String> parents;
   private MonitorContext context;

   public ServerEvent(final String fileId, final String etag, final String title, final List<String> parents, final MonitorContext context)
   {
      this.fileId = fileId;
      this.etag = etag;
      this.title = title;
      this.parents = parents;
      this.context = context;
   }

   public String getFileId()
   {
      return fileId;
   }

   public String getEtag()
   {
      return etag;
   }

   public String getTitle()
   {
      return title;
   }

   public List<String> getParents()
   {
      return parents;
   }

   public MonitorContext getContext()
   {
      return context;
   }
}
