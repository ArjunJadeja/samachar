package com.arjun.samachar.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.databinding.ItemHeadlineBinding
import com.arjun.samachar.utils.loadImage

typealias HeadlineHandler = (Headline) -> Unit

class HeadlinesAdapter(
    private val headlineList: ArrayList<Headline>,
    private var onHeadlineClicked: HeadlineHandler
) : RecyclerView.Adapter<HeadlinesAdapter.HeadlinesViewHolder>() {

    inner class HeadlinesViewHolder(var binding: ItemHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeadlinesViewHolder(
            ItemHeadlineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HeadlinesViewHolder, position: Int) {
        val article = headlineList[position]
        holder.apply {
            binding.apply {
                authorTextView.text = article.author
                titleTextView.text = article.title
                publishedAtTextView.text = article.publishedAt
                bannerImageView.loadImage(article.imageUrl.toString())
            }
            itemView.setOnClickListener {
                onHeadlineClicked(article)
            }
        }
    }

    override fun getItemCount(): Int = headlineList.size

    fun updateData(newHeadlines: List<Headline>) {
        headlineList.clear()
        headlineList.addAll(newHeadlines)
        notifyDataSetChanged()
    }

    fun setHeadlineHandler(handler: HeadlineHandler) {
        onHeadlineClicked = handler
    }

}