package com.arjun.samachar.ui.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arjun.samachar.R
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.databinding.ItemLanguageBinding

typealias LanguageHandler = (Language) -> Unit

class LanguagesAdapter(
    private var selectedLanguageCode: String,
    private val languages: ArrayList<Language>,
    private var onLanguageSelected: LanguageHandler
) : RecyclerView.Adapter<LanguagesAdapter.LanguageViewHolder>() {

    private var selectedItem = -1

    inner class LanguageViewHolder(var binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]

        holder.binding.apply {
            languageTextView.text = language.nativeName
            languageCard.setOnClickListener {
                val currentPosition = holder.adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION && currentPosition != selectedItem) {
                    selectedItem = currentPosition
                    notifyDataSetChanged()
                    onLanguageSelected(language)
                }
            }
            if (holder.adapterPosition == selectedItem) {
                languageCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.black)
                )
                languageTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.white)
                )
            } else {
                languageCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.white)
                )
                languageTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.black)
                )
            }
        }
    }

    override fun getItemCount() = languages.size

    fun updateData(list: List<Language>) {
        languages.addAll(list)
        selectedItem = languages.indexOfFirst { it.code == selectedLanguageCode }
        notifyDataSetChanged()
    }

    fun setSelectedLanguage(languageCode: String) {
        selectedLanguageCode = languageCode
    }

    fun setLanguageHandler(handler: LanguageHandler) {
        onLanguageSelected = handler
    }

}