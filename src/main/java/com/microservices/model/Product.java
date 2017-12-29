package com.microservices.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Product implements Comparable<Product>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String productId;
	private String name;
	private String desc;
	
	public Product() {
		super();
	}
	
	public Product(String productId, String name, String desc) {
		super();
		this.productId = productId;
		this.name = name;
		this.desc = desc;
	}

	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int compareTo(Product o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", desc=" + desc + "]";
	}
	
	
}
