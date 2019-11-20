package cn.adminzero.helloword;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {


    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        TextView userNameTextView = view.findViewById(R.id.userNameTextView);
        userNameTextView.setText(App.userNoPassword_global.getUserNickName());

        TextView userIDText = view.findViewById(R.id.userIDText);
        userIDText.setText("ID: "+App.userNoPassword_global.getUserID());

        TextView dakaDaysText = view.findViewById(R.id.dakaDaysText);
        dakaDaysText.setText("打卡  "+ App.userNoPassword_global.getDays() +" 天");
        return view;
    }

}
