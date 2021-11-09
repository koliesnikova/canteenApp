package sk.upjs.paz1c.storage;

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

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((description == null) ? 0 : description.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + ((image_url == null) ? 0 : image_url.hashCode());
//		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		long temp;
//		temp = Double.doubleToLongBits(price);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
//		result = prime * result + weight;
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Food other = (Food) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (image_url == null) {
			if (other.image_url != null)
				return false;
		} else if (!image_url.equals(other.image_url))
			return false;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.equals(other.ingredients))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
	
	
	
	
	
	

}
