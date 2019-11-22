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
        //TODO 处理用户已完成单词和目标完成单词（与称号机制挂钩）


        Button startRememberWordsButton = view.findViewById(R.id.start_learning_button);
        startRememberWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity().getApplicationContext(),RememberWordsActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }



}
