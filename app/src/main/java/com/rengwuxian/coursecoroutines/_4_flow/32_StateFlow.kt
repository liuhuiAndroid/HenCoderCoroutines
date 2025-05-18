package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：StateFlow
 * 特殊的 SharedFlow => value 最新一条事件的数据
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val name = MutableStateFlow("rengwuxian") // 参数是状态初始值
    val flow1 = flow {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }
    val readonlyFlow = name.asStateFlow() // => ReadonlyStateFlow 暴露给外部订阅
    val state = flow1.stateIn(scope)
    scope.launch {
        name.collect { // 订阅 StateFlow
            println("State: $it")
        }
    }
    scope.launch {
        delay(2000)
        name.emit("扔物线") // 更新
    }
    delay(10000)
}