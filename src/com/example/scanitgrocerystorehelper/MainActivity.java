package com.example.scanitgrocerystorehelper;

import android.os.Bundle;

public class MainActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		getActionBar().setTitle(R.string.title_activity_main);
    }
}
