package cn.adminzero.helloword.ui.login;

import android.support.annotation.Nullable;

/**
 * Data validation state of the login form.
 */

// User NickName Check Added by whl. 2019/11/15 13:12

class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer userNickNameError;

    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer userNickNameError ) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.userNickNameError = userNickNameError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.userNickNameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getUserNickNameError() {
        return userNickNameError;
    }


    boolean isDataValid() {
        return isDataValid;
    }
}
