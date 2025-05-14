package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：Flow 的工作原理和应用场景
 * flow 原理：把 flow 代码块的代码挪到 collect 来执行
 * Channel => Hot，调用 send 就生产，和是否调用 receive 无关
 * Flow => Cold，只存放生产规则，真正生产等每次 collect 才开始
 * Hot => 没收集数据的时候也可以生产数据
 * Cold => 开始收集数据的时候才生产数据
 */
fun main() = runBlocking<Unit> {
    val numsFlow = flow {
        emit(1)
        delay(100)
        emit(2)
    }
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
//        showWeather(weatherFlow)
        weatherFlow.collect {
            println("Weather: $it")
        }
        // log("done")
//        numsFlow.collect {
//            println("A: $it")
//        }
    }
    scope.launch {
        delay(50)
        numsFlow.collect { // collect 多次可以成功拿到数据
            println("B: $it")
        }
    }
    delay(10000)
}

/**
 * 功能拆分需要用到 flow，否则可以直接在协程里运行
 */
val weatherFlow = flow {
    while (true) {
        emit(getWeather())
        delay(60000)
    }
}

/**
 * 功能拆分
 */
suspend fun showWeather(flow: Flow<String>) {
    flow.collect {
        println("Weather: $it")
    }
}

suspend fun getWeather() = withContext(Dispatchers.IO) {
    "Sunny"
}