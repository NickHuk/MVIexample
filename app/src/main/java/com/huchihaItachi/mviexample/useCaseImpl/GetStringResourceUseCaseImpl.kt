package com.huchihaItachi.mviexample.useCaseImpl

import com.huchihaitachi.repository.ResourcesRepository
import com.huchihaitachi.usecase.GetStringResourceUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetStringResourceUseCaseImpl @Inject constructor(
  private val resourcesRepository: ResourcesRepository
) : GetStringResourceUseCase {

  override fun invoke(resId: Int, vararg params: Any): String =
    resourcesRepository.getString(resId, *params)
}