package com.example.scanitgrocerystorehelper.models;

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
}
