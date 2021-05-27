package com.huchihaitachi.usecase

import io.reactivex.Single

interface IsLoggedInUseCase {

    operator fun invoke(): Single<Boolean>
}