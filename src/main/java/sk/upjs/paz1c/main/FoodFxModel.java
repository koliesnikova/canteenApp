package sk.upjs.paz1c.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

public class FoodFxModel {

	private Long id;
	private StringProperty name = new SimpleStringProperty();
	private DoubleProperty price = new SimpleDoubleProperty();
	private IntegerProperty weight = new SimpleIntegerProperty();
	private StringProperty imagePath = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private List<String> ingredients = new ArrayList<String>();
	private List<Ingredient> ingredientsInFood = new ArrayList<Ingredient>();
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

	public FoodFxModel() {

	}

	public FoodFxModel(Food food) {
		this.id = food.getId();
		setName(food.getName());
		setPrice(food.getPrice());
		setWeight(food.getWeight());
		setImagePath(food.getImage_url());
		setDescription(food.getDescription());
		setIngredients(ingredientDao.getAllNames()); 
		
		Set<Ingredient> foodIngrs = food.getIngredients().keySet();
		for (Ingredient ingredient : foodIngrs) {
			ingredientsInFood.add(ingredient);
		}
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String string) {
		this.name.set(string);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public Double getPrice() {
		return price.get();
	}

	public void setPrice(Double double1) {
		this.price.set(double1);
	}

	public DoubleProperty priceProperty() {
		return price;
	}
	public Integer getWeight() {
		return weight.get();
	}

	public void setWeight(Integer integer) {
		this.weight.set(integer);
	}
	
	public IntegerProperty weightProperty() {
		return weight;
	}

	public String getImagePath() {
		return imagePath.get();
	}

	public void setImagePath(String string) {
		this.imagePath.set(string);
	}
	
	public StringProperty imagePathProperty() {
		return imagePath;
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String string) {
		this.description.set(string);
	}
	public StringProperty descriptionProperty() {
		return description;
	}

	public void setIngredients(List<String> names) {
		this.ingredients = names;
	}
	public List<String> getIngredients() {
		return ingredients;
	}
	
	public List<Ingredient> getIngredientsInFood() {
		return ingredientsInFood;
	}


// vratit mapu ingrediencii vo formate, ako ju ma food
//	public Map<Long, Integer> getIngredientsMap() {
//		Map<Long, Integer> map = new HashMap<Long, Integer>();
//		List<Ingredient> allIngrs = ingredientDao.getAll();
//		for (Ingredient ingr : allIngrs) {
//			if (this.ingredients.contains(ingr.getName())) {
//				// map.put(ingr.getId(), amountNeeded.)
//			}
//		}
//
//	}

//	public Food getFood() {
//		//TODO
//		return new Food(id, name, description, imagePath, price, weight, );
//
//	}

}
