package com.example.corki.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corki.R
import com.example.corki.databinding.CardItemBinding

class ItemAdapter(private val itemList: List<ItemsViewModel>,
                  private val onDataClickListener: OnDataClickListener
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface OnDataClickListener {
        fun onDataClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)

        return ItemViewHolder(itemView, onDataClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemsViewModel = itemList[position]
        holder.title.text = itemsViewModel.title
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(view: View, private val listener: OnDataClickListener
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val title : TextView = view.findViewById(R.id.title_card)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onDataClick(absoluteAdapterPosition)
        }
    }
}