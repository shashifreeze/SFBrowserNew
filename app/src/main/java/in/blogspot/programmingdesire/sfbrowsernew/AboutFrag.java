package in.blogspot.programmingdesire.sfbrowsernew;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabDataInfo;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFrag extends Fragment {
View v;
TextView fileExt,socialMedia,flatICon,appVersion;
    LinearLayout downFromMain;
    Button exit;

    TextView downloadLisene,privacyPolicy;
    public AboutFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_about, container, false);
        init();
        appVersion.setText("Version "+BuildConfig.VERSION_NAME);
        downFromMain.setVisibility(View.GONE);
        fileExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNewTab("https://www.flaticon.com/authors/dimitry-miroliubov");
            }
        });
        flatICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNewTab("https://www.flaticon.com");
            }
        });
        socialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNewTab("https://www.freepik.com/");
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downFromMain.setVisibility(View.VISIBLE);
                Fragment fragment=((FragmentActivity)getContext()).getSupportFragmentManager().findFragmentByTag("About");
                FragmentTransaction transaction=((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                transaction.remove(fragment).commit();
            }
        });
        downloadLisene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNewTab("https://drive.google.com/file/d/1fcOYLN7ukMDK_4ezJ9TYvOIaXwURTel8/view?usp=sharing");
                LaunchNewTab("https://drive.google.com/file/d/1fVS0Kj_jf8rDsvDBpx5ThmEwiwLmTws0/view?usp=sharing");
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNewTab("https://sites.google.com/view/sfbrowser/privacypolicy");
            }
        });
        return v;

    }

    private void LaunchNewTab(String url) {
        MainScreen m=new MainScreen();
        MainActivity.givenUrl=url;

        m.myUrl=url;
        FragmentTransaction ft=((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frm,m,MainActivity.tabCount+"").commit();

        // TabDataInfo.list.add(new TabItemList("BookMark",MainActivity.tabCount+""));
        TabDataInfo.addToList(getContext(),(new TabItemList("Credit Link",MainActivity.tabCount+"")));
        MainActivity.visileTabTag=MainActivity.tabCount+"";
        MainActivity.tabCount++;
        MainActivity.realTabCount++;
        TextView textView= (TextView) ((Activity)getContext()).findViewById(R.id.tabTextId);
        textView.setText(MainActivity.realTabCount+"");
        //removing current bookmark fragment
        downFromMain.setVisibility(View.VISIBLE);
        Fragment fragment=((FragmentActivity)getContext()).getSupportFragmentManager().findFragmentByTag("About");
        FragmentTransaction transaction=((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment).commit();
    }

    private void init() {
        downloadLisene= (TextView) v.findViewById(R.id.downloadLisene);
        fileExt= (TextView) v.findViewById(R.id.fileExtension);
        socialMedia= (TextView) v.findViewById(R.id.socialMedia);
        flatICon= (TextView) v.findViewById(R.id.flaticonLink);
        exit=(Button) v.findViewById(R.id.exitAbout);
        downFromMain= (LinearLayout) getActivity().findViewById(R.id.down);
        privacyPolicy=(TextView)v.findViewById(R.id.privacyPolicy);
        appVersion=v.findViewById(R.id.app_version);
    }

}
