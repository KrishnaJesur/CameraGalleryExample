package com.krishna.cameragalleryexample.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.krishna.cameragalleryexample.R
import com.krishna.cameragalleryexample.databinding.DialogMediaFilePickerBinding

typealias mediaFilePickerDialogDelegate = MediaFilePickerDialog.MediaFilePickerDialogViewClick
object MediaFilePickerDialog {
    private var dialog: Dialog? = null

    fun createDialog(
        context: Context,
        mediaFilePickerDialogViewClick: MediaFilePickerDialogViewClick
    ) {
        dialog = Dialog(context)

        val dialogMediaFilePickerBinding: DialogMediaFilePickerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_media_file_picker, null, false
        )

        dialog?.let {

            it.requestWindowFeature(Window.FEATURE_NO_TITLE)

            it.setContentView(dialogMediaFilePickerBinding.root)

            it.window!!.let { window ->
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window.setBackgroundDrawableResource(R.color.transparent)
            }

            it.show()
        }

        dialogMediaFilePickerBinding.cvCamera.setOnClickListener {
            mediaFilePickerDialogViewClick.onCameraButtonClick()
        }

        dialogMediaFilePickerBinding.cvGallery.setOnClickListener {
            mediaFilePickerDialogViewClick.onGalleryButtonClick()
        }
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    interface MediaFilePickerDialogViewClick {
        fun onCameraButtonClick()
        fun onGalleryButtonClick()
    }
}