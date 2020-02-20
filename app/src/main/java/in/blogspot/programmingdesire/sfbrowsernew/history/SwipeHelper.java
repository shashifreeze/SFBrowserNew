package in.blogspot.programmingdesire.sfbrowsernew.history;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by Shashi kumar on 10-Mar-18.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {
   HisAdapter hisAdapter;

    public SwipeHelper(HisAdapter hisAdapter) {
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT);
        this.hisAdapter = hisAdapter;
    }

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            hisAdapter.dismissHis(viewHolder.getAdapterPosition());


    }


}
