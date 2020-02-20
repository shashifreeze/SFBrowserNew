package in.blogspot.programmingdesire.sfbrowsernew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shashi kumar on 05-Apr-18.
 */

public class AutoCompleteTextViewDBAdapter extends SQLiteOpenHelper {

   private static final String name="Database";
    private static final int version=1;
    private SQLiteDatabase sqLiteDatabase;
    public AutoCompleteTextViewDBAdapter(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table ETTable(urls text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void openDB(){
        sqLiteDatabase=this.getWritableDatabase();
    }
    public void closeDB()
    {
        sqLiteDatabase.close();
    }
    public String[] read() {

        String urls[];
        String column[] = {"urls"};
        Cursor cursor = sqLiteDatabase.query("ETTable", column, null, null, null, null, null, null);
        if (cursor!=null&&cursor.getCount()<=0)
        {
            addData("programmingdesire.blogspot.com");
        }
        if ((cursor!=null)&&(cursor.getCount()>0)) {
            int i = 0;
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                i++;
            }
            if (i != 0) {
                urls = new String[i + 1];
                i = 0;
                cursor.moveToFirst();
                do {
                    urls[i] = cursor.getString(cursor.getColumnIndex("urls"));
                    i++;
                } while (cursor.moveToNext());
                return urls;
            }

        }
        cursor.close();
        return null;
    }

    public boolean compareString(String s)
    {
        String urls;
        String column[] = {"urls"};
        Cursor cursor = sqLiteDatabase.query("ETTable", column, null, null, null, null, null, null);

        if (cursor != null&&cursor.getCount()>0) {

                cursor.moveToFirst();
                do {
                    urls = cursor.getString(cursor.getColumnIndex("urls"));
                    boolean b=s.equals(urls);
                    if (b)
                        return b;

                } while (cursor.moveToNext());

            }
        return false;
    }
    public void addData(String urls)
    {
        ContentValues contentValues=new ContentValues(1);
        contentValues.put("urls",urls);
        sqLiteDatabase.insert("ETTable",null,contentValues);
    }
    public void deleteAll()
    {
        sqLiteDatabase.delete("ETTable",null,null);
    }

}
