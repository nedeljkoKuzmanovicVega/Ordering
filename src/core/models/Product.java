package core.models;

import java.util.UUID;

public class Product {
	private UUID id;
	public Manufacturer manufacturer;
	public float price;
	public float discount;
	
	public Product(Manufacturer manufacturer, float price, float discount) {
		super();
		this.manufacturer = manufacturer;
		this.price = price;
		this.discount = discount;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	public float getPrice() {
		return this.price;
	}
	
	public float getDiscount() {
		return this.discount;
	}
}
