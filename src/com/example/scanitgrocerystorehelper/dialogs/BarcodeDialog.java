package com.example.scanitgrocerystorehelper.dialogs;

import android.app.DialogFragment;

public abstract class BarcodeDialog extends DialogFragment {
	private String productName;
	private String barcode;

	public BarcodeDialog() {
		productName = "";
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
