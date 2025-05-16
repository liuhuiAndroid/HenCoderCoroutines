package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：shareIn() 操作符
 * 把普通 flow 转换为 SharedFlow
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }
    // scope: CoroutineScope 从哪里启动协程
    // started: SharingStarted 数据生产的启动时间 Eagerly 立即启动；Lazily collect 时 flow 才启动；WhileSubscribed 复杂化的 Lazily；
    // WhileSubscribed 有两个参数：stopTimeoutMillis 等待时长；replayExpirationMillis 缓存失效时间
    // replay: Int = 0 缓冲区大小，用于生产速度大于消费速度的场景，已消费过的数据也依然缓冲下来
    val sharedFlow = flow1.shareIn(scope, SharingStarted.WhileSubscribed(), 2)
    scope.launch {
        val parent = this
        launch {
            delay(4000)
            parent.cancel()
        }
        delay(1500)
        sharedFlow.collect { // collect 返回值是 Nothing，永远运行下去；可以用抛异常的方式结束
            println("SharedFlow in Coroutine 1: $it")
        }
    }
    scope.launch {
        delay(5000)
        sharedFlow.collect {
            println("SharedFlow in Coroutine 2: $it")
        }
    }
    delay(10000)
}