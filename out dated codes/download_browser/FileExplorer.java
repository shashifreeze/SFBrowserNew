package in.blogspot.programmingdesire.sfbrowsernew.download_browser;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileExplorer extends Fragment {

View v;
    LinearLayout downFromMain;
    RecyclerView rv;
   public FileBrowserAdapter fileBrowserAdapter;
    private ArrayList<FileModelClass> fileModelClassArrayList;
    public FileExplorer() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.file_explorer, container, false);
        findingIdOfViews();
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        fileBrowserAdapter=new FileBrowserAdapter(getContext(),this);
        populateRecyclerViewValues("SFDownloads");
        Button exit= (Button) v.findViewById(R.id.exit_dnd);
        downFromMain= (LinearLayout) getActivity().findViewById(R.id.down);
        downFromMain.setVisibility(View.GONE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downFromMain.setVisibility(View.VISIBLE);
                Fragment fragment=getActivity().getSupportFragmentManager().findFragmentByTag("Downloads");
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment).commit();
            }
        });
        return v;
    }

    private void findingIdOfViews() {
        rv= (RecyclerView) v.findViewById(R.id.fileBrowserRv);

    }
    public  void populateRecyclerViewValues(String fileName)
    {
        fileModelClassArrayList=new ArrayList<>();
        FileList f=new FileList(fileName);
        String [][] data=f.getFileList();
        for (int i=0;i<data.length;i++)
        {
            FileModelClass aClass=new FileModelClass();
            aClass.setFileName(data[i][0]);
            aClass.setFileSize(data[i][1]);
            aClass.setIconName(data[i][2]);
            fileModelClassArrayList.add(aClass);
        }
        fileBrowserAdapter.setListContent(fileModelClassArrayList);
        rv.setAdapter(fileBrowserAdapter);

    }
    public void playFile(String clickedFile,String ext)
    {
        File file = new File(clickedFile);
        Intent target = new Intent(Intent.ACTION_VIEW);
        if(ext.compareTo("pdf")==0)
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
        else if(ext.compareTo("mp3")==0)
            target.setDataAndType(Uri.fromFile(file),"audio/*");
        else if(ext.compareTo("txt")==0)
            target.setDataAndType(Uri.fromFile(file),"text/plain");
        else if(ext.compareTo("png")==0 ||ext.compareTo("psd")==0 ||ext.compareTo("jpg")==0 ||ext.compareTo("gif")==0)
            target.setDataAndType(Uri.fromFile(file),"image/*");
        else if(ext.compareTo("mp4")==0 ||ext.compareTo("mkv")==0 ||ext.compareTo("3gp")==0 ||ext.compareTo("avi")==0)
        target.setDataAndType(Uri.fromFile(file),"video/*");
        else if (ext.compareTo("zip")==0 ||ext.compareTo("rar")==0)
            target.setDataAndType(Uri.fromFile(file),"/*");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }


}
