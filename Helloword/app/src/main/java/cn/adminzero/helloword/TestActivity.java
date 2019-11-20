package cn.adminzero.helloword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.adminzero.helloword.util.MediaPlayUtil;

/**
 * 测试活动 便于测试某些点击时事件
 * 通过TestActivityEntry()函数进入
 */
public class TestActivity extends BaseActivity implements View.OnClickListener {


    private Button play = null;
    private EditText wordinput = null;
    private MediaPlayUtil mediaPlyUtil = new MediaPlayUtil();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        play = (Button) findViewById(R.id.play);
        wordinput = (EditText) findViewById(R.id.wordinput);
        play.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                String word = wordinput.getText().toString();
                mediaPlyUtil.playword(word);
        }
    }

}
