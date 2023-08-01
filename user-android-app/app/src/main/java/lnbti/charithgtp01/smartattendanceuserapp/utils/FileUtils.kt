package lnbti.charithgtp01.smartattendanceuserapp.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.MEDIA_TYPE_EMPLOYEE_SIGNATURE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class FileUtils {
    companion object {

        /**
         * Get Image File from the Type and the Unique ID
         * @param uuid Unique ID for identify the Image
         * @param type Type of the Image (Ex: Employee Signature, Profile Image)
         */
        fun getOutputMediaFile(context: Context, uuid: String, type: String): File? {
// Check that the SDCard is mounted
            var mediaStorageDir: File? = null
            var mediaFile: File? = null
            var directoryStartPath = uuid

            if ((type == MEDIA_TYPE_EMPLOYEE_SIGNATURE)) {
//            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES), Constants.TAG + "/" + uuid);
                mediaStorageDir = File(
                    context.getExternalFilesDir(
                        Environment.DIRECTORY_PICTURES
                    ), Constants.TAG + "/" + directoryStartPath
                )
            }

            // Create the storage directory(MyCameraVideo) if it does not exist
            if (!mediaStorageDir!!.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(Constants.TAG, "Failed to create directory MyCameraVideo.")
                    Toast.makeText(
                        context, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.")
                    return null
                }
            }


            // Create a media file name

            // For unique file name appending current timeStamp with file name
            val date = Date()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.time)
            if (type === MEDIA_TYPE_EMPLOYEE_SIGNATURE) {
                mediaFile = File(
                    (mediaStorageDir.path + File.separator +
                            "EMPLOYEE_SIGN_" + timeStamp + ".png")
                )
                Log.d(Constants.TAG, mediaFile.name)
            } else {
                return null
            }
            return mediaFile
        }
    }

}