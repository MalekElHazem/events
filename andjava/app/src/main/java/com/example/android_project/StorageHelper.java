package com.example.android_project;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class StorageHelper {
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();

    public static void uploadImage(Uri imageUri, OnImageUploadListener listener) {
        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storage.getReference().child("images/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri ->
                                    listener.onSuccess(uri.toString()))
                            .addOnFailureListener(listener::onFailure);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public interface OnImageUploadListener {
        void onSuccess(String imageUrl);
        void onFailure(Exception e);
    }
}