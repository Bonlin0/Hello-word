package cn.adminzero.helloword.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.Log;

import java.io.ByteArrayOutputStream;

import cn.adminzero.helloword.App;
import cn.adminzero.helloword.R;


public class DbUtil {
    private static final String TAG = "DbUtil";

    public static SQLiteDatabase getDatabase() {
        MyDatabaseHelper dbhelper = new MyDatabaseHelper(App.getContext(), App.getDbname(), null, App.getDbversion());
        try {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Log.d(TAG, "open databases successful!!!");
            return db;
        } catch (Exception e) {
            Log.d(TAG, "open databases failed!!!");
            e.printStackTrace();
        }
        return null;
    }

    public static SQLiteDatabase getDatabase(String dbname) {
        MyDatabaseHelper dbhelper = new MyDatabaseHelper(App.getContext(), dbname, null, App.getDbversion());
        try {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Log.d(TAG, "open databases successful!!!");
            return db;
        } catch (Exception e) {
            Log.d(TAG, "open databases failed!!!");
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 删除数据库
     */
    public static boolean MydeleteDatabase() {
        Log.d(TAG, "删除数据库");
        return App.getContext().deleteDatabase(App.getDbname());
    }

    public static boolean MydeleteDatabase(String dbname) {
        Log.d(TAG, "删除数据库");
        return App.getContext().deleteDatabase(dbname);

    }


    /**
     * 添加用户
     **/
    public static boolean AddUser(int user_id, String user_name, String password) {
        SQLiteDatabase db = getDatabase();
        try {
            //byte[] bytes=Drawabletobyte(avatar);
            // db.execSQL("insert into USER (user_id,user_name,password,email,goal)  values (?,?,?,?,?) ", new String[]{user_id+"", user_name,password, email, Integer.toString(goal) });
            db.execSQL("insert into USER (user_id,user_name,password)  values (?,?,?) ", new String[]{user_id + "", user_name, password});

            Log.d(TAG, "insert into 结束");
            //图片存储
        /*   ContentValues contentValues= new ContentValues();
           contentValues.put("avatar",bytes);
           db.update("USER",contentValues,"user_id=?",new String[]{user_id+""});
           contentValues.clear();
          */
        } catch (Exception e) {
            Log.d(TAG, "AddUser失败");
            return false;
        }
        return true;
    }


    /**
     * 添加用户和对应的记忆表
     */
    public static boolean AddUSER_HISTROY(int user_id, String history_name) {
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("insert into USER_HISTORY(user_id,history_name) values(?,?)", new String[]{user_id + "", history_name});
        } catch (Exception e) {
            Log.d(TAG, "AddUSER_HISTORY失败");
            return false;
        }
        return true;
    }

    /**
     * 添加小组记录
     */
    public static boolean AddGROUP_USER(int user_id, int group_id) {
        //前面的排序id  自增长，不需要插入
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("insert into GROUP_USER(user_id,group_id) values(?,?)", new String[]{user_id + "", group_id + ""});
        } catch (Exception e) {
            Log.d(TAG, "AddUSER_HISTORY失败");
            return false;
        }
        return true;
    }

    /**
     * 退群！
     */

    public static boolean QUIT_GROUP(int user_id) {
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("delete  from GROUP_USER where user_id=?", new String[]{user_id + ""});
        } catch (Exception e) {
            Log.d(TAG, "AddUSER_HISTORY失败");
            return false;
        }
        return true;
    }

    /**
     * 添加单词到分表
     **/
    public static boolean AddWORDS(int word_id, String word, String translation,
                                   String phonetic, String definition, int tag, String sentence) {
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("insert into WORDS(word_id,word,translation,phonetic,definition,tag,sentence) values(?,?,?,?,?,?,?)", new String[]{word_id + "", word, translation, phonetic, definition, tag + "", sentence});
        } catch (Exception e) {
            Log.d(TAG, "AddWORDS失败");
            return false;
        }
        return true;
    }


    /**
     * 设置用户目标单词
     */
    public static boolean set_goal(int user_id, int goal) {
        SQLiteDatabase db = getDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("goal", goal);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "set_goal失败");
            return false;
        }
        return true;
    }


    /**
     * 设置用户的email
     */
    public static boolean set_email(int user_id, String email) {
        SQLiteDatabase db = getDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", email);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "set_email失败");
            return false;
        }
        return true;
    }

    /**
     * 设置用户的password
     */
    public static boolean set_password(int user_id, String password) {
        SQLiteDatabase db = getDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("password", password);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "set_password失败");
            return false;
        }
        return true;
    }

    /**
     * 设置用户的user_name
     */
    public static boolean set_user_name(int user_id, String user_name) {
        SQLiteDatabase db = getDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_name", user_name);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "set_user_name失败");
            return false;
        }
        return true;
    }

    /**
     * 设置用户的avatar
     */
    public static boolean set_avatar(int user_id, Drawable avatar) {
        SQLiteDatabase db = getDatabase();
        try {
            byte[] bytes = Drawabletobyte(avatar);
            ContentValues contentValues = new ContentValues();
            contentValues.put("avatar", bytes);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "set_avatar失败");
            return false;
        }
        return true;
    }

    /**
     * 增加打卡天数days
     */
    public static boolean add_days(int user_id) {
        SQLiteDatabase db = getDatabase();
        int days = 0;
        try {
            Cursor cursor = db.rawQuery("select days from USER where user_id=?", new String[]{user_id + ""});
            while (cursor.moveToNext()) {
                int day = cursor.getInt(cursor.getColumnIndex("days"));
                cursor.close();
                days = day + 1;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("days", days);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "add_days失败");
            return false;
        }
        return true;
    }

    /**
     * 增加小组积分days
     */
    public static boolean add_contribution(int user_id, int add_contribution) {
        SQLiteDatabase db = getDatabase();
        int contribution = 0;
        try {
            Cursor cursor = db.rawQuery("select  contribution from GROUP_USER where user_id=?", new String[]{user_id + ""});
            while (cursor.moveToNext()) {
                contribution = cursor.getInt(cursor.getColumnIndex("contribution"));
                cursor.close();
                contribution = contribution + add_contribution;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("contribution", contribution);
            db.update("GROUP_USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "add_contribution失败");
            return false;
        }
        return true;
    }

    /**
     * 增加打卡points
     */
    public static boolean add_points(int user_id, int add_points) {
        SQLiteDatabase db = getDatabase();
        int points = 0;
        try {
            Cursor cursor = db.rawQuery("select points from USER where user_id=?", new String[]{user_id + ""});
            while (cursor.moveToNext()) {
                points = cursor.getInt(cursor.getColumnIndex("points"));
                cursor.close();
                points = points + add_points;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("points", points);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "add_points失败");
            return false;
        }
        return true;
    }

    /**
     * 设置用户的group_id
     */
    public static boolean set_group_id(int user_id, int group_id) {
        SQLiteDatabase db = getDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("group_id", group_id);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();

            //先退群
            QUIT_GROUP(user_id);
            //再加群
            AddGROUP_USER(user_id, group_id);

        } catch (Exception e) {
            Log.d(TAG, "set_group_id失败");
            return false;
        }
        return true;
    }


    /**
     * 设置用户的user_level
     */
    public static boolean set_user_level(int user_id, int user_level) {
        SQLiteDatabase db = getDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_level", user_level);
            db.update("USER", contentValues, "user_id=?", new String[]{user_id + ""});
            contentValues.clear();
        } catch (Exception e) {
            Log.d(TAG, "set_user_level失败");
            return false;
        }
        return true;
    }

    /**
     * Drawable 转Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
//        System.out.println("Drawable转Bitmap");
        try {
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    /**
     * bitmap 转 Drawable
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null)
            return new BitmapDrawable(App.getContext().getResources(), bitmap);
        return null;
    }


    /**
     * byte串转换为Bitmap
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    /**
     * bitmap 转byte
     */

    public static byte[] bitmapTobytes(Bitmap bm) {
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    /**
     * Drawable 转byte[]
     */
    public static byte[] Drawabletobyte(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        return bitmapTobytes(bitmap);
    }

    /**
     * byte转Drawable
     */
    public static Drawable bytetoDrawable(byte[] bytes) {
        Bitmap bitmap = bytesToBitmap(bytes);
        return bitmapToDrawable(bitmap);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void initialDatabase() {
        Log.d(TAG, "initial 开始");
        SQLiteDatabase db = getDatabase();
        Drawable avatar = App.getContext().getDrawable(R.drawable.personal);
        AddUser(1, "chenyuan", "password");
        db.execSQL("insert into USER(user_id,user_name,password,email,goal)  values (?,?,?,?,?) ", new String[]{"4", "chen_test_inital", "woaini", "sairen@whu.edu.cn", Integer.toString(2)});
        db.execSQL("insert into WORDS(word_id,word,translation,phonetic,definition,classfication,sentence) values(?,?,?,?,?,?,?)", new String[]{"1131", "apple", "苹果", "[ˈæpl]", "a kind of fruit", 0xA1000000 + "", "I love eating apple. 我爱吃苹果。"});
        AddUser(2017, "chenyuan2017", "password2017");
        AddGROUP_USER(2017, 6);
        AddUSER_HISTROY(2017, "HISTORY_2017");
        AddWORDS(30, "computer", "计算机", "[kəmˈpjuːtər]", "an electronic machine that can store, organize and find " +
                "information, do calculations and control other machines ", 0xFF000000, "I think computer is the greatest invention. 我认为计算机是最伟大的发明。 ");
        AddUser(2120, "Your Father", "12345");
        set_goal(1, 6);
        set_group_id(1, 34);
        set_avatar(1, avatar);
        set_email(2120, "34959373@qq.com");
        set_password(2120, "165432_2.0");
        set_user_name(2120, "Your Father2.0");
        set_user_level(2120, 12);
        add_points(1, 23);
        add_days(1);
        add_contribution(1, 12013);

        Log.d(TAG, "退出initialDatabase");
    }

}
