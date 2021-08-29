package com.akuwalink.kohane.logic.model

import android.opengl.GLSurfaceView

import com.akuwalink.kohane.R
import com.akuwalink.kohane.logic.physical.basic.Vec3
import com.akuwalink.kohane.util.Matrix
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class World(g:Vec3){

    /**
     * 模型列表，主动物体列表，待添加物体列表，光线，相机
     */
    var mod=ConcurrentHashMap<Integer,Model>()
    var model_list=ConcurrentHashMap<Integer,Model>()
    var activity_list=ConcurrentHashMap<Integer,Model>()
    var next_add_list=ConcurrentHashMap<Integer,Model>()
    var light=Light(Vec3(0f,0f,0f))
    var camera=Matrix()

    /**
     * 初始化相机
     */
    init {
        camera.setPresp(-1f,1f,-1f,1f,2f,10f)
    }

    /**
     * 添加模型
     */
    @Synchronized
    fun addModel(id:Integer,model: Model){
        next_add_list.set(id,model)
    }

    /**
     * 移除模型
     */
    @Synchronized
    fun removeModdel(id:Int,model: Model){
        model_list.remove((Integer(model.id)))
        if (model.move_flag==true) activity_list.remove(Integer(model.id))
    }

    /**
     * 移除所有模型
     */
    @Synchronized
    fun removeAll(){
        model_list.clear()
        activity_list.clear()
        next_add_list.clear()
    }

    /**
     * 将未添加的模型导入绘制列表，绘制
     */
    @Synchronized
    fun drawAll(drawMode:Int){
        for(i in next_add_list){
            model_list.put(i.key,i.value)
            if(i.value.move_flag==true){
                activity_list.put(i.key,i.value)
            }
            next_add_list.clear()
        }

        if (model_list.size > 0) {
            for (i in model_list) {
                i.value.drawSelf(light, camera)
            }
        }

    }
}