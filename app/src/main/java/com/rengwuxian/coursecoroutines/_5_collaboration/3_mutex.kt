package com.rengwuxian.coursecoroutines._5_collaboration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：互斥锁和共享变量
 */
//@Synchronized
fun main() = runBlocking {
    var number = 0
//    val lock = Any()
//    val thread1 = thread {
//        repeat(100_0000) {
//            synchronized(lock) {
//                number++ // 不是原子操作
//            }
//        }
//    }
//    val thread2 = thread {
//        repeat(100_0000) {
//            synchronized(lock) {
//                number--
//            }
//        }
//    }
//    thread1.join()
//    thread2.join()
//    println("number: $number")

    val scope = CoroutineScope(EmptyCoroutineContext)
    val mutex = Mutex() // mutual exclusion 针对协程
    val semaphore = Semaphore(3) // 协程版本的 Semaphore，定位和用法和 Java 版一样
    AtomicInteger()
    CopyOnWriteArrayList<String>()
    val job1 = scope.launch {
        semaphore.acquire()
        semaphore.release()
        repeat(100_0000) {
            mutex.withLock {
                number++
            }
        }
    }
    val job2 = scope.launch {
        repeat(100_0000) {
            mutex.withLock {
                number--
            }
        }
    }
    job1.join()
    job2.join()
    println("number: $number")
    delay(10000)
}

@Volatile
var v = 0

@Transient
var t = 0