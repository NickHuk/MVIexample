package com.huchihaitachi.usecase

interface GetStringResourceUseCase {

  operator fun invoke(resId: Int, vararg params: Any = emptyArray()): String
}