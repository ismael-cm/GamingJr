package com.example.gamingjr.model;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gamingjr.HomeActivity;
import com.example.gamingjr.ProfileActivity;
import com.example.gamingjr.R;

public class MenuHelper {
    public static void handleBottomNavigation(Context context, MenuItem item) {
        int navHome = R.id.nav_home;
        int navProgress = R.id.nav_progreso;
        int navProfile = R.id.nav_profile;
        int selectedItem = item.getItemId();
        int currentActivity = ((Activity) context).getLocalClassName().hashCode();


        if(selectedItem == navProgress) {
            // Logica para el progreso
        } else if (selectedItem == navProfile && currentActivity != ProfileActivity.class.getName().hashCode()) {
            Intent profileActivity = new Intent(context, ProfileActivity.class);
            context.startActivity(profileActivity);
        } else if (selectedItem == navHome && currentActivity != HomeActivity.class.getName().hashCode()) {
            Intent homeActivity = new Intent(context, HomeActivity.class);
            context.startActivity(homeActivity);
        }
    }
}

