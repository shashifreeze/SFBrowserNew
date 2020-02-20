package in.blogspot.programmingdesire.sfbrowsernew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shashi kumar on 08-Feb-18.
 */
public class DBAdapter extends SQLiteOpenHelper {
    static String name="SFDB";
    static  int version=1;
    Context context;
    SQLiteDatabase sqLiteDatabase;
    public DBAdapter(Context context) {
        super(context, name, null, version);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table hisTable(title text,url text,date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
   public void openDB()
    {
        sqLiteDatabase=this.getWritableDatabase();
    }
    public void closeDB()
    {
        sqLiteDatabase.close();
    }
    public Cursor readDB()
    {
        String colomn[]={"title","url","date"};
        return sqLiteDatabase.query("hisTable",colomn,null,null,null,null,null,null);
    }
    public void addData(String title,String url,String date)
    {

            ContentValues contentValues = new ContentValues(3);
            contentValues.put("title", title);
            contentValues.put("url", url);
            contentValues.put("date", date);
            sqLiteDatabase.insert("hisTable", null, contentValues);

    }


    public void deleteData()
    {
        sqLiteDatabase.delete("hisTable",null,null);
    }
    public void deleteSingleRow(String t)
    {
        //sqLiteDatabase.delete("hisTable","title",title[]);
        sqLiteDatabase.delete("hisTable","title=?",new String[]{t});
    }
}
