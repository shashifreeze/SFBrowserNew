package in.blogspot.programmingdesire.sfbrowsernew;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import in.blogspot.programmingdesire.sfbrowsernew.database.AutoCompleteTextViewDBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.database.DBAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private View v;
   private Button clearHis,clearCache,clearCookies,exitBtn,clearSearchBarHis;
    private RadioButton smallBtn,mediumBtn,largeBtn;
    CheckBox allowCookiesCB,textOnlyCB;
   private SharedPreferences p;
    RadioGroup rg;
    Button selectTextColor;
    LinearLayout downFromMain;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_setting, container, false);
        initializeViews();
        downFromMain.setVisibility(View.GONE);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downFromMain.setVisibility(View.VISIBLE);
                Fragment f=((FragmentActivity)getContext()).getSupportFragmentManager().findFragmentByTag("settings");
                FragmentTransaction t=getActivity().getSupportFragmentManager().beginTransaction();
                t.remove(f).commit();
            }
        });
        settingClicks();
        selectTextColor.setText("Select textcolor("+p.getString("PREF_textColor","black")+")");
        gettingFontSizeAndSettingToRadioBtn_And_Allow_Cookies();
        return v;
    }

    private void gettingFontSizeAndSettingToRadioBtn_And_Allow_Cookies() {
        if (p.contains(getContext().getString(R.string.pref_AllowCookies)))
        {
            boolean b=p.getBoolean(getContext().getString(R.string.pref_AllowCookies),true);
            allowCookiesCB.setChecked(b);
        }
        if (p.contains(getContext().getString(R.string.pref_text_size)))
        {
            String s=p.getString(getContext().getString(R.string.pref_text_size),"Medium");
            switch (s)
            {
                case "Small":smallBtn.setChecked(true);break;
                case "Medium":mediumBtn.setChecked(true);break;
                case "Large":largeBtn.setChecked(true);break;
            }
        }
    }

    private void settingClicks() {

        allowCookiesCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CookieSyncManager.createInstance(getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                SharedPreferences.Editor e=p.edit();
                e.putBoolean(getContext().getString(R.string.pref_AllowCookies),isChecked).apply();
                if (isChecked)
                {

                    cookieManager.setAcceptCookie(true);
                }else {
                    cookieManager.setAcceptCookie(false);
                }
            }
        });
//        textOnlyCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                SharedPreferences.Editor e=p.edit();
//                e.putBoolean(getContext().getString(R.string.pref_textOnly),isChecked).apply();
//            }
//        });

        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache();
            }
        });
        clearCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCookies();

            }
        });
        clearHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHis();
            }
        });
        selectTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTextColor();
            }
        });
        clearSearchBarHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchBarHis();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId==R.id.smallText)
                {
                    SharedPreferences.Editor e=p.edit();
                    e.putString(getContext().getString(R.string.pref_text_size),"Small").apply();

                }else
                if (checkedId==R.id.mediumText)
                {
                    SharedPreferences.Editor e=p.edit();
                    e.putString(getContext().getString(R.string.pref_text_size),"Medium").apply();

                }else {
                    SharedPreferences.Editor e=p.edit();
                    e.putString(getContext().getString(R.string.pref_text_size),"Large").apply();
                }
            }
        });

    }

    private void clearSearchBarHis() {
        AlertDialog.Builder b=new AlertDialog.Builder(getContext());
        b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        b.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    AutoCompleteTextViewDBAdapter adapter = new AutoCompleteTextViewDBAdapter(getContext());
                    adapter.openDB();
                    adapter.deleteAll();
                    adapter.closeDB();
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.setTitle("Alert!");
        b.setMessage("Are you sure you want to delete search bar history");

        AlertDialog alertDialog=b.create();
        alertDialog.show();

    }

    private void selectTextColor() {

        String defaultColor=p.getString("PREF_textColor","black");
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Enter a valid color name");
        final EditText editText=new EditText(getContext());

        builder.setView(editText);
        editText.setHint(defaultColor);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor e=p.edit();
                e.putString("PREF_textColor",editText.getText().toString()).apply();
                selectTextColor.setText("Select Text Color("+editText.getText()+")");
                Toast.makeText(getContext(), "Restart app to apply effect", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    private void deleteCookies() {
        AlertDialog.Builder b=new AlertDialog.Builder(getContext());
        b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        b.setPositiveButton("delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CookieSyncManager.createInstance(getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                cookieManager.setAcceptCookie(false);
            }
        });
        b.setTitle("Are you sure want to clear All Cookies??");

        AlertDialog alertDialog=b.create();
        alertDialog.show();
    }

    private void clearHis() {
        AlertDialog.Builder b=new AlertDialog.Builder(getContext());
        b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        b.setPositiveButton("delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBAdapter dbAdapter=new DBAdapter(getContext());//history databaseAdapter
                dbAdapter.openDB();
                dbAdapter.deleteData();
                dbAdapter.closeDB();
            }
        });
        b.setTitle("Are you sure want to clear All History??");

        AlertDialog alertDialog=b.create();
        alertDialog.show();
    }

    private void deleteCache() {
        AlertDialog.Builder b=new AlertDialog.Builder(getContext());
        b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        b.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //FileUtils.deleteQuietly(getContext().getCacheDir());
            }
        });
        b.setTitle("Are you sure want to clear App Cache??");

        AlertDialog alertDialog=b.create();
        alertDialog.show();
    }

    private void initializeViews() {
        clearSearchBarHis= (Button) v.findViewById(R.id.clearSearchBarHis);
        downFromMain= (LinearLayout) getActivity().findViewById(R.id.down);
        clearCache= (Button) v.findViewById(R.id.clearCacheBtn);
        rg= (RadioGroup) v.findViewById(R.id.rg);
        allowCookiesCB= (CheckBox) v.findViewById(R.id.alloCookiesID);
        // textOnlyCB= (CheckBox) v.findViewById(R.id.textOnlyId);
        clearCookies= (Button) v.findViewById(R.id.clearCookiesBtn);
        clearHis= (Button) v.findViewById(R.id.clearHisBtnInSetting);
        smallBtn= (RadioButton) v.findViewById(R.id.smallText);
        mediumBtn= (RadioButton) v.findViewById(R.id.mediumText);
        largeBtn= (RadioButton) v.findViewById(R.id.largeText);
        exitBtn= (Button) v.findViewById(R.id.exitSettingsBtn);
        selectTextColor= (Button) v.findViewById(R.id.selectTextColor);
        p =getContext().getSharedPreferences(getContext().getString(R.string.SF_PREFENCES), Context.MODE_PRIVATE);
    }

}
