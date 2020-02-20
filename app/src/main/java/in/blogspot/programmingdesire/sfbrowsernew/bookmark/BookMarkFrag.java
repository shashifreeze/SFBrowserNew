package in.blogspot.programmingdesire.sfbrowsernew.bookmark;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookMarkFrag extends Fragment {

    View v;
    Button back;
    RecyclerView rv;
    LinearLayout downFromMain;
    BookMarkAdapter bookMarkAdapter;
    public BookMarkFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_bookmark, container, false);
        initializeViews();
        downFromMain.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downFromMain.setVisibility(View.VISIBLE);
                Fragment fragment=((FragmentActivity)getContext()).getSupportFragmentManager().findFragmentByTag("Book");
                FragmentTransaction transaction=((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                transaction.remove(fragment).commit();
            }
        });
        return v;
    }

    private void initializeViews() {
        rv= (RecyclerView) v.findViewById(R.id.bookMarkRV);
        bookMarkAdapter=new BookMarkAdapter(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(bookMarkAdapter);
        back= (Button) v.findViewById(R.id.bookBackBtn);
        downFromMain= (LinearLayout) getActivity().findViewById(R.id.down);
    }




}
