package cn.adminzero.helloword.ui.login;

import androidx.annotation.Nullable;

import cn.adminzero.helloword.CommonClass.UserNoPassword;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private UserNoPassword success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable UserNoPassword success) {
        this.success = success;
    }

    @Nullable
    UserNoPassword getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
