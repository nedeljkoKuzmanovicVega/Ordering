package core.models;

import java.util.UUID;

public class Manufacturer {
	private UUID id;
	private String name;
	private String email;
	
	public Manufacturer(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
}
