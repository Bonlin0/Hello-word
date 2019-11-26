package cn.adminzero.helloword;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import org.apache.log4j.chainsaw.Main;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Message;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.CommonClass.DestoryData;
import cn.adminzero.helloword.NetWork.MinaService;
import cn.adminzero.helloword.NetWork.SessionManager;
import cn.adminzero.helloword.db.DbUtil;
import cn.adminzero.helloword.util.MyStorage;
import cn.adminzero.helloword.util.WordsLevelUtil;

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

        /**
         * TODO 创建单词表并且网络同步
         * */
        int userId = App.userNoPassword_global.getUserID();
        Log.d(TAG, "onCreate: 当前用户ID" + userId);
        if (userId == -1) {
            Log.d(TAG, "onCreate: 登录失败！程序退出");
            ActivityCollector.finishAll();
        }
        try {
            MyStorage myStorage = new MyStorage();
            if (myStorage.getInt("lastLoginAccount") == userId) {
                Log.d(TAG, "onCreate: 用户上次已经登录！无需创建数据库和同步数据库");
            } else {
                SQLiteDatabase db = DbUtil.getDatabase();
                // 判断用户的单词历史表是否存在  不存在则创建并且网络同步数据
                Cursor cursor = db.rawQuery("select name from sqlite_master where type='table';", null);
                String tablename;
                boolean isThisAccountFirstLogin = true;
                while (cursor.moveToNext()) {
                    //遍历出表名
                    tablename = cursor.getString(0);
                    Log.d(TAG, "onCreate: " + tablename);
                    if (tablename.equals("HISTORY_" + userId)) {
                        isThisAccountFirstLogin = false;
                        break;
                    }
                }
                cursor.close();
                if (isThisAccountFirstLogin) {// 创建其对应的数据表
                    Log.d(TAG, "onCreate: 欢迎新用户登录到本机APP，开始同步网络数据");
                    final String CREATE_HISTORY =
                            "create table if not exists " + "HISTORY_" + userId + "(" +
                                    "word_id integer primary key," +
                                    "level integer default(0)," +
                                    "yesterday integer default(0))";
                    db.execSQL(CREATE_HISTORY);
                    myStorage.storeInt("lastLoginAccount", userId);
                    Log.d(TAG, "onCreate: 创建数据库");
                    // TODO 网络同步数据  恢复数据库 待做
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: exception" + e.getMessage());
        } finally {
            Log.d(TAG, "onCreate: 词库设置完毕");
        }

        // 设置底部导航和页面滑动
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
        HomePageFragment homePageFragment = new HomePageFragment();
        ExploreFragment exploreFragment = new ExploreFragment();
        AboutMeFragment aboutMeFragment = new AboutMeFragment();
        list.add(homePageFragment);
        list.add(exploreFragment);
        list.add(aboutMeFragment);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);  //初始化显示第一个页面


        // 根据LoginActivity获取到的用户信息刷新UI

        //TextView pkPointNumberTextView_textView = findViewById(R.id.pkPointNumberTextView);
        //pkPointNumberTextView_textView.setText(App.userNoPassword_global.getpKPoint());


        // only for debug
//         Test test = new Test();
//         test.test();
        // TestActivityEntry();//进入测试活动

    }

    @Override
    protected void onResume() {

        super.onResume();
        // 检查用户当前词书是否合法 正确范围是[1,8]
        if (App.userNoPassword_global.getGoal() <= 0) {
            // 弹出对话框选择词书
            showChooseWordsBookDialog();

        }
/*        // 如果用户从设置界面退出
        if(App.isLoggingOut)
        {
            App.isLoggingOut = false;
            finish();
        }*/
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
        DestoryData destoryData = new DestoryData();
        Message mes = SendMsgMethod.getObjectMessage(CMDDef.DESTORY_SELF_SEND_DATA, destoryData);
        SessionManager.getInstance().writeToServer(mes);
        //TODO 当主活动结束的时候备份部分信息并发送至服务器
        stopService(new Intent(this, MinaService.class));
    }

    public void showChooseWordsBookDialog() {
        AlertDialog.Builder builder;


        //默认选中第一个
        final String[] items = {"中考", "高考", "CET4", "CET6", "托福", "雅思", "GRE", "考研"};
        chooseWordsBookChoice = 0;
        builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_book_64px).setTitle("在您开始使用前，请选择词书")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        chooseWordsBookChoice = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (chooseWordsBookChoice != -1) {
                            // 更换词书处理  0-7
                            WordsLevelUtil.initWorkBook(chooseWordsBookChoice);
                            Toast.makeText(MainActivity.this, "你选择了" + items[chooseWordsBookChoice], Toast.LENGTH_LONG).show();
                            //TODO 网络同步
                            App.Upadte_UserNoPassword();
                        }
                    }
                });
        // 设置不可取消
        builder.create().setCancelable(false);
        builder.create().show();;
    }

    // 当点击了选择词书
    public void onClickChooseWordsBookButton(View view) {
        /*Intent intent = new Intent(this, ChooseWordsBookActivity.class);
        startActivity(intent);*/
        showChooseWordsBookDialog();
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
