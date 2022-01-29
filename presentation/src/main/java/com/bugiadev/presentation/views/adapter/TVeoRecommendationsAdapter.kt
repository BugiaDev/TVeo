package com.bugiadev.presentation.views.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugiadev.presentation.databinding.MovieItemViewBinding
import com.bugiadev.presentation.display.MovieDisplay
import com.bugiadev.presentation.utils.loadFromUrl

class TVeoRecommendationsAdapter(
    private val onItemClick: TVeoMoviesCallback? = null,
    private var items: List<MovieDisplay>
) : RecyclerView.Adapter<TVeoRecommendationsAdapter.RecommendationItemViewHolder>() {
    class RecommendationItemViewHolder(
        private val binding: MovieItemViewBinding,
        private val onClickListener: TVeoMoviesCallback? = null
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieDisplay) {
            binding.apply {
                itemCell.setOnClickListener {
                    onClickListener?.let {
                        it(data)
                    }
                }

                favImage.visibility = GONE
                movieTitleTextView.text = data.name
                movieImage.loadFromUrl(data.imageURI.orEmpty(), movieImage.context)
            }
        }
    }

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationItemViewHolder =
        RecommendationItemViewHolder(
            MovieItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    override fun onBindViewHolder(holder: RecommendationItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}