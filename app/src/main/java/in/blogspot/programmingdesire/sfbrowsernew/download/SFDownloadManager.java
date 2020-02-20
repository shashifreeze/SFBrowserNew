package in.blogspot.programmingdesire.sfbrowsernew.download;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Shashi kumar on 09-Mar-18.
 */

public class SFDownloadManager implements DownloadListener{

    WebView webView;
    private Context context;
    private File sfFolder,musicFolder,videoFolder,docFolder,othersFolder,imageFolder;
    DownloadManager.Request request;
    private String filename;
    private String downloadFolderName;
    //private DownloadManager dm;

    public SFDownloadManager(WebView webView, Context context) {
        this.webView = webView;
        this.context = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
    {
            createFolders();
            request=new DownloadManager.Request(Uri.parse(url));
        request.allowScanningByMediaScanner();
        filename= URLUtil.guessFileName(url,contentDisposition,mimetype);
        checkFileType();
        DecimalFormat df=new DecimalFormat();
        df.setMaximumFractionDigits(2);
       // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       // request.setDestinationInExternalPublicDir("SFDownloads/"+downloadFolderName,filename);
        //dm= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        String length;
        if ((float)(contentLength/1024000)>0)
        {
         length= (df.format((float)(contentLength/1024000)))+  "MB";
        }else
        {
            length=   (df.format((float)(contentLength/1000)))+"KB";
        }
        alertDialogBuilder.setMessage("Download the file:\n"+filename+"\n"+ length);
                alertDialogBuilder.setPositiveButton("Download",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//                                Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();
//                                dm.enqueue(request);

                                DownloadingFileModelClass downloadingFileModelClass=new DownloadingFileModelClass(filename,url,Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/"+downloadFolderName);
                                DownloadsDataInfo.addToList(context,downloadingFileModelClass);
                                context.startActivity(new Intent(context,DownloadActivity.class));

                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void checkFileType() {

        if (filename.contains(".mp3"))
        {
            downloadFolderName="music";
        }else if(filename.contains(".mp4")||filename.contains(".mkv")||filename.contains("3gp")||filename.contains(".avi")
                ||filename.contains(".mov"))
        {
            downloadFolderName="video";
        }else if (filename.contains(".doc")||filename.contains(".docx")||filename.contains(".pdf")||filename.contains("xls")
                ||filename.contains(".xlsx")){
            downloadFolderName="documents";
        }else if (filename.contains(".jpg")||filename.contains(".png")||filename.contains(".gif"))
        {
                downloadFolderName="images";
        }else {
            downloadFolderName="others";
        }
    }

    private void createFolders() {
        requestPermissions();
        if(checkPermissioin(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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

            imageFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/images");
            if (!imageFolder.exists()) {
                imageFolder.mkdirs();
            }
            docFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/documents");
            if (!docFolder.exists()) {
                docFolder.mkdirs();
            }
        }
    }

    private boolean checkPermissioin(String permission) {

    return (ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);

    }

}
