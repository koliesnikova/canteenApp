package sk.upjs.paz1c.main;

public class Ingredient {
	private Long id;
	private String name;
	private double price;
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

	public double getPrice() {
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

	public void setAmountAvailiable(String amount) {
		this.amountAvailiable = amountAvailiable;
	}
	

}
