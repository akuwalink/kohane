package com.akuwalink.kohane.logic.model


import android.content.Context
import android.opengl.Matrix
import com.akuwalink.kohane.logic.physical.event.Event
import com.akuwalink.kohane.logic.physical.basic.BasicModel
import com.akuwalink.kohane.logic.physical.basic.CollisionModels
import com.akuwalink.kohane.logic.physical.basic.Vec3
import com.akuwalink.kohane.util.TextureUtil


open class Model(point:FloatArray, vein:FloatArray?,normal:FloatArray?,context: Context){
    /**
     * 自上往下为：自身变换矩阵，context，质量，光线粗糙度，是否可损坏物，是否可移动，碰撞模型<br/>
     *  速度，粗糙度，纹理id，碰撞模式，碰撞测试模型
     *
     */
    var martix_self:FloatArray= FloatArray(16)
    var context=context
    var quality:Float=0F
    var rough:Float=50F
    var frangible=false
    var move_flag=false
    lateinit var collision_model:BasicModel
    var speed=Vec3()
    var rub=0.003f
    var texId=0

    var collision_mode=CollisionModels.COLLISION_MODE_BOX
    var collision_model_temp: BasicModel= BasicModel()

    /**
     * 初始化模型
     */
    fun modelInit(quality:Float,rough:Float,frangible:Boolean,move_flag:Boolean,collision_model:BasicModel){
        Matrix.setRotateM(martix_self,0,0F,1F,0F,0F)
        this.frangible=frangible
        this.quality=quality
        this.move_flag=move_flag
        this.rough=rough
        this.collision_model=collision_model

    }

    /**
     * 给子类进行绘制自身
     */
    open fun drawself(light:Light,matrix:com.akuwalink.kohane.util.Matrix){

    }

    /**
     * 设置纹理id
     */
    open fun setTex(resId:Int){
        texId= TextureUtil.getTextureId(resId,context)
    }

    /**
     * 绘制阴影
     */
    open fun drawShadow(light:Light,matrix:com.akuwalink.kohane.util.Matrix){}

    /**
     * 碰撞事件
     */
    open fun collsionEvent(event: Event){}

    /**
     * 平行碰撞检测
     */
    fun moveTemp(x: Float,y: Float,z: Float){
        copyCollisionModel()
        collision_model_temp.center.x+=x
        collision_model_temp.center.y+=y
        collision_model_temp.center.z+=z
    }

    /**
     * 复制碰撞模型到碰撞测试模型
     */
    private fun copyCollisionModel(){
        collision_model_temp.center=Vec3(collision_model.center.x,collision_model.center.y,collision_model.center.z)
        collision_model_temp.height=collision_model.height
        collision_model_temp.width=collision_model.width
        collision_model_temp.long=collision_model.long
        collision_model_temp.r=collision_model.r

    }

    /**
     * 物体平移
     */
    fun translate(x:Float,y:Float,z:Float){
        martix_self[12]+=x
        martix_self[13]+=y
        martix_self[14]+=z
        if(collision_mode==CollisionModels.COLLISION_MODE_BOX){
            collision_model.center.x+=x
            collision_model.center.y+=y
            collision_model.center.z+=z
        }
    }

    /**
     * @param angle 角度
     * @param x x轴分量
     * @param y y轴分量
     * @param z z轴分量
     * 物体旋转
     */
    open fun rotate(angle:Float,x:Float,y:Float,z:Float){
        Matrix.rotateM(martix_self,0,angle,x,y,z)
        if(collision_mode==CollisionModels.COLLISION_MODE_BOX) {
            var temp=collision_model.width
            collision_model.width=collision_model.long
            collision_model.long=temp
        }
    }

    /**
     * 物体缩放
     */
    fun scale(x:Float,y:Float,z:Float){
        Matrix.scaleM(martix_self,0,x,y,z)
        if(collision_mode==CollisionModels.COLLISION_MODE_BOX) {
            if(x!=0F) collision_model.long*=x

            if(y!=0f) collision_model.width*=y

            if(z!=0f) collision_model.height*=z
        }
    }

}