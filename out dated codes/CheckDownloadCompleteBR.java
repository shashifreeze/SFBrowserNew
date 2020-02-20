package in.blogspot.programmingdesire.sfbrowsernew.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.core.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Shashi kumar on 18-Apr-18.
 */

public class CheckDownloadCompleteBR  extends BroadcastReceiver{
    String file;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        assert action != null;
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        {
            DownloadManager.Query query=new DownloadManager.Query();
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0));
            DownloadManager manager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            assert manager != null;
            Cursor cursor=manager.query(query);
            if (cursor.moveToFirst())
            {
                if (cursor.getCount()>0)
                {
                    int status=cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    Long download_id=intent.getLongExtra(DownloadManager.ACTION_DOWNLOAD_COMPLETE,0);
                    if (status==DownloadManager.STATUS_SUCCESSFUL)
                    {
                        file=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        Uri uri=null;
                        Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(context,DownloadCompletedAlert.class);

                        //enhanced code 11-dec-2018

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                            // Do something for Marshmello and above versions
                            uri=FileProvider.getUriForFile(context,"sf.browser",new File(file));
                            i.putExtra("file",uri);

                        } else{
                            // do something for phones running an SDK before marshmello
                            i.putExtra("file",file);
                        }
                        //........................................................................//


                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    if (status==DownloadManager.STATUS_FAILED)
                    {
                        file=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        Toast.makeText(context, "Download failed \n"+file, Toast.LENGTH_SHORT).show();
                    }

                }
            }
            cursor.close();

        }
    }

}
