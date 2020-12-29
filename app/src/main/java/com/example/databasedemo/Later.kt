package com.example.databasedemo

import kotlin.reflect.KProperty

class Later<T>(val block:() -> T) {
    var value:Any? =null
    operator fun getValue(any: Any?,property:KProperty<*>):T{
        if (value===null){
            value =block()
        }
        return value as T
    }
}
fun <T>later(block: () -> T)= Later(block)