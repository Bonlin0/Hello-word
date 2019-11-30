package cn.adminzero.helloword;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ShowAllGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.search_group_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 搜索小组的Dialog
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showInput();

            }
        });

        //TODO 设置RecyclerView

    }

    private void showInput() {
        AlertDialog.Builder builder;
        final EditText editText = new EditText(this);
        // TODO ： 这样的话小组名称也不应该重复了 可以将返回的多个结果重新放入Recycler View
        builder = new AlertDialog.Builder(this).setTitle("请输入您要搜索的小组名称").setView(editText)
                .setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ShowAllGroupActivity.this, "搜索小组名：" + editText.getText().toString()
                                , Toast.LENGTH_LONG).show();
                        // TODO 数据库搜索该名字的小组 返回一个ArrayList设置UI
                    }
                });
        builder.create().show();
    }
}
