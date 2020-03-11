package com.krishna.cameragalleryexample.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.krishna.cameragalleryexample.R

typealias showRationalPermissionDialogDelegate = ShowRationPermissionDialog.ShowRationPermissionDialogButtonClick

object ShowRationPermissionDialog {
    fun createDialog(
        context: Context,
        message: String,
        showRationPermissionDialogButtonClick: ShowRationPermissionDialogButtonClick
    ) {
        AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher)
            .setTitle(context.getString(R.string.alert))
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.setting)) { dialog, _ ->
                showRationPermissionDialogButtonClick.openAppSetting()
                dialog.dismiss()
            }.setNegativeButton(context.getString(R.string.not_now)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    interface ShowRationPermissionDialogButtonClick {
        fun openAppSetting()
    }
}