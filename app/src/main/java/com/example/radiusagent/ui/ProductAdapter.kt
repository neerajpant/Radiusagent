package com.example.radiusagent.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.radiusagent.R
import com.example.radiusagent.databinding.LayoutProductItemBinding
import com.example.radiusagent.databinding.LayoutProductsBinding
import com.example.radiusagent.pojo.Options
import com.google.android.material.snackbar.Snackbar


class ProductAdapter(
    private val options: ArrayList<Options>,
    private val listerner: OnItemClickListener
) :
    ListAdapter<Options, ProductAdapter.ProductViewHolder>(ProductComparator()) {
    private var selectedItems= arrayListOf<Options>()

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

    fun addUsers(options: List<Options>) {
        this.options.apply {
            clear()
            addAll(options)
        }

    }

    inner class ProductViewHolder(private val binding: LayoutProductsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var option: Options? = null

        init {
            binding.addItem.setOnClickListener(this)
            binding.deleteItem.setOnClickListener(this)
        }

        fun bind(option: Options) {
            this.option = option
            binding.apply {
                Glide.with(itemView)
                    .load(R.drawable.apartment)
                    .into(productImage)

                productName.text = option.name

                productId.text = option.id

            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                when (v?.id) {
                    binding.deleteItem.id -> {
                        if(option!=null)
                         {
                             option=null
                             selectedItems.clear()
                             Snackbar.make(v,"Item is Deleted",Snackbar.LENGTH_LONG).show()
                            // listerner.onItemDelete(option!!, id = "")
                         }

                    }

                    binding.addItem.id -> {
                         if(selectedItems.size>=1)
                         {
                             binding.addItem.isClickable=false
                             binding.addItem.isEnabled=false
                             Snackbar.make(v,"You can Select only one item",Snackbar.LENGTH_LONG).show()
                             return
                         }
                        else if(selectedItems.size==0)
                         {

                             Snackbar.make(v,"Item is Added",Snackbar.LENGTH_LONG).show()
                             listerner.onItemAdd(option!!, id = "")
                             selectedItems.add(option!!)
                             binding.addItem.isClickable=false
                             binding.addItem.isEnabled=false
                         }



                    }


                }
            }


        }

    }

    interface OnItemClickListener {
        fun onItemAdd(photo: Options, id: String)
        fun onItemDelete(photo: Options, id: String)

    }

    class ProductComparator : DiffUtil.ItemCallback<Options>() {
        override fun areItemsTheSame(oldItem: Options, newItem: Options): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Options, newItem: Options) =
            oldItem == newItem
    }


}