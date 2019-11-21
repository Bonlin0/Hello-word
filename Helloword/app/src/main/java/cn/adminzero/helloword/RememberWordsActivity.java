package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RememberWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_words);
    }

    public void rememberButtonOnClicked(View view)
    {
        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        startActivity(intent);
    }
}
