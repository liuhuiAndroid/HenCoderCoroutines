package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：Flow 的收集
 */
fun main() = runBlocking<Unit> {
    val flow = flow {
//        launch(Dispatchers.IO) { // 不允许切换协程调用 emit
//            delay(2000)
//            emit(2)
//        }
        delay(1000)
        emit(1)
    }
//        .flowOn(Dispatchers.IO) // 定制整体的工作协程
    val scope = CoroutineScope(EmptyCoroutineContext)

    flow.onEach { // 更简单规整
        println("flow: $it")
    }.launchIn(scope)

    scope.launch(Dispatchers.Default) {
        flow.collect {
            println("flow: $it")
        }
        flow.collectIndexed { index, value -> }
        flow.collectLatest { }
    }
    delay(10000)
}