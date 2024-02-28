/*
* Alyssa Cooper
* Dillon Greek
* Melissa Mika
* Sean Spring
* */

package edu.otc.camera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private File mPhotoFile; //Variable for File holder
    private ImageView mPhotoImageView; //ImageView variable for presenting to phone


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoImageView = findViewById(R.id.photoImg); //Using the "by ID" to place our image within the view
    }

    public void takePhotoClick(View view){ //On click event for taking the picture
        mPhotoFile = createImageFile(); //Calling a method
        Uri photoUri = FileProvider.getUriForFile(this, "edu.otc.camera.fileprovider", mPhotoFile); //URI refers to an object, so we are creating an object using the FileProvider

        mTakePicture.launch(photoUri);//Launching the action for taking the picture in and setting to the URI
    }

    private final ActivityResultLauncher<Uri> mTakePicture = //Setting a URI activity in the registry for future use
            registerForActivityResult(
                    new ActivityResultContracts.TakePicture(), //Using contracts to check and use the picture taken
                    success -> {
                        if (success) { //checking that a picture was taken (success)
                            displayPhoto();
                        }
                    });
    private File createImageFile(){ //Staging the photo
        String imageFileName = "pic.jpg"; //Giving our photo a name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //Finding the phone's directory for photo saves
        return new File(storageDir, imageFileName); //Returning both the location and image through a File
    }

    private void displayPhoto(){ //Pushing the photo to our app
        int targetWidth = mPhotoImageView.getWidth(); //Getting original width of photo
        int targetHeight = mPhotoImageView.getHeight(); //Getting the original height of photo
        BitmapFactory.Options bmOptions = new BitmapFactory.Options(); //Setting up a BitmapFactory object to convert to bitmap
        bmOptions.inJustDecodeBounds = true; //Allowing for customization of the image by reading the dimensions of the image prior to construction
        BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath(), bmOptions); //Decodes the filepath into a bitmap image
        int photoWidth = bmOptions.outWidth; //Sets bitmap width
        int photoHeight = bmOptions.outHeight; //Sets bitmap height

        int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight); //Scales the image down to bitmap specifications
        bmOptions.inJustDecodeBounds = false; //Locks the image into its changed form
        bmOptions.inSampleSize = scaleFactor; //Sets the size to scale
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath(), bmOptions); //Gives the bitmap its options

        mPhotoImageView.setImageBitmap(bitmap); //Presents the bitmap on app
    }
}