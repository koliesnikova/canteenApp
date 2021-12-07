package sk.upjs.paz1c.biznis;

import sk.upjs.paz1c.storage.Ingredient;

public class ShoppingListItemOverview {
	private Ingredient ingredient;
	private int toBuy = 0;
	
	ShoppingListItemOverview(Ingredient ingredient, int toBuy){
		this.ingredient = ingredient;
		this.toBuy = toBuy;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public int getToBuy() {
		return toBuy;
	}

	@Override
	public String toString() {
		return "ingredient = " + ingredient + ", toBuy = " + toBuy;
	}
	

}
