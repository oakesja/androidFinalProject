package com.example.scanitgrocerystorehelper.models;

import java.math.BigDecimal;

public class ListItem {

	private String itemName;
	private int quantity;
	private BigDecimal price;
	
	public ListItem(String name, int quantity){
		this.itemName = name;
		this.quantity = quantity;
		this.price = new BigDecimal(0);
	}
	
	public ListItem(String name, int quantity, BigDecimal price){
		this.itemName = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	public void setName(String name){
		this.itemName = name;
	}
	
	public String getName(){
		return this.itemName;
	}
	
	public void setQuantity(int qty){
		this.quantity = qty;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	
	public void setPrice (BigDecimal price){
		this.price = price;
	}
	
	public BigDecimal getPrice(){
		return this.price;
	}
}
