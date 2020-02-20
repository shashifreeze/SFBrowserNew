package in.blogspot.programmingdesire.sfbrowsernew.tabmanager;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.widget.TextView;

import in.blogspot.programmingdesire.sfbrowsernew.MainActivity;
import in.blogspot.programmingdesire.sfbrowsernew.MainScreen;
import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * Created by Shashi kumar on 10-Mar-18.
 */

public class TabSwipeHelper extends ItemTouchHelper.SimpleCallback {
   TabItemAdapter tabItemAdapter;
    Context context;
    MainScreen mainScreen;
    public TabSwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public TabSwipeHelper(TabItemAdapter tabItemAdapter, Context context, MainScreen mainScreen) {
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT);
        this.tabItemAdapter = tabItemAdapter;
        this.context=context;
        this.mainScreen=mainScreen;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        mainScreen.webView.stopLoading();
        tabItemAdapter.removeRowItemWithSwipe(viewHolder.getAdapterPosition());
        if (MainActivity.realTabCount!=1)
        MainActivity.realTabCount=MainActivity.realTabCount-1;
       TextView textView= (TextView) ((Activity)context).findViewById(R.id.tabTextId);
        textView.setText(MainActivity.realTabCount+"");


    }
}
