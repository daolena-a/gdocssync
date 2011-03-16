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

import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.*;
import com.google.gdata.data.media.MediaByteArraySource;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GoogleDocClient
{
   private static String username;
   private static String password;
   private static DocsService client = new DocsService("gds");

   public GoogleDocClient(String username, String password)
   {
      this.username = username;
      this.password = password;
      
      try
      {
         client.setUserCredentials(username, password);
      }
      catch (AuthenticationException e)
      {
         e.printStackTrace();  // TODO : manage
      }
   }

   public DocumentListFeed getFiles()
   {
      return fetchFeed(Urls.ALL_FILES);
   }

   public DocumentListFeed getFolders()
   {
      return fetchFeed(Urls.ALL_FOLDERS);
   }

   private <T> T fetchFeed(Urls urls)
   {
      try
      {
         Urls.UrlData data = urls.get();
         return (T) client.getFeed(data.getUrl(), data.getClazz());
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
    /*
            INSERT DATA
     */
    public DocumentListEntry createNewDocument(String title, String type)
    throws IOException, ServiceException {
          DocumentListEntry newEntry = null;
          if (type.equals("document")) {
            newEntry = new DocumentEntry();
          } else if (type.equals("presentation")) {
            newEntry = new PresentationEntry();
          } else if (type.equals("spreadsheet")) {
            newEntry = new SpreadsheetEntry();
          }
          newEntry.setTitle(new PlainTextConstruct(title));
          return client.insert(new URL("https://docs.google.com/feeds/default/private/full/"), newEntry);

   }
    public DocumentListEntry uploadFile(String filepath, String title)
    throws IOException, ServiceException  {
          File file = new File(filepath);
          String mimeType = DocumentListEntry.MediaType.fromFileName(file.getName()).getMimeType();

          DocumentListEntry newDocument = new DocumentListEntry();
          newDocument.setFile(file, mimeType);
          newDocument.setTitle(new PlainTextConstruct(title));

          // Prevent collaborators from sharing the document with others?
          // newDocument.setWritersCanInvite(false);
          // client.
          return client.insert(new URL("https://docs.google.com/feeds/default/private/full/"), newDocument);
    }
    public DocumentListEntry updateFile(String filepath, String title) throws IOException, ServiceException  {
         DocumentListFeed dlf =  getFiles();
         DocumentListEntry entry = getFileFromServerFromTitle(title); if (entry== null){
             throw new ServiceException("File not found on gdoc");
         }

        System.out.println("paf"+filepath);

        File file = new File(filepath);
        Scanner sc = new Scanner(file);
        System.out.println(sc.next());
          String mimeType = DocumentListEntry.MediaType.fromFileName(file.getName()).getMimeType();
        System.out.println("mime type "+filepath);


       // entry.setFile(file, "text/plain");
        entry.setMediaSource(new MediaByteArraySource(getTextByte(file), "text/plain"));
        return  entry.updateMedia(true);
        //entry.update();
        //return client.update(new URL(entry.getEditLink().getHref()), entry, entry.getEtag());
          //return client.update(new URL(entry.getEditLink().getHref()), entry, entry.getEtag());
    }

    
    private DocumentListEntry getFileFromServerFromTitle(String title){
        DocumentListFeed dlf =  getFiles();
        for (DocumentListEntry dle : dlf.getEntries()){
            System.out.println("test "+dle.getTitle().getPlainText()+" = "+ title);
             if (dle.getTitle().getPlainText().equals(title)){
                return dle;
             }
         }
        return null;
    }

    private byte[] getTextByte (File f){
        Scanner sc = null;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder res = new StringBuilder("");
        while (sc.hasNext()){
               res.append(sc.next()); 
        }
        return res.toString().getBytes();

    }
}
