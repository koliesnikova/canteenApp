package sk.upjs.paz1c.main;

public class Food {
	private String name;
	private String description;
	private String image_url;
	private double price;
	private int weight; //in grams
	
	public Food(String name, String description, String image_url, double price, int weight) {
		this.name = name;
		this.description = description;
		this.image_url = image_url;
		this.price = price;
		this.weight = weight;
	}
	
	public Food(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "Food [name=" + name + ", description=" + description + ", price=" + price + ", weight=" + weight + "]";
	}
	
	
	
	

}
