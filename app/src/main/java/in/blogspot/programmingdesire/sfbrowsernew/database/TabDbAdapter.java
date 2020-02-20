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

import in.blogspot.programmingdesire.sfbrowsernew.R;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemList;

/**
 * Created by Shashi kumar on 09-Apr-18.
 */

public class TabDbAdapter extends SQLiteOpenHelper {
    Context context;
    private SQLiteDatabase db;
    private List<TabItemList> list=new ArrayList<>();
    public TabDbAdapter(Context context) {
        super(context, "TAB_DB",null,1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TabTable(title text,Tabtag text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void openDb()
    {
        this.db=this.getWritableDatabase();
    }
    public void closeDb(){db.close();}
    public List<TabItemList> read()
    {
        String[]columns={"title","Tabtag"};
        Cursor cursor=db.query("TabTable",columns,null,null,null,null,null);
        if (cursor!=null&&cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do {
                String url=cursor.getString(cursor.getColumnIndex("Tabtag"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                TabItemList tabItemList=new TabItemList(title,url);
                list.add(tabItemList);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }
    public void addData(String title,String url)
    {
        ContentValues contentValues=new ContentValues(3);
        contentValues.put("title",title);
        contentValues.put("Tabtag",url);

        db.insert("TabTable",null,contentValues);
    }
    public void deleteTab(String t)
    {
        db.delete("TabTable","Tabtag=?",new String[]{t});
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(t);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(fragment).commit();

    }
    public void deleteAll()
    {
        //sqLiteDatabase.delete("hisTable","title",title[]);
        db.delete("TabTable",null,null);
        FragmentManager fm=((FragmentActivity)context).getSupportFragmentManager();
      fm.beginTransaction().remove(fm.findFragmentById(R.id.frm)).commit();
    }
    public void updateRow(String title,String tabtag)
    {
            if (!title.equals("HomePage")) {
                ContentValues cv = new ContentValues();
                cv.put("title", title);
                db.update("TabTable", cv, "Tabtag = ?", new String[]{tabtag});
            }
        }

    }

