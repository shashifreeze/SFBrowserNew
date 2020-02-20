package in.blogspot.programmingdesire.sfbrowsernew.download;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.blogspot.programmingdesire.sfbrowsernew.R;

public class DownloadActivity extends AppCompatActivity {

    private Button downloadingBtn,filesBtn;
    private FrameLayout downloadListFrameLayout;
    private LinearLayout files_linearLayout;
    private ImageView audio,video,doc,image;
    private Button others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initViews();
        showFiles(false);
        downloadingBtn.setEnabled(false);
        downloadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (downloadingBtn.isEnabled())
                {
                    openDownloadingFragment();
                    showFiles(false);
                    downloadingBtn.setEnabled(false);
                    filesBtn.setEnabled(true);
                    filesBtn.setTextColor(getResources().getColor(R.color.colorGreen));
                    filesBtn.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    downloadingBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                    downloadingBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                }
            }
        });

        filesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filesBtn.isEnabled())
                {
                    showFiles(true);
                    filesBtn.setEnabled(false);
                    downloadingBtn.setEnabled(true);
                    filesBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                    filesBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    downloadingBtn.setTextColor(getResources().getColor(R.color.colorGreen));
                    downloadingBtn.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
            }
        });

        openDownloadingFragment();// downloading fragment contains the downloading lists

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //necessary to say which directory to open for files
                FilesActivity.directoryPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/music";

                openFilesActivity();            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //necessary to say which directory to open for files
                FilesActivity.directoryPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/video";

                openFilesActivity();
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //necessary to say which directory to open for files
                FilesActivity.directoryPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/others";
                openFilesActivity();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //necessary to say which directory to open for files
                FilesActivity.directoryPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/image";
                openFilesActivity();
            }
        });
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //necessary to say which directory to open for files
                FilesActivity.directoryPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/SFDownloads/documents";
               openFilesActivity();
            }
        });
        
    }

    private void openFilesActivity() {
        startActivity(new Intent(DownloadActivity.this,FilesActivity.class));
    }

    public void showFiles(boolean t) {
        if (t)
        {
            downloadListFrameLayout.setVisibility(View.INVISIBLE);
            files_linearLayout.setVisibility(View.VISIBLE);

        }
        else {
            downloadListFrameLayout.setVisibility(View.VISIBLE);
            files_linearLayout.setVisibility(View.INVISIBLE);
        }

    }



    private void openDownloadingFragment() {

        DownloadingFiles downloadingFiles=new DownloadingFiles();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.downloadListFrameLayout,downloadingFiles).commit();
    }

    private void initViews() {
        downloadingBtn=findViewById(R.id.downloadingBtn);
        filesBtn=findViewById(R.id.filesBtn);
        downloadListFrameLayout=findViewById(R.id.downloadListFrameLayout);
        files_linearLayout=findViewById(R.id.files_ll);
        audio=findViewById(R.id.filesAudioIV);
        video=findViewById(R.id.filesVideoIV);
        doc=findViewById(R.id.filesDocumetIV);
        image=findViewById(R.id.filesImageIV);
        others=findViewById(R.id.filesOtherBtn);

    }
}
