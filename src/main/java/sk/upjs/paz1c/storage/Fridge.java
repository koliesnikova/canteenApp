package sk.upjs.paz1c.main;

import java.util.Map;

public class Fridge {
	
	private Map<Ingredient, Integer> ingredientsAvailable;
	
	public Fridge(Map<Ingredient, Integer> ingredientsAvailable) {
		this.ingredientsAvailable = ingredientsAvailable;
	}

	public Map<Ingredient, Integer> getIngredientsAvailable() {
		return ingredientsAvailable;
	}

	public void setIngredientsAvailable(Map<Ingredient, Integer> ingredientsAvailable) {
		this.ingredientsAvailable = ingredientsAvailable;
	}
	
	

}
