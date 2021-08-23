package com.akuwalink.kohane.logic.physical.event


import com.akuwalink.kohane.logic.model.Model
import com.akuwalink.kohane.logic.model.World

class Event{

    var model: Model?=null
    var collision_flag=false
    var collision_dire:Int=0
    var world:World?=null
}