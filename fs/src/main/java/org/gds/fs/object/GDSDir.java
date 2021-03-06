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

package org.gds.fs.object;

import org.gds.fs.mapping.annotations.Flat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GDSDir extends GDSObject
{
   @Flat
   private String etag;

   @Flat
   private String title;

   @Flat
   private List<String> parents;

   public GDSDir()
   {
      parents = new ArrayList<String>();
   }

   public String getEtag()
   {
      return etag;
   }

   public void setEtag(final String etag)
   {
      this.etag = etag;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(final String title)
   {
      this.title = title;
   }

   public List<String> getParents()
   {
      return parents;
   }

   public void setParents(final List<String> parent)
   {
      this.parents = parent;
   }
}
