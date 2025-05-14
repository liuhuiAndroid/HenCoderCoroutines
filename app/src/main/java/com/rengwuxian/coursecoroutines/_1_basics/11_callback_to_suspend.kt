package com.rengwuxian.coursecoroutines._1_basics

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.rengwuxian.coursecoroutines.R
import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 标题：连接线程世界：和回调型 API 协作
 */
class CallbackToSuspendActivity : ComponentActivity() {

    private lateinit var infoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_1)
        infoTextView = findViewById(R.id.infoTextView)

        val job = lifecycleScope.launch {
            try {
                val contributors = callbackToSuspend()
//                val contributors = callbackToCancellableSuspend()
                showContributors(contributors)
            } catch (e: Exception) {
                infoTextView.text = e.message
            }
        }
//
//        lifecycleScope.launch {
//            suspendCancellableCoroutine {
//
//            }
//        }
//        val job = lifecycleScope.launch {
//            println("Coroutine cancel: 1")
//            Thread.sleep(500)
//            println("Coroutine cancel: 2")
//        }

        lifecycleScope.launch {
            delay(200)
            job.cancel()
        }
    }

    /**
     * 挂起函数版本的网络请求
     * suspendCoroutine 将回调式函数转换为挂起函数
     */
    private suspend fun callbackToSuspend() = suspendCoroutine {
        gitHub.contributorsCall("square", "retrofit").enqueue(object : Callback<List<Contributor>> {
            override fun onResponse(
                call: Call<List<Contributor>>, response: Response<List<Contributor>>,
            ) {
                it.resume(response.body()!!)
            }

            override fun onFailure(call: Call<List<Contributor>>, t: Throwable) {
                it.resumeWithException(t)
            }
        })
    }

    /**
     * 挂起函数可以配合协程的取消行为，但是 suspendCoroutine 不配合
     * suspendCancellableCoroutine 支持取消
     */
    private suspend fun callbackToCancellableSuspend() = suspendCancellableCoroutine {
        it.invokeOnCancellation {
            // 执行协程取消时的收尾工作
        }
        gitHub.contributorsCall("square", "retrofit").enqueue(object : Callback<List<Contributor>> {
            override fun onResponse(
                call: Call<List<Contributor>>, response: Response<List<Contributor>>,
            ) {
                it.resume(response.body()!!)
            }

            override fun onFailure(call: Call<List<Contributor>>, t: Throwable) {
                it.resumeWithException(t)
            }
        })
    }

    private fun callbackStyle() {
        gitHub.contributorsCall("square", "retrofit").enqueue(object : Callback<List<Contributor>> {
            override fun onResponse(
                call: Call<List<Contributor>>, response: Response<List<Contributor>>,
            ) {
                showContributors(response.body()!!)
            }

            override fun onFailure(call: Call<List<Contributor>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun showContributors(contributors: List<Contributor>) =
        contributors.map { "${it.login} (${it.contributions})" }.reduce { acc, s -> "$acc\n$s" }
            .let { infoTextView.text = it }
}