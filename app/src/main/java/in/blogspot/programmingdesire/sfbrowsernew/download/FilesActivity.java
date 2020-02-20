package in.blogspot.programmingdesire.sfbrowsernew.download;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import java.io.File;

import in.blogspot.programmingdesire.sfbrowsernew.R;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.GetFilesFromFolder;

public class FilesActivity extends AppCompatActivity {
    private RecyclerView filesRV;
    public static String directoryPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        init();
        if(GetFilesFromFolder.getAllFilesList(new File(directoryPath)).size()==0)
        {
            Toast.makeText(this, "No Files Found", Toast.LENGTH_SHORT).show();
        }
        FilesAdapter filesAdapter=new FilesAdapter(GetFilesFromFolder.getAllFilesList(new File(directoryPath)));
        filesRV.setLayoutManager(new LinearLayoutManager(this));
        filesRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        filesRV.setAdapter(filesAdapter);

    }

    private void init() {

        filesRV=findViewById(R.id.filesRV);
    }

}
