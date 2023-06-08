package com.example.finalprojectamit.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.finalprojectamit.R;
import com.example.finalprojectamit.models.FirebaseController;
import com.example.finalprojectamit.models.UserData;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView username, age, height, threeP, twoP, fouls, blocks, assists, ballsLoose;
    ImageView ivImage;
    Button btnAddGame, btnPastGames;

    public static UserData CURRENT_USER;

    Uri mImageUri;

    final int GALLERY_REQUEST = 1;
    final int CAMERA_REQUEST = 2;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets(); // connecting widgets
    }


    public void initWidgets() {
        username = findViewById(R.id.usernameHomeScreen);
        age = findViewById(R.id.ageHomeScreen);
        height = findViewById(R.id.heightHomeScreen);
        threeP = findViewById(R.id.ThreePAvg);
        twoP = findViewById(R.id.TwoPAvg);
        fouls = findViewById(R.id.FoulAvg);
        blocks = findViewById(R.id.BlocksAvg);
        assists = findViewById(R.id.AssistsAvg);
        ballsLoose = findViewById(R.id.BallsLooseAvg);
        ivImage = findViewById(R.id.ivImage);
        btnAddGame = findViewById(R.id.btnAddGame);
        btnAddGame.setOnClickListener(this);
        btnPastGames = findViewById(R.id.btnPastGames);
        btnPastGames.setOnClickListener(this);


        username.setText(CURRENT_USER.getUserName());
        age.setText(String.valueOf(CURRENT_USER.getAge()));
        height.setText(String.valueOf(CURRENT_USER.getHeight()));
        threeP.setText(String.valueOf(CURRENT_USER.getThreePointsAv()));
        twoP.setText(String.valueOf(CURRENT_USER.getTwoPointsAv()));
        fouls.setText(String.valueOf(CURRENT_USER.getFoulAv()));
        blocks.setText(String.valueOf(CURRENT_USER.getBlocksAv()));
        assists.setText(String.valueOf(CURRENT_USER.getAssistsAv()));
        ballsLoose.setText(String.valueOf(CURRENT_USER.getBallsLooseAv()));

        if (CURRENT_USER.getUrl().isEmpty()) // if the user didn't put a profile picture
            ivImage.setImageResource(R.drawable.defult_prp); // puts a default profile picture only at this activity
        else Glide.with(this).load(CURRENT_USER.getUrl()).into(ivImage); // if he has a profile picture, load the profile picture
        ivImage.setOnClickListener(this); // makes the profile picture clickable in order to be able to change the profile picture
    }

    @Override
    protected void onResume() { // when returning to this activity all the widgets will change/stay as they were
        super.onResume();
        username.setText(CURRENT_USER.getUserName());
        age.setText(String.valueOf((int) CURRENT_USER.getAge()));
        height.setText(String.valueOf(CURRENT_USER.getHeight()));
        threeP.setText(String.valueOf(CURRENT_USER.getThreePointsAv()));
        twoP.setText(String.valueOf(CURRENT_USER.getTwoPointsAv()));
        fouls.setText(String.valueOf(CURRENT_USER.getFoulAv()));
        blocks.setText(String.valueOf(CURRENT_USER.getBlocksAv()));
        assists.setText(String.valueOf(CURRENT_USER.getAssistsAv()));
        ballsLoose.setText(String.valueOf(CURRENT_USER.getBallsLooseAv()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddGame: //when clicking the addGame button
                startActivity(new Intent(MainActivity.this, NewGameActivity.class)); // move to addGameActivity
                break;
            case R.id.btnPastGames: // when clicking the pastGames button
                startActivity(new Intent(MainActivity.this, PastGamesActivity.class)); //move to pastGamesActivity
                break;
            case R.id.ivImage: // when clicking the profile picture
                chooseImageDialog(); // calling for function that updates the profile picture
                break;
        }
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

    public Uri ConvertBitmapToUri(Bitmap inImage) { // converting the image from bitmap to Uri
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void chooseImageDialog() { //checks from where to take the picture
        dialog = new Dialog(MainActivity.this); // creating a dialog
        dialog.setContentView(R.layout.dialog); // connecting the dialog to the dialog xml layout
        dialog.setCancelable(true); // the user can exit the dialog by clicking on the screen

        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() { // making the camera image to be clickable
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

        dialog.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() { // making the gallery image to be clickable
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
            mImageUri = ConvertBitmapToUri(bitmap); // converting the image from bitmap to uri
            ivImage.setImageBitmap(bitmap); // replacing the old image to the new image
            new FirebaseController(this).updateImage(CURRENT_USER,mImageUri); // uploading the new image to the user's firebase
        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) { // checking if the image is from the gallery
            mImageUri = data.getData(); // receiving the image
            ivImage.setImageURI(mImageUri); // replacing the old image to the new image
            new FirebaseController(this).updateImage(CURRENT_USER,mImageUri); // uploading the new image to the user's firebase
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //after the user is requested to allow permissions
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // checking if the image is from the camera
            Intent camera = new Intent(); // creating a new intent for the camera
            camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE); // setting the intent to the camera
            startActivityForResult(camera, CAMERA_REQUEST); //starting the camera
            if (dialog != null && dialog.isShowing()) dialog.dismiss(); // dismissing the dialog
        } else if (requestCode == GALLERY_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // checking if the image is from the camera
            Intent gallery = new Intent(); // creating a new intent for the camera
            gallery.setType("image/*"); // setting the type
            gallery.setAction(Intent.ACTION_GET_CONTENT); // setting the intent to the gallery
            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), GALLERY_REQUEST); // starting gallery
            if (dialog != null && dialog.isShowing()) dialog.dismiss(); // dismissing the dialog
        }
    }
}
