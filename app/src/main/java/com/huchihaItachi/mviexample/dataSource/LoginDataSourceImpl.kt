package com.huchihaItachi.mviexample.dataSource

import android.content.Context
import com.huchihaitachi.datasource.LoginDataSource
import io.reactivex.Observable
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val context: Context
) : LoginDataSource {

    override fun loginRedirect(): Observable<Unit> {
        //TODO: open login
        return Observable.just(Unit)
    }
}