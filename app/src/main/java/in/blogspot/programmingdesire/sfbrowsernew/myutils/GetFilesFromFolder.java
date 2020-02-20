package in.blogspot.programmingdesire.sfbrowsernew.myutils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class GetFilesFromFolder {

    public static ArrayList<File> getAllFilesList(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            inFiles.addAll(Arrays.asList(files));
        }
        Collections.sort(inFiles, new Comparator<File>() {

            @Override
            public int compare(File file1, File file2) {
                long k = file1.lastModified() - file2.lastModified();
                if(k < 0){
                    return 1;
                }else if(k == 0){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        return inFiles;
    }
}
