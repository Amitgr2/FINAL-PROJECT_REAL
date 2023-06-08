package com.example.finalprojectamit.models;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalprojectamit.Constants;
import com.example.finalprojectamit.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseController {
    Activity activity;
    public FirebaseController(Activity activity) {
        this.activity = activity;
    }

    public Task<Void> updateUserToFirebase(UserData userData) {
        return FirebaseDatabase.getInstance().getReference(Constants.USERS_REF).child(userData.getUid()).setValue(userData.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateImage(UserData userData, Uri uri) { // updating image
        ProgressDialog progressDialog = new ProgressDialog(activity); // creating a new progress dialog
        progressDialog.setMessage("loading..."); // setting the message
        progressDialog.setCancelable(false); // cant cancel the progress dialog
        progressDialog.show(); // shows the dialog

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.USERS_REF); // getting the user reference from the firebase
        StorageReference stRef = FirebaseStorage.getInstance().getReference(); // getting the storage reference from the firebase
        final StorageReference fileRef = stRef.child(Constants.IMAGE_PROFILES_REF + userData.getUid());// implanting the image to the storage.
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(url -> {
            userData.setUrl(url.toString()); // changing the image in the user
                    ref.child(userData.getUid()).setValue(userData.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) { // when the previous task has completed
                            if (task.isSuccessful()) { // if the task was successful
                                Toast.makeText(activity, "The image has been updated", Toast.LENGTH_SHORT).show();
                            } else { // if the task wasn't successful
                                Toast.makeText(activity, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                })
        ).addOnFailureListener(e -> Toast.makeText(activity, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }


}
