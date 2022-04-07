// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"
package com.theartofdev.edmodo.cropper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImageView.*
import java.io.File
import java.io.IOException

/**
 * Built-in activity for image cropping.<br></br>
 * Use [CropImage.activity] to create a builder to start this activity.
 */
class CropImageActivity : AppCompatActivity(), OnSetImageUriCompleteListener, OnCropImageCompleteListener {
    /**
     * The crop image view library widget used in the activity
     */
    private var mCropImageView: CropImageView? = null

    /**
     * Persist URI image to crop URI if specific permissions are required
     */
    private var mCropImageUri: Uri? = null

    /**
     * the options that were set for the crop image
     */
    private var mOptions: CropImageOptions? = null
    @SuppressLint("NewApi")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crop_image_activity)
        mCropImageView = findViewById<View>(R.id.cropImageView) as CropImageView
        val iv_rotate = findViewById<View>(R.id.iv_rotate) as ImageView
        val tv_cancel = findViewById<View>(R.id.tv_cancel) as TextView
        val tv_done = findViewById<View>(R.id.tv_done) as TextView
        val bundle = intent.getBundleExtra(CropImageOptions.Companion.BUNDLE_KEY)
        mCropImageUri = bundle.getParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE)
        mOptions = bundle.getParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS)
        if (savedInstanceState == null) {
            if (mCropImageUri == null || mCropImageUri == Uri.EMPTY) {
                if (CropImage.isExplicitCameraPermissionRequired(this)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
                } else {
                    CropImage.startPickImageActivity(this)
                }
            } else if (CropImage.isReadExternalStoragePermissionsRequired(this, mCropImageUri!!)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
            } else {
                // no permissions required or already grunted, can start crop image activity
                mCropImageView!!.setImageUriAsync(mCropImageUri)
            }
        }
        iv_rotate.setOnClickListener { rotateImage(-mOptions!!.rotationDegrees) }
        tv_cancel.setOnClickListener { setResultCancel() }
        tv_done.setOnClickListener { cropImage() }
    }

    override fun onStart() {
        super.onStart()
        mCropImageView!!.setOnSetImageUriCompleteListener(this)
        mCropImageView!!.setOnCropImageCompleteListener(this)
    }

    override fun onStop() {
        super.onStop()
        mCropImageView!!.setOnSetImageUriCompleteListener(null)
        mCropImageView!!.setOnCropImageCompleteListener(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResultCancel()
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                //User cancelled the picker. We don't have anything to crop
                setResultCancel()
            }
            if (resultCode == RESULT_OK) {
                mCropImageUri = CropImage.getPickImageResultUri(this, data)

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, mCropImageUri!!)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    mCropImageView!!.setImageUriAsync(mCropImageUri)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                mCropImageView!!.setImageUriAsync(mCropImageUri)
            } else {
                Toast.makeText(this, R.string.crop_image_activity_no_permissions, Toast.LENGTH_LONG).show()
                setResultCancel()
            }
        }
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            //Irrespective of whether camera permission was given or not, we show the picker
            //The picker will not add the camera intent if permission is not available
            CropImage.startPickImageActivity(this)
        }
    }

    override fun onSetImageUriComplete(view: CropImageView?, uri: Uri?, error: Exception?) {
        if (error == null) {
            if (mOptions!!.initialCropWindowRectangle != null) {
                mCropImageView.setCropRect(mOptions!!.initialCropWindowRectangle)
            }
            if (mOptions!!.initialRotation > -1) {
                mCropImageView.setRotatedDegrees(mOptions!!.initialRotation)
            }
        } else {
            setResult(null, error, 1)
        }
    }

    override fun onCropImageComplete(view: CropImageView?, result: CropResult) {
        setResult(result.uri, result.error, result.sampleSize)
    }
    //region: Private methods
    /**
     * Execute crop image and save the result tou output uri.
     */
    protected fun cropImage() {
        if (mOptions!!.noOutputImage) {
            setResult(null, null, 1)
        } else {
            val outputUri = outputUri
            mCropImageView!!.saveCroppedImageAsync(outputUri,
                    mOptions!!.outputCompressFormat,
                    mOptions!!.outputCompressQuality,
                    mOptions!!.outputRequestWidth,
                    mOptions!!.outputRequestHeight,
                    mOptions!!.outputRequestSizeOptions)
        }
    }

    /**
     * Rotate the image in the crop image view.
     */
    protected fun rotateImage(degrees: Int) {
        mCropImageView!!.rotateImage(degrees)
    }

    /**
     * Get Android uri to save the cropped image into.<br></br>
     * Use the given in options or create a temp file.
     */
    protected val outputUri: Uri?
        protected get() {
            var outputUri = mOptions!!.outputUri
            if (outputUri == null || outputUri == Uri.EMPTY) {
                outputUri = try {
                    val ext = if (mOptions!!.outputCompressFormat == CompressFormat.JPEG) ".jpg" else if (mOptions!!.outputCompressFormat == CompressFormat.PNG) ".png" else ".webp"
                    Uri.fromFile(File.createTempFile("cropped", ext, cacheDir))
                } catch (e: IOException) {
                    throw RuntimeException("Failed to create temp file for output image", e)
                }
            }
            return outputUri
        }

    /**
     * Result with cropped image data or error if failed.
     */
    protected fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val resultCode = if (error == null) RESULT_OK else CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
        setResult(resultCode, getResultIntent(uri, error, sampleSize))
        finish()
    }

    /**
     * Cancel of cropping activity.
     */
    protected fun setResultCancel() {
        setResult(RESULT_CANCELED)
        finish()
    }

    /**
     * Get intent instance to be used for the result of this activity.
     */
    protected fun getResultIntent(uri: Uri?, error: Exception?, sampleSize: Int): Intent {
        val result = CropImage.ActivityResult(
                mCropImageView.getImageUri(),
                uri,
                error,
                mCropImageView.getCropPoints(),
                mCropImageView.getCropRect(),
                mCropImageView.getRotatedDegrees(),
                mCropImageView.getWholeImageRect(),
                sampleSize)
        val intent = Intent()
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result)
        return intent
    }
}