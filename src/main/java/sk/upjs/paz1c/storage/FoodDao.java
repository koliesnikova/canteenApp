package sk.upjs.paz1c.storage;

import java.util.List;
import java.util.Map;

public interface FoodDao {
	List<Food> getAll();

	Food save(Food food) throws EntityNotFoundException;

	Food delete(long idFood) throws EntityUndeletableException;

	Food getById(long idFood) throws EntityNotFoundException;
	
	Map<Ingredient, Integer> saveIngredient(Food food, Ingredient ingredient, Integer amount) throws EntityNotFoundException;
	
	Food deleteIngredient (Food food, Ingredient ingredient);

}
