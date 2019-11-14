package cn.adminzero.helloword.NetWork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.getStringExtra("message");
    }
}
