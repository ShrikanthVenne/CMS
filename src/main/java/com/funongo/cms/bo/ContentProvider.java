package com.funongo.cms.bo;

import java.util.Date;

public class ContentProvider {
	
	private long id;
	
	private String name;
	
	private String companyName;
	
	private String address;
	
	private Date contractStartDate;
	
	private Date contractEndDate;

	public final long getId() {
		return id;
	}

	public final void setId(long id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getCompanyName() {
		return companyName;
	}

	public final void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final Date getContractStartDate() {
		return contractStartDate;
	}

	public final void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public final Date getContractEndDate() {
		return contractEndDate;
	}

	public final void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}


}
