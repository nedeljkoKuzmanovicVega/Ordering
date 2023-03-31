package core.interfaces.services;


import core.dto.OrderDto;
import core.exceptions.PaymentMethodDoesNotExistException;
import core.exceptions.ValidationException;
import core.models.Order;

public interface OrderService {
	public Order processOrder(OrderDto orderData) throws ValidationException, PaymentMethodDoesNotExistException;
}
