package com.example.corki.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.corki.R

class ItemAdapter(private val itemList: List<ItemsViewModel>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemsViewModel = itemList[position]
        holder.title.text = itemsViewModel.title

        holder.detail.setOnClickListener {
            val bundle = bundleOf("title" to holder.title.text, "title1" to "title1")
            holder.itemView.findNavController()
                .navigate(R.id.action_navigation_search_to_fragment_details, bundle)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_card)
        val detail: Button = view.findViewById(R.id.details_button)
    }
}