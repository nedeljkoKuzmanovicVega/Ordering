package core.interfaces.repositories;

import java.util.UUID;

import core.models.Customer;

public interface CustomerRepository {
	public Customer findById(UUID id);
}
