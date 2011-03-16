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

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.PresentationEntry;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;
import org.gds.monitoring.remote.GoogleDocClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class LocalListener implements JNotifyListener
{
   private GoogleDocClient googleDocClient;
   public LocalListener(GoogleDocClient gDocClient){
       googleDocClient = gDocClient;

   }
   public static final int mask =
         JNotify.FILE_CREATED |
         JNotify.FILE_DELETED |
         JNotify.FILE_MODIFIED |
         JNotify.FILE_RENAMED;
/*
DocumentListEntry createdEntry = null;

// Create an empty word processor document
createdEntry = createNewDocument("NewDocTitle", "document");
System.out.println("Document now online @ :" + createdEntry.getDocumentLink().getHref());

// Create an empty presentation
createdEntry = createNewDocument("NewPresentationTitle", "presentation");
System.out.println("Presentation now online @ :" + createdEntry.getDocumentLink().getHref());

// Create an empty spreadsheet
createdEntry = createNewDocument("NewSpreadsheetTitle", "spreadsheet");
System.out.println("Spreadsheet now online @ :" + createdEntry.getDocumentLink().getHref());

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

  // Prevent collaborators from sharing the document with others?
  // newEntry.setWritersCanInvite(false);

  // You can also hide the document on creation
  // newEntry.setHidden(true);

  return client.insert(new URL("https://docs.google.com/feeds/default/private/full/"), newEntry);

 */
   public void fileCreated(final int i, final String s, final String s1)
   {
        System.out.println("created : " + s + " " + s1);
        DocumentListEntry createdEntry = null;

        // Create an empty word processor document
        try {
            //createdEntry.setFile(new File("/home/adrien/test.txt"),"text/plain");
            if (googleDocClient ==null){
                System.out.println("null");
            }
            createdEntry = googleDocClient.uploadFile(s+File.separator+s1, s1);
            System.out.println("Document now online @ :" + createdEntry.getDocumentLink().getHref());

            // Create an empty presentation
          //  createdEntry = createNewDocument("NewPresentationTitle", "presentation");
           // System.out.println("Presentation now online @ :" + createdEntry.getDocumentLink().getHref());

            // Create an empty spreadsheet
           // createdEntry = createNewDocument("NewSpreadsheetTitle", "spreadsheet");
           // System.out.println("Spreadsheet now online @ :" + createdEntry.getDocumentLink().getHref());
       } catch (IOException e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       } catch (ServiceException e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       }
   }



   public void fileDeleted(final int i, final String s, final String s1)
   {
      System.out.println("removed : " + s + " " + s1);
   }

   public void fileModified(final int i, final String s, final String s1)
   {
       System.out.println("Modified : " + s + " " + s1);
       System.out.println("updating ");
       DocumentListEntry uploadedEntry = null;
       try {
           uploadedEntry = googleDocClient.updateFile(s+File.separator+s1,s1);
       } catch (IOException e) {
           e.printStackTrace();
       } catch (ServiceException e) {
           e.printStackTrace(); 
       }
       System.out.println("Document now online and updated @ :" + uploadedEntry.getDocumentLink().getHref());


   }

   public void fileRenamed(final int i, final String s, final String s1, final String s2)
   {
      System.out.println("renamed : " + s + " " + s1);
   }
}
