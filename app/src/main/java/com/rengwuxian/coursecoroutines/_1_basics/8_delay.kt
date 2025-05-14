package com.rengwuxian.coursecoroutines._1_basics

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * 标题：「轻量级线程」的本质：从 delay() 说起
 * https://kotlinlang.org/docs/coroutines-basics.html#coroutines-are-light-weight
 */
@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking {
    repeat(50_000) { // launch a lot of coroutines
        launch {
            delay(5000L)    // 延时任务而不是耗时任务
            print(".")
        }
    }

    repeat(50_000) {
        thread {
            Thread.sleep(5000L)     // 阻塞线程
            print(".")
        }
    }

    val executor = Executors.newSingleThreadScheduledExecutor()
    repeat(50_000) {    // 等价的单线程实现方案
        executor.schedule({
            print(".")
        }, 5, TimeUnit.SECONDS)
    }

    val dispatcher = newFixedThreadPoolContext(50000, "50K")
    repeat(50_000) {    // 协程也得完蛋
        launch(dispatcher) {
            Thread.sleep(5000L)
            print(".")
        }
    }
}