package cn.adminzero.helloword.db;

import android.util.Log;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Decoder;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.CommonClass.Group;
import cn.adminzero.helloword.CommonClass.WordsLevel;
import cn.adminzero.helloword.NetWork.SessionManager;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static cn.adminzero.helloword.App.userNoPassword_global;

public class ServerDbUtil {
    public static org.apache.log4j.Logger logger = Logger.getLogger(ServerDbUtil.class);
    public static  void Upadte_UserNoPassword(){
        //System.out.println("更新用戶信息函数");
        Log.e(TAG, "Upadte_UserNoPassword:更新用戶信息函数 ");
        SessionManager.getInstance().writeToServer(SendMsgMethod.getObjectMessage(CMDDef.UPDATE_USER_REQUESET,
                userNoPassword_global));
    }

    public static void CreateGroup(Group group){
        SessionManager.getInstance().writeToServer(SendMsgMethod.getObjectMessage(CMDDef.CREATE_GROUP_REQUEST,
                group));
        Log.e("tag","组ID" + group.getGroup_id());
    }
    public static void updateGroup(Group group){
        SessionManager.getInstance().writeToServer(SendMsgMethod.getObjectMessage(CMDDef.UPDATE_GROUP_REQUEST,
                group));
    }
    public  static Group getGroup(int user_id){
        SessionManager.getInstance().writeToServer(SendMsgMethod.getIntMessage(CMDDef.GET_GROUP_REQUEST,
                user_id));
       return null;
    }
    public static void UpdateHistory(ArrayList<WordsLevel> wordsLevelArrayList){
        SessionManager.getInstance().writeToServer(SendMsgMethod.getObjectMessage(CMDDef.UPDATE_HISTORY_REQUEST,
                wordsLevelArrayList));
    }
    public static void GetHistory( ){
        SessionManager.getInstance().writeToServer(SendMsgMethod.getNullMessage(CMDDef.GET_HISTORY_REQUSEST));
    }


 //   Group group=new Group(10005,16);
//        int user_id=group.getUser_id();
//        CreateGroup(group);
//        group.setMax_member(18);
//        updateGroup(group);
//      //  Group group0=getGroup(userNoPassword_global.getUserID());


//           ArrayList<WordsLevel> wordsIdToUpdate=new ArrayList<WordsLevel>();
//           WordsLevel wordsLevel1=new WordsLevel((short)1);
//           wordsLevel1.setLevel((short)3);
//            WordsLevel wordsLevel2=new WordsLevel((short)4);
//            wordsIdToUpdate.add(wordsLevel1);
//            wordsIdToUpdate.add(wordsLevel2);
    //调用 UpdateHistory方法 就可以更新这个列表
//            UpdateHistory(wordsIdToUpdate);
}
