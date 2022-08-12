package com.thuanpx.mvvm_architecture.feature.home.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.thuanpx.mvvm_architecture.R
import com.thuanpx.mvvm_architecture.base.recyclerview.BaseRecyclerViewAdapter
import com.thuanpx.mvvm_architecture.databinding.ItemBottomSheetBinding
import com.thuanpx.mvvm_architecture.feature.home.bottomsheet.CategoryAdapter.CategoryViewHolder
import com.thuanpx.mvvm_architecture.model.entity.ItemCatergory


class CategoryAdapter(
    private val listener: OnItemClickedListener
) : BaseRecyclerViewAdapter<ItemCatergory, CategoryViewHolder>() {
    private var selectedPosition: Int = 0

    inner class CategoryViewHolder(
        private val itemBottomSheetBinding: ItemBottomSheetBinding,
        private val listener: OnItemClickedListener
    ) :
        RecyclerView.ViewHolder(itemBottomSheetBinding.root) {
        fun bindItem(category: ItemCatergory, position: Int) {
            with(itemBottomSheetBinding) {
                ivImage.setImageResource(category.url)
                tvTitle.text = category.name
                tvTitle.setTextColor(
                    ContextCompat
                        .getColor(tvTitle.context, R.color.C_BF000000)
                )
                if (category.isSelected) {
                    tvTitle
                        .setTextColor(
                            ContextCompat
                                .getColor(tvTitle.context, R.color.blue_700)
                        )
                }
            }
            itemView.setOnClickListener {
                listener.onItemClicked(category, position, selectedPosition)
                selectedPosition = position
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        return CategoryViewHolder(
            ItemBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getData()[position]
        holder.itemView.isSelected = selectedPosition == position
        holder.bindItem(category, position)
    }

    override fun getItemCount(): Int {
        return getData().size
    }

    interface OnItemClickedListener {
        fun onItemClicked(category: ItemCatergory, newPosition: Int, oldPosition: Int)
    }
}
