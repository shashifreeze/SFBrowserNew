package in.blogspot.programmingdesire.sfbrowsernew.history;

import android.content.Context;
import android.database.Cursor;

import  in.blogspot.programmingdesire.sfbrowsernew.database.DBAdapter;

/**
 * Created by Shashi kumar on 08-Feb-18.
 */

public class HistoryData {

    String urlArray[];
    String urlTitle[];
    String urlDate[];
    private DBAdapter dbAdapter;
    private Context context;
    public HistoryData(Context context) {
       this.context=context;
    }

    public void fillingDataIntoArray()
    {
        dbAdapter=new DBAdapter(context);
        dbAdapter.openDB();
        Cursor cursor=dbAdapter.readDB();
        if (cursor!=null)
        {
            int i=0;
            cursor.moveToFirst();
            while (cursor.moveToNext())
            {
                i++;
            }
            if (i!=0) {
                urlDate = new String[i+1];
                urlTitle = new String[i+1];
                urlArray = new String[i+1];
                i = 0;
                cursor.moveToFirst();
                do {
                    urlArray[i] = cursor.getString(cursor.getColumnIndex("url"));
                    urlTitle[i] = cursor.getString(cursor.getColumnIndex("title"));
                    urlDate[i] = cursor.getString(cursor.getColumnIndex("date"));

                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dbAdapter.closeDB();
    }
}
