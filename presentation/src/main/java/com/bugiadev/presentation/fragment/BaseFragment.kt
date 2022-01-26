package com.bugiadev.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.bugiadev.presentation.viewmodel.ErrorState
import com.bugiadev.presentation.viewmodel.NoNetworkError
import com.bugiadev.presentation.views.ConfirmationDialog
import com.bugiadev.presentation.views.LoadingDialog
import com.bugiadev.presentation.R
import com.bugiadev.presentation.TVeoInjection
import com.bugiadev.presentation.display.MovieDisplay
import java.lang.IllegalStateException

private const val LOADER_MINIMAL_SHOW_TIME = 300

open class BaseFragment<T>: Fragment() where T : ViewDataBinding {
    private var _binding: T? = null
    protected var binding: T
        get() = requireBinding()
        set(value) { _binding = value }

    protected lateinit var sharedPrefs: SharedPreferences
    private var onActivityWasNotCreated = false
    private var loadingDialog: LoadingDialog? = null
    private var startTime: Long = 0

    private fun requireBinding(): T {
        _binding?.let {
            return it
        } ?: throw IllegalStateException("Binding is null, check the status of your fragment ${this::class}")
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity?.lifecycle?.currentState == Lifecycle.State.CREATED) {
            createDaggerComponent()
        } else {
            onActivityWasNotCreated = true
        }
    }

    open fun createDaggerComponent() {
        // Make secure operations with activity already created
    }

    open fun markAsFav(movieId: String) {
        // Use SharedPreferences for add favorites movies to a set
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs = (activity as TVeoInjection).getSharedPreferences()
        if (onActivityWasNotCreated) {
            createDaggerComponent()
        }
    }

    fun showErrorState(errorState: ErrorState) {
        ConfirmationDialog(getErrorString(errorState)).show(
            childFragmentManager, ConfirmationDialog.TAG
        )
    }

    fun showLoader(visible: Boolean) {
        if (visible) {
            if (loadingDialog == null) {
                startTime = System.currentTimeMillis()
                loadingDialog = LoadingDialog.newInstance()
                activity?.let {
                    loadingDialog?.show(it.supportFragmentManager)
                }
            }
        } else {
            tryHideLoader()
        }
    }

    private fun tryHideLoader() {
        val showTime = System.currentTimeMillis() - startTime
        if (showTime > LOADER_MINIMAL_SHOW_TIME) {
            hideLoader()
        } else {
            Handler().postDelayed({
                hideLoader()
            }, LOADER_MINIMAL_SHOW_TIME - showTime
            )
        }
    }

    private fun hideLoader() {
        loadingDialog?.dismissAllowingStateLoss()
        loadingDialog = null
    }

    private fun getErrorString(errorState: ErrorState): Int =
        when (errorState) {
            NoNetworkError -> R.string.no_network_error
            else -> R.string.unexpected_error
        }

    fun handleBackButton(onBackPressed: () -> Unit) {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }
}