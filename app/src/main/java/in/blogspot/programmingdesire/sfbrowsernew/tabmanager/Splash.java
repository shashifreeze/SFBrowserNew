package in.blogspot.programmingdesire.sfbrowsernew.tabmanager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import in.blogspot.programmingdesire.sfbrowsernew.MainActivity;
import in.blogspot.programmingdesire.sfbrowsernew.R;

public class Splash extends AppCompatActivity {
TextView tagLine,product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/3M Trislan.ttf");
        product= (TextView) findViewById(R.id.product);
        product.setTypeface(typeface);
        deleteDatabase("TAB_DB");

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        },400);
    }

    }

