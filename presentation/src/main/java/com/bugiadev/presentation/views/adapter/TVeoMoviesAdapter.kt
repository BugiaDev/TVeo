package com.bugiadev.presentation.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugiadev.presentation.R
import com.bugiadev.presentation.databinding.MovieItemViewBinding
import com.bugiadev.presentation.display.MovieDisplay
import com.bugiadev.presentation.utils.loadFromUrl
import okhttp3.internal.toImmutableList

typealias TVeoMoviesCallback = (MovieDisplay) -> Unit

class TVeoMoviesAdapter(
    private val onItemClick: TVeoMoviesCallback? = null,
    private val onFavClick: TVeoMoviesCallback? = null,
    private var items: List<MovieDisplay>
) : RecyclerView.Adapter<TVeoMoviesAdapter.MovieItemViewHolder>() {

    class MovieItemViewHolder(
        private val binding: MovieItemViewBinding,
        private val onClickListener: TVeoMoviesCallback? = null,
        private val onFavListener: TVeoMoviesCallback? = null
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieDisplay) {
            binding.apply {
                itemCell.setOnClickListener {
                    onClickListener?.let {
                        it(data)
                    }
                }

                val imageResource = if (data.isFav == true)
                    R.drawable.ic_baseline_star_24
                else R.drawable.ic_baseline_star_border_24

                favImage.setImageResource(imageResource)
                favImage.setOnClickListener {
                    onFavListener?.let {
                        it(data)
                    }
                }

                movieTitleTextView.text = data.name
                movieImage.loadFromUrl(data.imageURI.orEmpty(), movieImage.context)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMovies(newMovies: List<MovieDisplay>) {
        val newList = items.toMutableList()
        newList.addAll(newMovies)
        items = newList.toImmutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun favAndReload(favItemId: String, isFav: Boolean) {
        items.single { display -> display.id == favItemId }.isFav = isFav
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder =
        MovieItemViewHolder(
            MovieItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick, onFavClick
        )

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}