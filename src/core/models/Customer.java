package core.models;

import java.util.UUID;

public class Customer {
	private UUID id;
	private String fullName;
	private String address;
	private String city;
	private String country;
	private String email;
	private String mobileNumber;
	
	public Customer(String fullName, String address, String city, String country, String email,
			String mobileNumber) {
		super();
		this.fullName = fullName;
		this.address = address;
		this.city = city;
		this.country = country;
		this.email = email;
		this.mobileNumber = mobileNumber;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getFullName() {
		return this.fullName;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getMobileNumber() {
		return this.mobileNumber;
	}
}
