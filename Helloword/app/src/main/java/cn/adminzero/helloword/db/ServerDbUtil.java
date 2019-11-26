package cn.adminzero.helloword.db;

import android.util.Log;

import org.apache.log4j.Logger;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Decoder;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.CommonClass.Group;
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

}
