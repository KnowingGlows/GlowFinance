package com.knowingglows.glowfinance;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class GlowCoins

{

    public interface OnGlowCoinsLoadedListener {
        void onGlowCoinsLoaded(long glowCoins);
        void onError(String errorMessage);
    }

    private static final String USERS_COLLECTION = "users";
    private static final String GLOW_COINS_FIELD = "GlowCoins";

    private final FirebaseFirestore db;

    public GlowCoins()
    {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }

    public void updateGlowCoins(String userId, long newGlowCoins, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference userRef = db.collection(USERS_COLLECTION).document(userId);
        userRef
                .update(GLOW_COINS_FIELD, newGlowCoins)
                .addOnCompleteListener(onCompleteListener);
    }

    public void addGlowCoins(String userId, long glowCoinsToAdd, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference userRef = db.collection(USERS_COLLECTION).document(userId);
        userRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            long currentGlowCoins = document.getLong(GLOW_COINS_FIELD);
                            updateGlowCoins(userId, currentGlowCoins + glowCoinsToAdd, onCompleteListener);
                        } else {
                            // Handle document does not exist
                        }
                    } else {
                        // Handle error
                    }
                });
    }


    public static void getGlowCoins(String userId, OnGlowCoinsLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long glowCoins = documentSnapshot.getLong("GlowCoins");
                        if (glowCoins != null) {
                            listener.onGlowCoinsLoaded(glowCoins);
                        } else {
                            listener.onError("GlowCoins field is null");
                        }
                    } else {
                        listener.onError("User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onError("Error getting GlowCoins: " + e.getMessage());
                });
    }
}

