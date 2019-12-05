package cn.adminzero.helloword;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import org.apache.log4j.chainsaw.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Message;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.Common.Utils.SerializeUtils;
import cn.adminzero.helloword.CommonClass.DestoryData;
import cn.adminzero.helloword.CommonClass.Group;
import cn.adminzero.helloword.CommonClass.GroupMember;
import cn.adminzero.helloword.CommonClass.MemberItem;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import cn.adminzero.helloword.CommonClass.WordsLevel;
import cn.adminzero.helloword.NetWork.MinaService;
import cn.adminzero.helloword.NetWork.SessionManager;
import cn.adminzero.helloword.db.DbUtil;
import cn.adminzero.helloword.db.ServerDbUtil;
import cn.adminzero.helloword.ui.login.LoginActivity;
import cn.adminzero.helloword.util.MyStorage;

import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsLevelUtil;
import cn.adminzero.helloword.util.WordsUtil;

import static cn.adminzero.helloword.db.ServerDbUtil.ChangeBook;
import static java.lang.Thread.sleep;
import static cn.adminzero.helloword.db.ServerDbUtil.GetGroupMember;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private MainActivitBroadcastReceiver Receiver;
    private IntentFilter intentFilter;
    private MenuItem menuItem;
    private List<Fragment> list;
    private TabFragmentPagerAdapter adapter;
    private ViewPager viewPager;
    // 本地H表为空，像服务器请求H表时加载进度条
    ProgressDialog syncHistoryProgressDialog;
    //选择词书对话框的选择结果
    private int chooseWordsBookChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册监听广播
        Receiver = new MainActivitBroadcastReceiver();
        intentFilter = new IntentFilter(CMDDef.MINABroadCast);
        LocalBroadcastManager.getInstance(this).registerReceiver(Receiver, intentFilter);
        //这里请求获取小组成员列表
        GetGroupMember();


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
            SQLiteDatabase db = DbUtil.getDatabase();
            MyStorage myStorage = new MyStorage();
            Cursor cursor = null;
            /**
             * 判断这个用户是否第一次登录
             * 创建此用户的HISTORY、TODAY表！
             * */
            //上次已經登陸 H表不做修改
            if (myStorage.getInt("lastLoginAccount") == userId) {
                Log.d(TAG, "onCreate: 用户上次已经登录！无需创建数据库和同步数据库");
            } else {
                // 判断用户的单词历史表是否存在  不存在则创建并且网络同步数据
                cursor = db.rawQuery("select name from sqlite_master where type='table';", null);
                String tablename = null;
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
                if (cursor != null) {
                    cursor.close();
                }
                if (isThisAccountFirstLogin) {// 创建其对应的数据表
                    Log.d(TAG, "onCreate: 欢迎新用户登录到本机APP，开始同步网络数据");
                    final String CREATE_HISTORY =
                            "create table if not exists " + "HISTORY_" + userId + "(" +
                                    "word_id integer primary key," +
                                    "level integer default(0)," +
                                    "yesterday integer default(0))";
                    final String CREATE_TODAY =
                            "create table if not exists " + "TODAY_" + userId + "(" +
                                    "word_id integer," +
                                    "level integer," +
                                    "yesterday integer)";
                    db.execSQL(CREATE_HISTORY);
                    db.execSQL(CREATE_TODAY);
                    ServerDbUtil.GetHistory();
                    // 弹出加载进度框
                    syncHistoryProgressDialog = new ProgressDialog(this);
                    syncHistoryProgressDialog.setIcon(R.drawable.ic_autorenew_black_64dp);
                    syncHistoryProgressDialog.setTitle("同步");
                    syncHistoryProgressDialog.setMessage("同步您的记忆历史中...请稍作等待");
                    syncHistoryProgressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
                    syncHistoryProgressDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
                    syncHistoryProgressDialog.show();
                    myStorage.storeInt("lastLoginAccount", userId);
                    Log.d(TAG, "onCreate: 创建数据库");
                    //  网络同步数据  恢复数据库待做
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
        viewPager = findViewById(R.id.main_view_pager);
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

        bottomNavigationView.setOnNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener());
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
        // 返回至HomePage 避免一些UI问题
        viewPager.setCurrentItem(0);
        // 检查用户当前词书是否合法 正确范围是[0,7]
        if (App.userNoPassword_global.getGoal() < 0) {
            // 弹出对话框选择词书
            showChooseWordsBookDialog(1);
        }
        // 更新主活动UI 更新打卡界面
        HomePageFragment homePageFragment = new HomePageFragment();
        list.remove(0);
        list.add(0, homePageFragment);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
/*        // 如果用户从设置界面退出
        if(App.isLoggingOut)
        {
            App.isLoggingOut = false;
            finish();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
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

        // 当主活动结束的时候备份部分信息并发送至服务器
        // 网络同步
        ServerDbUtil.Upadte_UserNoPassword();
        //注销广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(Receiver);
        DestoryData destoryData = new DestoryData();
        Message mes = SendMsgMethod.getObjectMessage(CMDDef.DESTORY_SELF_SEND_DATA, destoryData);
        SessionManager.getInstance().writeToServer(mes);
        stopService(new Intent(this, MinaService.class));
    }

    // 显示选择词书对话框
    // 参数 表示是否是第一次选择词书（即是否之前有词书）
    //  1 表示 第一次选择出词书
    //  0 表示 已经有词书
    public void showChooseWordsBookDialog(int parameter) {
        AlertDialog.Builder builder = null;
        final String[] items0 = {"中考", "高考", "CET4", "CET6", "托福", "雅思", "GRE", "考研", "不做改动"};
        final String[] items1 = {"中考", "高考", "CET4", "CET6", "托福", "雅思", "GRE", "考研"};
        if(parameter == 1){
            builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_book_64px).setTitle("在您开始使用前，请选择词书")
                    .setSingleChoiceItems(items1, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            chooseWordsBookChoice = i;
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (chooseWordsBookChoice != 8) {
                                // 更换词书处理  0-7
                                WordsLevelUtil.initWorkBook(chooseWordsBookChoice);
                                Toast.makeText(MainActivity.this, "你选择了" + items1[chooseWordsBookChoice], Toast.LENGTH_LONG).show();
                                // 网络同步
                                ServerDbUtil.Upadte_UserNoPassword();
                            }
                        }
                    });
        }
        // 丑陋的写法
        if(parameter == 0){
            builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_book_64px).setTitle("在您开始使用前，请选择词书")
                    .setSingleChoiceItems(items0, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            chooseWordsBookChoice = i;
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (chooseWordsBookChoice != 8) {
                                // 更换词书处理  0-7
                                WordsLevelUtil.initWorkBook(chooseWordsBookChoice);
                                Toast.makeText(MainActivity.this, "你选择了" + items0[chooseWordsBookChoice], Toast.LENGTH_LONG).show();
                                // 网络同步
                                ServerDbUtil.Upadte_UserNoPassword();
                            }
                        }
                    });
        }

        //默认选中第一个
        chooseWordsBookChoice = 0;

        // 设置不可取消

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

    }

    // 当点击了选择词书
    public void onClickChooseWordsBookButton(View view) {
        /*Intent intent = new Intent(this, ChooseWordsBookActivity.class);
        startActivity(intent);*/
        showChooseWordsBookDialog(0);
    }

    public void onClickFreshButton(View view) {
        // 圆圈加载进度的 dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_autorenew_black_64dp);
        progressDialog.setTitle("刷新");
        progressDialog.setMessage("刷新中...请稍作等待");
        progressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
        progressDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
        progressDialog.show();
        // 网络同步
        ServerDbUtil.Upadte_UserNoPassword();

        // 因为速度太快让人误认为功能失效 使用倒计时空转几圈
        CountDownTimer timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();


    }

    // 打开我的词库的按钮 by whl
    public void onClickWordsButton(View view) {
        Intent intent = new Intent(this, CheckOutWordsActivity.class);
        startActivity(intent);
    }

    //接受History表的广播监听器
    class MainActivitBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            short cmd = intent.getShortExtra(CMDDef.INTENT_PUT_EXTRA_CMD, (short) -1);
            switch (cmd) {
                case CMDDef.GET_HISTORY_REPLY: {

                    byte[] data = intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA);
                    try {
                        //在MainActivity的OncCreat 方法请求了History
                        //在这里面接收
                        ArrayList<WordsLevel> wordlist = (ArrayList<WordsLevel>) SerializeUtils.serializeToObject(data);
                        WordsLevelUtil.updateWordLevelByArraylist(wordlist);
                        // 将加载对话框取消
                        App.stop = false;
                        syncHistoryProgressDialog.dismiss();
                    } catch (Exception e) {
                        //获取失败
                        System.out.println("从服务器获取History表失败");
                    }
                }
                break;
                case CMDDef.GET_GROUPMEMBER_REPLY:{
                    byte[] data = intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA);
                    try {
                        //获取小组成员类
                        GroupMember groupMember=(GroupMember)SerializeUtils.serializeToObject(data);
                        //获取成员列表
                         ArrayList<MemberItem> memberlist=groupMember.getMemberlist();
                         MemberItem member1=memberlist.get(0);//这里随便看一个成员
                         //获取小组组长
                         Group master =groupMember.getMaster();


                    }catch (Exception e){

                    }
                }
                break;
                case CMDDef.FORCE_OFFLINE:{
//                    Log.e("tag","广播转发的我收到了!");
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//                    builder.setTitle("强制下线");
//                    builder.setMessage("您的账户在异地登录，如果非本人所为，经更改您的账户信息!");
//                    builder.setCancelable(false);
//                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            ActivityCollector.finishAll();
//                            Intent intent1 = new Intent(context, LoginActivity.class);
//                            App.isForceOffline = true;
//                            context.startActivity(intent1);
//                        }
//                    });
//                    builder.show();
                }
                break;
            }
        }

    }

    // 用于MainActivity底部导航栏的监听器
    class MyOnNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

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

    }
}
