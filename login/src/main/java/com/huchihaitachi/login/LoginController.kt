package com.huchihaitachi.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.google.android.material.snackbar.Snackbar
import com.huchihaitachi.base.visible
import com.huchihaitachi.login.databinding.ControllerLoginBinding
import com.huchihaitachi.login.di.LoginSubcomponentProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class LoginController : Controller(), LoginView {
    private lateinit var presenter: LoginPresenter
    private lateinit var binding: ControllerLoginBinding
    private val disposables: CompositeDisposable = CompositeDisposable()
    private var snackbar: Snackbar? = null

    private val _loginIntent: PublishSubject<Unit> = PublishSubject.create()
    override val loginIntent: Observable<Unit>
        get() = _loginIntent

    init {
        (applicationContext as LoginSubcomponentProvider).provideLoginSubcomponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        presenter.bind(this)
        binding = ControllerLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        binding.signInB.setOnClickListener {
            _loginIntent.onNext(Unit)
            _loginIntent.onComplete()
        }
        presenter.bindIntents()
    }

    override fun onDestroyView(view: View) {
        presenter.unbind()
        disposables.dispose()
        disposables.clear()
        super.onDestroyView(view)
    }

    override fun render(state: LoginViewState) {
        when (state) {
            is LoginViewState.ShowLogin -> {
                binding.loginPb.visible = false
                snackbar?.dismiss()
            }
            is LoginViewState.Loading -> {
                binding.loginPb.visible = true
                snackbar?.dismiss()
            }
            is LoginViewState.Success -> {
                binding.loginPb.visible = false
                snackbar?.dismiss()
            }
            is LoginViewState.Error -> {
                binding.loginPb.visible = false
                snackbar = Snackbar.make(
                    binding.root,
                    state.error.message ?: "Undefined error",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar?.show()
            }
        }
    }
}