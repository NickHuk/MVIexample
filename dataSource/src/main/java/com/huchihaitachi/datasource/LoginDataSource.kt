package com.huchihaitachi.datasource

import io.reactivex.Observable

interface LoginDataSource {

    fun loginRedirect(): Observable<Unit>
}