package com.rengwuxian.coursecoroutines._3_scope_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：coroutineScope() 和 supervisorScope()
 * coroutineScope() 没有参数，不能定制 CoroutineContext，launch 有参数；
 * coroutineScope() 是挂起函数，是串行的，launch 是并行的；
 *
 * coroutineScope() 可以用来在挂机函数里提供一个 CoroutineScope 的上下文，这是最重要的一点。
 * coroutineScope() 有返回值，返回最后一行的值，launch 没有返回值；很实用，负责封装。
 * supervisorScope() 提供 SupervisorJob() 类似功能
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        supervisorScope {

        }
        val name = try {
            // 使用 coroutineScope 来进行业务的封装
            coroutineScope {
                val deferred1 = async { "rengwuxian" }
                val deferred2: Deferred<String> = async { throw RuntimeException("Error!") }
                "${deferred1.await()} ${deferred2.await()}"
            }
        } catch (e: Exception) {
            e.message
        }
        println("Full name: $name")
        val startTime1 = System.currentTimeMillis()
        coroutineScope {
            launch {
                delay(2000)
            }
            delay(1000)
            println("Duration within coroutineScope: ${System.currentTimeMillis() - startTime1}")
        }
        println("Duration of coroutineScope: ${System.currentTimeMillis() - startTime1}")
        val startTime = System.currentTimeMillis()
        launch {
            delay(1000)
            println("Duration within launch: ${System.currentTimeMillis() - startTime}")
        }
        println("Duration of launch: ${System.currentTimeMillis() - startTime}") // 比 launch 内的打印早
    }
    delay(10000)
}

/**
 * coroutineScope 可以用来在挂机函数里提供一个 CoroutineScope 的上下文
 */
private suspend fun someFun() = coroutineScope { // 需要提供 coroutineScope
    launch {

    }
}