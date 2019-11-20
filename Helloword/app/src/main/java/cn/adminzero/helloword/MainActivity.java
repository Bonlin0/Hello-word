package cn.adminzero.helloword;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import cn.adminzero.helloword.NetWork.MinaService;
import cn.adminzero.helloword.ui.login.LoginActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private MenuItem menuItem;
    private List<Fragment> list;
    private TabFragmentPagerAdapter adapter;

    //选择词书对话框的选择结果
    private int chooseWordsBookChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        final ViewPager viewPager = findViewById(R.id.main_view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menuItem = item;
                int i = item.getItemId();
                if (i == R.id.navigation_home) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (i == R.id.navigation_explore) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (i == R.id.navigation_about_me) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
        list = new ArrayList<>();
        list.add(new HomePageFragment() );
        list.add(new ExploreFragment() );
        list.add(new AboutMeFragment() );
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);  //初始化显示第一个页面


        /** only for debug*/
//        Test test = new Test();
//        test.test();
        // TestActivityEntry();//进入测试活动

    }

    /**
     * -------------code zhaojunchen start--------------
     * */

    /**
     * 进入测试活动
     */
    private void TestActivityEntry() {
        Intent testintent = new Intent(this, TestActivity.class);
        startActivity(testintent);
    }


    /**
     * -------------code zhaojunchen end----------------
     */


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 当主活动结束的时候备份部分信息并发送至服务器
        stopService(new Intent(this, MinaService.class));
    }

    public void onClickChooseWordsBookButton(View view) {
        /*Intent intent = new Intent(this, ChooseWordsBookActivity.class);
        startActivity(intent);*/
        AlertDialog.Builder builder;
        //默认选中第一个
        final String[] items = {"中考", "高考", "CET4", "CET6", "考研", "GRE", "雅思", "托福"};
        chooseWordsBookChoice = -1;
        builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_book_64px).setTitle("选择词书")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        chooseWordsBookChoice = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (chooseWordsBookChoice != -1) {
                            //TODO 更换词书处理
                            Toast.makeText(MainActivity.this, "你选择了" + items[chooseWordsBookChoice], Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.create().show();
    }

    public void onClickFreshButton(View view) {
        // 圆圈加载进度的 dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_autorenew_black_64dp);
        progressDialog.setTitle("刷新");
        progressDialog.setMessage("刷新中...请稍作等待");
        progressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
        progressDialog.setCancelable(true);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
        progressDialog.show();
        //TODO 增加刷新的动作 目前可以取消之后要progressDialog.setCancelable(false);
    }

    // 打开我的词库的按钮 by whl
    public void onClickWordsButton(View view) {
        Intent intent = new Intent(this, CheckOutWordsActivity.class);
        startActivity(intent);
    }




}
