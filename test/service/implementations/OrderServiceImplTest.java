package service.implementations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import core.dto.OrderDto;
import core.exceptions.*;
import core.interfaces.repositories.*;
import core.interfaces.services.PaymentService;
import core.models.*;

@RunWith(Suite.class)
@SuiteClasses({})
public class OrderServiceImplTest {
	private final UUID CUSTOMER_ID = UUID.fromString("1fc03087-d265-11e7-b8c6-83e29cd24f4c");
	private final ArrayList<UUID> PRODUCT_IDS = new ArrayList<UUID>() {
        private static final long serialVersionUID = 1L;

		{
            add(UUID.fromString("2fc03087-d265-11e7-b8c6-83e29cd24f4c"));
            add(UUID.fromString("3fc03087-d265-11e7-b8c6-83e29cd24f4c"));
            add(UUID.fromString("4fc03087-d265-11e7-b8c6-83e29cd24f4c"));
        }
    };
    private final Integer CARD_PAYMENT_METHOD = 1;
	private final Integer CASH_PAYMENT_METHOD = 2;
	private final Integer CRYPTO_PAYMENT_METHOD = 3;
	private final Integer INVALID_PAYMENT_METHOD = 6666666;
	private final UUID ORDER_ID = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c");

	private CustomerRepository customerRepositoryMock;
	private ProductRepository productRepositoryMock;
	private OrderRepository orderRepositoryMock;
	private OrderItemRepository orderItemRepositoryMock;
	private PaymentService paymentServiceMock;
	
	private OrderServiceImpl orderService;

	
	@BeforeEach
    public void setUp() {
		customerRepositoryMock = mock(CustomerRepository.class);
		productRepositoryMock = mock(ProductRepository.class);
		orderRepositoryMock = mock(OrderRepository.class);
		orderItemRepositoryMock = mock(OrderItemRepository.class);
		paymentServiceMock = mock(PaymentService.class);

    	orderService = new OrderServiceImpl(
    			customerRepositoryMock, 
    			productRepositoryMock, 
    			orderRepositoryMock,
    			orderItemRepositoryMock,
    			paymentServiceMock
        );
    }
	
	@Test
	public void processOrder_whenCustomerIdNotProvider_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		orderData.setCustomerId(null);
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null); 
	}
	
	@Test
	public void processOrder_whenProductIdsNotProvider_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		orderData.setProductIds(null);
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null); 
	}
	
	@Test
	public void processOrder_whenPaymentMethodNotProvider_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		orderData.setPaymentMethod(null);
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null); 
	}
	
	@Test
	public void processOrder_whenCustomerIdIsNotValid_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		when(customerRepositoryMock.findById(any(UUID.class))).thenReturn(null);
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null);
	}
	
	@Test
	public void processOrder_whenProductIdsIsEmpty_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		orderData.setProductIds(new ArrayList<UUID>());
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null);
	}
	
	@Test
	public void processOrder_whenAtLeastOneOfTheProductsIsNotValid_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		when(productRepositoryMock.findById(any(UUID.class))).thenReturn(null);
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null);
	}
	
	@Test
	public void processOrder_whenAtLeastOneOfTheProductsPriceIsLessThenZero_thenValidationExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		mockCustomer();
		Product product = new Product(getManufacturerMock(), -100, 10);
		when(productRepositoryMock.findById(any(UUID.class))).thenReturn(product);
		
		ValidationException thrownException = assertThrows(
				ValidationException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw ValidationException, but it didn't"
	 	    );
	 
		assertTrue(thrownException != null);
	}
	
	@Test
	public void processOrder_whenOrderDataIsValid_thenOrderIsSaved() throws ValidationException, PaymentMethodDoesNotExistException {
		OrderDto orderData = getOrderDataMock();
		mockCustomer();
		mockProducts();
		Order order = getOrderWithIdMock();
		mockOrder(order);
		
		Order orderId = orderService.processOrder(orderData);

		assertTrue(orderId != null);
    	assertTrue(ORDER_ID.equals(orderId.getId()));
    	
    	verify(orderRepositoryMock, times(1)).put(any(Order.class));
    	verify(orderItemRepositoryMock, times(1)).put(argThat(x -> x.size() == 3));
    	verify(orderItemRepositoryMock, times(1)).put(argThat(x -> verifyOrderItems(x)));
	}
	
	@Test
	public void processOrder_whenOrderPaymentMethodIsCard_thenCardPaymentProcessed() throws ValidationException, PaymentMethodDoesNotExistException {
		OrderDto orderData = getOrderDataMock();
		orderData.setPaymentMethod(CARD_PAYMENT_METHOD);
		mockCustomer();
		mockProducts();
		Order order = getOrderWithIdMock();
		mockOrder(order);
		
		orderService.processOrder(orderData);

		verify(paymentServiceMock, times(1)).processCardPayment(any());
		verify(paymentServiceMock, times(0)).processCashPayment(any());
		verify(paymentServiceMock, times(0)).processCryptoPayment(order);
	}
	
	@Test
	public void processOrder_whenOrderPaymentMethodIsCash_thenCashPaymentProcessed() throws ValidationException, PaymentMethodDoesNotExistException {
		OrderDto orderData = getOrderDataMock();
		orderData.setPaymentMethod(CASH_PAYMENT_METHOD);
		mockCustomer();
		mockProducts();
		Order order = getOrderWithIdMock();
		mockOrder(order);
		
		orderService.processOrder(orderData);

		verify(paymentServiceMock, times(0)).processCardPayment(any());
		verify(paymentServiceMock, times(1)).processCashPayment(any());
		verify(paymentServiceMock, times(0)).processCryptoPayment(order);
	}
	
	@Test
	public void processOrder_whenOrderPaymentMethodIsCrypto_thenCryptoPaymentProcessed() throws ValidationException, PaymentMethodDoesNotExistException {
		OrderDto orderData = getOrderDataMock();
		orderData.setPaymentMethod(CRYPTO_PAYMENT_METHOD);
		mockCustomer();
		mockProducts();
		Order order = getOrderWithIdMock();
		mockOrder(order);
		
		orderService.processOrder(orderData);

		verify(paymentServiceMock, times(0)).processCardPayment(any());
		verify(paymentServiceMock, times(0)).processCashPayment(any());
		verify(paymentServiceMock, times(1)).processCryptoPayment(order);
	}
	
	@Test
	public void processOrder_whenOrderPaymentMethodIsNotValid_thenPaymentMethodDoesNotExistExceptionThrown() {
		OrderDto orderData = getOrderDataMock();
		orderData.setPaymentMethod(INVALID_PAYMENT_METHOD);
		mockCustomer();
		mockProducts();
		Order order = getOrderWithIdMock();
		mockOrder(order);
		
		PaymentMethodDoesNotExistException thrownException = assertThrows(
				PaymentMethodDoesNotExistException.class,
	 	           () -> orderService.processOrder(orderData),
	 	           "Expected processOrder() to throw PaymentMethodDoesNotExistException, but it didn't"
	 	    );
	
		assertTrue(thrownException != null);
	}
	
	private OrderDto getOrderDataMock() {
		return new OrderDto(CUSTOMER_ID, PRODUCT_IDS, CARD_PAYMENT_METHOD);
	}
	
	private Manufacturer getManufacturerMock() {
		return new Manufacturer("manufacturerMoc", "manufacturer@mock.com");
	}
	
	private Customer getCustomerMock() {
		return new Customer("Customer name", "address", "city", "country", "test@customer.com", "+3338881722");
	}
	
	private void mockCustomer() {
		when(customerRepositoryMock.findById(any(UUID.class))).thenReturn(getCustomerMock());
	}
	
	private void mockProducts() {
		Product product1 = new Product(getManufacturerMock(), 100, 10);
		product1.setId(PRODUCT_IDS.get(0));
		when(productRepositoryMock.findById(same(PRODUCT_IDS.get(0)))).thenReturn(product1);
		
		Product product2 = new Product(getManufacturerMock(), 125, 0);
		product2.setId(PRODUCT_IDS.get(1));
		when(productRepositoryMock.findById(same(PRODUCT_IDS.get(1)))).thenReturn(product2);
		
		Product product3 = new Product(getManufacturerMock(), 200, 20);
		product3.setId(PRODUCT_IDS.get(2));
		when(productRepositoryMock.findById(same(PRODUCT_IDS.get(2)))).thenReturn(product3);
	}
	
	private Order getOrderMock() {
		return new Order(getCustomerMock());
	}
	
	private Order getOrderWithIdMock() {
		Order order = getOrderMock();
		order.setId(ORDER_ID);
		
		return order;
	}
	
	private void mockOrder(Order order) {
		when(orderRepositoryMock.put(any(Order.class))).thenReturn(order);
	}
	
	private boolean verifyOrderItems(ArrayList<OrderItem> actualOrderItems) {
		ArrayList<OrderItem> expectedOrderItems = new ArrayList<OrderItem>() {
			private static final long serialVersionUID = 1L;

			{
				add(new OrderItem(ORDER_ID, PRODUCT_IDS.get(0), new BigDecimal(108)));
				add(new OrderItem(ORDER_ID, PRODUCT_IDS.get(1), new BigDecimal(150)));
				add(new OrderItem(ORDER_ID, PRODUCT_IDS.get(2), new BigDecimal(192)));
			}
		};
		
		if (expectedOrderItems.size() != actualOrderItems.size()) {
			return false;
		}
		
		for (OrderItem orderItem : expectedOrderItems) {
			OrderItem actualOrderItem = actualOrderItems.stream().
					filter(item -> item.getProductId() == orderItem.getProductId())
					.findFirst().get();
			
			if (actualOrderItem == null || 
					actualOrderItem.getPriceAtOrderTime().compareTo(orderItem.getPriceAtOrderTime()) != 0 ||
					actualOrderItem.getOrderId() != orderItem.getOrderId()) {
				return false;
			}
		}
	
		return true;
	}
}
