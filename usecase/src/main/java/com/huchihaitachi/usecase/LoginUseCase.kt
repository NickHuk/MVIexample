package com.huchihaitachi.usecase

import io.reactivex.Observable

interface LoginUseCase {

    operator fun invoke(): Observable<Unit>
}