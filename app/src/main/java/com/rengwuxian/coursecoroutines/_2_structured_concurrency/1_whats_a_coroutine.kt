package com.rengwuxian.coursecoroutines._2_structured_concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.IO)
    var innerJob: Job? = null
    var innerScope: CoroutineScope? = null
    val outerJob = scope.launch(Dispatchers.Default) {
        innerJob = coroutineContext[Job]
        innerScope = this
        launch {

        }
    }
    outerJob.cancel()

    scope.async {

    }
    println("outerJob: $outerJob")
    println("innerJob: $innerJob")
    println("outerJob === innerJob: ${outerJob === innerJob}")
    println("outerJob === innerScope: ${outerJob === innerScope}")
}