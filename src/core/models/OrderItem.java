package core.models;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem {
	private UUID id;
	private UUID orderId;
	private UUID productId;
	private BigDecimal priceAtOrderTime;
	
	public OrderItem(UUID orderId, UUID productId, BigDecimal priceAtOrderTime) {
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

	public BigDecimal getPriceAtOrderTime() {
		return priceAtOrderTime;
	}

}
