package com.example.android.elderlyapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import java.util.List;

/**
 * Created by Darsh_Shah on 23-06-2017.
 */

public class AlertDialogActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Test")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Confirm Alarm", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();

                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
