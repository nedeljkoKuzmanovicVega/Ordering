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
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public ArrayList<UUID> getProductIds() {
		return productIds;
	}

	public void setProductIds(ArrayList<UUID> productIds) {
		this.productIds = productIds;
	}
	
	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
