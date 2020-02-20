package in.blogspot.programmingdesire.sfbrowsernew.download_browser;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * Created by Shashi kumar on 28-Mar-18.
 */

public class FileBrowserAdapter extends RecyclerView.Adapter<FileBrowserAdapter.MyViewHolder>{
List<FileModelClass> modelClassList=new ArrayList<>();
    Context context;
    FolderHistory folderHistory;//
    Stack<String> stack;
   FileExplorer fileExplorer;
    public FileBrowserAdapter( Context context,FileExplorer fileExplorer) {
        this.context = context;
        this.fileExplorer=fileExplorer;
        folderHistory=new FolderHistory();
        stack=folderHistory.getFolderHistory();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.file_browser_single_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.iv.setImageResource(getImageId(modelClassList.get(position).getIconName()));
        holder.filenameTV.setText(modelClassList.get(position).getFileName());
        holder.fileSizeTV.setText(modelClassList.get(position).getFileSize());
    }
    public int getImageId(String s)
    {
        return  context.getResources().getIdentifier("drawable/" + s,null,context.getPackageName());
    }

    //array of model class setting object
    public void setListContent(ArrayList<FileModelClass> list)
    {
        modelClassList=list;
        notifyItemRangeChanged(0,list.size());
    }
    //when hardware back button is pressed go to parent directory
    public boolean goBackParent()
    {
        if (stack.isEmpty())
            return true;
        stack.pop();
        if (!stack.isEmpty())
            fileExplorer.populateRecyclerViewValues(stack.peek());
        return false;


    }
    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView iv;
        TextView filenameTV,fileSizeTV;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.file_Icon_IV);
            filenameTV= (TextView) itemView.findViewById(R.id.file_name_TV);
            fileSizeTV= (TextView) itemView.findViewById(R.id.file_size_TV);
            iv.setOnClickListener(this);
            filenameTV.setOnClickListener(this);
            fileSizeTV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            String clicked = filenameTV.getText().toString();

            if (stack.isEmpty())
                stack = folderHistory.getFolderHistory();
            String history = stack.peek();
            File clickedFile = new File(history, clicked);

            if (clickedFile.isDirectory()) {
                stack.push(clickedFile.toString());
                fileExplorer.populateRecyclerViewValues(clickedFile.toString());
            } else {
                String ext = getExtension(clickedFile.toString());
                fileExplorer.playFile(clickedFile.toString(),ext);
            }
        }
        }

    private String getExtension(String s) {
        int l = s.length();

        return s.substring(l-3,l);
    }
}

