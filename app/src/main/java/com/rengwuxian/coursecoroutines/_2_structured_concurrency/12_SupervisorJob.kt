package com.rengwuxian.coursecoroutines._2_structured_concurrency

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 标题：SupervisorJob
 * 不会因为子协程的普通异常被连带取消，又会承担把异常向外抛到线程世界的工作
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(SupervisorJob())

    // override fun childCancelled(cause: Throwable): Boolean = false
    val supervisorJob = SupervisorJob()
    val job = Job()
//    scope.launch(supervisorJob) {
    scope.launch {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught in handler: $exception")
        }
        launch(SupervisorJob(coroutineContext.job) + handler) {
            launch {
                throw RuntimeException("Error!")
            }
        }
    }
    delay(1000)
    println("Parent Job cancelled: ${supervisorJob.isCancelled}")
    println("Parent Job cancelled: ${job.isCancelled}")
    delay(10000)
}