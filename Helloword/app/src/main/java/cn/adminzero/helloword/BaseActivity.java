package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// 这是一个所有活动的父类
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
