package com.bugiadev.presentation.views

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bugiadev.presentation.R

class ConfirmationDialog constructor(@StringRes val message: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()

    companion object {
        const val TAG = "ConfirmationDialog"
    }
}