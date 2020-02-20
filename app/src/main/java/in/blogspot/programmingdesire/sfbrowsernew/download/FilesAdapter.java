package in.blogspot.programmingdesire.sfbrowsernew.download;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.R;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.MyUtils;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.OpenFileByClick;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesViewHolder> {

    private  List<File> filesList;
    private View view;

    public FilesAdapter(List<File> filesList) {
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.files_single_row_item,parent,false);

        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, int position) {

        File file=filesList.get(position);
        String filename=file.getName();
        long filesize=file.length();
        Date date=new Date(file.lastModified());
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy HH:MM");
        String filedate = sm.format(date);

        String file_extension=FileUtils.getExtension(file.getAbsolutePath());

        holder.file_name.setText(filename);
        holder.file_ext.setText(file_extension);
        holder.file_size.setText(MyUtils.BytesToMbKb(filesize));
        holder.file_date.setText(filedate);

        //setting click listener
        holder.itemView.setOnClickListener(v -> OpenFileByClick.openFile(v.getContext(),file.getAbsolutePath(),file_extension));
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    class FilesViewHolder extends RecyclerView.ViewHolder
    {

        TextView file_name,file_size,file_ext,file_date;

        FilesViewHolder(View itemView) {
            super(itemView);
            file_name=itemView.findViewById(R.id.files_fileName_TV);
            file_date=itemView.findViewById(R.id.files_fileDate_TV);
            file_size=itemView.findViewById(R.id.files_fileSize_TV);
            file_ext=itemView.findViewById(R.id.files_fileExt_TV);
        }
    }
}
