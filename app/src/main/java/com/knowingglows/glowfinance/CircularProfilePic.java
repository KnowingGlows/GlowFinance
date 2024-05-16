package com.knowingglows.glowfinance;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
public class CircularProfilePic
{
    public static void loadCircularImage(Context context, ImageView imageView) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Glide.with(context)
                    .load(user.getPhotoUrl()) // Get profile picture URL from FirebaseUser
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .placeholder(R.drawable.glowfinance_logo)
                    .error(R.drawable.glowfinance_logo)
                    .into(imageView);
        }
    }
}

