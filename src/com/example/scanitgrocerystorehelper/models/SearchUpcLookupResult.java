package com.example.scanitgrocerystorehelper.models;

public class SearchUpcLookupResult {
	private String productname;
	private String imageurl;
	private double price;
	private String currency;
	private String saleprice;
	private String storename;

	public SearchUpcLookupResult(String productname, String imageurl,
			double price, String currency, String saleprice, String storename) {
		this.productname = productname;
		this.imageurl = imageurl;
		this.price = price;
		this.currency = currency;
		this.saleprice = saleprice;
		this.storename = storename;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(String saleprice) {
		this.saleprice = saleprice;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}
	
	@Override
	public String toString() {
		return productname + " " + imageurl;
	}

}
