package com.rengwuxian.coursecoroutines._3_scope_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor

/**
 * 标题：CoroutineScope 和 CoroutineContext 的定位
 * CoroutineContext：协程上下文；协程用到的所有信息
 * Job：管理流程
 * ContinuationInterceptor：管理线程
 *
 * CoroutineScope 是 CoroutineContext 的容器；CoroutineScope 有一个属性是 CoroutineContext
 * CoroutineScope 的作用之一是使用CoroutineContext 属性来保存对应的协程代码块的上下文信息
 * CoroutineScope 的另一个作用是启动协程
 * 手动创建的 CoroutineScope 不对应任何协程
 */
fun main() = runBlocking<Unit> {
    val context = Dispatchers.IO + Job() + Job()
    val scope = CoroutineScope(Dispatchers.IO)
    val job = scope.launch {
        this.coroutineContext[Job]
        coroutineContext.job
        coroutineContext[ContinuationInterceptor]
    }
    delay(10000)
}