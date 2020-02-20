package in.blogspot.programmingdesire.sfbrowsernew.download;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Shashi kumar on 02-Apr-18.
 */

public class ContextMenuImageDownloader {


    public static void downloadImge(String imageUrl, Context context){
        if (URLUtil.isValidUrl(imageUrl))
        {
//            DownloadManager.Request request=new DownloadManager.Request(Uri.parse(imageUrl));
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setDestinationInExternalPublicDir("SFDownloads/images","image"+ new Random().nextInt(1000000)+".png");
//            DownloadManager dm= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            dm.enqueue(request);

            String filename="image"+ new Random().nextInt(1000000)+".png";

            DownloadingFileModelClass downloadingFileModelClass=new DownloadingFileModelClass(filename,imageUrl,Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/image");
            DownloadsDataInfo.addToList(context,downloadingFileModelClass);
            context.startActivity(new Intent(context,DownloadActivity.class));
        }
        else {
            Toast.makeText(context, "Not a valid Url", Toast.LENGTH_SHORT).show();
        }
    }


}
