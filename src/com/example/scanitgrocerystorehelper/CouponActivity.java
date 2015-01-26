package com.example.scanitgrocerystorehelper;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class CouponActivity extends DrawerActivity {

	private static final String mUrl = "http://www.groceryserver.com/groceryserver/gsmobile/site/index.jsp?zipCode=47803&items=bread|milk";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);
		
		// need to manipulate url above with Zipcode and specified lists or single item
		// need to validate the url after each item adition to it to make sure it is still valid
		// replace spaces with %20
		try {
		    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
		    startActivity(myIntent);
		} catch (ActivityNotFoundException e) {
		    Toast.makeText(this, "No application can handle this request."
		        + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
		}
	}
}
