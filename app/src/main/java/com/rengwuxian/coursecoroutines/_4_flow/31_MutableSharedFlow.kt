package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：MutableSharedFlow
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        emit(1) // 数据流设计为只能从内部 emit
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }
    // 参数一：replay: Int = 0, 缓冲区大小
    // 参数二：extraBufferCapacity: Int = 0, 单独增加缓冲
    // 参数三：onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND 缓冲溢出策略
    val clickFlow = MutableSharedFlow<String>() // 事件流选择 MutableSharedFlow；已经有了生产事件流的 flow 用 shareIn
    val readonlyClickFlow = clickFlow.asSharedFlow() // => ReadonlySharedFlow 暴露给外部订阅
    val sharedFlow = flow1.shareIn(scope, SharingStarted.WhileSubscribed(), 2)
    scope.launch {
        clickFlow.emit("Hello") // MutableSharedFlow 可以 emit：事件流设计为可以从外部发送数据；外部数据源
        delay(1000)
        clickFlow.emit("Hi")
        delay(1000)
        clickFlow.emit("你好")
        val parent = this
        launch {
            delay(4000)
            parent.cancel()
        }
        delay(1500)
        sharedFlow.collect {
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