package in.blogspot.programmingdesire.sfbrowsernew;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import in.blogspot.programmingdesire.sfbrowsernew.custom_webview.VideoEnabledWebChromeClient;
import in.blogspot.programmingdesire.sfbrowsernew.custom_webview.VideoEnabledWebView;
import in.blogspot.programmingdesire.sfbrowsernew.database.AutoCompleteTextViewDBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.database.BookMarkDBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.database.DBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.download.ContextMenuImageDownloader;
import in.blogspot.programmingdesire.sfbrowsernew.download.SFDownloadManager;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabDataInfo;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static in.blogspot.programmingdesire.sfbrowsernew.MainActivity.channelArtLink;
import static in.blogspot.programmingdesire.sfbrowsernew.MainActivity.localFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainScreen extends Fragment {
    //for full screen mode
 public    VideoEnabledWebView webView;

    View nonVideoLayout;
    ViewGroup videoLayout;
    View loadingView;
    //....................................
    View v;
    ValueCallback<Uri[]> mFilePathCallback;//for file upload
    int PICK_FILE_REQUEST_CODE=118;
    ImageView tabFavicon;
    String textColor;
    FrameLayout webViewParent;
    ProgressBar progressBar;
    public String myUrl;
    private String thisTabTag;
    DBAdapter dbAdapter;
    ImageView faviconIV,channelArtIV;
    CardView channelArtCardView;
    public LinearLayout rootLayout;
    AutoCompleteTextView editText;
    FrameLayout titleAndSearchFrameLayout;
    FrameLayout refresh_btnAndCloseFrameLayout;
    TextView titleTV;
    SharedPreferences preferences;
    ImageButton closeImgBtn,refreshImgBtn,leftBtn,homeBtn,rightBtn,addressBarCancelImgBtn,addBookmarkBtn;
    public CardView bottomNavLayout;
    public CardView addressbar;
    public LinearLayout homePageLinearLayout;
    private LinearLayout bottomSwipeLL;
    //using linear layouts as buttons to click on them

    private imageTabAsyncTask iTA;

    public MainScreen() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final WebView.HitTestResult result=webView.getHitTestResult();

        MenuItem.OnMenuItemClickListener handler=new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId()==666)
                {
                    Toast.makeText(getContext(), "save Image", Toast.LENGTH_SHORT).show();
                    ContextMenuImageDownloader.downloadImge(result.getExtra(),getContext());
                }
                if (item.getItemId()==777)
                {
                    //Toast.makeText(getContext(), "share Image", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(Intent.ACTION_SEND);
                    in.setType("text/plain");
                    in.putExtra(Intent.EXTRA_TEXT,result.getExtra());
                    startActivity(in);
                }
                if (item.getItemId()==888)
                {
                    //Toast.makeText(getContext(), "save Image", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(Intent.ACTION_SEND);
                    in.setType("text/plain");
                    in.putExtra(Intent.EXTRA_TEXT,result.getExtra());
                    startActivity(in);
                }
                if (item.getItemId()==999)
                {
                    //Toast.makeText(getContext(), "save Image", Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager= (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData=ClipData.newPlainText("Link",result.getExtra());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getContext(), "Link copied", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId()==555)
                {
                    //Toast.makeText(getContext(), "save Image", Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager= (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData=ClipData.newPlainText("Link",result.getExtra());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getContext(), "Link copied", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId()==444)
                {
                    openNewTab(result.getExtra());
                }
                if (item.getItemId()==333)
                {
                    openNewTab(result.getExtra());
                }
                if (item.getItemId()==111)
                {
                    addBookmark(result.getExtra());
                }
                return true;
            }
        };
        menu.setHeaderTitle("Options");
        if (result.getType()==WebView.HitTestResult.IMAGE_TYPE||result.getType()==WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE)
        {
            menu.add(0,666,0,"Save Image").setOnMenuItemClickListener(handler);
            menu.add(0,777,1,"Share Image").setOnMenuItemClickListener(handler);
            menu.add(0,555,3,"Copy Image Link").setOnMenuItemClickListener(handler);
            menu.add(0,333,4,"Open in New Tab").setOnMenuItemClickListener(handler);

        }
        if (result.getType()== WebView.HitTestResult.SRC_ANCHOR_TYPE)
        {
            menu.add(0,888,1,"Share Link").setOnMenuItemClickListener(handler);
            menu.add(0,999,2,"Copy Link").setOnMenuItemClickListener(handler);
            menu.add(0,444,3,"Open in new Tab").setOnMenuItemClickListener(handler);
            menu.add(0,111,4,"Add as BookMark").setOnMenuItemClickListener(handler);

        }

    }

    private void openNewTab(String extra) {
        MainScreen mainScreen=new MainScreen();
        mainScreen.myUrl=extra;
        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frm,mainScreen,MainActivity.tabCount+"").commit();
      //  TabDataInfo.list.add(new TabItemList("New Tab",MainActivity.tabCount+""));

        TabDataInfo.addToList(getContext(),new TabItemList("New Tab",MainActivity.tabCount+""));
        MainActivity.visileTabTag=MainActivity.tabCount+""; //setting the new created tab as visible tab tag
        MainActivity.realTabCount++;//incrementing the number of real tabs
        MainActivity.tabCount++;//total tabs includeing deleted tabs
    }

//......................................OnCreateView.........................................................................

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_main_screen, container, false);

        initializeNow();
        focusChangeHandling();
        addressBarCancelImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleAndSearchFrameLayout.setVisibility(View.VISIBLE);
                addressBarCancelImgBtn.setVisibility(View.GONE);
                refresh_btnAndCloseFrameLayout.setVisibility(View.VISIBLE);
                addBookmarkBtn.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                editText.setText("");
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                manageEditText(textView);
                return false;
            }
        });

        refreshImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
        closeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.stopLoading();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView!=null)
                {
                    if (webView.canGoBack())
                    {
                        webView.goBack();

                        homePageLinearLayout.setVisibility(View.GONE);
                        if (!webView.canGoBack())
                        {
                            homePageLinearLayout.setVisibility(View.VISIBLE);
                            addressbar.setVisibility(View.VISIBLE);
                            faviconIV.setImageResource(R.drawable.google);
                        }
                    }
                    else
                    {
                        homePageLinearLayout.setVisibility(View.VISIBLE);
                        addressbar.setVisibility(View.VISIBLE);
                        faviconIV.setImageResource(R.drawable.google);
                    }
                }else {
                    Toast.makeText(getContext(), "Webview is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView!=null)
                {
                    if (webView.canGoForward())
                    {   homePageLinearLayout.setVisibility(View.GONE);
                        webView.goForward();

                    }
                }
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePageLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleTV.setVisibility(View.GONE);
            }
        });

        addBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(null);
            }

        });
        //Toast.makeText(v.getContext(), "hii", Toast.LENGTH_SHORT).show();
        settingWebView();
        registerForContextMenu(webView);
        managingHomePageLayout();
        settingACTV();
        handleSwipe();
        manageTextColor();
        //............................
        try {
            channelArtSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    private void channelArtSetting() throws IOException {

        channelArtIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(getActivity(), channelArtLink, Toast.LENGTH_SHORT).show();
                myUrl=channelArtLink;
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        Fragment f= this;
        if (f.getTag().equals("0")) {

            downloadImageFromFirebase();
           // Toast.makeText(getActivity(), "0th tab", Toast.LENGTH_SHORT).show();
        }
        else {
            if(localFile!=null)
            {
                channelArtIV.setVisibility(View.VISIBLE);
                channelArtCardView.setVisibility(View.VISIBLE);
                Picasso.get().load(localFile).into(channelArtIV);
            }
        }
    }
    /*
    *       this function runs once for the first fragment
    *
    *
    * */
    private void downloadImageFromFirebase() throws IOException {
        localFile= File.createTempFile("images","jpg");
        StorageReference mStorageRef= FirebaseStorage.getInstance().getReferenceFromUrl("gs://sf-browser-786.appspot.com/").child("cA.jpg");

        if (mStorageRef!=null)
        mStorageRef.getFile(localFile)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        localFile=null; //if there is an error then local file would be null
                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                channelArtCardView.setVisibility(View.VISIBLE);
                Picasso.get().load(localFile).into(channelArtIV);
            }
        });

        //getting channelArt link from firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference df= database.getReference("cA");

        if (df!=null)
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               MainActivity.channelArtLink=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void manageTextColor() {
       textColor= preferences.getString("PREF_textColor","black");
    }

    private void addBookmark(String link) {

     final String title,url;
        if (link!=null)
        {
         url=link;
            title=webView.getTitle();
        }
        else {
            url=webView.getUrl();
            title=webView.getTitle();
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Add BookMark");
        builder.setMessage(" Title : "+title+"\nLink : "+url);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookMarkDBAdapter adapter=new BookMarkDBAdapter(getContext());
                adapter.openDB();
                adapter.addData(url,title);
                Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override// pause the video only when the tab is not visible
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
        {
            webView.onPause();
        }
        else {
            webView.onResume();
        }
    }

    private void handleSwipe() {

    }

//...................................................settingACTV........................................................................

    private void settingACTV() {
        new ACTvAsync().execute();
    }
    private class ACTvAsync extends AsyncTask<Void,Void,Void>
    {
            String s[];

        @Override
        protected Void doInBackground(Void... params) {
            AutoCompleteTextViewDBAdapter dbAdapter1=new AutoCompleteTextViewDBAdapter(getContext());
            dbAdapter1.openDB();
             s=dbAdapter1.read();//getting data from database
            dbAdapter1.closeDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (s!=null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,s);
                editText.setThreshold(1);
                editText.setAdapter(adapter);
            }
        }
    }

    //.......................................focus change handling.................................................................
    private void focusChangeHandling() {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setFocusable(true);
               // editText.setText(webView.getUrl());
                editText.showDropDown();
                editText.setFocusableInTouchMode(true);
                addressBarCancelImgBtn.setVisibility(View.VISIBLE);
                refresh_btnAndCloseFrameLayout.setVisibility(View.GONE);
                addBookmarkBtn.setVisibility(View.GONE);
                return false;
            }
        });

    }
//........................................managing home page layout.......................................................................
    //manages homepage layout and its activity
    private void managingHomePageLayout() {

        TextView topSites[] = new TextView[8];
         v.findViewById(R.id.googleLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://google.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.fbLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://facebook.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.instaLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://instagram.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.cricketLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="http://www.cricbuzz.com/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.amazonLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://amazon.in";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.twitterLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://twitter.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.sarkariResultLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://sarkariresult.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        v.findViewById(R.id.youTubeLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://youtube.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });


        //..............................Top news sites...................................................
        for (int i=0;i<6;i++)
        {
            int id=v.getResources().getIdentifier("TS"+i,"id",getContext().getPackageName());
            topSites[i]= (TextView) v.findViewById(id);
        }

        topSites[0].setText("Dainik Jagran");
        topSites[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://www.jagran.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[1].setText("Dainik Bhaskar");
        topSites[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://www.bhaskar.com/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[2].setText("Zee News Hindi");
        topSites[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myUrl="http://zeenews.india.com/hindi";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[3].setText("Amar Ujala");
        topSites[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://www.amarujala.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[4].setText("Prabhat khabar");
        topSites[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://www.prabhatkhabar.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[5].setText("Hindustan");
        topSites[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://www.livehindustan.com";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });
        //..............................Top Movies & music sites...................................................
        for (int i=0;i<6;i++)
        {
            int id=v.getResources().getIdentifier("MS"+i,"id",getContext().getPackageName());
            topSites[i]= (TextView) v.findViewById(id);
        }

        topSites[0].setText("WorldFree4u");
        topSites[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="http://www.bolly2u.com/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[2].setText("Bolly4u");
        topSites[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://bolly4u.bid/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[1].setText("mkvmoviespoint");
        topSites[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myUrl="https://www.mkvmoviespoint.com/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[3].setText("Downloadming");
        topSites[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://downloadming.ms/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[4].setText("FreshMaza");
        topSites[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://freshmaza.in/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

        topSites[5].setText("Mr-jatt");
        topSites[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUrl="https://www.mr-jatt.org/";
                webView.loadUrl(myUrl);
                homePageLinearLayout.setVisibility(View.GONE);
            }
        });

    }
//......................................................managing editText ...................................................................

    private void manageEditText(TextView tv) {
        InputMethodManager inputMethodManager= (InputMethodManager) tv.getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(tv.getWindowToken(),0);
        StringBuilder builder=new StringBuilder();

        if(editText.getText().toString().contains(".")) {
            if(!(editText.getText().toString().startsWith("http://")) && (!(editText.getText().toString().startsWith("https://")))){
                builder.append("http://");
            }
            builder.append(editText.getText());
            if (URLUtil.isValidUrl(builder.toString())) {
                myUrl = builder.toString();
                //checking for incognito mode
                if (!(preferences.contains(getContext().getString(R.string.pref_incognito))&&preferences.getBoolean(getString(R.string.pref_incognito),false))){

                    addDataToDataBase(editText.getText().toString());
                }
            }

            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
        else
        {   //checking for incognito mode
            if (!(preferences.contains(getContext().getString(R.string.pref_incognito))&&preferences.getBoolean(getString(R.string.pref_incognito),false))){

                addDataToDataBase(editText.getText().toString());
            }

          myUrl="https://www.google.com/search?q="+editText.getText().toString();
        }

      //  String html = "<html><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>body{background-color: #FF4081; padding:15px;font-size:200%}img{width:100%}.videoWrapper{position:relative;padding-bottom:56.25%;padding-top:25px;height:0}.videoWrapper iframe{position:absolute;top:0;left:0;width:100%;height:100%}@media screen and (-webkit-min-device-pixel-ratio: 0){body{word-break:break-word}}</style></head><body><p></p><p></p><p></p><p></p><p></p><p></p><p><div class=\"videoWrapper\"><iframe src=\"https://www.youtube.com/embed/h_UQ6o0Ti0w\" allowfullscreen frameborder=\"0\"></iframe></div></p><p><br></p><h2>Title 1 &nbsp;</h2><p>美麗的生命在於能勇於更新，且願意努力學習。像螃蟹脫殼是為了讓自己長得更健壯；而毛<br>毛蟲蛻變為蝴蝶，或是蝌蚪蛻變為青蛙，才使生命煥然一新。e人的一生，也需要蛻變，才能成長。<br>每一次蛻變都使生命走進人生的新領域、新境界，使我們獲得新的接觸、 新的感受、新的驚喜。<br>周大觀小朋友從小是父母的心肝寶貝，生活過得快樂充實，七歲就會寫詩，到了九歲得到癌<br>症，他開始跟病魔戰鬥，接受截肢手術，雖然少了一隻腿，他仍然努力活下去，並且寫好幾首詩，<br>表達自己不向困境低頭的心意。<br>後來醫師幫助他做化學治療，使得他全身有刺骨之痛，頭髮掉光、身體軟弱無力，可是大觀<br>仍然勇敢地參加為自己而開的醫療會議，聆聽一生命運無望的判決，還表示能夠接受這樣的結論，<br>並向多位醫生叔叔、伯伯道謝，感謝這些日子來辛勞的照顧。<br>這位生命的勇者在十歲時離開人世。可是他並沒有離開我們，因為他留下了另一種生命，就<br>是他的精神和他的詩： ...<span id=\"selectionBoundary_1468824201030_4881703862896598\" class=\"rangySelectionBoundary\">\uFEFF</span><span id=\"selectionBoundary_1468824200207_32529137540397257\" class=\"rangySelectionBoundary\">\uFEFF</span><br></p></body></html>\n";
       // webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        webView.loadUrl(myUrl);

        homePageLinearLayout.setVisibility(View.GONE);

        titleTV.setText(webView.getTitle());
        addressBarCancelImgBtn.setVisibility(View.GONE);//hiding close btn
        refresh_btnAndCloseFrameLayout.setVisibility(View.VISIBLE);
        addBookmarkBtn.setVisibility(View.VISIBLE);
        settingACTV();
    }
//.........................................................adding edittext data to database............................................

    private void addDataToDataBase(String s) {
        new AddDataToDataBaseAsync(s).execute();
    }
    private class AddDataToDataBaseAsync extends AsyncTask<Void,Void,Void>
    {   String s;
        public AddDataToDataBaseAsync(String s) {
            this.s = s;
        }


        @Override
        protected Void doInBackground(Void... params) {
            AutoCompleteTextViewDBAdapter autoCompleteTextViewDBAdapter=new AutoCompleteTextViewDBAdapter(getContext());
            autoCompleteTextViewDBAdapter.openDB();
            if (!autoCompleteTextViewDBAdapter.compareString(s))//cheking that the string is already present in data base or not if not then only add string to data base
                autoCompleteTextViewDBAdapter.addData(s);
            autoCompleteTextViewDBAdapter.closeDB();
            return null;
        }
    }

//....................................................................setting initial webView Setting........................................................
    private void settingWebView() {

        VideoEnabledWebChromeClient  webChromeClient=new VideoEnabledWebChromeClient(nonVideoLayout,videoLayout,loadingView,webView)
        {

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

                TabDataInfo.faviconImage.set(Integer.parseInt(thisTabTag),icon);
                faviconIV.setImageBitmap(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!view.getTitle().contentEquals("HomePage")) {
                    if (titleTV!=null) {
                        titleTV.setVisibility(View.VISIBLE);
                        titleTV.setText(title);
                    }
                    TabDataInfo.tabTitle.set(Integer.parseInt(thisTabTag),title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress==100)
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                mFilePathCallback=filePathCallback;
                pickTheFile();
                return true;
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                if (fullscreen)
                {
                    getActivity().findViewById(R.id.down).setVisibility(View.GONE);
                    //setting addressbar and bottom layout Gone
                    bottomNavLayout.setVisibility(View.GONE);
                    addressbar.setVisibility(View.GONE);
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
                    }
                }
                else
                {
                    getActivity().findViewById(R.id.down).setVisibility(View.VISIBLE);//bottom nevigation should be visible from main activity
                    bottomNavLayout.setVisibility(View.VISIBLE);
                    addressbar.setVisibility(View.VISIBLE);
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                        //
                    }
                }
            }
        });

        webView.setGestureDetector(new GestureDetector(new CustomeGestureDetector()));//for right left up down swipe scroll
        webView.setWebChromeClient(webChromeClient);
        webView.setDownloadListener(new SFDownloadManager(webView,webView.getContext()));
        MyWebViewClient myWebViewClient=new MyWebViewClient();
        webView.setWebViewClient(myWebViewClient);

        //Toast.makeText(getContext(), myUrl, Toast.LENGTH_SHORT).show();
        if (myUrl==null)
        {
            new Runnable(){
                @Override
                public void run() {
                    webView.loadUrl("file:///android_asset/hp.html");
                }
            }.run();

        }
        new Runnable(){
            @Override
            public void run() {
                webView.loadUrl(myUrl);
            }
        }.run();


        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        webView.setDrawingCacheEnabled(true);

        webView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSaveFormData(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setGeolocationEnabled(true);


        settingFont();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
//....................................setting user agent...............................................

   // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public void settingUserAgent(boolean status) {
//
//            String newUserAgent="Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
//        if (status)
//        {
//                webView.getSettings().setUserAgentString(newUserAgent);
//                webView.getSettings().setUseWideViewPort(true);
//                webView.getSettings().setLoadWithOverviewMode(true);
//                settingWebView();
//            }
//            else {
////                webView.getSettings().setUseWideViewPort(false);
////                webView.getSettings().getUserAgentString();
////                webView.getSettings().setLoadWithOverviewMode(true);
////                settingWebView();
//            }
//        }

    //....................................................setting fonts....................................................................
    private void settingFont() {

        webView.getSettings().setFixedFontFamily("casual");
        //its not working

        if (preferences.contains("PREF_textSize"))
        {
            String s=preferences.getString("PREF_textSize","Medium");
            switch (s)
            {
                case "Small":webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);break;

                case "Medium":webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);break;

                case "Large":webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);break;
            }
        }
    }
//...................................................initialize now....................................................................
    private void initializeNow() {

        bottomSwipeLL= (LinearLayout) v.findViewById(R.id.bottomSwipeLL);
        channelArtCardView= (CardView) v.findViewById(R.id.channelArtCardView);
        editText= (AutoCompleteTextView) v.findViewById(R.id.editText);
        webViewParent= (FrameLayout) v.findViewById(R.id.webViewParent);
        faviconIV= (ImageView) v.findViewById(R.id.faviconIV);
        rootLayout=(LinearLayout)v.findViewById(R.id.rootLayout);
        addressbar= (CardView) v.findViewById(R.id.addressBar);
        bottomNavLayout= (CardView) v.findViewById(R.id.bottomNavLayout);
        titleTV= (TextView) v.findViewById(R.id.titleTV);
        refreshImgBtn= (ImageButton) v.findViewById(R.id.refresh_btn);
        closeImgBtn= (ImageButton) v.findViewById(R.id.img_clz_btn);
        webView= (VideoEnabledWebView) v.findViewById(R.id.webViewID);
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);
        leftBtn= (ImageButton) v.findViewById(R.id.left_btn);
        rightBtn= (ImageButton) v.findViewById(R.id.right_btn);
        homeBtn= (ImageButton) v.findViewById(R.id.home_btn);
        dbAdapter=new DBAdapter(v.getContext());
        dbAdapter.openDB();
        channelArtIV=(ImageView)v.findViewById(R.id.channelArtIV);
        preferences=getActivity().getSharedPreferences("SFPrefences",Context.MODE_PRIVATE);
        homePageLinearLayout= (LinearLayout) v.findViewById(R.id.homePageLinearLayout);
        addressBarCancelImgBtn= (ImageButton) v.findViewById(R.id.addressBarCancelImgBtn);
        titleAndSearchFrameLayout= (FrameLayout) v.findViewById(R.id.title_urlEditTextFrameLayout);
        refresh_btnAndCloseFrameLayout= (FrameLayout) v.findViewById(R.id.refresh_btnAndCloseFrameLayout);
        addBookmarkBtn= (ImageButton) v.findViewById(R.id.addBookMark);
        //managing focuses....
        //.............................................full screen webview layout...............
         nonVideoLayout = v.findViewById(R.id.nonVideoLayout); // Your own view, read class comments
         videoLayout = (ViewGroup)v.findViewById(R.id.videoLayout); // Your own view, read class comments
         loadingView =LayoutInflater.from(getContext()).inflate(R.layout.view_loading_video,null); // Your own view, read class comments
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();
        dbAdapter.closeDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
    //.............................................handling webView client..............................................
 private class MyWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//handle to open any intent with link url
            if(url.startsWith("intent://") && url.contains("scheme=http")){
                url = Uri.decode(url);
                String bkpUrl = null;
                Pattern regexBkp = Pattern.compile("intent://(.*?)#");
                Matcher regexMatcherBkp = regexBkp.matcher(url);
                if (regexMatcherBkp.find()) {
                    bkpUrl = regexMatcherBkp.group(1);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+bkpUrl));
                    startActivity(myIntent);
                    return true;
                }else{
                    return false;
                }
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
           // Toast.makeText(getContext(),error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
           // Toast.makeText(getContext(),errorResponse.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Toast.makeText(getContext(), "SSL", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("Alert!!");
            builder.setMessage(error.getPrimaryError()+"\nThe website is not secure\nare you sure want to load ??");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Runnable(){
                        @Override
                        public void run() {

                            handler.proceed();
                        }
                    };
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            myUrl=url;
            String color= "  \""+textColor+"\");  "  ;

           // "javascript:document.body.style.setProperty(\"color\", \"white\")"
            String htmlColor="javascript:document.body.style.setProperty(\"color\","+color;
            view.loadUrl(htmlColor);

// now working fine with help of image compressor
            if (!url.equals("file:///android_asset/hp.html"))
               // convertWebViewToBitmap();

               // this was working fine but too large bitmaps are being generated and this makes the app slower

            //.....................................................
            closeImgBtn.setVisibility(View.INVISIBLE);
            refreshImgBtn.setVisibility(View.VISIBLE);
            addressBarCancelImgBtn.setVisibility(View.GONE);
            if (!url.contentEquals("file:///android_asset/hp.html")) {

                if (!preferences.getBoolean("PREF_incognito",false)) {

                    dbAdapter.addData(view.getTitle(), url, new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date()));
                }
                else {
                    //if incognito is true

                }



            } else {
                titleTV.setVisibility(View.GONE);
            }
            settingFont();//if any changes has made then check for font size and apply it on webview
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            myUrl=url;
            if(!URLUtil.isValidUrl(url))
                view.stopLoading();
            //.............................................................................
            if (!url.equals("file:///android_asset/hp.html")) {
                editText.setText(url);
                homePageLinearLayout.setVisibility(View.GONE);
                thisTabTag=MainActivity.visileTabTag;//getting the visible tab tag as page started
                TabDataInfo.tabUrl.set(Integer.parseInt(thisTabTag),url);
            }
            closeImgBtn.setVisibility(View.VISIBLE);
            refreshImgBtn.setVisibility(View.INVISIBLE);

            if (preferences.getBoolean("PREF_fbFaster", true)) {
                if (url.contains("facebook.com") && (!url.contains("mbasic.facebook.com")) || url.contains("fb.com")) {
                    view.loadUrl("https://mbasic.facebook.com");
                }
            }else{
                if (url.contains("facebook.com") && (url.contains("mbasic.facebook.com")) || url.contains("fb.com")) {
                    view.loadUrl("https://m.facebook.com");
                }
            }
        }
    }

    private void convertWebViewToBitmap() {
          iTA=new imageTabAsyncTask();
         iTA.execute();
    }

    private File getActualImageFile(Bitmap bmp,Bitmap.CompressFormat format,int quality) {

        File f=new File(getContext().getCacheDir(),"tab_image");
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,quality,os);

        byte[] bitmapData=os.toByteArray();

        //write the bytes in file

        try {
            FileOutputStream fileOutputStream=new FileOutputStream(f);
            fileOutputStream.write(bitmapData);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    private class imageTabAsyncTask extends AsyncTask<Void,Void,Void>
    {

        Bitmap bmp;
        Bitmap bmp1= null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (webView!=null) {
                webView.setDrawingCacheEnabled(true);
                webView.buildDrawingCache();
                bmp = Bitmap.createBitmap(webView.getDrawingCache());

                // final Bitmap bmp1=resize(bmp,webView.getWidth()/2,webView.getHeight()/2);

                Canvas canvas = new Canvas(bmp);
                webView.draw(canvas);
            }


    }
        @Override
        protected Void doInBackground(Void... params) {

            if (bmp!=null) {
                try {
                    bmp1 = new Compressor(getActivity())
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(50)
                            .compressToBitmap(getActualImageFile(bmp, Bitmap.CompressFormat.PNG, 50));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bmp1!=null) {
                  //  TabDataInfo.tabImage.set(Integer.parseInt(thisTabTag), bmp1);
                }
            }
        return null;
    }


    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }


// this was working fine but too large bitmaps are being generated and this makes the app slower

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private void pickTheFile() {
        Intent in=new Intent(Intent.ACTION_GET_CONTENT);
        in.setType("*/*");
        startActivityForResult(in,PICK_FILE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_FILE_REQUEST_CODE)
        {
            Uri result=data==null||resultCode!=RESULT_OK?null:data.getData();
            Uri[] uris=new Uri[1];
            uris[0]=result;
            try {
                mFilePathCallback.onReceiveValue(uris);
            }catch (Exception e)
            {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class CustomeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 == null || e2 == null) return false;
            if(e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            else {
                try {
                    if(e1.getY() - e2.getY() > 20 ) {
                        // Hide Actionbar
                       // Toast.makeText(getActivity(), "hide", Toast.LENGTH_SHORT).show();
                        if (!preferences.getBoolean("PREF_full",false))
                        addressbar.setVisibility(View.GONE);
                        return false;
                    }
                    else if (e2.getY() - e1.getY() > 20 ) {
                        // Show Actionbar
                        if (!preferences.getBoolean("PREF_full",false))
                        addressbar.setVisibility(View.VISIBLE);
                       // Toast.makeText(getActivity(), "show", Toast.LENGTH_SHORT).show();
                        return false;
                    }//
                   /* else  if (e1.getX()-e2.getX()>100&&Math.abs(velocityX)>800)
                    {
                       // Toast.makeText(getActivity(), "R 2 l", Toast.LENGTH_SHORT).show();
                        if (webView!=null)
                        {
                            if (webView.canGoForward())
                            {   homePageLinearLayout.setVisibility(View.GONE);
                                webView.goForward();

                            }
                        }

                    }else if (e2.getX()-e1.getX()>100&&Math.abs(velocityX)>800)
                    {
                       // Toast.makeText(getActivity(), "L 2 r", Toast.LENGTH_SHORT).show();
                        if (webView!=null)
                        {
                            if (webView.canGoBack())
                            {
                                webView.goBack();

                                homePageLinearLayout.setVisibility(View.GONE);
                                if (!webView.canGoBack())
                                {
                                    homePageLinearLayout.setVisibility(View.VISIBLE);
                                    faviconIV.setImageResource(R.drawable.google);
                                }
                            }
                            else
                            {
                                homePageLinearLayout.setVisibility(View.VISIBLE);
                                faviconIV.setImageResource(R.drawable.google);
                            }
                        }else {
                            Toast.makeText(getContext(), "Webview is null", Toast.LENGTH_SHORT).show();
                        }
                    }*/

                } catch (Exception e) {
                    assert webView != null;
                    webView.invalidate();
                }
                return false;
            }


        }
    }

}

