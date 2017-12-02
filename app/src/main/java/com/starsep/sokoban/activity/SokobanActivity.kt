package com.starsep.sokoban.activity

import android.app.Activity
import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.content.ContextCompat
import com.starsep.sokoban.R

abstract class SokobanActivity : Activity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (taskDescription == null) {
                val launcherIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
                taskDescription = ActivityManager.TaskDescription(
                        getString(R.string.app_name),
                        launcherIcon,
                        ContextCompat.getColor(this, R.color.colorTitle)
                )
            }
            setTaskDescription(taskDescription)
        }
    }

    companion object {
        private var taskDescription: ActivityManager.TaskDescription? = null
    }
}
