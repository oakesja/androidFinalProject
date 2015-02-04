package com.example.scanitgrocerystorehelper.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class List {

	private String name;
	private String description;
	private ArrayList<ListItem> list;
	private Date created;
	private Date modified;
	private String author;

	public List(String name, String description) {
		this.created = new Date();
		this.modified = new Date();
		this.name = name;
		this.description = description;
		this.list = new ArrayList<ListItem>();
	}

	public List(String name, String description, ArrayList<ListItem> list) {
		this.created = new Date();
		this.modified = new Date();
		this.name = name;
		this.description = description;
		this.list = list;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public ArrayList<ListItem> getList() {
		return this.list;
	}

	public Date getDateCreated() {
		return this.created;
	}

	public Date getDateModified() {
		return this.modified;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setList(ArrayList<ListItem> list) {
		this.list = list;
	}

	public void setDateCreated(Date created) {
		this.created = created;
	}

	public void setDateModified(Date modified) {
		this.modified = modified;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	// model for list
	// may have list of items, author?, date created?, last modified?

	public String toString() {
		// This is ugly
		String ret = "";
		ret += name;
		ret += ";" + description;
		ret += ";";
		for (ListItem l : this.list) {
			ret += l.toString() + "/";
		}
		ret.substring(0, ret.length() - 1);
		ret += ";";
		ret += ";" + created;
		ret += ";" + modified;
		ret += ";" + author;
		return ret;
	}

	public List fromString(String s) {
		String[] splitted = s.split(";");
		ArrayList<ListItem> nList = new ArrayList<ListItem>();
		List ret = new List(splitted[0], splitted[1]);
		String[] lItems = splitted[2].split("/");
		for (String nItem : lItems) {
			String[] item = nItem.split(",");
			nList.add(new ListItem(item[0], Integer.parseInt(item[1]),
					new BigDecimal(item[2])));
		}
		ret.setDateCreated(new Date(splitted[3]));
		ret.setDateModified(new Date(splitted[4]));
		ret.setAuthor(splitted[5]);
		return ret;
	}
}
