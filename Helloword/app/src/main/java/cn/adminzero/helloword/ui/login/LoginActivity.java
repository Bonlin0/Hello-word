package cn.adminzero.helloword.ui.login;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import cn.adminzero.helloword.R;
import cn.adminzero.helloword.ui.login.LoginViewModel;
import cn.adminzero.helloword.ui.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                // TODO 打开主活动
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
                        passwordEditText.getText().toString(),userNickNameEditText.getText().toString(),signUpSwitch.isChecked());
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
                    if(signUpSwitch.isChecked()){
                        loginViewModel.signUp(
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                userNickNameEditText.getText().toString());
                    }
                    else {
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
                if(signUpSwitch.isChecked()){
                    loginViewModel.signUp(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            userNickNameEditText.getText().toString());
                }
                else {
                    loginViewModel.login(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }

            }
        });

        signUpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    userNickNameEditText.setText("");
                    userNickNameEditText.setVisibility(View.VISIBLE);
                    loginButton.setText("注册");
                }
                else
                {
                    userNickNameEditText.setVisibility(View.GONE);
                    loginButton.setText("登录");
                }
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),userNickNameEditText.getText().toString(),signUpSwitch.isChecked());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
