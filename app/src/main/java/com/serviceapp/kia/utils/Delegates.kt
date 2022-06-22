package com.serviceapp.kia.utils

import kotlinx.coroutines.*


/*
 *Created by Adithya T Raj on 24-06-2021
*/

fun<T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}