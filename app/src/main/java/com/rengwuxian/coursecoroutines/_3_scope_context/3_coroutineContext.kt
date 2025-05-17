package com.rengwuxian.coursecoroutines._3_scope_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * 标题：从挂起函数里获取 CoroutineContext
 * coroutineContext / currentCoroutineContext (避免命名冲突)
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        coroutineContext
        showDispatcher()
    }
    delay(10000)
}

@OptIn(DelicateCoroutinesApi::class)
private fun flowFun() {
    flow<String> {
        coroutineContext
    }
    GlobalScope.launch {
        flow<String> {
            coroutineContext // launch 的 CoroutineContext // 双重环境
            currentCoroutineContext() // 避免命名冲突 ✅
        }
    }
}

private suspend fun showDispatcher() {
    delay(1000)
    // 合法但是得手动引入
    println("Dispatcher: ${coroutineContext[ContinuationInterceptor]}")
}