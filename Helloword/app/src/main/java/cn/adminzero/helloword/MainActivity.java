package cn.adminzero.helloword;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.NetWork.MessageBroadcastReceiver;
import cn.adminzero.helloword.NetWork.MinaService;

public class MainActivity extends BaseActivity {
    private TextView mTextMessage;
    private MessageBroadcastReceiver receiver;
    private static final String TAG = "MainActivity";
    private MenuItem menuItem;
    private List<Fragment> list;
    private TabFragmentPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册广播
        registerBroadcast();
        Intent intent = new Intent(this, MinaService.class);
        //开启MINA服务
        startService(intent);

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
    }

    private void registerBroadcast() {
        receiver = new MessageBroadcastReceiver();
        IntentFilter filter = new IntentFilter(CMDDef.MINABroadCast);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private void unregisterBroadcast() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MinaService.class));
        unregisterBroadcast();
    }
}
