package cn.adminzero.helloword.db;

//            "create table USER(" +
//                    "user_id int primary key ," +
//                    "user_name text NOT NULL," +
//                    "password text NOT NULL," +
//                    "email text ," +
//                    "avatar blob ," +
//                    "goal int default(-1) ," +
//                    "days int default(0)," +
//                    "group_id int default(-1)," +
//                    "user_level int default(0)," +
//                    "points int default(0) )";

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import static cn.adminzero.helloword.db.DbUtil.bytetoDrawable;
import static cn.adminzero.helloword.db.DbUtil.getDatabase;

public class USER {
    public  int user_id;
    public  String user_name;
    public  String password;
    public  String email;
    public Drawable avatar;
    public  int  goal;
    public int days;
    public  int user_level;
    public  int points;
    public USER(int user_id){
        this.user_id=user_id;
        SQLiteDatabase db = getDatabase();
        Cursor cursor=db.rawQuery("select *  from USER where id=?",new String[]{user_id+""});
        while (cursor.moveToNext()){
            byte[] bytes;
            user_name=cursor.getString(cursor.getColumnIndex("user_name"));
            password=cursor.getString(cursor.getColumnIndex("password"));
            email=cursor.getString(cursor.getColumnIndex("email"));
            bytes=cursor.getBlob(cursor.getColumnIndex("avatar"));
            avatar=bytetoDrawable(bytes);
            goal=cursor.getInt(cursor.getColumnIndex("goal"));
            days=cursor.getInt(cursor.getColumnIndex("days"));
            user_level=cursor.getInt(cursor.getColumnIndex("user_level"));
            points=cursor.getInt(cursor.getColumnIndex("points"));

        }
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
