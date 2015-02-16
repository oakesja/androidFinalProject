package com.example.scanitgrocerystorehelper.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.scanitgrocerystorehelper.utils.ListAuthenticator;

public class ListAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private ListAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new ListAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}