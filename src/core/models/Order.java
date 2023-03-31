package core.models;

import java.util.UUID;

public class Order {
	private UUID id;
	private Customer customer;
	
	public Order(Customer customer) {
		this.customer = customer;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
