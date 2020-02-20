package in.blogspot.programmingdesire.sfbrowsernew.download;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadingFiles extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    public DownloadingFiles() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_downloading_files, container, false);
        initViews();
        List<DownloadingFileModelClass> list=DownloadsDataInfo.getList(getContext());
        DownloadingFileAdapter downloadingFileAdapter=new DownloadingFileAdapter(list);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(downloadingFileAdapter);
        return view;
    }

    private void initViews() {

        recyclerView=view.findViewById(R.id.downloadingListRV);
    }

}
