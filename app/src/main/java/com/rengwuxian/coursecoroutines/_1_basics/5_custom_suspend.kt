package com.rengwuxian.coursecoroutines._1_basics

import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub

/**
 * 标题：自定义挂起函数
 * 用到别的 suspend 函数，才需要；否则不需要；IDE 让我们加再加
 */
suspend fun getRetrofitContributors(): List<Contributor> {
    return gitHub.contributors("square", "retrofit")
}

// 多余 suspend
suspend fun customSuspendFun() {

}