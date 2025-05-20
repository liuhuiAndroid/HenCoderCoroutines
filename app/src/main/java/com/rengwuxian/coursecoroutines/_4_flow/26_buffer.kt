package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：buffer() 系列操作符
 * buffer() 就是使用 Channel 实现的，用于给 flow 加上缓冲功能
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val start = System.currentTimeMillis()
    val flow1 = flow {
        for (i in 1..5) {
            emit(i)
            println("Emitted: $i - ${System.currentTimeMillis() - start}ms")
        }
    }
        .buffer(1) // 暂存上游生产好的数据，参数同前面 Channel
        .flowOn(Dispatchers.IO) // 协程切换使用 Channel 实现的，也用到了缓冲功能但是不需要配置，默认开启了 buffer
        .buffer(2) // buffer fuse
//        .conflate() // 只缓冲最新一条数据
        .map { it + 1 }
        .map { it * 2 }
    scope.launch {
        flow1
            .mapLatest { it }
            .buffer(0) // 关掉缓冲，没来得及转换会导致丢数据
            .collect {
                delay(1000)
                println("Data: $it")
            }
//            .collectLatest {  // = mapLatest(action).buffer(0).collect()
//
//            } // 当前数据处理过程中，下一条数据就可以进行生产
    }
    delay(10000)
}