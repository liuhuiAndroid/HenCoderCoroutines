package com.rengwuxian.coursecoroutines._3_scope_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：GlobalScope
 * GlobalScope 的 CoroutineContext 为空，自然 Job 为空，不会被任何组件的关闭而取消协程
 * GlobalScope 专门用来启动没有生命周期的协程的单例的 CoroutineScope
 */
@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    CoroutineScope(EmptyCoroutineContext).launch {

    }
    GlobalScope.launch {
        coroutineContext[Job] // 可能 null
    }
    GlobalScope.cancel()
    val job = GlobalScope.async {
        delay(1000)
    }
    println("job parent: ${job.parent}")
    delay(10000)
}