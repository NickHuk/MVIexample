package com.huchihaitachi.usecase

import io.reactivex.Completable

interface LoginUseCase {

  operator fun invoke(code: String): Completable
}