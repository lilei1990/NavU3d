package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.lei.core.base.BaseViewModel


class ShareVM<T> : BaseViewModel() {
    private val data = MutableLiveData<T>()

    fun postValue(value: T) {
        data.postValue(value)
    }
}