package sk.upjs.paz1c.storage;

import java.util.List;
import java.util.Map;

public interface FoodDao {
	List<Food> getAll();

	Food save(Food food);

	Food delete(long idFood) throws EntityUndeletableException;

	Food getById(long idFood);
	
	Map<Ingredient, Integer> addIngredient(Ingredient ingredient, Integer amount);

}
