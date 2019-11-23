package cn.adminzero.helloword;

/**
 * author : zhaojunchen
 * date   : 2019/11/2321:28
 * desc   : 活动管理器
 */

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;


public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}
