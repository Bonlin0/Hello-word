package Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: 王翔
 * @date: 2019/11/30-16:47
 * @description: <br>
 * <EndDescription>
 */
public class TestMain {
    private static Timer t = new Timer();
    private static long nowtime = 0;
    static class task extends TimerTask{
        public int i = 0;
        public task(int i)
        {
            this.i = i;
        }
        @Override
        public void run() {
            long noTime = System.currentTimeMillis();
            System.out.println("我执行了一次,我的ID是" + i);
            System.out.println("经历了" + (noTime - nowtime) + "时间");
            this.cancel();
        }
    }

    public static void main(String[] args) {
        Map<Integer, Integer> a = new HashMap<>();
        Map<Integer, task> b = new HashMap<>();
        task tt = new task(20);
        a.put(10,20);
        System.out.println(get(a));
    }
    static int get(Map<Integer, Integer> map)
    {
        return map.get(20);
    }
}
