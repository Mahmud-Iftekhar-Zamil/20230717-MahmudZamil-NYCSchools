package com.example.nycschools.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nycschools.databinding.ItemSchoolBinding
import com.example.nycschools.model.data.SchoolDetailData

const val TAG = "SchoolsAdapter"

class SchoolsAdapter(private val schoolClickListener: OnSchoolClickListener): ListAdapter<SchoolDetailData, SchoolsAdapter.SchoolsViewHolder>(DiffCallback()) {

    interface OnSchoolClickListener {
        fun onSchoolSelected(schoolDetailData: SchoolDetailData)
    }

    inner class SchoolsViewHolder(private val binding: ItemSchoolBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val schoolDetailData = getItem(position)
                        schoolClickListener.onSchoolSelected(schoolDetailData)
                    }
                }
            }
        }
        fun bind(schoolDetailData: SchoolDetailData) {
            binding.apply {
                schoolName.text = schoolDetailData.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolsViewHolder {
        val binding = ItemSchoolBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return SchoolsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SchoolsViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem)
    }

    class DiffCallback: DiffUtil.ItemCallback<SchoolDetailData>() {
        override fun areItemsTheSame(
            oldItem: SchoolDetailData,
            newItem: SchoolDetailData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SchoolDetailData,
            newItem: SchoolDetailData
        ): Boolean {
            return oldItem == newItem
        }

    }
}