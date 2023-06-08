package com.example.finalprojectamit.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.finalprojectamit.R;
import com.example.finalprojectamit.models.AuthHelper;

import java.io.ByteArrayOutputStream;


public class RegisterActivity extends AppCompatActivity {

    ImageView ibAddImage;
    Uri mImageUri;
    EditText etUsername, etPassword, etAge, etHeight, etEmail;
    TextView haveAUser;

    final int GALLERY_REQUEST = 1;
    final int CAMERA_REQUEST = 2;
    Dialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ibAddImage = findViewById(R.id.ibAddImage);
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etEmail = findViewById(R.id.tvAssists);
        haveAUser = findViewById(R.id.tvHaveAUser);


        ibAddImage.setOnClickListener(new View.OnClickListener() { //if clicking the image
            @Override
            public void onClick(View view) {
                chooseImageDialog(); // calling for a function
            }


        });

    }


    private void requestStoragePermission() { // requesting storage permission in order to be able to access the gallery
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_REQUEST);
    }

    private void requestCameraPermission() { // requesting storage permission in order to be able to access the camera
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }

    private boolean checkStoragePermission() { // checking if the permission to the storage is granted
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;

    }

    private boolean checkCameraPermission() { // checking if the permission to the camera is granted
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) { // converting the image from bitmap to Uri
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void chooseImageDialog() { //checks from where to take the picture
        dialog = new Dialog(RegisterActivity.this); // creating a dialog
        dialog.setContentView(R.layout.dialog); // connecting the dialog to the dialog xml layout
        dialog.setCancelable(true); // the user can exit the dialog by clicking on the screen

        ImageView camera = dialog.findViewById(R.id.camera);
        ImageView gallery = dialog.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() { // making the camera image to be clickable
            @Override
            public void onClick(View view) { // when choosing the camera option
                if (!checkCameraPermission()) // checks if the user didn't give camera permissions
                    requestCameraPermission(); // requesting camera permission
                else { // if the user gave camera permission
                    Intent camera = new Intent();
                    camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE); // creating an intent to the camera
                    startActivityForResult(camera, CAMERA_REQUEST); // opening the camera
                    dialog.dismiss(); // dismissing the dialog
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() { // making the gallery image to be clickable
            @Override
            public void onClick(View view) { // when choosing the gallery option
                if (!checkStoragePermission()) // checks if the user didn't give storage permissions
                    requestStoragePermission(); // requesting storage permission
                else { // if the user gave storage permission
                    Intent gallery = new Intent();
                    gallery.setType("image/*"); // sets the type of input in the intent
                    gallery.setAction(Intent.ACTION_GET_CONTENT); // creating an intent to the gallery
                    startActivityForResult(Intent.createChooser(gallery, "Select Picture"), GALLERY_REQUEST); // opening the gallery
                    dialog.dismiss(); // dismissing the gallery
                }
            }
        });
        dialog.show(); // showing the dialog


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // after choosing an image from the gallery/camera
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) { // checking if the image is from the camera
            Bitmap bitmap = (Bitmap) data.getExtras().get("data"); // converting the image to bitmap
            mImageUri = getImageUri(RegisterActivity.this, bitmap); // converting the image from bitmap to uri
            ibAddImage.setImageBitmap(bitmap); // setting the profile picture to the image that was entered
        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) { // checking if the image is from the gallery
            mImageUri = data.getData(); // receiving the image
            ibAddImage.setImageURI(mImageUri); // setting the profile picture to the image that was entered
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //after the user is requested to allow permissions
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent camera = new Intent();
            camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_REQUEST);
            if(dialog!=null && dialog.isShowing()) dialog.dismiss();
        } else if(requestCode == GALLERY_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), GALLERY_REQUEST);
            if(dialog!=null && dialog.isShowing()) dialog.dismiss();
        }
    }

    public void register(View view) { // if clicking the register button
        if(mImageUri!=null){ // checking if the user has uploaded an image
            AuthHelper authHelper = new AuthHelper(this); // creating a new authHelper
            authHelper.register( //registering with the information that the user has entered
                    etEmail.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    etUsername.getText().toString().trim(),
                    etAge.getText().toString().trim(),
                    etHeight.getText().toString().trim(),
                    mImageUri);
        } else // if the user hasn't uploaded an image
            showUploadImageDialog(); //calling for a function
    }

    public void showUploadImageDialog() { // function that pops up a dialog if registering without adding a profile picture
        AlertDialog.Builder d = new AlertDialog.Builder(this); // creating a new alert dialog
        d.setTitle("WARRING"); // setting a title for the dialog
        d.setMessage("Are you sure you don't want to add a profile picture? (you can add it after)"); // setting the message for the alert dialog

        d.setPositiveButton("YES", new DialogInterface.OnClickListener() { // setting a positive button with the word "YES"
            @Override
            public void onClick(DialogInterface dialog, int which) { // if clicking "YES"
                AuthHelper authHelper = new AuthHelper(RegisterActivity.this); // creating a new authHelper
                authHelper.register( //registering with the information that the user has entered
                        etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        etUsername.getText().toString().trim(),
                        etAge.getText().toString().trim(),
                        etHeight.getText().toString().trim(),
                        mImageUri);
            }
        });

        d.setNegativeButton("NO", new DialogInterface.OnClickListener() { // setting a positive button with the word "NO"
            @Override
            public void onClick(DialogInterface dialog, int which) { // if clicking "NO"
                dialog.dismiss(); // dismissing the dialog
            }
        });

        d.show(); // showing the dialog
    }

    public void BackToLogIn(View view) { // onClick for pressing the sentence that says that "Already have a user? Log in"
        Intent intent = new Intent(this, LoginActivity.class); // creating a new intent to the LoginActivity
        startActivity(intent); // starting the intent
    }
}