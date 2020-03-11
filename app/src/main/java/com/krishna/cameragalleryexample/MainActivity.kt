package com.krishna.cameragalleryexample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.krishna.cameragalleryexample.databinding.ActivityMainBinding
import com.krishna.cameragalleryexample.dialog.MediaFilePickerDialog
import com.krishna.cameragalleryexample.dialog.ShowRationPermissionDialog
import com.krishna.cameragalleryexample.dialog.mediaFilePickerDialogDelegate
import com.krishna.cameragalleryexample.dialog.showRationalPermissionDialogDelegate
import com.krishna.cameragalleryexample.helper.PermissionHelper
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity(), mediaFilePickerDialogDelegate,
    showRationalPermissionDialogDelegate {

    lateinit var mainBinding: ActivityMainBinding
    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.imageView2.setOnClickListener {
            MediaFilePickerDialog.createDialog(this, this)
        }

    }

    override fun onCameraButtonClick() {
        if (!PermissionHelper.checkPermission(
                this,
                Manifest.permission.CAMERA
            ) || !PermissionHelper.checkPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            PermissionHelper.askPermission(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                REQUEST_CODE_STORAGE_CAMERA_PERMISSION
            )
        } else {
            openCameraApp()
        }
    }

    private fun openCameraApp() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

        MediaFilePickerDialog.dismissDialog()
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_PARENT_PHOTO", ".jpg", storageDir).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    override fun onGalleryButtonClick() {
        if (!PermissionHelper.checkPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) || !PermissionHelper.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            PermissionHelper.askPermission(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_STORAGE_PERMISSION
            )
        } else {
            openGalleyApp()
        }
    }

    private fun openGalleyApp() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
        MediaFilePickerDialog.dismissDialog()
    }


    override fun openAppSetting() {
        openAppSettings()
    }

    private fun Context.openAppSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCameraApp()
                } else {
                    if (!PermissionHelper.showRational(this, permissions[0])) {
                        ShowRationPermissionDialog.createDialog(
                            this,
                            getString(ProjectStrings.rational_camera),
                            this
                        )
                        return
                    }

                    if (!PermissionHelper.showRational(this, permissions[1])) {
                        ShowRationPermissionDialog.createDialog(
                            this,
                            getString(ProjectStrings.rational_storage_camera),
                            this
                        )
                        return
                    }
                }
            }
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleyApp()
                } else {
                    if (!PermissionHelper.showRational(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) || !PermissionHelper.showRational(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )

                    ) {
                        ShowRationPermissionDialog.createDialog(
                            this,
                            getString(ProjectStrings.rational_storage),
                            this
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //To get the File for further usage
            val imageFile = File(mCurrentPhotoPath!!)
            mainBinding.imageView2.setImageURI(Uri.parse(imageFile.toString()))

        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c = contentResolver.query(selectedImage!!, filePath, null, null, null)
                c?.moveToFirst()
                val columnIndex = c?.getColumnIndex(filePath[0])
                val picturePath = c?.getString(columnIndex!!)
                c?.close()
                val imageFile = File(picturePath)
                mainBinding.imageView2.setImageURI(Uri.parse(imageFile.toString()))
            }
        }
    }

}
