package core.interfaces.services;

import core.dto.OrderDto;
import core.exceptions.PaymentMethodDoesNotExistException;
import core.exceptions.ValidationException;

public interface OrderService {
	public void processOrder(OrderDto orderData) throws ValidationException, PaymentMethodDoesNotExistException;
}
