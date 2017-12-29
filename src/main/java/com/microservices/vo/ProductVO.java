package com.microservices.vo;

import java.util.ArrayList;
import java.util.List;

public class ProductVO {
	private String productId;
	private String name;
	private String desc;
	private List<AllergenVO> allergens;
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
	public List<AllergenVO> getAllergens() {
		if(this.allergens == null) {
			this.allergens = new ArrayList<>();
		}
		return allergens;
	}
	public void setAllergens(List<AllergenVO> allergens) {
		this.allergens = allergens;
	}

}
