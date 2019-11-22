package cn.adminzero.helloword;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMeFragment extends Fragment {

    private Button settingButton;

    public AboutMeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);

        /** 实例化 settingButton */
        settingButton = view.findViewById(R.id.settings_about_me_button);


        TextView userNameTextView = view.findViewById(R.id.userNameTextView);
        userNameTextView.setText(App.userNoPassword_global.getUserNickName());

        TextView userIDText = view.findViewById(R.id.userIDText);
        Integer UserId = App.userNoPassword_global.getUserID();
        userIDText.setText("ID: " + UserId.toString());

        TextView dakaDaysText = view.findViewById(R.id.dakaDaysText);
        dakaDaysText.setText("打卡  " + new Integer(App.userNoPassword_global.getDays()).toString() + " 天");

        // 更新称号UI部分
        TextView userLevelTitleText = view.findViewById(R.id.userLevelTitleText);
        String userLevelTitleStr = "称号 ";
        String levelToAppend = null;
        switch (App.userNoPassword_global.getLevel()) {
            case 0:
                levelToAppend = "无名菜鸟";
                break;
            case 1:
                levelToAppend = "初出茅庐";
                break;
            case 2:
                levelToAppend = "略有所得";
                break;
            case 3:
                levelToAppend = "渐入佳境";
                break;
            case 4:
                levelToAppend = "胸有成竹";
                break;
            case 5:
                levelToAppend = "举世无双";
                break;
            default:
                // Bug 才会出现这种情况
                levelToAppend = "扫地僧";
        }
        userLevelTitleStr += levelToAppend;
        userLevelTitleText.setText(userLevelTitleStr);

        //TODO 如果之后需要添加头像在这里添加

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * 启动设置活动
         * */
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });


    }
}
