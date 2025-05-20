package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：SharedFlow 的效果和适用场景
 */
fun main() = runBlocking<Unit> {
    Ticker.start()
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }
    val flow2 = callbackFlow {
        Ticker.subscribe { trySend(it) }
        awaitClose()
    }
    scope.launch {
        delay(2500)
        flow2.collect {
            println("flow2 - 1: $it")
        }
    }
    scope.launch {
        delay(1500)
        flow2.collect {
            println("flow2 - 2: $it")
        }
    }
    // shareIn 把普通的 flow 转换为 SharedFlow；转发
    // 转换为 SharedFlow 后多次 collect 数据源共享；发送流程和数据收集流程分开了；像 Channel
    // 1. 希望共享数据生产流程使用 shareIn
    // 2. 数据生产的提前启动
    // 生产之后才收集的话会漏数据
    val sharedFlow = flow1.shareIn(scope, SharingStarted.Eagerly)
    scope.launch {
        delay(500)
        sharedFlow.collect { // SharedFlow 会漏数据
            println("SharedFlow in Coroutine 1: $it")
        }
    }
    // Channel: hot；不读取也会发送
    // Flow: cold；collect 才会发送 => 用于收集
    // SharedFlow: cold but 生产独立 => 用于订阅
    scope.launch {
        delay(1500)
        sharedFlow.collect {
            println("SharedFlow in Coroutine 2: $it")
        }
    }
    delay(10000)
}

/**
 * 模拟 SharedFlow 原理
 */
object Ticker {
    private var time = 0
        set(value) { // Kotlin setter
            field = value
            subscribers.forEach { it(value) }
        }

    private val subscribers = mutableListOf<(Int) -> Unit>()

    fun subscribe(subscriber: (Int) -> Unit) {
        subscribers += subscriber
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun start() {
        GlobalScope.launch {
            while (true) {
                delay(1000)
                time++
            }
        }
    }
}