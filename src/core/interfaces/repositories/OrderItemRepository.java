package core.interfaces.repositories;

import java.util.ArrayList;

import core.models.OrderItem;

public interface OrderItemRepository {
	public void put(ArrayList<OrderItem> orderItems);
}
