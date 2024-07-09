package com.arjun.samachar.ui.source

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arjun.samachar.data.model.Source
import com.arjun.samachar.databinding.ItemSourceBinding

typealias SourceHandler = (Source) -> Unit

class SourcesAdapter(
    private val sources: ArrayList<Source>,
    private var onSourceSelected: SourceHandler
) : RecyclerView.Adapter<SourcesAdapter.SourceViewHolder>() {

    inner class SourceViewHolder(var binding: ItemSourceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(
            ItemSourceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val source = sources[position]
        holder.apply {
            binding.sourceNameTextView.text = source.name
            itemView.setOnClickListener {
                onSourceSelected(source)
            }
        }
    }

    override fun getItemCount(): Int = sources.size

    fun updateData(list: List<Source>) {
        sources.clear()
        sources.add(Source())
        sources.addAll(list)
        notifyDataSetChanged()
    }

    fun setSourceHandler(handler: SourceHandler) {
        this.onSourceSelected = handler
    }

}