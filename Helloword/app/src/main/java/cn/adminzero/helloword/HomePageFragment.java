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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.adminzero.helloword.util.WordsLevelUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {



    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        TextView number_of_punch_days_textView = view.findViewById(R.id.number_of_punch_days);
        int days = App.userNoPassword_global.getDays();
        // 因为UserNoPassword定义中 days 是基本类型 int 而不是 Integer包装类
        // 所以需要这里处理一下以转字符串
        String daysStr = new Integer(days).toString();
        number_of_punch_days_textView.setText(daysStr);
        // 处理用户已完成单词和目标完成单词（与称号机制挂钩）
        Integer wordsFinished = WordsLevelUtil.getLevel7Count();
        Integer wordsRequiredForNextLevel;


        switch (App.userNoPassword_global.getLevel())
        {
            // 无名菜鸟
            case 0:
                wordsRequiredForNextLevel = 25;
                break;
            case 1:
                // 初出茅庐
                wordsRequiredForNextLevel = 75;
                break;
            case 2:
                // 略有所得
                wordsRequiredForNextLevel = 200;
                break;
            case 3:
                // 渐入佳境
                wordsRequiredForNextLevel = 500;
                break;
            case 4:
                // 胸有成竹
                wordsRequiredForNextLevel = 1500;
                break;
            case 5:
                // 举世无双
                wordsRequiredForNextLevel = 5000;
                break;
            default:
                // 不可能进入
                wordsRequiredForNextLevel = 10000;
                break;
        }

        // 检查是否满足升级称号的条件
        if(wordsFinished<wordsRequiredForNextLevel)
        {
            App.isAbleToUpgradeTitle=false;
        }
        else
        {
            App.isAbleToUpgradeTitle=true;
        }

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(wordsRequiredForNextLevel);
        if(wordsFinished<wordsRequiredForNextLevel){
            progressBar.setProgress(wordsFinished);
        }
        else {
            // 避免溢出
            progressBar.setProgress(wordsRequiredForNextLevel);
        }

        TextView words_remembered = view.findViewById(R.id.words_remembered);
        words_remembered.setText(wordsFinished.toString());
        TextView words_to_remember = view.findViewById(R.id.words_to_remember);
        words_to_remember.setText(wordsRequiredForNextLevel.toString());

        Button startRememberWordsButton = view.findViewById(R.id.start_learning_button);
        startRememberWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity().getApplicationContext(),RememberWordsActivity.class);
                startActivity(intent);
            }
        });

        TextView have_punched_hint_textView = view.findViewById(R.id.have_punched_hint_textView);


        // 处理打卡事件
        if(App.userNoPassword_global.getIsPunch()==1){
            have_punched_hint_textView.setVisibility(View.VISIBLE);
            startRememberWordsButton.setEnabled(false);
            startRememberWordsButton.setVisibility(View.INVISIBLE);
        }
        else{
            startRememberWordsButton.setVisibility(View.VISIBLE);
            startRememberWordsButton.setEnabled(true);
            have_punched_hint_textView.setVisibility(View.INVISIBLE);
        }



        return view;
    }



}
