package com.example.bluegit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

//            if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
//                Toast.makeText(context, "internet: " + "true", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "internet: " + "false", Toast.LENGTH_SHORT).show();
//            }

            if(activeNetworkInfo == null || !activeNetworkInfo.isConnected()){
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

//            ConnectivityManager connMgr = context.getSystemService(ConnectivityManager.class);
//            android.net.NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//            if(!networkInfo.isConnected()){
//                Intent i = new Intent(context, LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//            }
        }

    }
}