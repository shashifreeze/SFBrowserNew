package in.blogspot.programmingdesire.sfbrowsernew.download;

//this class is immediater between downloads db adapter class and the downloading file adapter class

import android.content.Context;

import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.database.DownloadsDbAdapter;


public class DownloadsDataInfo {
    
    public static List<DownloadingFileModelClass> getList(Context context)
    {
        DownloadsDbAdapter downloadsDbAdapter=new DownloadsDbAdapter(context);
        downloadsDbAdapter.openDb();
        List<DownloadingFileModelClass> list=downloadsDbAdapter.read();
        downloadsDbAdapter.close();
        return list;
    }
    public static void addToList(Context context, DownloadingFileModelClass downloadingFileModelClass)
    {
        DownloadsDbAdapter tabDbAdapter=new DownloadsDbAdapter(context);
        tabDbAdapter.openDb();
        tabDbAdapter.addData(downloadingFileModelClass.getFilename(),downloadingFileModelClass.getUrl(),downloadingFileModelClass.getDownloadFilePath());
        tabDbAdapter.closeDb();
    }

}
