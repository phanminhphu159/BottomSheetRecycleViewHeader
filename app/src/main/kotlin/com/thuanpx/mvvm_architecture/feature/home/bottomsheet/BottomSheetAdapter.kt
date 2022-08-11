package com.thuanpx.mvvm_architecture.feature.home.bottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thuanpx.ktext.recyclerView.initRecyclerViewAdapter
import com.thuanpx.mvvm_architecture.R
import com.thuanpx.mvvm_architecture.databinding.ItemRvBottomSheetBinding
import com.thuanpx.mvvm_architecture.feature.home.bottomsheet.BottomSheetAdapter.BottomSheetViewHolder
import com.thuanpx.mvvm_architecture.model.entity.Item
import com.thuanpx.mvvm_architecture.model.entity.ItemDiffCallback


class BottomSheetAdapter : PagingDataAdapter<Item, BottomSheetViewHolder>(ItemDiffCallback) {

    class BottomSheetViewHolder(
        private val viewBinding: ItemRvBottomSheetBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun onBindData(item: Item?) {
            with(viewBinding) {
                val listImage = ArrayList<Int>()
                for (i in 0 until 6) {
                    listImage.add(R.drawable.img_user)
                    if ( i == 1) tvBusinessName.text
                }
                tvBusinessName.text = item?.name
                initImageRecyclerView(listImage, viewBinding)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun initImageRecyclerView(
            intList: ArrayList<Int>,
            viewBinding: ItemRvBottomSheetBinding
        ) {
//            val disabler: RecyclerView.OnItemTouchListener = RecyclerViewDisabler()
            val bottomSheetItemImageAdapter = BottomSheetItemImageAdapter(intList)
            with(viewBinding.rvBottomSheetContentImage) {
                initRecyclerViewAdapter(
                    bottomSheetItemImageAdapter,
                    LinearLayoutManager.HORIZONTAL,
                    true
                )
//                addOnItemTouchListener(disabler) // disables scrolling
                setOnTouchListener(View.OnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN ->
                            v.parent.requestDisallowInterceptTouchEvent(true)
                        MotionEvent.ACTION_UP ->
                            v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    v.onTouchEvent(event)
                    true
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            ItemRvBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.onBindData(getItem(position))
    }
}
