package service.implementations;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.ArrayList;

import core.dto.OrderDto;
import core.exceptions.PaymentMethodDoesNotExistException;
import core.exceptions.ValidationException;
import core.interfaces.repositories.CustomerRepository;
import core.interfaces.repositories.OrderItemRepository;
import core.interfaces.repositories.OrderRepository;
import core.interfaces.repositories.ProductRepository;
import core.interfaces.services.OrderService;
import core.interfaces.services.PaymentService;
import core.models.Customer;
import core.models.Order;
import core.models.OrderItem;
import core.models.Product;

public class OrderServiceImpl implements OrderService {
	
	private CustomerRepository customerRepository;
	private ProductRepository productRepository;
	private OrderRepository orderRepository;
	private OrderItemRepository orderItemRepository;
	private PaymentService paymentService;
	
	public OrderServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository ,PaymentService paymentService) {
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.paymentService = paymentService;
	}

	public Order processOrder(OrderDto orderData) throws ValidationException, PaymentMethodDoesNotExistException {
		if (orderData.getCustomerId() != null && orderData.getProductIds() != null && orderData.getPaymentMethod() != null) {
			if (this.customerRepository.findById(orderData.getCustomerId()) != null) { // validate customer

				if (!orderData.getProductIds().isEmpty()) {// validate products 

					for (UUID productId : orderData.getProductIds() ) { // validate each product

						if (productId != null && this.productRepository.findById(productId) == null) { // check if product exists
							
							throw new ValidationException("Product with id: " + 1 + " does not exist");
						} else if (this.productRepository.findById(productId).getPrice() < 0) {
							throw new ValidationException("Product price with id: " + 1 + " does is not valid");
						}
					}
					
					// save new order
					Customer cst = this.customerRepository.findById(orderData.getCustomerId());
					Order or = this.orderRepository.put(new Order(cst));

					// save order items
					ArrayList<OrderItem> orderItems = new ArrayList<>();
					for (UUID productId : orderData.getProductIds()) {
						Product p = this.productRepository.findById(productId);
						float dsc = p.getDiscount();
						BigDecimal pr;
						
						if (dsc > 0) { // calculate with discount if exists
							pr = new BigDecimal(p.getPrice() - (p.getPrice() * p.getDiscount() / 100));
						} else {
							pr = new BigDecimal(p.getPrice());
						}
						
						// Adding the tax of 20%;
						BigDecimal t = new BigDecimal(20);
						pr = pr.add(pr.multiply(t.divide(new BigDecimal(100))));
						
						orderItems.add(new OrderItem(or.getId(), p.getId(), pr));
					}
					
					orderItemRepository.put(orderItems);

					// process the payment
					switch (orderData.getPaymentMethod()) {
						case 1:
							this.paymentService.processCardPayment(or);
							break;
						case 2:
							this.paymentService.processCashPayment(or);
							break;
						case 3:
							this.paymentService.processCryptoPayment(or);
							break;
						default:
							throw new PaymentMethodDoesNotExistException();
					}
					
					return or;
				} else {
					throw new ValidationException("Products not provided");
				}
			} else {
				throw new ValidationException("Customer not valid");
			}
		} else {
			throw new ValidationException("Order data is not complete");
		}
	}

}
