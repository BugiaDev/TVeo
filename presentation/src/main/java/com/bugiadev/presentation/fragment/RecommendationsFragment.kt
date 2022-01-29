package com.bugiadev.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.bugiadev.presentation.TVeoComponent
import com.bugiadev.presentation.TVeoInjection
import com.bugiadev.presentation.databinding.FragmentRecommendationsBinding
import com.bugiadev.presentation.utils.viewModel
import com.bugiadev.presentation.viewmodel.TVeoRecommendationsViewModel
import com.bugiadev.presentation.views.adapter.TVeoRecommendationsAdapter


class RecommendationsFragment : BaseFragment<FragmentRecommendationsBinding>() {
    private val args: RecommendationsFragmentArgs by navArgs()
    private lateinit var tVeoComponent: TVeoComponent
    private val viewModel: TVeoRecommendationsViewModel by viewModel { tVeoComponent.tVeoRecommendationsViewModel }

    private lateinit var adapter: TVeoRecommendationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        handleBackButton {
            findNavController().navigateUp()
        }

        viewModel.getMovieRecommendationsList(args.movieAssetId)
    }

    private fun initObservers() {
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            showLoader(isLoading)
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            showErrorState(error)
        })

        viewModel.movies.observe(viewLifecycleOwner, { movies ->
            adapter = TVeoRecommendationsAdapter(
                onItemClick = { display ->
                    display.id?.let {
                        viewModel.onMovieSelected(it)
                    }
                },
                items = movies
            )

            binding.recommendationsList.adapter = adapter
            binding.recommendationsList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        })

        viewModel.selectedMovie.observe(viewLifecycleOwner, { id ->
            findNavController().navigate(
                RecommendationsFragmentDirections.actionRecommendationsToDetail(id)
            )
        })
    }

    override fun createDaggerComponent() {
        super.createDaggerComponent()
        tVeoComponent = (activity as TVeoInjection).getTVeoComponent()
        tVeoComponent.inject(this)
    }
}