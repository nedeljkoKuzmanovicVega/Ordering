package core.dto;

import java.util.ArrayList;
import java.util.UUID;

public class OrderDto {
	public UUID customerId;
	public ArrayList<UUID> productIds;
	public Integer paymentMethod;
	
	public OrderDto(UUID customerId, ArrayList<UUID> productIds, Integer paymentMethod) {
		this.customerId = customerId;
		this.productIds = productIds;
		this.paymentMethod = paymentMethod;
	}
	
	public UUID getCustomerId() {
		return this.customerId;
	}
	
	public ArrayList<UUID> getProductIds() {
		return this.productIds;
	}
	
	public Integer getPaymentMethod() {
		return this.paymentMethod;
	}
}
