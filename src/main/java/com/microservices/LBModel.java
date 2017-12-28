package com.microservices;

public class LBModel {

	private Long id;

	private String applianceUuid;

	private String appliancePlatformName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplianceUuid() {
		return applianceUuid;
	}

	public void setApplianceUuid(String applianceUuid) {
		this.applianceUuid = applianceUuid;
	}

	public String getAppliancePlatformName() {
		return appliancePlatformName;
	}

	public void setAppliancePlatformName(String appliancePlatformName) {
		this.appliancePlatformName = appliancePlatformName;
	}

}
