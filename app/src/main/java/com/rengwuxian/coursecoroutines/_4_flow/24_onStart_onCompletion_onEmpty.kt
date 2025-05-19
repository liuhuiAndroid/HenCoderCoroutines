package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：onStart() 等全流程监听系列操作符
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        try {
            for (i in 1..5) {
                emit(i)
            }
        } catch (e: Exception) {
            println("try / catch: $e")
        }
    }
        .onStart { // 调用 collect 之前，数据还没有开始生产
            println("onStart 1")
            throw RuntimeException("onStart error") // 不能被 emit 的 try catch 捕获 // 可以用 catch 操作符捕获
        }
        .onStart { println("onStart 2") }
        .onCompletion {
            println("onCompletion: $it") // 异常结束也会触发，但是不拦截异常
        }
        .onEmpty { println("onEmpty") } // 正常结束且未发送数据会触发
        .catch { println("catch: $it") }

    scope.launch {
        flow1.collect {
            println("Data: $it")
        }
    }
    delay(10000)
}