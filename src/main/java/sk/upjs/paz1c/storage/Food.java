package sk.upjs.paz1c.main;

import java.util.HashMap;
import java.util.Map;

public class Food {
	private Long id;
	private String name;
	private String description;
	private String image_url;
	private double price;
	private int weight; //in grams
	
	private Map<Ingredient, Integer> ingredients;
	
	public Food(String name, String description, String image_url, double price, int weight,
			Map<Ingredient, Integer> ingredients) {
		super();
		this.name = name;
		this.description = description;
		this.image_url = image_url;
		this.price = price;
		this.weight = weight;
		this.ingredients = ingredients;
	}

	public Food(String name, String description, String image_url, double price, int weight) {
		this.name = name;
		this.description = description;
		this.image_url = image_url;
		this.price = price;
		this.weight = weight;
		this.ingredients = new HashMap<>();
	}
	
	public Food(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Map<Ingredient, Integer> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Map<Ingredient, Integer> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Food [id=" + id + ", name=" + name + ", description=" + description + ", image_url=" + image_url
				+ ", price=" + price + ", weight=" + weight + ", ingredients=" + ingredients + "]";
	}
	
	
	
	
	
	

}
