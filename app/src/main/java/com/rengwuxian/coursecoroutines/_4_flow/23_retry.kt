package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeoutException
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：retry() 和 retryWhen() 操作符
 * 重启上游链条或让异常向下抛，下游是无感知的
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        for (i in 1..5) {
            // 数据库读数据
            // 网络请求
            if (i == 3) {
                throw RuntimeException("flow() error")
            } else {
                emit(i)
            }
        }
    }
        .map { it * 2 }
        .retry(3) {
            it is RuntimeException // 判断条件
        }
//        .retryWhen { cause, attempt -> }
    scope.launch {
        try {
            flow1.collect {
                println("Data: $it")
            }
        } catch (e: TimeoutException) {
            println("Network error: $e")
        } catch (e: RuntimeException) {
            println("RuntimeException: $e")
        }
    }
    delay(10000)
}