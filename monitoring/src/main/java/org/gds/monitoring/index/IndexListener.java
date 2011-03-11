package org.gds.monitoring.index;

import net.contentobjects.jnotify.JNotifyListener;
import org.gds.fs.GDSDir;
import org.gds.fs.GDSFSManager;
import org.gds.fs.IndexCache;
import org.gds.fs.mapping.FlatMapping;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: adrien
 * Date: 3/10/11
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexListener implements JNotifyListener {
    private IndexCache cache;
    private GDSFSManager fileSystemManager;

    public IndexListener (){
        cache = new IndexCache();
    }
    public IndexListener (GDSFSManager manager){
        this();
        fileSystemManager = manager;
    }
    public void fileCreated(int i, String s, String s1) {
        System.out.println("index created" + s +" "+ s1);
        if (s1.startsWith("folders")){
            FlatMapping mapping = new FlatMapping();
            GDSDir dir = mapping.toObject(s+s1 , GDSDir.class);
            File dir2create  = new File(fileSystemManager.getMonitoredFilePath()+"/"+dir.getDocId());
            System.out.println(dir.getDocId());
            dir2create.mkdir();
            //cache.getFolder(s1);
            //File file = new File(fileSystemManager.getMonitoredFilePath());
        }
        else if (s1.startsWith("files")){
            
        }

    }

    public void fileDeleted(int i, String s, String s1) {
        System.out.println("index deleted"+ s +" "+ s1);    }

    public void fileModified(int i, String s, String s1) {
        System.out.println("index modified"+ s +" "+ s1);
         if (s1.startsWith("folders")){
            //FlatMapping mapping = new FlatMapping();
            //GDSDir dir = mapping.toObject(s+s1 , GDSDir.class);

    //            File dir2create   = new File(fileSystemManager.getMonitoredFilePath()+"/"+dir.getDocId());
             String docId = null;
             if (s1.lastIndexOf("/") == s1.length()){
                 String[] parts = s1.split(File.separator);
                 int length = parts.length;
                 String lastPart = parts[length-2];
                 GDSDir dir = cache.getFolder(lastPart);
                 //find parents
                 
                 File f = new File(s+dir.getTitle());
                 f.mkdir();


             }
             else
             docId = s1.substring(s1.lastIndexOf("/"),s1.length());
             System.out.println("docID "+docId);
            //cache.getFolder(s1);
            //File file = new File(fileSystemManager.getMonitoredFilePath());
        }
    }

    public void fileRenamed(int i, String s, String s1, String s2) {
        System.out.println("index renamed"+ s +" "+ s1);
    }
}
