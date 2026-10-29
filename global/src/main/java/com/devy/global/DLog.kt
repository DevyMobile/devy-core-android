package com.devy.global

import timber.log.Timber

/**
 * Created by JeYHoon on 2025. 10. 3..
 */
object DLog {

    private const val TAG = "JTest, DTest"

    fun Throwable.log() {
        e(this)
    }

    fun java.lang.Exception.log() {
        e(this)
    }

    fun String.dLog() {
        this.log()
    }

    fun Any.dLog() {
        this.log()
    }

    fun Pair<Any, Any>.dLog() {
        this.log()
    }

    fun String.log() {
        f(this)
    }

    fun Any.log() {
        toString().log()
    }

    fun Pair<Any, Any>.log() {
        f(this)
    }

    fun f(vararg values: Pair<Any, Any>) {
        val msg = parseString(values)
        f(msg)
    }

    fun f(msg: String) {
        Timber.tag(TAG).d(toBuildMessage(msg))
    }

    fun f(tag: String, msg: String) {
        Timber.tag("$TAG, $tag").d(toBuildMessage(msg))
    }

    fun e(ex: Exception) {
        e(toBuildMessage(ex.stackTraceToString()))
    }

    fun es(ex: Exception) {
        e(toBuildMessage(ex.toString()))
    }

    fun e(tr: Throwable) {
        Timber.tag(TAG).e(tr)
    }

    fun e(msg: String) {
        Timber.tag(TAG).e(msg)
    }

    private fun parseString(values: Array<out Pair<Any, Any>>): String {
        var msg = ""
        values.forEach {
            val ele = "${it.first} : ${it.second}"
            msg =
                if (msg.isEmpty()) ele
                else "$msg, $ele"
        }

        return msg
    }

    private fun toBuildMessage(message: String): String {
        // 1. JLog 클래스의 정확한 이름을 가져옵니다. (하드코딩 대신)
        val loggerClassName = DLog::class.java.name

        // 2. 현재 스레드의 스택 트레이스를 가져옵니다.
        val stackTrace = Thread.currentThread().stackTrace

        // 3. JLog 내부의 모든 호출을 건너뛰고 실제 호출자를 찾습니다.
        val callerStackTrace = stackTrace
            // 스택의 처음부터 JLog 클래스를 만날 때까지 모두 버립니다. (e.g., getStackTrace)
            .dropWhile { it.className != loggerClassName }
            // JLog 클래스에 속한 요소들을 모두 버립니다.
            .dropWhile { it.className == loggerClassName }
            // 그 다음 첫 번째 요소가 바로 우리가 찾는 외부 호출자입니다.
            .firstOrNull()

        // 4. 찾은 호출 지점의 정보로 로그 메시지를 만듭니다.
        return callerStackTrace?.let {
            "[%s > %s](%s:%d) %s".format(
                it.fileName,
                it.methodName,
                it.fileName,
                it.lineNumber,
                message
            )
        } ?: "[Unknown Location] $message" // 만약 찾지 못했을 경우를 대비
    }
}