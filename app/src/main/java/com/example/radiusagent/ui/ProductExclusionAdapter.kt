package com.example.radiusagent.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.radiusagent.R
import com.example.radiusagent.databinding.LayoutProductsBinding
import com.example.radiusagent.pojo.DisplayExclusionList
import com.example.radiusagent.pojo.DisplayItem
import com.google.android.material.snackbar.Snackbar

class ProductExclusionAdapter(
    private val options: ArrayList<DisplayItem>,
    private val listerner: OnItemClickListener,
    private val exclusionList: ArrayList<DisplayExclusionList>
) :
    ListAdapter<DisplayItem, ProductExclusionAdapter.ProductViewHolder>(DisplayListCompartor()) {
    val set = HashSet<DisplayItem>(options)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            LayoutProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = options[position]
        val exclusionList = exclusionList[position]
        if (currentItem != null) {
            holder.bind(currentItem, exclusionList)
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }


    inner class ProductViewHolder(private val binding: LayoutProductsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var option: DisplayItem? = null
        private var exclusionList: DisplayExclusionList? = null

        init {
            binding.addItem.setOnClickListener(this)
            binding.deleteItem.setOnClickListener(this)
        }

        fun bind(
            option: DisplayItem,
            exclusionList: DisplayExclusionList
        ) {
            this.option = option
            this.exclusionList = exclusionList
            binding.apply {
                Glide.with(itemView)
                    .load(R.drawable.apartment)
                    .into(productImage)

                productName.text = option.name
                productId.text = option.options_id
            }
            //disable the list



        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                when (v?.id) {
                    binding.deleteItem.id ->

                        listerner.onItemDelete(option!!, id = "")
                    binding.addItem.id -> {
                        if (exclusionList!!.status) {
                            Snackbar.make(binding.root, "Exclusion item .Cannot be Added", Snackbar.LENGTH_LONG)
                                .show()
                            binding.addItem.isEnabled = false
                            binding.addItem.isClickable = false
                        } else {
                            listerner.onItemAdd(option!!, id = "")
                        }


                    }


                }
            }


        }

    }

    interface OnItemClickListener {
        fun onItemAdd(photo: DisplayItem, id: String)
        fun onItemDelete(photo: DisplayItem, id: String)

    }

    class DisplayListCompartor : DiffUtil.ItemCallback<DisplayItem>() {
        override fun areItemsTheSame(oldItem: DisplayItem, newItem: DisplayItem): Boolean {
            return oldItem.facility_id == newItem.facility_id
        }

        override fun areContentsTheSame(oldItem: DisplayItem, newItem: DisplayItem) =
            oldItem == newItem
    }


}