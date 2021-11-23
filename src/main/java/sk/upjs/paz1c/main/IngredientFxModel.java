package sk.upjs.paz1c.main;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.paz1c.storage.Ingredient;

public class IngredientFxModel {
	
	private Long id;
	private String unit;
	private StringProperty name = new SimpleStringProperty();
	private DoubleProperty price = new SimpleDoubleProperty();
	private StringProperty amount = new SimpleStringProperty();
	private IntegerProperty amountAvailable = new SimpleIntegerProperty();
	
	public IngredientFxModel() {
		
	}
	
	public IngredientFxModel(Ingredient ingredient) {
		this.id = ingredient.getId();
		this.unit = ingredient.getAmount().split(" ")[1];
		setName(ingredient.getName());
		setPrice(ingredient.getPrice());
		setAmount(ingredient.getAmount());
		setAmountAvailable(ingredient.getAmountAvailiable());
	}

	public Long getId() {
		return id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty nameProperty() {
		return name;
	}

	public Double getPrice() {
		return price.get();
	}

	public void setPrice(Double price) {
		this.price.set(price);
	}
	
	public DoubleProperty priceProperty() {
		return price;
	}

	public String getAmount() {
		return amount.get();
	}

	public void setAmount(String amount) {
		this.amount.set(amount.split(" ")[0]);
	}
	
	public StringProperty amountProperty() {
		return amount;
	}

	public Integer getAmountAvailable() {
		return amountAvailable.get();
	}

	public void setAmountAvailable(Integer amountAvailable) {
		this.amountAvailable.set(amountAvailable);
	}
	
	public IntegerProperty amountAvailableProperty() {
		return amountAvailable;
	}
	
	public Ingredient getIngredient() {
		return new Ingredient(id, getName(), getPrice(), getAmount() + " " + unit, getAmountAvailable());
	}


}
