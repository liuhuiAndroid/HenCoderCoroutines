package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：Flow 的功能定位
 *
 * StateFlow：状态订阅，使用 SharedFlow 实现的
 * SharedFlow：事件订阅，使用 Flow 实现的
 * Flow：协程版的 Sequence
 * Sequence：提供一个边生产边消费的数据序列；只有生产规则，没有内部数据；不支持协程；想用挂起函数用 Flow
 */
fun main() = runBlocking<Unit> {
    val list = buildList {
//    while (true) {
        add(getData())
//    }
    }
    for (num in list) {
        println("List item: $num")
    }
//    list.asSequence()
    val nums = sequence {
        while (true) {
            yield(1) // 生产数据，消极生产
//            yield(getData()) // 报错：@RestrictsSuspension 限制挂起，只能调用自己的挂起函数 yield 和 yieldAll
        }
    }.map { "number $it" }
    for (num in nums) {
        println(num)
    }

    val numsFlow = flow {
        while (true) {
            emit(getData())
        }
    }.map { "number $it" }
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        numsFlow.collect { // 不能使用 for 循环
            println(it)
        }
    }
    delay(10000)
}

/**
 * 模拟获取网络数据
 */
suspend fun getData(): Int {
    return 1
}
