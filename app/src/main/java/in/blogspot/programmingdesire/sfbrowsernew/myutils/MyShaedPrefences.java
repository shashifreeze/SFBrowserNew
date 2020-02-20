package in.blogspot.programmingdesire.sfbrowsernew.myutils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shashi kumar on 21-Mar-18.
 */

public class MyShaedPrefences {
    Context context;

    SharedPreferences sharedPreferences;

    public MyShaedPrefences(Context context) {
        this.context = context;
    }

    public boolean checksharedPrefences(String key){
        sharedPreferences=context.getSharedPreferences("SFPrefences",Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }
    public void createSharedPrefences()
    {
        sharedPreferences=context.getSharedPreferences("SFPrefences",Context.MODE_PRIVATE);
        //for full screen setting
        if (!sharedPreferences.contains("PREF_full"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_full",false).apply();//setting initial value of the fullscreen move to false when launched 1st time
        }
        //for incognito mode setting
        if (!sharedPreferences.contains("PREF_incognito"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_incognito",false).apply();//setting incognito mote to false when browser launches 1st time
        }
        //for setting text size
        if (!sharedPreferences.contains("PREF_textSize"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("PREF_textSize","Medium").apply();//setting incognito mote to false when browser launches 1st time
        }
        //for fb mode
        if (!sharedPreferences.contains("PREF_fbFaster"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_fbFaster",true).apply();//setting incognito mote to false when browser launches 1st time
        }
        //for desktop mode
        if (!sharedPreferences.contains("PREF_DesktopMode"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_DesktopMode",false).apply();//setting incognito mote to false when browser launches 1st time
        }
        if (!sharedPreferences.contains("PREF_AllowCookies"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_AllowCookies",true).apply();//setting incognito mote to false when browser launches 1st time
        }
        if (!sharedPreferences.contains("PREF_desktopView"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_desktopView",false).apply();//setting incognito mote to false when browser launches 1st time
        }
        if (!sharedPreferences.contains("PREF_visibleTabTag"))
        {

            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("PREF_visibleTabTag","0").apply();//setting incognito mote to false when browser launches 1st time
        }
//        if (!sharedPreferences.contains("PREF_canvasWorking"))
//        {
//
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putBoolean("PREF_canvasWorking",true).apply();//setting incognito mote to false when browser launches 1st time
//        }
        if (!sharedPreferences.contains("PREF_textColor"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("PREF_textColor","tomato").apply();//setting incognito mote to false when browser launches 1st time
        }
        if (!sharedPreferences.contains("PREF_delHisOnExit"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PREF_delHisOnExit",false).apply();//setting incognito mote to false when browser launches 1st time
        }
        if (!sharedPreferences.contains("PREF_savedDate"))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("PREF_savedDate",1).apply();//setting incognito mote to false when browser launches 1st time
        }

    }
}
