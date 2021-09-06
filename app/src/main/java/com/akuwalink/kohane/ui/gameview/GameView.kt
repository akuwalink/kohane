package com.akuwalink.kohane.ui.gameview

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.akuwalink.kohane.logic.model.Light
import com.akuwalink.kohane.logic.model.World
import com.akuwalink.kohane.logic.model.models.Ball
import com.akuwalink.kohane.logic.model.models.Human
import com.akuwalink.kohane.logic.physical.basic.Vec3
import com.akuwalink.kohane.util.loadTOBJ
import com.akuwalink.kohane.util.loadVNOBJ
import com.akuwalink.kohane.util.loadVOBJ
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GameView(context: Context,world: World) :GLSurfaceView(context){
    init{
        setEGLContextClientVersion(3)
        setRenderer(GameRenderer(context,world))
        renderMode= RENDERMODE_CONTINUOUSLY
    }

    class GameRenderer(context: Context,world: World):GLSurfaceView.Renderer{
        val world=world
        var scale=1
        var ball: Ball?=null
        val context=context
        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
            ball!!.drawSelf(world.light,world.camera)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            val ratio=(width/(height*1.0f))
            GLES30.glViewport(0,0,width,height)

            world.camera.setPresp(-ratio*scale,ratio*scale,-1f*scale,1f*scale,28f,32f)
            world.light= Light(Vec3(15f,0f,20f))
            world.camera.setCamera(0f,0f,30f,0f,0f,0f,0f,1f,0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(0.9f,0.9f,0.9f,0.5f)
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            var v:FloatArray?= loadVOBJ("E_BC_Hood_Body_001.obj",context )
            var t:FloatArray?= loadTOBJ("E_BC_Hood_Body_001.obj",context)
            var vn:FloatArray?= loadVNOBJ("E_BC_Hood_Body_001.obj",context)
            ball=Ball(v!!,t!!,vn!!,context)

        }

    }
}