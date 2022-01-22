package com.bugiadev.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bugiadev.presentation.DaggerTVeoComponent
import com.bugiadev.presentation.R
import com.bugiadev.presentation.TVeoComponent
import com.bugiadev.presentation.TVeoInjection
import com.bugiadev.presentation.databinding.ActivityMainBinding
import com.bugiadev.presentation.utils.PresentationConstants
import com.bugiadev.presentation.utils.PresentationConstants.DEFAULT_VALUE
import com.bugiadev.presentation.utils.PresentationConstants.MOVIES_NUMBER
import com.bugiadev.presentation.utils.injector

class MainActivity : AppCompatActivity(), TVeoInjection {
    private var _binding: ActivityMainBinding? = null
    private var binding: ActivityMainBinding
        get() = _binding
            ?: throw IllegalStateException("Binding is null, check the status of your Activity ${this::class}")
        set(value) {
            _binding = value
        }

    private lateinit var tVeoComponent: TVeoComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        binding.tvHello.text = intent.getIntExtra(MOVIES_NUMBER, DEFAULT_VALUE).toString()
        tVeoComponent = DaggerTVeoComponent.factory().create(injector)
    }

    override fun getTVeoComponent(): TVeoComponent = tVeoComponent
}