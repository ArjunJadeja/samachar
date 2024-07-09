package com.arjun.samachar.ui.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arjun.samachar.data.model.Country
import com.arjun.samachar.databinding.ItemCountryBinding

typealias CountryHandler = (Country) -> Unit

class CountriesAdapter(
    private val countries: ArrayList<Country>,
    private var onCountrySelected: CountryHandler
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(var binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.apply {
            binding.apply {
                countryFlagTextView.text = country.flag
                countryNameTextView.text = country.name
            }
            itemView.setOnClickListener {
                onCountrySelected(country)
            }
        }
    }

    override fun getItemCount(): Int = countries.size

    fun updateData(list: List<Country>) {
        countries.clear()
        countries.addAll(list)
        notifyDataSetChanged()
    }

    fun setCountryHandler(handler: CountryHandler) {
        this.onCountrySelected = handler
    }

}