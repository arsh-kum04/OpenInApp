package com.example.openinapp.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openinapp.data.RecentLink
import com.example.openinapp.databinding.RecentLinkItemsBinding

class RecentLinksAdapter(private val context: Context,private val list:List<RecentLink>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        inner class MyViewHolder(binding:RecentLinkItemsBinding): RecyclerView.ViewHolder(binding.root)
        {
            val maintv=binding.mainTv
            val clicknum=binding.clickNum
            val datestv=binding.datesTv
            val linktv=binding.tvLink
            val item_iv=binding.itemIv

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

                Glide.with(holder.itemView.context)
                    .load(model.original_image)
                    .into(holder.item_iv)

                holder.linktv.setOnClickListener {
                    copyLinkToClipboard(model.web_link)
                }
            }
        }
    private fun copyLinkToClipboard(link: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("Smart Link", link)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }


    }
