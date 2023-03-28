package com.app.feenix.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

open class BaseWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        return Result.success()
    }
}