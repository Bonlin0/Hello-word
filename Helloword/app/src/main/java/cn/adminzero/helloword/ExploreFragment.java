package cn.adminzero.helloword;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


        TextView groupNameTextView = view.findViewById(R.id.groupNameTextView);

        TextView groupMemberNumberTextView = view.findViewById(R.id.groupMemberNumberTextView);
        TextView groupRankingNumberTextView = view.findViewById(R.id.groupRankingNumberTextView);
        //TODO 为小组组名和各种状态赋值

        // TODO 称号需要达到略有所得（Level=2）才可以解锁PKgame

        TextView pkLevelTextView = view.findViewById(R.id.pkLevelTextView);
        TextView pKRankingNumberTextView = view.findViewById(R.id.pKRankingNumberTextView);
        TextView pkPointNumberTextView = view.findViewById(R.id.pkPointNumberTextView);

        Button goToGroupButton = view.findViewById(R.id.goToGroupButton);
        goToGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyGroupActivity.class);
                startActivity(intent);
            }
        });

        Button goToPkButton = view.findViewById(R.id.goToPkButton);
        goToPkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GameQueueActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
