package cn.adminzero.helloword.ui.login;

import android.app.Activity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Utils.SerializeUtils;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import cn.adminzero.helloword.MainActivity;
import cn.adminzero.helloword.NetWork.MinaService;
import cn.adminzero.helloword.R;

import static cn.adminzero.helloword.App.userNoPassword_global;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private LoginActivityBroadcastReceiver loginActivityBroadcastReceiver;
    private LoginViewModel loginViewModel;
    private IntentFilter intentFilter;

    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivityBroadcastReceiver = new LoginActivityBroadcastReceiver();
        intentFilter = new IntentFilter(CMDDef.MINABroadCast);
        sharedPreferences = super.getSharedPreferences("LoginSharedPreference",MODE_PRIVATE);

        Intent intent = new Intent(LoginActivity.this, MinaService.class);
        //开启MINA服务
        startService(intent);

        LocalBroadcastManager.getInstance(this).
                registerReceiver(loginActivityBroadcastReceiver, intentFilter);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final EditText userNickNameEditText = findViewById(R.id.user_nickname);
        final Switch signUpSwitch = findViewById(R.id.sign_in_or_sign_up);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            // 检查登录的输入是否正确 如果不正确 使得按钮不能点击 by whl
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                // added 检查用户昵称是否有误 并设置
                if (loginFormState.getUserNickNameError() != null) {
                    userNickNameEditText.setError(getString(loginFormState.getUserNickNameError()));
                }
            }
        });

        // 监视登录，一旦ViewModel里面的LoginResult设置为Success 这里会直接结束本活动
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    // 如果是Error 直接退出
                    return;
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                // 加入sharedPreference 以后检查自动登录
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLogin",true);
                editor.putString("storedUserEmail",usernameEditText.getText().toString());
                editor.putString("storedPassword",passwordEditText.getText().toString());
                editor.commit();

                //Complete and destroy login activity once successful
                Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), userNickNameEditText.getText().toString(), signUpSwitch.isChecked());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        userNickNameEditText.addTextChangedListener(afterTextChangedListener);

        // 这里就是输入法右下角一般会有的按钮 可以在输入法里点登陆直接登录
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (signUpSwitch.isChecked()) {
                        loginViewModel.signUp(
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                userNickNameEditText.getText().toString());
                    } else {
                        loginViewModel.login(
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // 按钮被点击后判断是否为注册
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if (signUpSwitch.isChecked()) {
                    loginViewModel.signUp(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            userNickNameEditText.getText().toString());
                } else {
                    loginViewModel.login(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
            }
        });

        signUpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userNickNameEditText.setText("");
                    userNickNameEditText.setVisibility(View.VISIBLE);
                    loginButton.setText("注册");
                } else {
                    userNickNameEditText.setVisibility(View.GONE);
                    loginButton.setText("登录");
                }
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), userNickNameEditText.getText().toString(), signUpSwitch.isChecked());
            }
        });


        //检查SharedPreference 如果有，则自动填充尝试登录
        if(sharedPreferences.getBoolean("isLogin",false)){
            usernameEditText.setText(sharedPreferences.getString("storedUserEmail",""));
            passwordEditText.setText(sharedPreferences.getString("storedPassword",""));
            signUpSwitch.setChecked(false);
            loginViewModel.login(
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginActivityBroadcastReceiver);
    }

    private void updateUiWithUser(UserNoPassword userNoPassword) {
        String welcome = getString(R.string.welcome) + userNoPassword_global.getUserNickName();
        // TODO : initiate successful logged in experience
        View view = getWindow().getDecorView().findViewById(R.id.container);
        Snackbar.make(view, welcome, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        // 清空邮箱输入
        EditText usernameEditText = findViewById(R.id.username);
        usernameEditText.setText("");

        View view = getWindow().getDecorView().findViewById(R.id.container);
        Snackbar.make(view, errorString, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        // Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    class LoginActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            short cmd = intent.getShortExtra(CMDDef.INTENT_PUT_EXTRA_CMD, (short) -1);
            switch (cmd) {
                case CMDDef.REPLY_SIGN_UP_REQUEST:
                {
                    Log.e("tag", "" + cmd);
                    byte[] data = intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA);
                    try {
                        UserNoPassword userNoPassword = (UserNoPassword) SerializeUtils.serializeToObject(data);
                        //如果收到的UserNoPassword不合法，检查并以error报出
                        if(!userNoPassword.isValid())                        {
                            MutableLiveData<LoginResult> loginResult = (MutableLiveData<LoginResult>) loginViewModel.getLoginResult();
                            loginResult.setValue(new LoginResult(R.string.invalid_sign_up));
                        }
                        else {
                            MutableLiveData<LoginResult> loginResult = (MutableLiveData<LoginResult>) loginViewModel.getLoginResult();
                            userNoPassword_global = userNoPassword; // 这一行必须放在下一行前面，因为更改以后会尝试请求该变量
                            loginResult.setValue(new LoginResult(userNoPassword));
                        }

                    } catch (IOException e) {
                        Log.e("tag","序列化失败!");
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case  CMDDef.REPLY_SIGN_IN_REQUEST:
                {
                    Log.e("tag", "" + cmd);
                    byte[] data = intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA);
                    try {
                        UserNoPassword userNoPassword = (UserNoPassword) SerializeUtils.serializeToObject(data);
                        //如果收到的UserNoPassword不合法，检查并以error报出
                        if(!userNoPassword.isValid())                        {
                            MutableLiveData<LoginResult> loginResult = (MutableLiveData<LoginResult>) loginViewModel.getLoginResult();
                            loginResult.setValue(new LoginResult(R.string.invalid_sign_in));
                        }
                        else {
                            MutableLiveData<LoginResult> loginResult = (MutableLiveData<LoginResult>) loginViewModel.getLoginResult();
                            userNoPassword_global = userNoPassword; // 这一行必须放在下一行前面，因为更改以后会尝试请求该变量
                            loginResult.setValue(new LoginResult(userNoPassword));
                        }
                    } catch (IOException e) {
                        Log.e("tag","序列化失败!");
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        }
    }
}
