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

package org.gds.fs.mapping;

import org.gds.fs.mapping.annotations.Flat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FlatMapping
{
   public String toString(Object mapped)
   {
      Class mappedClass = mapped.getClass();
      Field[] fields = mappedClass.getDeclaredFields();
      StringBuffer sb = new StringBuffer();
      for (Field field: fields)
      {
         if (field.isAnnotationPresent(Flat.class))
         {
            try
            {
               Method getter = mappedClass.getMethod("get" + capitalize(field.getName()));
               Object value = getter.invoke(mapped);
               if (value == null)
               {
                  continue;
               }
               else if (value.getClass().isArray())
               {
                  Object[] tvalue = (Object[]) value;
                  if (tvalue.length == 0)
                  {
                     continue;
                  }
                  else
                  {
                     for (Object o : tvalue)
                     {
                        sb
                           .append(field.getName())
                           .append(": ")
                           .append(o)
                           .append("\n");
                     }
                  }
               }
               else
               {
                  sb
                     .append(field.getName())
                     .append(": ")
                     .append(value)
                     .append("\n");
               }
            }
            catch (NoSuchMethodException e)
            {
               // TODO : log
            }
            catch (InvocationTargetException e)
            {
               // TODO : log
            }
            catch (IllegalAccessException e)
            {
               // TODO : log
            }
         }
      }
      return sb.toString();
   }

   public <T> T toObject(String src, Class<T> clazz)
   {
      return null;
   }

   private String capitalize(String src)
   {
      return src.substring(0,1).toUpperCase() + src.substring(1);
   }
}
