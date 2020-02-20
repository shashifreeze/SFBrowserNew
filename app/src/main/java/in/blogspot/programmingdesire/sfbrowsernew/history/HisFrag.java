package in.blogspot.programmingdesire.sfbrowsernew.history;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.MainActivity;
import in.blogspot.programmingdesire.sfbrowsernew.MainScreen;
import in.blogspot.programmingdesire.sfbrowsernew.R;
import in.blogspot.programmingdesire.sfbrowsernew.database.DBAdapter;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabDataInfo;
import in.blogspot.programmingdesire.sfbrowsernew.tabmanager.TabItemList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HisFrag extends Fragment {
View v;
    RecyclerView recyclerView;
    HistoryData historyData;
    List<HistoryRowItem> historyRowItemList=new ArrayList<>();
    Button clearBtn,backBtn;
    LinearLayout downFromMain;

    public HisFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_his, container, false);
        historyData=new HistoryData(v.getContext());
        historyData.fillingDataIntoArray();

        downFromMain= (LinearLayout) getActivity().findViewById(R.id.down);
        downFromMain.setVisibility(View.GONE);
        clearBtn= (Button) v.findViewById(R.id.clearHistoryId);
        backBtn= (Button) v.findViewById(R.id.backHisId);
        recyclerView= (RecyclerView) v.findViewById(R.id.HisRecyclerId);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        HisAdapter hisAdapter=new HisAdapter(v.getContext(),historyRowItemList);
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(hisAdapter);
        recyclerView.addOnItemTouchListener(new HisTouch(v.getContext(),recyclerView, new HisTouch.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                    HistoryRowItem item=historyRowItemList.get(position);
                    String url=item.getHisUrl();
                MainScreen mainScreen=new MainScreen();
                mainScreen.myUrl=url;
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frm,mainScreen,MainActivity.tabCount+"").commit();
                //TabDataInfo.list.add(new TabItemList("History",MainActivity.tabCount+""));
                TabDataInfo.addToList(getContext(),new TabItemList("History",MainActivity.tabCount+""));
                MainActivity.visileTabTag=MainActivity.tabCount+"";
                MainActivity.realTabCount++;
                TextView textView= (TextView) (getActivity()).findViewById(R.id.tabTextId);
                textView.setText(MainActivity.realTabCount+"");
                MainActivity.tabCount++;
                downFromMain.setVisibility(View.VISIBLE);
                handleBackButton();

            }
        }));
        //to handle swipe to delete a single row
        ItemTouchHelper.Callback callback=new SwipeHelper(hisAdapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        if (historyData.urlDate!=null) {
            fillData();
        }
        else
        {
            Toast.makeText(v.getContext(), "History Empty", Toast.LENGTH_SHORT).show();
            handleBackButton();
        }
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b=new AlertDialog.Builder(getContext());
                b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                b.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBAdapter dbAdapter=new DBAdapter(getContext());
                        dbAdapter.openDB();
                        dbAdapter.deleteData();
                        dbAdapter.closeDB();
                        recyclerView.setVisibility(View.INVISIBLE);
                        Toast.makeText(v.getContext(), "History deleted", Toast.LENGTH_SHORT).show();
                        handleBackButton();
                    }
                });
                b.setTitle("Are you sure want to clear All History??");

                AlertDialog alertDialog=b.create();
                alertDialog.show();

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackButton();
            }
        });
        return v;
    }

    private void handleBackButton() {
        downFromMain.setVisibility(View.VISIBLE);
        Fragment fragment=getActivity().getSupportFragmentManager().findFragmentByTag("newHisTab");
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment).commit();

    }

    private void fillData() {

        for (int i=historyData.urlArray.length-1;i>=0;i--)
        {
            HistoryRowItem item=new HistoryRowItem(historyData.urlTitle[i],historyData.urlArray[i],historyData.urlDate[i]);
            historyRowItemList.add(item);
        }
    }

}
