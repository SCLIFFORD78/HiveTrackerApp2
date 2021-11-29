package org.wit.hivetrackerapp.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.hivetrackerapp.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_hive_image.toString())
    intentLauncher.launch(chooseFile)
}