package com.example.bluegit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DatabaseService extends Service {
    public DatabaseService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}