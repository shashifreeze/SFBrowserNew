package in.blogspot.programmingdesire.sfbrowsernew.history;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import  in.blogspot.programmingdesire.sfbrowsernew.R;
import  in.blogspot.programmingdesire.sfbrowsernew.database.DBAdapter;

/**
 * Created by Shashi kumar on 07-Feb-18.
 */

public class HisAdapter extends RecyclerView.Adapter<HisAdapter.MyViewHolder> {
    Context context;
    List<HistoryRowItem> rowItems;

    public HisAdapter(Context context, List<HistoryRowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.history_row_item,null,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.hisURL.setText(rowItems.get(position).getHisUrl());
        holder.hisDate.setText(rowItems.get(position).getHisDate());
        holder.hisTitle.setText(rowItems.get(position).getHisTitle());

    }

    @Override
    public int getItemCount() {
        return rowItems.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hisTitle, hisURL, hisDate;

        MyViewHolder(View itemView) {
            super(itemView);

            hisDate = (TextView) itemView.findViewById(R.id.hisDateId);
            hisTitle = (TextView) itemView.findViewById(R.id.hisTitleId);
            hisURL = (TextView) itemView.findViewById(R.id.hisLinkId);

        }
    }
    //dismiss
public void dismissHis(int pos)

{   String ht=rowItems.get(pos).getHisTitle();
    rowItems.remove(pos);
    this.notifyItemRemoved(pos);
    DBAdapter dbAdapter=new DBAdapter(context);
    dbAdapter.openDB();
    dbAdapter.deleteSingleRow(ht);
    dbAdapter.closeDB();
    Toast.makeText(context, "Row Deleted", Toast.LENGTH_SHORT).show();
}
}


