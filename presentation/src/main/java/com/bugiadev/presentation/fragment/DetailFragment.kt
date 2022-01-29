package com.bugiadev.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bugiadev.presentation.R
import com.bugiadev.presentation.TVeoComponent
import com.bugiadev.presentation.TVeoInjection
import com.bugiadev.presentation.databinding.FragmentDetailBinding
import com.bugiadev.presentation.utils.loadFromUrl
import com.bugiadev.presentation.utils.viewModel
import com.bugiadev.presentation.viewmodel.TVeoDetailViewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var tVeoComponent: TVeoComponent
    private lateinit var movieAssetId: String
    private val viewModel: TVeoDetailViewModel by viewModel { tVeoComponent.tVeoDetailViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
        viewModel.getMovieDetail(args.movieId)
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            showLoader(isLoading)
        })

        viewModel.movieDetail.observe(viewLifecycleOwner, { movie ->
            movieAssetId = movie.assetExternalId.orEmpty()
            binding.apply {
                movieImage.loadFromUrl(movie.imageURI.orEmpty(), movieImage.context)
                titleTextview.text = movie.name
                durationTextview.text = movie.duration
                typeTextview.text = "${movie.type} (${movie.year.toString()})"
                descriptionTextview.text = movie.description
            }
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            showErrorState(error)
        })
    }

    private fun initListeners() {
        binding.recommendationsButton.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailToRecommendations(args.movieId, movieAssetId)
            )
        }

        handleBackButton {
            findNavController().navigateUp()
        }
    }

    override fun createDaggerComponent() {
        super.createDaggerComponent()
        tVeoComponent = (activity as TVeoInjection).getTVeoComponent()
        tVeoComponent.inject(this)
    }
}