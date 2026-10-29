package com.devy.network.retrofit2.adapter

import com.devy.network.retrofit2.response.ApiResponse
import com.devy.network.retrofit2.response.Error
import com.devy.network.retrofit2.response.ErrorDto
import com.squareup.moshi.Moshi
import okhttp3.Request
import okhttp3.ResponseBody
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

private const val ERROR_BODY_MAX_LENGTH = 500

private val errorAdapter by lazy { Moshi.Builder().build().adapter(ErrorDto::class.java) }

class ResponseCall<T>(private val call: Call<T>): Call<ApiResponse<T>> {
    override fun clone(): Call<ApiResponse<T>> = ResponseCall(call.clone())

    override fun execute(): Response<ApiResponse<T>> = Response.success(call.execute().toApiResponse())

    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        call.enqueue(object: Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@ResponseCall,
                    Response.success(response.toApiResponse())
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ResponseCall,
                    Response.success(t.toFailure())
                )
            }
        })
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    private fun Response<T>.toApiResponse(): ApiResponse<T> =
        try{
            if (isSuccessful) {
                body()?.let { ApiResponse.Success(it) }
                    ?: ApiResponse.Failure.Exception(Throwable("Response body is Null"))
            } else {
                ApiResponse.Failure.HttpError(
                    code(),
                    message(),
                    parseErrorBody(code(), message(), errorBody())
                )
            }
        } catch (t: Throwable) {
            t.toFailure()
        }

    // 비-2xx 본문이 JSON 이 아니면(HTML, plain text 등) 파싱 예외가 IOException 계열로 새어
    // NetworkError 로 오분류되므로, 상태 코드를 보존한 HttpError 용 ErrorDto 로 대체한다.
    // 원문은 message 가 아닌 detail["body"] 에 둔다: message 는 소비 쪽에서 키워드 분류에 쓰이고 저장되기도 한다.
    private fun parseErrorBody(statusCode: Int, statusMessage: String?, body: ResponseBody?): ErrorDto? {
        if (body == null) return null
        val raw = body.string()
        val parsed: ErrorDto? = try {
            errorAdapter.fromJson(raw)
        } catch (e: Exception) {
            null
        }
        if (parsed != null) return parsed

        val excerpt = raw.trim().take(ERROR_BODY_MAX_LENGTH)
        return ErrorDto(
            Error(message = statusMessage, code = statusCode.toString()),
            if (excerpt.isEmpty()) null else mapOf("body" to excerpt)
        )
    }

    private fun Throwable.toFailure(): ApiResponse.Failure =
        if(this is IOException) {
            ApiResponse.Failure.NetworkError(this)
        } else {
            ApiResponse.Failure.Exception(this)
        }
}