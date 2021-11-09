package sk.upjs.paz1c.storage;

public class Ingredient {
	private Long id;
	private String name;
	private Double price;
	private String amount; //extract number + unit
	private String amountAvailiable;
	
	
	public Ingredient(Long id, String name, double price, String amount, String amountAvailiable) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.amountAvailiable = amountAvailiable;
	}
	

	public Ingredient(String name, double price, String amount) {
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.amountAvailiable = "0";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getAmountAvailiable() {
		return amountAvailiable;
	}

	public void setAmountAvailiable(String amountAvailiable) {
		this.amountAvailiable = amountAvailiable;
	}


	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name + ", price=" + price + ", amount=" + amount
				+ ", amountAvailiable=" + amountAvailiable + "]";
	}
	
	
}
