package com.example.databasedemo

import java.lang.StringBuilder
import java.time.DayOfWeek
import kotlin.reflect.KProperty

//泛型类写法
class MyClass<T>{
    fun abc(param: T):T{
        return param
    }
}

class TestClass(){
    //委托属性
    var p by Delete()
    val abc by lazy {  }

    //泛型方法写法
    fun<T: Any>abc(param: T):T{
        return param
    }
    fun<T: Number>abc(param: T):T{
        return param
    }
}

class Delete {
   private var propValue:Any?=null
    operator fun getValue(testClass: TestClass, property: KProperty<*>): Any? {
        return propValue
    }

    operator fun setValue(testClass: TestClass, property: KProperty<*>, value: Any?) {
        propValue = value
    }
}

fun <T> T.build(block:T.() -> Unit):T{
    block()
    return this
}

//by委托让别人实现
class MySet<T>(private val helperSet: HashSet<T>):Set<T> by helperSet{
    private val a =100
    fun abc(){}
    override fun isEmpty(): Boolean {
        return false
    }
}

fun main(){
    val stringBuilder=StringBuilder()
    stringBuilder.build{
        append(123)
        append(456)
        append(789)
    }
    val myClass1=MyClass<String>()
    println(myClass1.abc("你好,泛型"))
    val myClass2=MyClass<String>()
    println(myClass2.abc("123"))

    val testClass=TestClass()
    println(testClass.abc("你好,泛型"))
    println(testClass.abc(123))
    println(testClass.abc(666))
    testClass.build{
        println(testClass.abc("你好,泛型"))
        println(testClass.abc(123))
        println(testClass.abc(666))
    }
}