package sk.upjs.paz1c.biznis;

import sk.upjs.paz1c.storage.Ingredient;

public class ShoppingListItemOverview {
	private Ingredient ingredient;
	private int toBuy = 0;
	
	public ShoppingListItemOverview(Ingredient ingredient, int toBuy){
		this.ingredient = ingredient;
		this.toBuy = toBuy;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public int getToBuy() {
		return toBuy;
	}
	public void setToBuy(int toBuy) {
		this.toBuy = toBuy;
	}

	@Override
	public String toString() {
		return "ingredient = " + ingredient + ", toBuy = " + toBuy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ingredient == null) ? 0 : ingredient.hashCode());
		result = prime * result + toBuy;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingListItemOverview other = (ShoppingListItemOverview) obj;
		if (ingredient == null) {
			if (other.ingredient != null)
				return false;
		} else if (!ingredient.equals(other.ingredient))
			return false;
		if (toBuy != other.toBuy)
			return false;
		return true;
	}
	
	
	

}
