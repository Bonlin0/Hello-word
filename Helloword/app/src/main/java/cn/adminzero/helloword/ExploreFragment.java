package cn.adminzero.helloword;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
        Button goToPkButton = view.findViewById(R.id.goToPkButton);
        ImageView pkLevelImageView = view.findViewById(R.id.pkLevelImageView);
        if(App.userNoPassword_global.getLevel()<=1) {
            pkLevelTextView.setText("称号等级为2时方可使用");
            pKRankingNumberTextView.setText("-");
            pkPointNumberTextView.setText("-");
            // 按钮设成 enabled false
            goToPkButton.setEnabled(false);
        }
        else{
            // 段位分级
            String pkLevelText = null;

            pKRankingNumberTextView.setText(">50");
            Integer pkPointsInteger = new Integer(App.userNoPassword_global.getpKPoint());
            pkPointNumberTextView.setText(pkPointsInteger.toString());

            if(pkPointsInteger<= 1000)
            {
                pkLevelText = "黑铁";
                pkLevelImageView.setImageResource(R.drawable.level_logo1);
            }
            else if(pkPointsInteger<1100)
            {
                pkLevelText = "青铜";
                pkLevelImageView.setImageResource(R.drawable.level_logo2);
            }
            else if(pkPointsInteger<1200)
            {
                pkLevelText = "白银";
                pkLevelImageView.setImageResource(R.drawable.level_logo3);
            }
            else if(pkPointsInteger<1300)
            {
                pkLevelText = "黄金";
                pkLevelImageView.setImageResource(R.drawable.level_logo4);
            }
            else if(pkPointsInteger<1450)
            {
                pkLevelText = "铂金";
                pkLevelImageView.setImageResource(R.drawable.level_logo5);
            }
            else if(pkPointsInteger<1600)
            {
                pkLevelText = "钻石";
                pkLevelImageView.setImageResource(R.drawable.level_logo6);
            }
            else if(pkPointsInteger<1800)
            {
                pkLevelText = "宗师";
                pkLevelImageView.setImageResource(R.drawable.level_logo7);
            }
            else
            {
                pkLevelText = "王者";
                pkLevelImageView.setImageResource(R.drawable.level_logo8);
            }
            goToPkButton.setEnabled(true);
            pkLevelTextView.setText(pkLevelText);
        }

        // 为界面的两个按钮设置监听
        Button goToGroupButton = view.findViewById(R.id.goToGroupButton);
        goToGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyGroupActivity.class);
                startActivity(intent);
            }
        });

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
