package com.bugiadev.data.network

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import kotlin.reflect.KClass
import io.reactivex.functions.Function

@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorType(val type: KClass<*>)

class TVeoRXCallAdapterFactory(
    private val rxCallAdapterFactory: RxJava2CallAdapterFactory
) : CallAdapter.Factory() {

    companion object {
        fun create(): TVeoRXCallAdapterFactory {
            return TVeoRXCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        @Suppress("UNCHECKED_CAST")
        val rxJava2CallAdapter: CallAdapter<Any, Any> =
            rxCallAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return TVeoRxCallAdapter(annotations, retrofit, rxJava2CallAdapter)
    }

    private class TVeoRxCallAdapter constructor(
        private val annotations: Array<Annotation>,
        private val retrofit: Retrofit,
        private val callAdapter: CallAdapter<Any, Any>
    ) : CallAdapter<Any, Any> {
        override fun adapt(call: Call<Any>): Any =
            when (val any = callAdapter.adapt(call)) {
                is Observable<*> -> any.onErrorResumeNext(Function {
                    Observable.error(handleError(annotations, retrofit, it))
                })
                is Single<*> -> {
                    any.onErrorResumeNext {
                        Single.error(handleError(annotations, retrofit, it))
                    }
                }
                is Completable -> {
                    any.onErrorResumeNext {
                        Completable.error(handleError(annotations, retrofit, it))
                    }
                }
                else -> any
            }

        private fun handleError(
            annotations: Array<Annotation>,
            retrofit: Retrofit,
            throwable: Throwable
        ): Throwable {
            val errorType = annotations.find { it is ErrorType } as? ErrorType
            return if (errorType != null && throwable is HttpException) {
                val error = parseError(retrofit, throwable, errorType.type)
                TVeoHttpException(error, throwable.message(), throwable)
            } else {
                throwable
            }
        }

        private fun parseError(
            retrofit: Retrofit,
            httpException: HttpException,
            kClass: KClass<*>
        ): Any? {
            if (httpException.response()?.isSuccessful == true) {
                return null
            }
            val errorBody = httpException.response()?.errorBody() ?: return null
            val converter: Converter<ResponseBody, Any> =
                retrofit.responseBodyConverter(kClass.java, arrayOf())
            return converter.convert(errorBody)
        }

        override fun responseType(): Type {
            return callAdapter.responseType()
        }
    }
}

class TVeoHttpException(
    val error: Any?,
    override val message: String = "",
    val httpException: HttpException
) : RuntimeException()