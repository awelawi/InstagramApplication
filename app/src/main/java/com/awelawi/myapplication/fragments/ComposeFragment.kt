package com.awelawi.myapplication.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.awelawi.myapplication.MainActivity
import com.awelawi.myapplication.Post
import com.awelawi.myapplication.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File



class ComposeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    val photoFileName = "photo.jpg"
    var photoFile : File? = null

    lateinit var ivImage: ImageView
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set onCliclisteners setup logic of buttons

        ivImage = view.findViewById(R.id.ivPost)
        view.findViewById<Button>(R.id.submitPost).setOnClickListener {
            //send post to server
            //first send post without an image
            //Get the description the user has inputed
            val description = view.findViewById<EditText>(R.id.postDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(description, user, photoFile!!)
                //EROR HERE
            } else {
                //TODO Pring error log message
                //TODO: show a toast to the user
            }
        }
        view.findViewById<Button>(R.id.btnCapture).setOnClickListener {
            //Launch camera to let user take picture
            onLaunchCamera()
        }
    }

        fun submitPost(description: String, user: ParseUser, file: File){
            //create the post object
            val post = Post()
            post.setDescription(description)
            post.setUser(user)
            post.setImage(ParseFile(file))
            post.saveInBackground { exception ->
                if(exception!= null) {
                    //Something went wrong
                    Log.e(MainActivity.TAG, "Error while saving post")
                    exception.printStackTrace()

                    //TODO show a toassr to tell the user something went wrong with saving the post
                }
                else{
                    Log.i(MainActivity.TAG, "successfully saved post")
                    //TODO: Reset the editText view to emoty
                    //TODO: Reset the imageView to empty
                }


            }
        }


        fun getPhotoFileUri(fileName: String): File {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            val mediaStorageDir =
                File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(MainActivity.TAG, "failed to create directory")
            }

            // Return the file target for the photo based on filename
            return File(mediaStorageDir.path + File.separator + fileName)
        }
        fun onLaunchCamera() {
            // create Intent to take a picture and return control to the calling application
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Create a File reference for future access
            photoFile = getPhotoFileUri(photoFileName)

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            if (photoFile != null) {
                val fileProvider: Uri =
                    FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
                }
            }
        }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                ivImage.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}