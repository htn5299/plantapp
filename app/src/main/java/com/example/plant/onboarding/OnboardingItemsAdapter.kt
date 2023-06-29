package com.example.plant.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.R

class OnboardingItemsAdapter(private val onboardingItems: List<OnboardingItem>) : RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingItemViewHolder>() {
    inner class  OnboardingItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val imageOnboarding = view.findViewById<ImageView>(R.id.imgOnboarding)
        private val txtTitle = view.findViewById<TextView>(R.id.textTitle)
        private val content = view.findViewById<TextView>(R.id.textContent)

        fun bind(onboardingItem: OnboardingItem){
            imageOnboarding.setImageResource((onboardingItem.onboardingImage))
            txtTitle.text = onboardingItem.title
            content.text = onboardingItem.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }
}