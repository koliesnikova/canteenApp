package sk.upjs.paz1c.biznis;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.EntityUndeletableException;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

class DefaultCanteenManagerTest {
	
	private OrderDao orderDao;
	private FoodDao foodDao;
	private IngredientDao ingredientDao;
	private Order savedOrder;
	private Food savedFood1;
	private Food savedFood2;
	private Ingredient savedIngredient;
	private CanteenManager canteenManager;
	
	public DefaultCanteenManagerTest() {
		DaoFactory.INSTANCE.testing();
		orderDao = DaoFactory.INSTANCE.getOrderDao();
		foodDao = DaoFactory.INSTANCE.getFoodDao();
		ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
		canteenManager = new DefaultCanteenManager();
	}

	@BeforeEach
	void setUp() throws Exception {
		savedIngredient = ingredientDao.save(new Ingredient("test ingred. ", 5.5, "100 ml", 0));
		HashMap<Ingredient, Integer> ingredientsInFood = new HashMap<Ingredient, Integer>();
		ingredientsInFood.put(savedIngredient, 2);
		savedFood1 = foodDao.save(new Food("testCanteenManager1", "descript", "nope", 0.05, 100, ingredientsInFood ));
		savedFood2 = foodDao.save(new Food("testCanteenManager2"));
		Map<Food, Integer> portions = new HashMap<>();
		portions.put(savedFood1, 1);
		savedOrder = orderDao.save(new Order(LocalDateTime.of(2021, 12, 1, 0, 0), portions));
	}

	@AfterEach
	void tearDown() throws Exception {
		savedOrder.setPortions(null);
		orderDao.insertFoods(savedOrder);
		foodDao.delete(savedFood1.getId());
		foodDao.delete(savedFood2.getId());
		orderDao.delete(savedOrder.getId());
		
		ingredientDao.delete(savedIngredient.getId());
	}

	@Test
	void filterFoodNotInOrderTest() throws EntityUndeletableException {
		int count = canteenManager.filterFoodNotInOrder(savedOrder.getId()).size();
		savedOrder.getPortions().put(savedFood2, 1);
		orderDao.insertFoods(savedOrder);
		int movedFoodToOrder = canteenManager.filterFoodNotInOrder(savedOrder.getId()).size();
		assertEquals(count - 1, movedFoodToOrder);
		Food newFood = foodDao.save(new Food("TestCanteenManagerFood3"));
		int addedNewFood = canteenManager.filterFoodNotInOrder(savedOrder.getId()).size();
		assertEquals(count, addedNewFood);
		
		for (Food f : savedOrder.getPortions().keySet()) {
			boolean inAll = false;
			for (Food allFood : canteenManager.filterFoodNotInOrder(savedOrder.getId())) {
				if (f.getId().equals(allFood.getId())) {
					inAll = true;
					break;
				}
			}
			assertFalse(inAll);
		}
		
		int foodsInOrder = orderDao.getById(savedOrder.getId()).getPortions().size();
		int foodsNotInOrder = canteenManager.filterFoodNotInOrder(savedOrder.getId()).size();
		int allFoods = foodDao.getAll().size();
		assertEquals(foodsInOrder + foodsNotInOrder, allFoods);
		
		foodDao.delete(newFood.getId());
	}
	@Test
	void getAllToBuyTest() {
	List<ShoppingListItemOverview> all = canteenManager.getAllToBuy();
	int count = all.size();
	boolean found = false;
	for(ShoppingListItemOverview item : all) {
		if(item.getIngredient().getId().equals(savedIngredient.getId())) {
			found = true;
			int portionsOfFood = savedOrder.getPortions().get(savedFood1);
			int amoutNeeded = savedFood1.getIngredientsById().get(savedIngredient.getId())*portionsOfFood - savedIngredient.getAmountAvailiable();
			assertEquals(2, amoutNeeded);
		}
	}
	assertTrue(found);
	assertEquals(1, count);
	
	}


	@Test
	void getItemsForShoppingList() {
		LocalDateTime date = savedOrder.getDay();
		List<ShoppingListItemOverview> items = canteenManager.getItemsForShoppingList(date);
		assertEquals(1, items.size());
		//TODO test
	}

	@Test
	void getNumberOfToBuyTest() {
		int number = canteenManager.getNumberOfToBuy();
		assertEquals(canteenManager.getAllToBuy().size(), number);
	}

}
