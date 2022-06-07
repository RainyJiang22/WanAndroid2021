package com.base.mvi_core

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author jiangshiyu
 * @date 2022/6/7
 *
 * ListEvent事件
 * 负责处理多维度一次性event,支持多个监听者
 * 打个比方，就是我们请求的时候发起loading事件，然后在请求完成后需要dismissLoading与toast事件
 * 如果我们在请求开始后返回桌面，但是成功后返回到app，这样就会有一个事件会被覆盖，所以这里通过list来存储
 */
class LiveEvents<T> : MutableLiveData<List<T>>() {

    private val observers = hashSetOf<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in List<T>>) {
        // existing
        observers.find { it.observer === observer }?.let { _ ->
            return
        }
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, observer)
    }


    @MainThread
    override fun observeForever(observer: Observer<in List<T>>) {
        // existing
        observers.find { it.observer === observer }?.let { _ ->
            return
        }
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(observer)

    }

    @MainThread
    override fun removeObserver(observer: Observer<in List<T>>) {
        if (observer is ObserverWrapper<*> && observers.remove(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }



    private class ObserverWrapper<T>(val observer: Observer<in List<T>>) : Observer<List<T>> {
        private val pending = AtomicBoolean(false)
        private val eventList = mutableListOf<List<T>>()

        override fun onChanged(t: List<T>?) {

            if (pending.compareAndSet(true, false)) {
                observer.onChanged(eventList.flatten())
                eventList.clear()
            }
        }

        fun newValue(t: List<T>?) {
            pending.set(true)
            t?.let {
                eventList.add(it)
            }
        }

    }

}