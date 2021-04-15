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
import com.example.radiusagent.pojo.DisplayItem
import com.example.radiusagent.pojo.Exclusion
import com.example.radiusagent.pojo.Options

class DisplayListAdapter( private val options: ArrayList<DisplayItem>,
private val listerner: OnItemClickListener,private val exclusionList:ArrayList<ArrayList<Exclusion>>
) :
ListAdapter<DisplayItem, DisplayListAdapter.ProductViewHolder>(DisplayListCompartor()) {
    val set= HashSet<DisplayItem>(options)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            LayoutProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = options[position]
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }
    fun parseExclusionList()
    {
       for(list in exclusionList)
       {
          val facility= list.get(0).facility_id
           val optionId= list.get(0).options_id
           val facility2= list.get(1).facility_id
           val optionId2= list.get(1).options_id

       }
        for (index in 0..options.size-1)
        {

            if(index<exclusionList.size-1)
            {
                val facility= exclusionList.get(0).get(0).facility_id
                val optionId= exclusionList.get(0).get(0).options_id
                val facility2= exclusionList.get(0).get(1).facility_id
                val optionId2= exclusionList.get(0).get(1).options_id



            }
        }

    }



    inner class ProductViewHolder(private val binding: LayoutProductsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var option: DisplayItem?=null
        init {
            binding.addItem.setOnClickListener(this)
            binding.deleteItem.setOnClickListener(this)
        }

        fun bind(option: DisplayItem) {

            this.option=option
            binding.apply {
                Glide.with(itemView)
                    .load(R.drawable.apartment)
                    .into(productImage)

                productName.text = option.name
                productId.text=option.options_id



            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                when (v?.id) {
                    binding.deleteItem.id ->

                        listerner.onItemDelete(option!!, id = "")
                    binding.addItem.id ->
                        listerner.onItemAdd(option!!, id = "")


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
