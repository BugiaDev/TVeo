package com.bugiadev.presentation.utils

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bugiadev.presentation.ApplicationComponentProvider
import com.bugiadev.presentation.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

fun ImageView.loadFromUrl(url: String, context: Context) =
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.tveo_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun <T> Single<T>.prepareForUI(): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.subscribe(
    disposables: CompositeDisposable,
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit = {}
) {
    disposables.add(subscribe(onSuccess, onError))
}

inline fun <reified T : ViewModel> Fragment.viewModel(
    crossinline factory: () -> T
) = viewModels<T> {
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            factory() as T
    }
}

val Activity.injector
    get() = (application as ApplicationComponentProvider).provideApplicationComponent()
