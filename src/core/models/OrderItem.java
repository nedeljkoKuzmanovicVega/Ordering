package core.models;

import java.util.UUID;

public class OrderItem {
	private UUID id;
	private UUID orderId;
	private UUID productId;
	private float priceAtOrderTime;
	
	public OrderItem(UUID orderId, UUID productId, float priceAtOrderTime) {
		this.orderId = orderId;
		this.productId = productId;
		this.priceAtOrderTime = priceAtOrderTime;
	}
	
	public UUID getId() {
		return id;
	}
	
	public UUID getOrderId() {
		return this.orderId;
	}

	public UUID getProductId() {
		return productId;
	}

	public float getPriceAtOrderTime() {
		return priceAtOrderTime;
	}

}
