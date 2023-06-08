package com.example.finalprojectamit.models;

import static android.content.Context.MODE_PRIVATE;
import static com.example.finalprojectamit.activities.MainActivity.CURRENT_USER;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalprojectamit.Constants;
import com.example.finalprojectamit.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AuthHelper {
    Activity activity;

    public AuthHelper(Activity activity) {
        this.activity = activity;
    }

    public boolean isThereEmptyData(String email, String password, String userName, String ageText, String heightText) { //checks if the user has entered all the data that is needed
        return email.trim().isEmpty() || password.trim().isEmpty() || userName.trim().isEmpty() || ageText.trim().isEmpty() || heightText.trim().isEmpty();
    }

    public void ifRememberUserLogin() { //checks if the user pressed the remember me button last time
        if (FirebaseAuth.getInstance().getCurrentUser() != null) { //checking if the user does exist
            SharedPreferences prefs = activity.getSharedPreferences(Constants.SHARED_PREFERENCE_AUTH_FOLDER, MODE_PRIVATE); // receiving the user's data
            String email = prefs.getString(Constants.SHARED_PREFERENCE_AUTH_EMAIL, ""); // getting the email of the user
            String password = prefs.getString(Constants.SHARED_PREFERENCE_AUTH_PASSWORD, ""); // getting the password of the user
            if (!email.trim().isEmpty() && !password.trim().isEmpty()) // if the email and the password aren't null
                login(email, password, true); //login in with the email and password
        }
    }

    public void login(String email, String password, boolean remember) { // login in function
        ProgressDialog progressDialog = new ProgressDialog(activity); // creating a new progress dialog
        progressDialog.setMessage("loading..."); // setting the message in the progress dialog
        progressDialog.setCancelable(false); // setting that the user won't be able to exit the dialog
        progressDialog.show(); // showing the progress dialog
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getCurrentUser().addOnSuccessListener(dataSnapshot -> {
                        if (remember) { // saves the email and password as a xml file if the user marks the remember me button
                            SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.SHARED_PREFERENCE_AUTH_FOLDER, MODE_PRIVATE).edit(); // making shared preference
                            editor.putString(Constants.SHARED_PREFERENCE_AUTH_EMAIL, email.trim()); //inserting a value
                            editor.putString(Constants.SHARED_PREFERENCE_AUTH_PASSWORD, password.trim()); // inserting a value
                            editor.apply(); // applying the values
                        }
                        progressDialog.dismiss(); // dismissing the progress dialog
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    }).addOnFailureListener(e -> Toast.makeText(activity, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                } else { // if it didn't work
                    progressDialog.dismiss();
                    Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public Task<DataSnapshot> getCurrentUser() { // giving the current user
        return FirebaseDatabase.getInstance().getReference(Constants.USERS_REF).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener( // getting the user from the firebase
                task -> { // takes the data from firebase about the user by the user uid
                    if (task.isSuccessful()) { // if the task worked
                        CURRENT_USER = new UserData((HashMap<String, Object>) task.getResult().getValue()); // connecting CURRENT USER to the real user
                    } else // if the task didn't work
                        Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show(); // making the failure toast
                }
        );
    }

    public void updateUserInfo(ProgressDialog progressDialog, String uid, String userName, int age, int height) { //updating the user without a profile image
        UserData userData = new UserData(uid, userName, age, height); // creating a new userdata with the new fields
        FirebaseDatabase.getInstance().getReference(Constants.USERS_REF).child(uid).setValue(userData.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() { // getting the user from the firebase
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CURRENT_USER = userData; // changing the current user
                    progressDialog.dismiss();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                } else { // if the task didn't work
                    progressDialog.dismiss(); // dismissing the progress dialog
                    Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show(); // making the failure toast
                }
            }
        });

    }

    public void updateUserInfo(ProgressDialog progressDialog, String uid, String userName, int age, int height, Uri uri) { //updating the user with a profile image
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.USERS_REF); // getting the user reference from the firebase
        StorageReference stRef = FirebaseStorage.getInstance().getReference(); // getting the storage reference from the firebase
        final StorageReference fileRef = stRef.child(Constants.IMAGE_PROFILES_REF + uid); // implanting the image to the storage.

        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(url -> {
                    CURRENT_USER = new UserData(uid, userName, age, height,url.toString()); // creating a new current user with the new fields
                    ref.child(CURRENT_USER.getUid()).setValue(CURRENT_USER.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finish();
                            } else { // if the task didn't work
                                progressDialog.dismiss(); // dismissing the progress dialog
                                Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show(); // making the failure toast
                            }
                        }
                    });
                })
        ).addOnFailureListener(e -> Toast.makeText(activity, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

    }

    public void register(String email, String password, String userName, String ageText, String heightText, Uri uri) { // register function
        ProgressDialog progressDialog = new ProgressDialog(activity); // creating a new progress dialog
        progressDialog.setMessage("loading..."); // setting the message
        progressDialog.setCancelable(false); // cant cancel the progress dialog
        progressDialog.show(); // shows the dialog

        if (!isThereEmptyData(email, password, userName, ageText, heightText)) { // if the user filled all the required fields
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() { // creating a new user in the firebase
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) { // when the previous task has completed
                    if (task.isSuccessful()) { // if the task was successful
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(userName.trim()).build();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // creating a new firebase user
                        user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) { // when the previous task has completed
                                if (task.isSuccessful()) { // if the task was successful
                                    if (uri != null) // if the user hasn't entered a profile picture
                                        updateUserInfo(progressDialog, FirebaseAuth.getInstance().getCurrentUser().getUid(), userName.trim(), Integer.parseInt(ageText), Integer.parseInt(heightText), uri);
                                    else // if the user entered a profile picture
                                        updateUserInfo(progressDialog, FirebaseAuth.getInstance().getCurrentUser().getUid(), userName.trim(), Integer.parseInt(ageText), Integer.parseInt(heightText));
                                } else { // if the task wasn't successful
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else { // if the task wasn't successful
                        progressDialog.dismiss();
                        Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { // if the task wasn't successful
            progressDialog.dismiss();
            Toast.makeText(activity, "Enter all data!", Toast.LENGTH_SHORT).show();
        }
    }

}
