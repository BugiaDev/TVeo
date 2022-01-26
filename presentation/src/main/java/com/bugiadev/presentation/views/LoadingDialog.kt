package com.bugiadev.presentation.views

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bugiadev.presentation.R
import com.bugiadev.presentation.databinding.FragmentLoadingDialogBinding

private const val DIALOG_TAG = "Loader"

class LoadingDialog : DialogFragment() {
    private lateinit var binding: FragmentLoadingDialogBinding

    companion object {
        private const val VIEW_POSITION = "position"
        private const val VIEW_HEIGHT = "height"
        private const val VIEW_WIDTH = "width"

        fun newInstance(point: Point, width: Int, height: Int): LoadingDialog {
            return LoadingDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(VIEW_POSITION, point)
                    putInt(VIEW_HEIGHT, height)
                    putInt(VIEW_WIDTH, width)
                }
            }
        }

        fun newInstance(): LoadingDialog = LoadingDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        arguments?.let {
            val position = it.getParcelable<Point>(VIEW_POSITION)
            position?.let { point ->
                val width = it.getInt(VIEW_WIDTH)
                val height = it.getInt(VIEW_HEIGHT)
                binding.loadingLayout.x =
                    (point.x + width / 2 - resources.getDimensionPixelSize(R.dimen.progress_size) / 2).toFloat()
                binding.loadingLayout.y =
                    (point.y - height / 2 + resources.getDimensionPixelSize(R.dimen.progress_size) / 2).toFloat()
            }
        } ?: run {
            val layoutParams = binding.loadingLayout.layoutParams as FrameLayout.LayoutParams
            layoutParams.gravity = Gravity.CENTER
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun show(manager: FragmentManager) {
        showNow(manager, DIALOG_TAG)
    }
}