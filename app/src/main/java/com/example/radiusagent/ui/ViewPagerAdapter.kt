package com.example.radiusagent.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.radiusagent.R
import com.example.radiusagent.databinding.LayoutProductItemBinding
import com.example.radiusagent.pojo.Facilty
import com.example.radiusagent.pojo.Options
import java.util.Collections.addAll


class ViewPagerAdapter(val facilty: ArrayList<Facilty>, private val listener: OnItemClickListener) :
    ListAdapter<Facilty, ViewPagerAdapter.FaciltyViewHolder>(FaciltyComparator()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FaciltyViewHolder {
        val binding =
            LayoutProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaciltyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaciltyViewHolder, position: Int) {
        val currentItem = facilty[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }


    }

    fun addOption(users: List<Facilty>) {

        this.facilty.apply {
            clear()
            addAll(users)
        }

    }

    override fun getItemCount(): Int {
        //  Log.d("ViewPagerAdapter","getItemCount"+facilty.get(1).name)
        return facilty.size
    }


    inner class FaciltyViewHolder(private val binding: LayoutProductItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var facilty: Facilty? = null

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(facilty: Facilty) {
            this.facilty = facilty
            Log.d("ViewPagerAdapter", "text" + facilty.name)
            when (facilty.name) {
                "Property Type" -> {
                    binding.apply {
                        Glide.with(itemView)
                            .load(R.drawable.apartment)
                            .into(faciltyImage)

                        faciltyName.text = facilty.name
                    }

                }
                "Number of Rooms" -> {
                    binding.apply {
                        Glide.with(itemView)
                            .load(R.drawable.rooms)
                            .into(faciltyImage)

                        faciltyName.text = facilty.name
                    }
                }
                "Other facilities" -> {
                    binding.apply {
                        Glide.with(itemView)
                            .load(R.drawable.swimming)
                            .into(faciltyImage)

                        faciltyName.text = facilty.name
                    }
                }

            }

            binding.apply {
                Glide.with(itemView)
                    .load(R.drawable.apartment)
                    .into(faciltyImage)

                faciltyName.text = facilty.name

            }


        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(facilty?.options!!.toMutableList(), facilty!!.facility_id)

            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(photo: List<Options>, id: String)
    }

    class FaciltyComparator : DiffUtil.ItemCallback<Facilty>() {
        override fun areItemsTheSame(oldItem: Facilty, newItem: Facilty): Boolean {
            return true
            // return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Facilty, newItem: Facilty) =
            false;
        //  oldItem == newItem
    }


}