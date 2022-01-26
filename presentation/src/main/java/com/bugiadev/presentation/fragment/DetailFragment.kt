package com.bugiadev.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bugiadev.presentation.TVeoComponent
import com.bugiadev.presentation.TVeoInjection
import com.bugiadev.presentation.databinding.FragmentDetailBinding
import com.bugiadev.presentation.utils.viewModel
import com.bugiadev.presentation.viewmodel.TVeoDetailViewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private lateinit var tVeoComponent: TVeoComponent
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

        viewModel.getMovieDetail("")

        handleBackButton {
            findNavController().navigateUp()
        }
    }

    private fun initObservers() {
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            showLoader(isLoading)
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            showErrorState(error)
        })
    }

    override fun createDaggerComponent() {
        super.createDaggerComponent()
        tVeoComponent = (activity as TVeoInjection).getTVeoComponent()
        tVeoComponent.inject(this)
    }
}