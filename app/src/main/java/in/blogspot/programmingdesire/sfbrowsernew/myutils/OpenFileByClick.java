package in.blogspot.programmingdesire.sfbrowsernew.myutils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;

public class OpenFileByClick {

    public static void openFile(Context activity, String clickedFile, String ext) {
        File file = new File(clickedFile);
        Uri uri;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
             uri = FileProvider.getUriForFile(activity, "sf.browser", file);
        }else
        {
            uri=Uri.fromFile(file);
        }

        Intent target = new Intent(Intent.ACTION_VIEW);
        if(ext.compareTo("pdf")==0)
            target.setDataAndType(uri,"application/pdf");
        else if(ext.compareTo("mp3")==0)
            target.setDataAndType(uri,"audio/*");
        else if(ext.compareTo("txt")==0)
            target.setDataAndType(uri,"text/plain");
        else if(ext.compareTo("png")==0 ||ext.compareTo("psd")==0 ||ext.compareTo("jpg")==0 ||ext.compareTo("gif")==0)
            target.setDataAndType(uri,"image/*");
        else if(ext.compareTo("mp4")==0 ||ext.compareTo("mkv")==0 ||ext.compareTo("3gp")==0 ||ext.compareTo("avi")==0)
            target.setDataAndType(uri,"video/*");
        else if (ext.compareTo("zip")==0 ||ext.compareTo("rar")==0)
            target.setDataAndType(uri,"*/*");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.putExtra(Intent.EXTRA_STREAM, uri);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }
}
