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
import java.util.Collection;
import java.util.Scanner;

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
               else if (value instanceof Collection)
               {
                  Collection collection = (Collection) value;
                  if (collection.size() == 0)
                  {
                     continue;
                  }
                  else
                  {
                     for (Object o : collection)
                     {
                        sb
                           .append(field.getName())
                           .append(": ")
                           .append(o)
                           .append(System.getProperty("line.separator"));
                     }
                  }
               }
               else
               {
                  sb
                     .append(field.getName())
                     .append(": ")
                     .append(value)
                     .append(System.getProperty("line.separator"));
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
      try
      {
         T t = clazz.newInstance();
         Scanner sc = new Scanner(src);
         while (sc.hasNextLine())
         {
            String line = sc.nextLine();
            int index = line.indexOf(" ");
            String key = line.substring(0, index - 1);
            String value = line.substring(index + 1);
            Field field = clazz.getDeclaredField(key);
            if (!Collection.class.isAssignableFrom(field.getType()))
            {
               Method method = clazz.getMethod("set" + capitalize(key), field.getType());
               method.invoke(t, value);
            }
            else
            {
               Method getter = clazz.getMethod("get" + capitalize(key));
               Collection collection = (Collection) getter.invoke(t);
               collection.add(value);
            }
         }
         return t;
      }
      catch (InstantiationException e)
      {
         e.printStackTrace(); // TODO : manage
      }
      catch (IllegalAccessException e)
      {
         e.printStackTrace(); // TODO : manage
      }
      catch (NoSuchMethodException e)
      {
         e.printStackTrace(); // TODO : manage
      }
      catch (NoSuchFieldException e)
      {
         e.printStackTrace(); // TODO : manage
      }
      catch (InvocationTargetException e)
      {
         e.printStackTrace(); // TODO : manage
      }
      return null;
   }

   private String capitalize(String src)
   {
      return src.substring(0,1).toUpperCase() + src.substring(1);
   }
}
