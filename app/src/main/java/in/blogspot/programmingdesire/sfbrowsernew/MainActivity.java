package in.blogspot.programmingdesire.sfbrowsernew;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.bookmark.BookMarkFrag;
import in.blogspot.programmingdesire.sfbrowsernew.database.DBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.database.TabDbAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.download.DownloadActivity;
import in.blogspot.programmingdesire.sfbrowsernew.history.HisFrag;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.AppUpdateChecker;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.MyShaedPrefences;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.MyUtils;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabDataInfo;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabIemTouch;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemList;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabSwipeHelper;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG="Check For update";
public static String givenUrl;
FrameLayout frameLayout;
MainScreen mainScreen;
ImageButton menuImgBtn;
ImageButton tabBtn;
ImageView fullScreenIV;
TextView tabNoTV;
    public static String channelArtLink;
    public static File localFile;//for storing image from firebase database for channelArt
//FileExplorer fileExplorer;
public static FragmentManager fm;
public static int tabCount=0;
public static String visileTabTag="0";

    public static int realTabCount=1; //real tabCount counts current opened tabs

    int spanCount = 2; // 2 columns
    int spacing = 75; // 50px
public LinearLayout bottomLayout;
  SharedPreferences preferences;

    @Override
    protected void onResume() {
        super.onResume();
        checkingExternalIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteDatabase("TAB_DB");

       // clearTabDB();
        initializeNow();
       // handleOrientation();
        checkingExternalIntent();//checking for External link to be opened in this app
        settingMainScreen();
        MyUtils.requiredPermissions(this);

        if (MyUtils.checkPermissioin(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            MyUtils.createFolders();
        }else
        {
            MyUtils.requiredPermissions(this);
        }

        fillingFavicon();

        new MyShaedPrefences(getApplicationContext()).createSharedPrefences();

        menuImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuBtnClicked();
            }
        });
        tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabBtnClick();

            }
        });
        tabNoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tabBtnClick();

            }
        });
        fullScreenIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlFullScreen();
            }
        });

        checkForUpdateOnceADay();
    }

    private void checkForUpdateOnceADay() {
        Date date=new Date();
        int currentDate=date.getDate();
        int savedDate=preferences.getInt("PREF_savedDate",1);
        if (currentDate!=savedDate)
        {
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("PREF_savedDate",currentDate).apply();
            AppUpdateChecker updateChecker=new AppUpdateChecker(this);
            updateChecker.checkForUpdate(false);
        }
    }

    private void clearTabDB() {
        TabDataInfo.deleteAllTab(this);
    }

    private void fillingFavicon() {
      new FaviconAsync().execute();

    }
    private class FaviconAsync extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
             Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.sf);
            Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.logo);

            for (int i=0;i<=20;i++)
            {
                TabDataInfo.faviconImage.add(i,bitmap1);
                TabDataInfo.tabImage.add(i,bitmap);
                // TabDataInfo.tabImage.add(i,bitmap);
                TabDataInfo.tabTitle.add("SF Browser");
                TabDataInfo.tabUrl.add("New Tab");
            }
            return null;
        }
    }

    //to start the app using external link
    private void checkingExternalIntent() {
        Intent intent = getIntent();
        if (intent!=null) {
            Uri uri = intent.getData();
            if (uri!=null) {
               givenUrl=uri.toString();
                //LaunchNewTab(givenUrl);
                //Toast.makeText(this,"foo="+ uri, Toast.LENGTH_LONG).show();
            }
        }
    }
    //for launching new tabs from external links
    private void LaunchNewTab(String url) {
        MainScreen m=new MainScreen();
        m.myUrl=url;
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frm,m,MainActivity.tabCount+"").commit();

        // TabDataInfo.list.add(new TabItemList("BookMark",MainActivity.tabCount+""));
        TabDataInfo.addToList(this,(new TabItemList("External Link",MainActivity.tabCount+"")));
        MainActivity.visileTabTag=MainActivity.tabCount+"";
        MainActivity.tabCount++;
        MainActivity.realTabCount++;
        TextView textView= (TextView) findViewById(R.id.tabTextId);
        textView.setText(MainActivity.realTabCount+"");

    }

    private void controlFullScreen() {

        if (new MyShaedPrefences(getApplicationContext()).checksharedPrefences("PREF_full")){
            if(preferences.getBoolean("PREF_full",false)&&mainScreen.bottomNavLayout.getVisibility()==View.GONE)

            {
                exitFullScreen();
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("PREF_full",false).apply();
            }
            else{
                allowFullScreen();
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("PREF_full",true).apply();
            }
        }

    }

    private void allowFullScreen() {
        mainScreen.bottomNavLayout.setVisibility(View.GONE);
        mainScreen.addressbar.setVisibility(View.GONE);
        menuImgBtn.setVisibility(View.GONE);
        tabBtn.setVisibility(View.GONE);
        tabNoTV.setVisibility(View.GONE);
        fullScreenIV.setVisibility(View.VISIBLE);

    }

    private void exitFullScreen() {
        mainScreen.bottomNavLayout.setVisibility(View.VISIBLE);
        mainScreen.addressbar.setVisibility(View.VISIBLE);
        menuImgBtn.setVisibility(View.VISIBLE);
        tabBtn.setVisibility(View.VISIBLE);
        tabNoTV.setVisibility(View.VISIBLE);
        fullScreenIV.setVisibility(View.GONE);
    }


    private void tabBtnClick() {

       final Dialog sheetDialog=new Dialog(this);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setContentView(R.layout.tab_sheet_dialog);
        sheetDialog.setCancelable(true);
        sheetDialog.setCanceledOnTouchOutside(true);
        sheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RecyclerView recyclerView= (RecyclerView) sheetDialog.findViewById(R.id.tabListId);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));


          //  setting the grid layout and item decorator
//        assert recyclerView != null;
//
//        if (getApplicationContext().getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
//        {
//            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
//             spanCount = 2; // 2 columns
//             spacing = 50; // 50px
//        }else {
//            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
//             spanCount = 3; // 3 columns
//             spacing = 100; // 100px
//        }
//
//
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));


        Button addNewTab= (Button) sheetDialog.findViewById(R.id.addTabInDialogId);
        Button closeAll= (Button) sheetDialog.findViewById(R.id.closeAllTab);
        Button backBtn= (Button) sheetDialog.findViewById(R.id.backID);
       // final List<TabItemList> tabItemLists=new ArrayList<>();
        final TabItemAdapter tabItemAdapter=new TabItemAdapter(MainActivity.this,sheetDialog);
        recyclerView.setAdapter(tabItemAdapter);
        recyclerView.addOnItemTouchListener(new TabIemTouch(MainActivity.this, recyclerView, new TabIemTouch.ClickListener() {
           private String tag;

            @Override
            public void onClick(View view, int position) {

                  //  tag=tabItemLists.get(position).getTabTag();
                tag=TabDataInfo.getList(MainActivity.this).get(position).getTabTag();
                    visileTabTag=tag;
                if(preferences.contains("PREF_visibleTabTag"))
                {
                    SharedPreferences.Editor e=preferences.edit();
                    e.putString("PREF_visibleTabTag",visileTabTag).apply();
                }
                   // hideTabsExcept(tag ,tabItemLists);//hides all the tag and show selected tag
                //hideTabsExcept(tag ,TabDataInfo.list);//hides all the tag and show selected tag
                hideTabsExcept(tag ,TabDataInfo.getList(MainActivity.this));//hides all the tag and show selected tag
                    sheetDialog.dismiss();
            }
        }));
        //to Handle swipe for removing tabs
        ItemTouchHelper.Callback c= new TabSwipeHelper(tabItemAdapter,MainActivity.this,this.mainScreen);
        ItemTouchHelper helper=new ItemTouchHelper(c);
        helper.attachToRecyclerView(recyclerView);

      //  for(int i=0;i<TabDataInfo.list.size();i++)
            for(int i=0;i<TabDataInfo.getList(this).size();i++)
            {
                if (visileTabTag!=null)
                  {
                      if (i==Integer.parseInt(visileTabTag)){
                          if (TabDataInfo.getList(this).get(i)!=null) {
                              TabDbAdapter tabDbAdapter=new TabDbAdapter(this);
                              tabDbAdapter.openDb();
                              tabDbAdapter.updateRow(mainScreen.webView.getTitle(),i+"");
                              tabDbAdapter.closeDb();
                              //TabDataInfo.getList(this).get(i).setUrlTitle(mainScreen.webView.getTitle());//setting title to tab
                          }
                      }
                  }
        }
        assert addNewTab != null;
        addNewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    hideAllTabs();
                    mainScreen = new MainScreen();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.frm, mainScreen, tabCount + "");
                    transaction.commit();
                    //  TabDataInfo.list.add(new TabItemList("New Tab ",tabCount+""));
                    TabDataInfo.addToList(getApplicationContext(), new TabItemList("New Tab ", tabCount + ""));
                    //real tabcount
                    realTabCount=realTabCount+1;
                    tabNoTV.setText(realTabCount + "");

                    visileTabTag = tabCount + "";//this set the new tab as visible so visible tabtag will be current count
                    tabCount++;
                    tabItemAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "New Tab Added", Toast.LENGTH_SHORT).show();
                    sheetDialog.dismiss();
                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        assert closeAll != null;
        closeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realTabCount>1) {
                    TabDataInfo.deleteAllTab(MainActivity.this);
                    realTabCount = 1;
                    tabNoTV.setText("1");
                }
                sheetDialog.dismiss();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
            }
        });

        sheetDialog.setCanceledOnTouchOutside(true);
        sheetDialog.setCancelable(true);
        sheetDialog.show();

    }

    private void hideTabsExcept(String tag,List<TabItemList> list) {
            //hides all the fragments and then show the  the clicked or selected tab
                TabItemList tabItemList;
            for (int i=0;i<list.size();i++)
            {
                tabItemList=list.get(i);

               String t= tabItemList.getTabTag();

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(t);
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.hide(fragment).commit();
            }
//
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.show(fragment).commit();
        mainScreen= (MainScreen) fragment;
       // tabNoTV.setText(tag);
        realTabCount=list.size();
        tabNoTV.setText(realTabCount+"");

    }

    private void hideAllTabs()
    {      List<TabItemList> list= TabDataInfo.getList(MainActivity.this);
        //hides all the fragments and then show the  the clicked or selected tab
        TabItemList tabItemList;
        for (int i=0;i<list.size();i++)
        {
            tabItemList=list.get(i);

            String t= tabItemList.getTabTag();

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(t);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.hide(fragment).commit();
        }
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager=MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fragments = fragmentManager.getFragments();
        }
        if (fragments!=null)
        {
            for (Fragment fragment:fragments)
            {
                if (fragment!=null&&fragment.isVisible())
                {
                    return fragment;
                }
            }
        }
        return null;
    }

    private void handleOrientation() {
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)bottomLayout.getLayoutParams();
            params.weight=1.7f;
            bottomLayout.setLayoutParams(params);
            Toast.makeText(MainActivity.this,"landscape mode enabled",Toast.LENGTH_SHORT).show();
        }
    }

    private void settingMainScreen() {
        //setting the screen layout when app launches
        mainScreen=new MainScreen();
        mainScreen.myUrl=givenUrl;
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frm,mainScreen,tabCount+"");
        if (TabDataInfo.getList(this).size()==0) {
//            TabDataInfo.list.add(0, new TabItemList("New Tab ", tabCount + ""));

            TabDataInfo.addToList(this,new TabItemList("New Tab ", tabCount + ""));
            tabNoTV.setText("1");
            tabCount++;
        }
        transaction.commit();
    }

    public void onMenuBtnClicked()
    {
        showMenu();
       // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }


     private void showMenu() {

        final BottomSheetDialog sheetDialog=new BottomSheetDialog(this);
        sheetDialog.setCancelable(true);
        sheetDialog.setCanceledOnTouchOutside(true);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sheetDialog.setContentView(R.layout.menu_dialog_new);
        sheetDialog.show();
         //these all are not buttons ....i am using these linear layouts as buttons
        final LinearLayout dndBtn ,historyBtn,incogBtn,settingBtn,aboutBtn,exitBtn,bookMarkBtn,shareBtn,checkUpdateLL,fullscreenll,sharePageLL,closeBottomSheetLL;//these are not buttons ...these are imageviews
         fullscreenll=sheetDialog.findViewById(R.id.fullScreenLL);
         assert fullscreenll != null;
         fullscreenll.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 controlFullScreen();
                 sheetDialog.dismiss();
             }
         });
         shareBtn= (LinearLayout) sheetDialog.findViewById(R.id.shareLL);
         assert shareBtn != null;
         shareBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 shareAppLink();
                 sheetDialog.dismiss();
             }
         });
         checkUpdateLL= (LinearLayout) sheetDialog.findViewById(R.id.checkUpdateLL);
         checkUpdateLL.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sheetDialog.dismiss();
                 AppUpdateChecker updateChecker=new AppUpdateChecker(MainActivity.this);
                 updateChecker.checkForUpdate(true);
             }
         });
         closeBottomSheetLL= (LinearLayout) sheetDialog.findViewById(R.id.closeBottomSheetLL);
         closeBottomSheetLL.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sheetDialog.dismiss();
             }
         });
         sharePageLL= (LinearLayout) sheetDialog.findViewById(R.id.sharePageLL);
         sharePageLL.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent in=new Intent(Intent.ACTION_SEND);
                 in.setType("text/plain");
                 in.putExtra(Intent.EXTRA_TEXT,mainScreen.webView.getUrl());
                 startActivity(in);
             }
         });
         bookMarkBtn= (LinearLayout) sheetDialog.findViewById(R.id.bookMarkId);
//         Button trueIndian= (Button) sheetDialog.findViewById(R.id.trueIndian);
//         Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/3M Trislan.ttf");
//         trueIndian.setTypeface(typeface);
         //CheckBox desktopViewCB= (CheckBox) sheetDialog.findViewById(R.id.desktopViewCB);
         //for desktop mode

         Switch fbSwitch= (Switch) sheetDialog.findViewById(R.id.fbSwitch);
         assert bookMarkBtn != null;
         bookMarkBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 BookMarkFrag bookMarkFrag=new BookMarkFrag();
                 FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                 transaction.add(R.id.frm,bookMarkFrag,"Book").commit();
                 sheetDialog.dismiss();
             }
         });
         //for facebook
         if (preferences.contains("PREF_fbFaster"))
         {
             boolean b=preferences.getBoolean("PREF_fbFaster",true);
             if (fbSwitch!=null)
             fbSwitch.setChecked(b);
         }
            if (fbSwitch!=null)
         fbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (preferences.contains("PREF_fbFaster"))
                 {
                         SharedPreferences.Editor e=preferences.edit();
                         e.putBoolean("PREF_fbFaster",isChecked).apply();

                 }
             }
         });
         dndBtn= (LinearLayout) sheetDialog.findViewById(R.id.dndbtn);
        dndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.requiredPermissions(MainActivity.this);
                if (android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                        openDownlodActivity();
                sheetDialog.dismiss();

            }
        });
        historyBtn= (LinearLayout) sheetDialog.findViewById(R.id.history);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHistoryManager();
                sheetDialog.dismiss();

            }
        });
        incogBtn= (LinearLayout) sheetDialog.findViewById(R.id.incognito);
         if(preferences.getBoolean("PREF_incognito",false))//setting the initial background
         {
             incogBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
         }
        incogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incognitoFun(incogBtn);
                sheetDialog.dismiss();
            }
        });
        settingBtn= (LinearLayout) sheetDialog.findViewById(R.id.sett);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting();
                sheetDialog.dismiss();
            }
        });
        aboutBtn= (LinearLayout) sheetDialog.findViewById(R.id.aboutUs);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment about=new AboutFrag();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frm,about,"About").commit();
                sheetDialog.dismiss();
            }
        });
        exitBtn= (LinearLayout) sheetDialog.findViewById(R.id.exitOpt);
         assert exitBtn != null;
         exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               exitAlertDialog();
            }
        });

       }

    private void openDownlodActivity() {

        startActivity(new Intent(MainActivity.this,DownloadActivity.class));
    }

    private void shareAppLink() {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"Download SF Browser -an Indian product ,browse without ads \nhttps://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
        startActivity(i);
    }

    private void exitAlertDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Want to Exit ??");
        final CheckBox deleteHis=new CheckBox(MainActivity.this);
        deleteHis.setText("Delete History & Cache on Exit ");
        deleteHis.setChecked(preferences.getBoolean("PREF_delHisOnExit",false));
        builder.setView(deleteHis);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (deleteHis.isChecked())
                {
                    try {
                        FileUtils.deleteDirectory(MainActivity.this.getCacheDir());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DBAdapter dbAdapter=new DBAdapter(MainActivity.this);
                    dbAdapter.openDB();
                    dbAdapter.deleteData();
                    dbAdapter.closeDB();
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("PREF_delHisOnExit",true).apply();
                    finish();
                }else {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("PREF_delHisOnExit",false).apply();
                    finish();
                }

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (deleteHis.isChecked())
                {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("PREF_delHisOnExit",true).apply();
                }else {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("PREF_delHisOnExit",false).apply();
                }
            }

        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void openSetting() {

        SettingFragment sf=new SettingFragment();
        FragmentTransaction t=getSupportFragmentManager().beginTransaction();
        t.add(R.id.frm,sf,"settings").commit();
    }

    private void incognitoFun(LinearLayout v) {

        if (preferences.contains("PREF_incognito"))
        {
            boolean b=preferences.getBoolean("PREF_incognito",false);

            if (b)
            {
                SharedPreferences.Editor e=preferences.edit();
                e.putBoolean("PREF_incognito",false).apply();
                v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                Toast.makeText(this, "Incognito mode off", Toast.LENGTH_SHORT).show();

            }else {
                SharedPreferences.Editor e=preferences.edit();
                e.putBoolean("PREF_incognito",true).apply();
                v.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                Toast.makeText(this, "Incognito mode on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openHistoryManager() {
        HisFrag hisFrag=new HisFrag();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frm,hisFrag,"newHisTab").commit();
    }

    private void initializeNow() {
        tabBtn= (ImageButton) findViewById(R.id.tabBtnId);
        tabNoTV= (TextView) findViewById(R.id.tabTextId);
        fullScreenIV= (ImageView) findViewById(R.id.fullScreenIV);
        menuImgBtn= (ImageButton) findViewById(R.id.opt_btn);
        frameLayout= (FrameLayout) findViewById(R.id.frm);
        bottomLayout= (LinearLayout) findViewById(R.id.down);
         fm =getSupportFragmentManager();
        preferences=this.getSharedPreferences("SFPrefences",MODE_PRIVATE);

    }

    @Override
    public void onBackPressed() {

        if (mainScreen!=null)
        {
            if (mainScreen.webView!=null) {
                if (mainScreen.webView.canGoBack()) {
                    mainScreen.webView.goBack();
                    if (!mainScreen.webView.canGoBack())
                    {
                        mainScreen.homePageLinearLayout.setVisibility(View.VISIBLE);
                        mainScreen.addressbar.setVisibility(View.VISIBLE);
                    }
                } else
                {
                    mainScreen.homePageLinearLayout.setVisibility(View.VISIBLE);
                    mainScreen.addressbar.setVisibility(View.VISIBLE);
                }
            }
        }

    }


}
