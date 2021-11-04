package sk.upjs.paz1c.main;

import java.util.List;

public interface IngredientDao {

	List<Ingredient> getAll(); 
	
	Ingredient save(Ingredient ingredient);
	
	Ingredient delete(long idIgredient);
}
