package com.devy.network.retrofit2.adapter

import com.devy.network.retrofit2.response.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory.getParameterUpperBound
import retrofit2.CallAdapter.Factory.getRawType
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.jvm.java

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

class ResponseCallAdapter(private val responseType: Type): CallAdapter<Type, Call<ApiResponse<Type>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> = ResponseCall(call)

    class Factory: CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit,
        ): CallAdapter<*, *>? {
            if (getRawType(returnType) != Call::class.java) return null
            check(returnType is ParameterizedType) {
                "Return type must be parameterized as Call<?>"
            }

            val responseType = getParameterUpperBound(0, returnType)
            if (getRawType(responseType) != ApiResponse::class.java) {
                return null
            }
            check(responseType is ParameterizedType) {
                "responseType must be parameterized as ApiResponse<?>"
            }

            val bodyType = getParameterUpperBound(0, responseType)
            return ResponseCallAdapter(bodyType)
        }
    }
}