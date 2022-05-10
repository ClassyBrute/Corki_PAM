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
import com.example.corki.models.post.Post
import com.example.corki.models.post.Posts

class PostsAdapter(private val posts: List<Post>
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val postsView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)

        return PostsViewHolder(postsView)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val postsViewModel = posts[position]
        holder.title.text = postsViewModel.title

//        holder.detail.setOnClickListener {
//            val bundle = bundleOf("title" to holder.title.text, "title1" to "title1")
//            holder.itemView.findNavController()
//                .navigate(R.id.action_navigation_search_to_fragment_details, bundle)
//        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_card)
        val detail: Button = view.findViewById(R.id.details_button)
    }
}