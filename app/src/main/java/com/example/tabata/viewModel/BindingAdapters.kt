package com.example.tabata.viewModel
import androidx.databinding.BindingAdapter

import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("itemViewModels")
fun bindItemViewModels(recyclerView: RecyclerView, itemViewModels: List<ItemViewModel>?) {
    val adapter = getOrCreateAdapter(recyclerView)
    adapter.updateItems(itemViewModels)
}

@BindingAdapter("EditViewModel")
fun bindEditViewModels(recyclerView: RecyclerView, editViewModel: EditViewModel) {
    val ad = getOrCreateAdapter(recyclerView)
    ad.updateEditViewModel(editViewModel)
}

private fun getOrCreateAdapter(recyclerView: RecyclerView): BindableRecyclerViewAdapter {
    return if (recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter) {
        recyclerView.adapter as BindableRecyclerViewAdapter
    } else {
        val bindableRecyclerAdapter = BindableRecyclerViewAdapter()
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}