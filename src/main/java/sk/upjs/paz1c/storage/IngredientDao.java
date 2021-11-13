package sk.upjs.paz1c.storage;

import java.util.List;

public interface IngredientDao {

	List<Ingredient> getAll(); 
	
	Ingredient save(Ingredient ingredient) throws EntityNotFoundException;
	
	Ingredient delete(long idIgredient) throws EntityUndeletableException;
	
	Ingredient getById(long idIngredient) throws EntityNotFoundException;

}
