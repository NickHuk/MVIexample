package com.huchihaitachi.login

import com.huchihaitachi.base.BaseViewState

sealed class LoginViewState: BaseViewState {
    class ShowLogin(): LoginViewState()
    class Success(): LoginViewState()
    class Loading(): LoginViewState()
    class Error(val error: Throwable): LoginViewState()
}