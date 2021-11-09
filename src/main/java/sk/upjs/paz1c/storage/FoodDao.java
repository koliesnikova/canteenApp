package sk.upjs.paz1c.storage;

import java.util.List;

public interface FoodDao {
	List<Food> getAll();

	Food save(Food food);

	Food delete(long idFood);

	Food getById(long idFood);

}
