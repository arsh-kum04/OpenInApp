package com.example.openinapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp.data.model.RecentLink
import com.example.openinapp.databinding.RecentLinkItemsBinding

class RecentLinksAdapter(private val list:List<RecentLink>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        inner class MyViewHolder(binding:RecentLinkItemsBinding): RecyclerView.ViewHolder(binding.root)
        {
            val maintv=binding.mainTv
            val clicknum=binding.clickNum
            val datestv=binding.datesTv
            val linktv=binding.tvLink


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(RecentLinkItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

        override fun getItemCount(): Int {
            return list.size

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val model=list[position]
            if(holder is MyViewHolder)
            {
                holder.maintv.text=model.title
                holder.clicknum.text=model.total_clicks.toString()
                holder.datestv.text=model.times_ago
                holder.linktv.text=model.web_link
            }
        }


    }