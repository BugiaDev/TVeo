package com.bugiadev.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bugiadev.presentation.TVeoComponent
import com.bugiadev.presentation.TVeoInjection
import com.bugiadev.presentation.databinding.FragmentListBinding
import com.bugiadev.presentation.utils.PresentationConstants.DEFAULT_VALUE
import com.bugiadev.presentation.utils.PresentationConstants.FAVORITES_SET_NAME
import com.bugiadev.presentation.utils.PresentationConstants.MOVIES_NUMBER
import com.bugiadev.presentation.utils.PresentationConstants.SCROLL_DIRECTION_BOTTOM
import com.bugiadev.presentation.utils.viewModel
import com.bugiadev.presentation.viewmodel.TVeoListViewModel
import com.bugiadev.presentation.views.adapter.TVeoMoviesAdapter

class ListFragment : BaseFragment<FragmentListBinding>() {
    private lateinit var tVeoComponent: TVeoComponent
    private val viewModel: TVeoListViewModel by viewModel { tVeoComponent.tVeoListViewModel }

    private lateinit var adapter: TVeoMoviesAdapter
    private var moviesNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
        moviesNumber = activity?.intent?.getIntExtra(MOVIES_NUMBER, DEFAULT_VALUE) ?: DEFAULT_VALUE
        sharedPrefs.getStringSet(FAVORITES_SET_NAME, emptySet())?.let {
            viewModel.setFavorites(it.toMutableSet())
        }
        viewModel.start(moviesNumber)
    }

    private fun initObservers() {
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            showLoader(isLoading)
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            showErrorState(error)
        })

        viewModel.movies.observe(viewLifecycleOwner, { movies ->
            if (this::adapter.isInitialized) {
                if (movies.isNotEmpty()) {
                    adapter.addMovies(movies)
                    if (binding.movieList.adapter == null) setAdapter()
                }
            } else {
                adapter = TVeoMoviesAdapter(
                    onItemClick = { display ->
                        display.id?.let {
                            viewModel.onMovieSelected(it)
                        }
                    },
                    onFavClick = {
                        markAsFav(it.id.orEmpty())
                    },
                    items = movies
                )

                setAdapter()
            }
        })

        viewModel.selectedMovie.observe(viewLifecycleOwner, { id ->
            findNavController().navigate(
                ListFragmentDirections.actionListToDetail(id)
            )
        })
    }

    private fun initListeners() {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_DIRECTION_BOTTOM)) {
                    viewModel.downloadMoreMovies()
                }
            }
        }

        binding.movieList.addOnScrollListener(scrollListener)
    }

    private fun setAdapter() {
        binding.movieList.adapter = adapter
        binding.movieList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    @SuppressLint("MutatingSharedPrefs", "NotifyDataSetChanged")
    override fun markAsFav(movieId: String) {
        super.markAsFav(movieId)
        if (movieId.isNotEmpty()) {
            val currentFavorites =
                sharedPrefs.getStringSet(FAVORITES_SET_NAME, emptySet())?.toMutableSet()

            currentFavorites?.let {
                if (it.contains(movieId)) {
                    it.remove(movieId)
                    adapter.favAndReload(movieId, false)
                }
                else {
                    it.add(movieId)
                    adapter.favAndReload(movieId, true)
                }

                with(sharedPrefs.edit()) {
                    putStringSet(FAVORITES_SET_NAME, it)
                    apply()
                }

                viewModel.setFavorites(it)
            }
        }
    }

    override fun createDaggerComponent() {
        super.createDaggerComponent()
        tVeoComponent = (activity as TVeoInjection).getTVeoComponent()
        tVeoComponent.inject(this)
    }
}