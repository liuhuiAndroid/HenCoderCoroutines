package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.runBlocking

/**
 * 标题：Channel 和 Flow 简介与对比
 */
fun main() = runBlocking<Unit> {
    // StateFlow：状态订阅，使用 SharedFlow 实现的
    // SharedFlow // event flow：事件订阅，使用 Flow 实现的
    // Flow // data flow：数据流
    // Channel：相当于多条数据版的 async ，实现协程之间传递数据的功能，Flow 下层的关键支撑
    // async {  }：一次性
}