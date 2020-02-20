package in.blogspot.programmingdesire.sfbrowsernew.download_browser;

import android.os.Environment;

import java.io.File;
import java.util.Stack;

/**
 * Created by Shashi kumar on 28-Mar-18.
 */

public class FolderHistory {
    Stack<String> folderHistory;

    public FolderHistory() {
        this.folderHistory=new Stack<>();
    }
    public Stack<String> getFolderHistory(){

        File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads");
        folderHistory.add(f.toString());
        return folderHistory;
    }
}
