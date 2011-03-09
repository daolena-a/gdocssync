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

import org.gds.fs.mapping.annotations.Flat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GDSDir
{
   private String docId;

   @Flat
   private String etag;

   @Flat
   private String title;

   @Flat
   private List<String> parent;

   public GDSDir()
   {
      parent = new ArrayList<String>();
   }

   public String getDocId()
   {
      return docId;
   }

   public void setDocId(final String docId)
   {
      if (docId == null)
      {
         throw new IllegalArgumentException("docId is null");
      }
      this.docId = docId;
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

   public List<String> getParent()
   {
      return parent;
   }

   public void setParent(final List<String> parent)
   {
      this.parent = parent;
   }

   @Override
   public boolean equals(final Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      GDSDir gdsDir = (GDSDir) o;

      if (docId != null ? !docId.equals(gdsDir.docId) : gdsDir.docId != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      return docId != null ? docId.hashCode() : 0;
   }
}
