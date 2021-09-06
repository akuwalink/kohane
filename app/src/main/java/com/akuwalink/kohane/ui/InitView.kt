package com.akuwalink.kohane.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akuwalink.kohane.logic.model.World
import com.akuwalink.kohane.logic.physical.basic.Vec3
import com.akuwalink.kohane.ui.gameview.GameView

class InitView: AppCompatActivity() {
    var gameView:GameView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView= GameView(this,world = World(Vec3()))
        setContentView(gameView)

    }

    override fun onPause() {
        super.onPause()
        gameView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        gameView!!.onResume()
    }
}