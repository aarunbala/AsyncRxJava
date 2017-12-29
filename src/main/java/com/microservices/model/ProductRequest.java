package com.microservices.model;

import java.util.List;

public class ProductRequest {
	private List<String> productIds;
	private List<String> profiles;
	private String storeId;
	
	public ProductRequest() {
		super();
	}
	
	public ProductRequest(List<String> productIds, List<String> profiles, String storeId) {
		super();
		this.productIds = productIds;
		this.profiles = profiles;
		this.storeId = storeId;
	}

	public List<String> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}
	public List<String> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
}
