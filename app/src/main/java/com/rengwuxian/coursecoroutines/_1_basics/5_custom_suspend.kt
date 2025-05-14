package com.rengwuxian.coursecoroutines._1_basics

import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub

/**
 * 标题：自定义挂起函数
 */
suspend fun getRetrofitContributors(): List<Contributor> {
    return gitHub.contributors("square", "retrofit")
}

suspend fun customSuspendFun() {

}