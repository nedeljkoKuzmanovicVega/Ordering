package core.interfaces.services;

import core.models.Order;

public interface PaymentService {
	public void processCardPayment(Order order);
	public void processCashPayment(Order order);
	public void processCryptoPayment(Order order);
}
