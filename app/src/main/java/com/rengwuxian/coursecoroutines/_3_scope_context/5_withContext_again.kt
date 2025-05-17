package com.rengwuxian.coursecoroutines._3_scope_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：再谈 withContext()
 * withContext 可用来切线程
 * withContext 是用来启动串行协程的，定制 coroutineContext。
 * launch / async 是并行的。
 * withContext 是可以定制 coroutineContext 的 coroutineScope 函数
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        withContext(coroutineContext) {

        }
        coroutineScope { // coroutineScope 相当于没填参数的 withContext

        }
    }
    delay(10000)
}