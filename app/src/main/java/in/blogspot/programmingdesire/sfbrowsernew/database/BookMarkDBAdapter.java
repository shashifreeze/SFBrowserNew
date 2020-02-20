package in.blogspot.programmingdesire.sfbrowsernew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.bookmark.BookMarkModelClass;

/**
 * Created by Shashi kumar on 08-Apr-18.
 */

public class BookMarkDBAdapter extends SQLiteOpenHelper {
   static final String  name="BookMarkDB";
    SQLiteDatabase db;
    private List<BookMarkModelClass> markModelClasses=new ArrayList<>();
    public BookMarkDBAdapter(Context context) {
        super(context, name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table BM_TABLE(url text,title text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void openDB()
    {
        this.db=this.getWritableDatabase();
    }
    public void closeDb()
    {
        this.db.close();
    }
    public List<BookMarkModelClass> readData()
    {
        String[]columns={"url","title"};
        Cursor cursor=db.query("BM_TABLE",columns,null,null,null,null,null);
        if (cursor!=null&&cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do {
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                BookMarkModelClass modelClass=new BookMarkModelClass(url,title);
                markModelClasses.add(modelClass);
            }while (cursor.moveToNext());

        }
       return markModelClasses;
    }
    public void addData(String url,String title)
    {
        ContentValues contentValues=new ContentValues(2);
        contentValues.put("url",url);
        contentValues.put("title",title);
        db.insert("BM_TABLE",null,contentValues);
    }
    public void deleteAbookMark(String t)
    {
        //sqLiteDatabase.delete("hisTable","title",title[]);
        db.delete("BM_TABLE","title=?",new String[]{t});

    }
    public void deleteAll()
    {
        //sqLiteDatabase.delete("hisTable","title",title[]);
       db.delete("BM_TABLE",null,null);

    }
}
