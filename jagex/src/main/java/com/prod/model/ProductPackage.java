package com.prod.model;

import java.util.List;

public class ProductPackage {

	private String id;

	private String name;

	private String description;

	private List<Product> products;

	private int totalPrice;

	public ProductPackage(String id, String name, String description, List<Product> products, int totalPrice) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.products = products;
		this.totalPrice = totalPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
