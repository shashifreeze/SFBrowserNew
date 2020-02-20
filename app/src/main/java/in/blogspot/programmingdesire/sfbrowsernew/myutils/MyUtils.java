package in.blogspot.programmingdesire.sfbrowsernew.myutils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
/*

Myutils contailns the following functions

1.BytesToMbKb
2. requiredPermissions
3.checkPermission
4.createFolders

 */
public class MyUtils {


    //storage unit converter
    public static String BytesToMbKb(long bytes)
    {

        if (bytes/(1024*1024)>=1)
        {
            return bytes/(1024*1024)+"Mb";
        }else if (bytes/(1024)>=1){
            return bytes/(1024)+"Kb";
        }else
        return  bytes+"bytes";
    }

    public static void requiredPermissions(Activity activity) {

        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CLEAR_APP_CACHE},300);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},400);
    }

    public static boolean checkPermissioin(Activity activity,String permission) {

        return (ContextCompat.checkSelfPermission(activity,permission)== PackageManager.PERMISSION_GRANTED);
    }

    public static void createFolders() {
        new CreateFolderAsyncTask().execute();
    }
    private static class CreateFolderAsyncTask extends AsyncTask<Void,Void,Void>
    {
        File sfFolder,musicFolder,videoFolder,docFolder,othersFolder,imageFolder;

        @Override
        protected Void doInBackground(Void... params) {

                sfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads");
                if (!sfFolder.exists()) {
                    sfFolder.mkdirs();
                }
                musicFolder =new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/music");
                if (!musicFolder.exists()) {
                    musicFolder.mkdirs();
                }
                videoFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/video");
                if (!videoFolder.exists()) {
                    videoFolder.mkdirs();
                }
                othersFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/others");
                if (!othersFolder.exists()) {
                    othersFolder.mkdirs();
                }

                imageFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/image");
                if (!imageFolder.exists()) {
                    imageFolder.mkdirs();
                }
                docFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/documents");
                if (!docFolder.exists()) {
                    docFolder.mkdirs();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
