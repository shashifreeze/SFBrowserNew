package in.blogspot.programmingdesire.sfbrowsernew.tabmanager;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.database.TabDbAdapter;

/**
 * Created by Shashi kumar on 20-Mar-18.
 */

public class TabDataInfo {

    //this class contains the tabs information and provides functionality to save info in databases

 //public static   List<TabItemList> list=new ArrayList<>();

    public static List<Bitmap> faviconImage=new ArrayList<>();
    public static List<Bitmap> tabImage=new ArrayList<>();
    public static List<String> tabTitle=new ArrayList<>();
    public static List<String> tabUrl=new ArrayList<>();

 public static List<TabItemList> getList(Context context)
 {
     TabDbAdapter tabDbAdapter=new TabDbAdapter(context);
     tabDbAdapter.openDb();
     List<TabItemList> list=tabDbAdapter.read();
     tabDbAdapter.close();
     return list;
 }
 public static void addToList(Context context, TabItemList tabItemList)
 {
     TabDbAdapter tabDbAdapter=new TabDbAdapter(context);
     tabDbAdapter.openDb();
     tabDbAdapter.addData(tabItemList.getUrlTitle(),tabItemList.getTabTag());
     tabDbAdapter.closeDb();
 }
    public static void deleteTab(Context context,String tag)
    {
        TabDbAdapter tabDbAdapter=new TabDbAdapter(context);
        tabDbAdapter.openDb();
        tabDbAdapter.deleteTab(tag);
        tabDbAdapter.closeDb();
    }
    public static void deleteAllTab(Context context)
    {
        TabDbAdapter tabDbAdapter=new TabDbAdapter(context);
        tabDbAdapter.openDb();
        tabDbAdapter.deleteAll();
        tabDbAdapter.closeDb();
    }
}
