package com.huida.navu3d

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LitpalService : Service() {
    override fun onCreate() {
        Log.i("Kathy", "onCreate - Thread ID = " + Thread.currentThread().id)
        super.onCreate()
    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.i(
//            "Kathy",
//            "onStartCommand - startId = $startId, Thread ID = " + Thread.currentThread()
//                .id
//        )
//        return super.onStartCommand(intent, flags, startId)
//    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.i(
            "Kathy",
            "onDestroy - Thread ID = " + Thread.currentThread().id
        )
        super.onDestroy()
    }


}