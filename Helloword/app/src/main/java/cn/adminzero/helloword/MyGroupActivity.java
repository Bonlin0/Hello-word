package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        Button all_group_button = findViewById(R.id.all_group_button);
        all_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupActivity.this, ShowAllGroupActivity.class);
                startActivity(intent);
            }
        });

        Button all_group_member_button = findViewById(R.id.all_group_member_button);
        all_group_member_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupActivity.this, ShowGroupMemberActivity.class);
                startActivity(intent);
            }
        });

        Button manage_group_button = findViewById(R.id.manage_group_button);
        manage_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupActivity.this, ManageGroupActivity.class);
                startActivity(intent);
            }
        });

        Button exit_group_button = findViewById(R.id.exit_group_button);
        exit_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"你退出了小组",Toast.LENGTH_LONG).show();
                // TODO 退出小组操作
            }
        });
    }
}
