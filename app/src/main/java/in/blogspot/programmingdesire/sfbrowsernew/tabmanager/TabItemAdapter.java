package in.blogspot.programmingdesire.sfbrowsernew.tabmanager;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.MainActivity;
import in.blogspot.programmingdesire.sfbrowsernew.MainScreen;
import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * Created by Shashi kumar on 16-Feb-18.
 */

public class TabItemAdapter extends RecyclerView.Adapter<TabItemAdapter.MyViewHolder> {
    Context context;
    private String tabtagString;
    Dialog dialog;
   // List<TabItemList> TabDataInfo.list=TabDataInfo.list;

    public TabItemAdapter(Context context,Dialog dialog) {
        this.context = context;
       // this.TabDataInfo.list = TabDataInfo.list;
        this.dialog=dialog;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //for grid tab view
       // View view= LayoutInflater.from(context).inflate(R.layout.tab_item_new,parent,false);

        //for linear tab view
        View view= LayoutInflater.from(context).inflate(R.layout.tab_item,parent,false);
        return new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        new MyTask(position,holder).execute();

    }

    @Override
    public int getItemCount() {
        return TabDataInfo.getList(context).size();
    }

 public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView b1,urlTV; CardView cardView;
        ImageView imageView,tabFavicon;

        MyViewHolder(View itemView) {
            super(itemView);
            b1= (TextView) itemView.findViewById(R.id.b1);
            cardView= (CardView) itemView.findViewById(R.id.tabRowCard);
            imageView= (ImageView) itemView.findViewById(R.id.tabImage);
            tabFavicon= (ImageView) itemView.findViewById(R.id.tabFavicon);
            urlTV= (TextView) itemView.findViewById(R.id.urlTV);
        }
    }
    public void removeRowItemWithSwipe(int pos)
    {
        if (TabDataInfo.getList(context).size()!=1) {
            tabtagString = TabDataInfo.getList(context).get(pos).getTabTag();
           // TabDataInfo.getList(context).remove(pos);//removes the swiped item from list
            TabDataInfo.deleteTab(context,tabtagString);
            this.notifyItemRemoved(pos);
            notifyItemRangeChanged(0,TabDataInfo.getList(context).size());
        }
    }
    private MainScreen getVisibleFragment() {
        FragmentManager fragmentManager=((FragmentActivity)context).getSupportFragmentManager();
        List<Fragment> fragments= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fragments = fragmentManager.getFragments();
        }
        if (fragments!=null)
        {
            for (Fragment fragment:fragments)
            {
                if (fragment!=null&&!fragment.isHidden())
                {
                    return (MainScreen)(fragment);
                }
            }
        }
        return null;
    }
    private class MyTask extends AsyncTask<Void,Void,Void>
    {
        int position;
        MyViewHolder holder;
        String tag;
        private MyTask(int pos,MyViewHolder holder) {
            position=pos;
            this.holder=holder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            tag=TabDataInfo.getList(context).get(position).getTabTag();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (MainActivity.visileTabTag.equals(tag))
            {
                holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.style_selected_tab));
            }
            else {
               // holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.style_non_selected_tab));
            }
            if (TabDataInfo.faviconImage.size()>0)
                holder.tabFavicon.setImageBitmap( TabDataInfo.faviconImage.get(Integer.parseInt(tag)));//getting the icon

            if (TabDataInfo.tabTitle.size()>0)
                holder.b1.setText( TabDataInfo.tabTitle.get(Integer.parseInt(tag)));//getting the title

            holder.urlTV.setText(TabDataInfo.tabUrl.get(Integer.parseInt(tag)));
            // this is working fine but there is performance issue with this technique as bitmaps are too heavy to crash the app

            //for the time being i am not using this technique
           // if (TabDataInfo.tabImage.size()>0)
             //   holder.imageView.setImageBitmap( TabDataInfo.tabImage.get(Integer.parseInt(tag)));


        }
    }
}
