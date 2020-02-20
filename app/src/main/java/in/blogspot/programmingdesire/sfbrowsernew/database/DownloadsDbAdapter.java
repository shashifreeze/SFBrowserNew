package in.blogspot.programmingdesire.sfbrowsernew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.download.DownloadingFileModelClass;

public class DownloadsDbAdapter extends SQLiteOpenHelper {

    Context context;
    private SQLiteDatabase db;
    private List<DownloadingFileModelClass> list=new ArrayList<>();

    private static final String table_name="DOWNLOADS_TABLE";
    private static final String db_name="DOWNLOADS_DB";
    public DownloadsDbAdapter(Context context) {
        super(context, db_name,null,1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table DOWNLOADS_TABLE(filename text,url text,filepath text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void openDb()
    {
        this.db=this.getWritableDatabase();
    }
    public void closeDb(){db.close();}
    public List<DownloadingFileModelClass> read()
    {
        String[]columns={"filename","url","filepath"};
        Cursor cursor=db.query(table_name,columns,null,null,null,null,null);
        if (cursor!=null&&cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do {
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String filename=cursor.getString(cursor.getColumnIndex("filename"));
                String filepath=cursor.getString(cursor.getColumnIndex("filepath"));
               // TabItemList tabItemList=new TabItemList(filename,url);
                DownloadingFileModelClass downloadingFileModelClass=new DownloadingFileModelClass(filename,url,filepath);
                list.add(downloadingFileModelClass);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }
    public void addData(String filename,String url,String filepath)
    {
        ContentValues contentValues=new ContentValues(3);
        contentValues.put("filename",filename);
        contentValues.put("url",url);
        contentValues.put("filepath",filepath);

        db.insert(table_name,null,contentValues);
    }
    public void deleteTab(String t)
    {
        db.delete(table_name,"url=?",new String[]{t});
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(t);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(fragment).commit();

    }
}
