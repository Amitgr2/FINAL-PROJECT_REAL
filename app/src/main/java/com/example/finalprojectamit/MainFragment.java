package com.example.finalprojectamit;


import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {


    public static boolean SHOW_BATTERY_AS_IMAGE = true;
    public static int BATTERY_LEVEL;

    BroadcastReceiver mNetworkReceiver;
    public static Activity CURRENT_ACTIVITY;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = new Intent(getActivity(), InternetConnectionService.class);
        getActivity().startService(intent);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        batteryLevel(menu.findItem(R.id.action_battery));
    }

    private void batteryLevel(MenuItem batteryItem) { // setting the battery level button
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() { // creating a new broadcast receiver
            public void onReceive(Context context, Intent intent) {
                BATTERY_LEVEL = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0); //getting the battery percentage
                if (SHOW_BATTERY_AS_IMAGE) // if the user wants to see the battery image
                    batteryItem.setIcon(getBatteryImage(BATTERY_LEVEL)); // setting the battery image as the percentage
                else { // if the user wants to see the battery percentage
                    batteryItem.setIcon(0); // removing the battery icon
                    batteryItem.setTitle("" + BATTERY_LEVEL + "%"); // showing the battery percentage
                }
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    public int getBatteryImage(int percent) { // getting the battery icon as the percentage
        if (percent == 100) return R.drawable.ic_baseline_battery_full_24;
        if (percent >= 84) return R.drawable.ic_baseline_battery_6_bar_24;
        if (percent >= 67) return R.drawable.ic_baseline_battery_5_bar_24;
        if (percent >= 51) return R.drawable.ic_baseline_battery_4_bar_24;
        if (percent >= 34) return R.drawable.ic_baseline_battery_3_bar_24;
        if (percent >= 18) return R.drawable.ic_baseline_battery_2_bar_24;
        else return R.drawable.ic_baseline_battery_1_bar_24;
    }

    private void closeApp() { //exit dialog
        new AlertDialog.Builder(getActivity())
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() { // if clicking YES
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) { // signing out
                            FirebaseAuth.getInstance().signOut();
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_AUTH_FOLDER, MODE_PRIVATE).edit();
                            editor.putString(Constants.SHARED_PREFERENCE_AUTH_EMAIL, "");
                            editor.putString(Constants.SHARED_PREFERENCE_AUTH_PASSWORD, "");
                            editor.apply();
                        }
                        requireActivity().finishAffinity();
                        requireActivity().finishAndRemoveTask();// exiting all the activities
                        requireActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() { // if clicking NO
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing will happen
                    }
                }).show(); // show to dialog

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_battery: // if clicking the battery icon
                SHOW_BATTERY_AS_IMAGE = !SHOW_BATTERY_AS_IMAGE;
                if (SHOW_BATTERY_AS_IMAGE) // if it shows the battery icon
                    item.setIcon(getBatteryImage(BATTERY_LEVEL));
                else { // if it shows the battery percentage
                    item.setIcon(0); // removing the battery icon
                    item.setTitle("" + BATTERY_LEVEL + "%"); // showing the battery percentage
                }
                break;
            case R.id.action_logout: // if clicking the exit button
                closeApp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}