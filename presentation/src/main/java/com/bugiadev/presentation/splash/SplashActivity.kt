package com.bugiadev.presentation.splash

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bugiadev.presentation.DaggerTVeoComponent
import com.bugiadev.presentation.R
import com.bugiadev.presentation.TVeoComponent
import com.bugiadev.presentation.databinding.ActivitySplashBinding
import com.bugiadev.presentation.main.MainActivity
import com.bugiadev.presentation.utils.PresentationConstants.MOVIES_NUMBER
import com.bugiadev.presentation.utils.injector
import com.bugiadev.presentation.viewmodel.SplashViewModel
import java.lang.IllegalStateException

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashBinding? = null
    private var binding: ActivitySplashBinding
        get() = _binding
            ?: throw IllegalStateException("Binding is null, check the status of your Activity ${this::class}")
        set(value) {
            _binding = value
        }

    private lateinit var tVeoComponent: TVeoComponent
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_splash
        )

        tVeoComponent = DaggerTVeoComponent.factory().create(injector)
        viewModel = tVeoComponent.splashViewModel
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.splashAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) { /* Do nothing */ }
            override fun onAnimationEnd(animation: Animator?) { /* Do nothing */ }
            override fun onAnimationRepeat(animation: Animator?) { viewModel.getMoviesNumber()}
            override fun onAnimationCancel(animation: Animator?) { /* Do nothing */ }
        })
    }

    private fun initObservers() {
        viewModel.moviesNumber.observe(this, { number ->
            Intent(applicationContext, MainActivity::class.java).apply {
                putExtra(MOVIES_NUMBER, number)
                startActivity(this)
                finish()
            }
        })

        viewModel.error.observe(this, {
            Log.e("Kiko", it.toString())
        })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}