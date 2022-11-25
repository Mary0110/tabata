package com.example.tabata.View.adapters

import android.util.TypedValue
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.viewModel.ItemViewModel
import com.example.tabata.viewModel.EditViewModel


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

@BindingAdapter("android:textSize")
fun bindTextSize(textView: TextView, size: String) {
    if(size == "1")
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textView.textSize *0.5).toFloat())
    else if(size == "2")
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textView.textSize).toFloat())
    else if(size == "3")
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,(textView.textSize *1.2).toFloat())
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