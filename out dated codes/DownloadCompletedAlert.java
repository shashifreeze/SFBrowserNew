package in.blogspot.programmingdesire.sfbrowsernew.download;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Shashi kumar on 18-Apr-18.
 */

public class DownloadCompletedAlert extends Activity {
    String file="hello";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if (intent!=null)
        {
            file=intent.getStringExtra("file");
        }
        showDialog(file);

    }

    private void showDialog(final String file) {

        AlertDialog.Builder builder=new AlertDialog.Builder(DownloadCompletedAlert.this);
        builder.setTitle("Download completed");
       // builder.setIcon(context.getResources().getDrawable(R.drawable.ic_file_download_black_24dp));
        builder.setMessage("Open the file \n" +file);
        builder.setCancelable(false);
        builder.setPositiveButton("open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playFile(file,getExtension(file),DownloadCompletedAlert.this);
                //finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }
    private String getExtension(String s) {
        int l = s.length();

        return s.substring(l-3,l);
    }
    public void playFile(String clickedFile,String ext,Context context)
    {
        File file = new File(clickedFile);
        Uri uri;
        Intent target = new Intent(Intent.ACTION_VIEW);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            // Do something for Marshmello and above versions
            uri=FileProvider.getUriForFile(context,"sf.browser",file);
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            target.putExtra(Intent.EXTRA_STREAM, uri);

        } else{
            // do something for phones running an SDK before marshmello
            uri=Uri.fromFile(file);
        }
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
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
              context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
