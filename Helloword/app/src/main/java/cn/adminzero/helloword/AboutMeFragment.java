package cn.adminzero.helloword;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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

        //点击词库按钮
        Button words_about_me_button = view.findViewById(R.id.words_about_me_button);
        words_about_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CheckOutWordsActivity.class);
                startActivity(intent);
            }
        });

        // 点击帮助按钮
        Button help_and_issue_about_me_button = view.findViewById(R.id.help_and_issue_about_me_button);
        help_and_issue_about_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpAndIssueDialog();
            }
        });

        // 展示称号功能的介绍dialog 提供提升称号等级的入口
        Button userTitleButton = view.findViewById(R.id.userTitleButton);
        userTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleDialog();
            }
        });

        //小组功能
        Button group_about_me_button = view.findViewById(R.id.group_about_me_button);
        group_about_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyGroupActivity.class);
                startActivity(intent);
            }
        });

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

    // 当用户点击帮助与反馈时
    private void showHelpAndIssueDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext()).setIcon(R.drawable.ic_help_outline_black_24dp).setTitle("帮助与反馈")
                .setMessage(getString(R.string.help_and_issue_content)).setPositiveButton("打开浏览器", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 打开网页
                        Toast.makeText(getContext(), "正在打开网页", Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse("https://github.com/Haulyn5/Hello-word");
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    // 当用户点击称号时
    private void showTitleDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext()).setIcon(R.drawable.ic_star_border_black_24dp).setTitle("称号机制")
                .setMessage(getString(R.string.title_dialog_content)).setPositiveButton("开始测验！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 称号功能
                        Toast.makeText(getContext(), "准备开始测验！", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(),WordTestActivity.class);
                        intent.putExtra("is_from_title_upgrade",true);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button btn =  dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        // 检查用户已经背完的单词是否满足条件
        if(!App.isAbleToUpgradeTitle)
        {
            btn.setEnabled(false);
        }

    }


}
