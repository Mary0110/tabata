package com.example.tabata.viewModel

import androidx.annotation.LayoutRes
import androidx.databinding.BaseObservable
import com.example.tabata.R
import com.example.tabata.viewModel.EditViewModel

interface ItemViewModel {
    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = 0
}

class PhaseViewModel(val title: String = "default title"/*, val duration: Int*/) : BaseObservable(), ItemViewModel {
    override val layoutId: Int = R.layout.phase_row
    override val viewType: Int = EditViewModel.PHASE_ITEM

}