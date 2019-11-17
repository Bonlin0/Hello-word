package cn.adminzero.helloword.data;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.CommonClass.SignUpRequest;
import cn.adminzero.helloword.NetWork.SessionManager;
import cn.adminzero.helloword.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> signUp(String username, String password, String userNickName){
        try {
            // TODO: handle Sign Up process
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "注册 Doe");
            SessionManager.getInstance().writeToServer(SendMsgMethod.getObjectMessage(CMDDef.SIGN_UP_REQUESET,
                    new SignUpRequest(username,password,userNickName)));
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error signing up", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
