package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：flowOn() 操作符
 * flowOn 只对上游有效，可以用来切线程；粒度比较粗，withContext 粒度细
 */
@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val flow1 = flow {
        println("CoroutineContext in flow(): ${currentCoroutineContext()}")
        for (i in 1..5) {
            emit(i) // withContext 包出 emit 会导致切换下游线程，这是违规的，会报错
        }
    }
        .map {
            println("CoroutineContext in map() 1: ${currentCoroutineContext()}")
            it * 2
        }
        .flowOn(Dispatchers.IO)
        .flowOn(CoroutineName("flowOn")) // 连续调用两个 flowOn 会 fuse 融合到一起；CoroutineContext 相加
        .map {
            println("CoroutineContext in map() 2: ${currentCoroutineContext()}")
            it * 2
        }
        .flowOn(newFixedThreadPoolContext(2, "TestPool"))
    val flow2 = channelFlow {
        println("CoroutineContext in channelFlow(): ${currentCoroutineContext()}")
        for (i in 1..5) {
            send(i)
        }
    }
        .map { it }
        .flowOn(Dispatchers.IO) // flowOn 会跟 channelFlow fuse 融合；用 map 隔开不会 fuse，会创建新的对象
    scope.launch {
        flow1.map {
            it + 1
        }
            .onEach {
                println("Data: $it - ${currentCoroutineContext()}")
            }
            .flowOn(Dispatchers.IO)
            .collect {

            }
        flow2.collect()
    }
//    flow1.map {
//        it + 1
//    }
//        .onEach {
//            println("Data: $it - ${currentCoroutineContext()}")
//        }
//        .launchIn(scope + Dispatchers.IO)
    delay(10000)
}