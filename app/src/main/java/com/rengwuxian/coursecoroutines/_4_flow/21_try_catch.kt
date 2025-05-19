package com.rengwuxian.coursecoroutines._4_flow

import com.rengwuxian.coursecoroutines.common.unstableGitHub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeoutException
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：try_catch 和 Flow 的异常可见性
 * 别用 try catch 包住 emit
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        try {
            for (i in 1..5) {
                // 数据库读数据
                // 网络请求
                emit(i)
            }
        } catch (e: Exception) { // emit 的 catch 会影响 collect 的异常；collect 逻辑是在 emit 里面运行的；
            println("Error in flow(): $e")
            throw e // 异常交给下游数据处理过程处理 // 或者不要用 try catch 包住 emit
        }
    }.map { throw NullPointerException() } // collect 的异常跳过了 map 的数据转换代码 // map 如果抛异常 会抛到上游的 emit
        .onEach { throw NullPointerException() } // 同 map
        .transform<Int, Int> { // transform 能拦截到下游的异常
            val data = it * 2
            emit(data) // 不要用 try catch 包住 emit
            emit(data)
        }
    // Exception Transparency
    scope.launch {
        try {
            flow1.collect {
                val contributors = unstableGitHub.contributors("square", "retrofit")
                println("Contributors: $contributors")
            }
        } catch (e: TimeoutException) {
            println("Network error: $e")
        } catch (e: NullPointerException) {
            println("Null data: $e")
        }
    }
    delay(10000)
}

private fun fun1() {
    fun2()
}

private fun fun2() {
    fun3()
}

private fun fun3() {
    throw NullPointerException("User null")
}
