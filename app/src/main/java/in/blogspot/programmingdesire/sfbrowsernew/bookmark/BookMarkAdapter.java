package in.blogspot.programmingdesire.sfbrowsernew.bookmark;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.MainActivity;
import in.blogspot.programmingdesire.sfbrowsernew.MainScreen;
import in.blogspot.programmingdesire.sfbrowsernew.R;
import in.blogspot.programmingdesire.sfbrowsernew.database.BookMarkDBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabDataInfo;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemList;

/**
 * Created by Shashi kumar on 08-Apr-18.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.MyViewHolder> {

    Context context;
    List<BookMarkModelClass> modelClassList;
    View v;

    private void gettingList()
    {
        BookMarkDBAdapter adapter=new BookMarkDBAdapter(context);
        adapter.openDB();
        modelClassList= adapter.readData();
        adapter.closeDb();
    }
    public BookMarkAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v= LayoutInflater.from(context).inflate(R.layout.book_row_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.url.setText(modelClassList.get(position).getBM_url());
        holder.title.setText(modelClassList.get(position).getBM_title());
    }

    @Override
    public int getItemCount() {
        gettingList();
        return modelClassList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,url;
        CardView cardView;

       MyViewHolder(View itemView) {
           super(itemView);
           url= (TextView) itemView.findViewById(R.id.url);
           title= (TextView) itemView.findViewById(R.id.title);
           cardView= (CardView) itemView.findViewById(R.id.bookCard);
           cardView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
               @Override
               public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                   MenuItem.OnMenuItemClickListener handler=new MenuItem.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem item) {
                           if (item.getItemId()==123)
                           {
                               modelClassList.remove(getAdapterPosition());
                               BookMarkDBAdapter adapter=new BookMarkDBAdapter(context);
                               adapter.openDB();
                               adapter.deleteAbookMark(title.getText().toString());
                               adapter.closeDb();
                               notifyDataSetChanged();
                               notifyItemRemoved(getAdapterPosition());
                           }
                           if (item.getItemId()==124)
                           {
                               modelClassList.clear();
                               BookMarkDBAdapter adapter=new BookMarkDBAdapter(context);
                               adapter.openDB();
                               adapter.deleteAll();
                               adapter.closeDb();
                               notifyDataSetChanged();
                               notifyItemRemoved(getAdapterPosition());
                           }

                           return true;
                       }
                   };
                   menu.add(0,123,0,"Delete").setOnMenuItemClickListener(handler);
                   menu.add(0,124,0,"Delete All").setOnMenuItemClickListener(handler);
               }
           });
           cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   LaunchNewTab(url);
               }

           });


       }
   }

    private void LaunchNewTab(TextView url) {

        MainScreen m=new MainScreen();
        MainActivity.givenUrl=url.getText().toString();

        m.myUrl=url.getText().toString();
        FragmentTransaction ft=((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frm,m,MainActivity.tabCount+"").commit();

        LinearLayout downFromMain= (LinearLayout) ((FragmentActivity) context).findViewById(R.id.down);
        downFromMain.setVisibility(View.VISIBLE);
       // TabDataInfo.list.add(new TabItemList("BookMark",MainActivity.tabCount+""));
        TabDataInfo.addToList(context,(new TabItemList("BookMark",MainActivity.tabCount+"")));
        MainActivity.visileTabTag=MainActivity.tabCount+"";
        MainActivity.tabCount++;
        MainActivity.realTabCount++;
        TextView textView= (TextView) ((Activity)context).findViewById(R.id.tabTextId);
        textView.setText(MainActivity.realTabCount+"");
        //removing current bookmark fragment
        Fragment fragment=((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag("Book");
        FragmentTransaction transaction=((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment).commit();
    }
}
