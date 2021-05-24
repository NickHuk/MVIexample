package com.huchihaItachi.mviexample.useCaseImpl

import android.util.Log
import com.huchihaitachi.repository.UserRepository
import com.huchihaitachi.usecase.IsLoggedInUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsLoggedInUseCaseImpl @Inject constructor(
  private val userRepository: UserRepository
) : IsLoggedInUseCase {

  init {
    Log.d("debug", "fa")
  }

  override fun invoke(): Single<Boolean> = userRepository.isAuthToken().subscribeOn(Schedulers.io())
}