package gds.test.UnixFileSystem;

import junit.framework.TestCase;
import org.gds.fs.GDSFSManager;
import org.gds.fs.IndexCache;
import org.gds.fs.unix.GDSFSUnixManager;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: adrien
 * Date: 3/11/11
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemTestCase extends TestCase {
    
    @Test
    public void testFileSystemLinux() throws  Throwable{
        File f = new File ("src/test/resource");
        if (f.isDirectory() == false || f.exists() == false){
           throw new FileNotFoundException("no resource folder found or incorrect");
        }
        GDSFSManager filesystem = new GDSFSUnixManager(f);
        filesystem.init();
        File files = new File ("src/test/resource/.gds/files");
        File paths = new File ("src/test/resource/.gds/folders");
        if (files.exists() == false || files.isDirectory() == false){
            throw new FileNotFoundException("file's directory not found");
        }
        if (paths.exists() == false){
            throw new FileNotFoundException("folder's directory not found");
        }
        IndexCache ic  = filesystem.getCache();
        if (ic == null){
            throw new NullPointerException("cache is null");
        }

        
    }
}
