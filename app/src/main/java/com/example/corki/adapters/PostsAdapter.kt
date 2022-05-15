package com.example.corki.adapters

import android.annotation.SuppressLint
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
import java.lang.NullPointerException
import java.text.DateFormat
import java.text.SimpleDateFormat

class PostsAdapter(
    private val posts: List<Post>
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val postsView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)

        return PostsViewHolder(postsView)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val postsViewModel = posts[position]
        with(holder) {
            title.text = postsViewModel.title
            subject.text = postsViewModel.subjects[0].replaceFirst(
                oldChar = postsViewModel.subjects[0][0],
                newChar = postsViewModel.subjects[0][0].uppercase().toCharArray()[0]
            )

            try {
                val format = "yyyy-MM-dd'T'kk:mm:ss.sss'Z'"
                val dateFrom = SimpleDateFormat(format).parse(postsViewModel.dateFrom)?.let {
                    DateFormat.getDateTimeInstance().format(it)
                }

                val dateTo = SimpleDateFormat(format).parse(postsViewModel.dateTo)?.time
                val dateFrom1 = SimpleDateFormat(format).parse(postsViewModel.dateFrom)?.time
                val duration = dateFrom1?.let { dateTo?.minus(it) }

                if (duration != null) date.text = "$dateFrom - ${duration / 60000} min"
            } catch (ex: NullPointerException) {
                date.text = holder.itemView.context.getString(R.string.invalid_time)
            }

            city.text = postsViewModel.cities[0]
            price.text = "${postsViewModel.price}zł"
        }

        holder.detail.setOnClickListener {
            val bundle = bundleOf("id" to postsViewModel.id)
            holder.itemView.findNavController()
                .navigate(R.id.action_navigation_search_to_fragment_details, bundle)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_card)
        val subject: TextView = view.findViewById(R.id.subject_card)
        val date: TextView = view.findViewById(R.id.date_card)
        val city: TextView = view.findViewById(R.id.city_card)
        val price: TextView = view.findViewById(R.id.price_card)
        val detail: Button = view.findViewById(R.id.details_button)
    }
}