package core.interfaces.repositories;

import java.util.UUID;

import core.models.Product;

public interface ProductRepository {
	public Product findById(UUID id);
}
