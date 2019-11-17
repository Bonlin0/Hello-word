package cn.adminzero.helloword.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import cn.adminzero.helloword.CommonClass.UserNoPassword;
import cn.adminzero.helloword.data.LoginRepository;
import cn.adminzero.helloword.data.Result;
import cn.adminzero.helloword.data.model.LoggedInUser;
import cn.adminzero.helloword.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }


    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        // 原版的登录需要接受数据并处理，这里由于在LoginActivity进行接收所以注释这里
        /*if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new UserNoPassword(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }

    public void signUp(String username, String password, String userNickName) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.signUp(username, password, userNickName);

        // 原版的登录需要接受数据并处理，这里由于在LoginActivity进行接收所以注释这里
        /*if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new UserNoPassword(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }

    public void loginDataChanged(String username, String password, String userNickName, boolean isSignUP) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null));
        } else if (!isUserNickNameValid(userNickName) && isSignUP){
            // 注意这里检查了是不是注册 避免不是注册仍然检查用户名
            loginFormState.setValue(new LoginFormState(null,null, R.string.invalid_userNickName));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check 用户名格式检查
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check 密码格式检查
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder userNickName validation check 昵称格式检查 by whl
    private boolean isUserNickNameValid(String userNickName) {
        return userNickName != null && userNickName.trim().length() > 0;
    }

    public void setLoginFormState(MutableLiveData<LoginFormState> loginFormState) {
        this.loginFormState = loginFormState;
    }

    public void setLoginResult(MutableLiveData<LoginResult> loginResult) {
        this.loginResult = loginResult;
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public void setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
}
