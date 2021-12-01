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
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

class DefaultCanteenManagerTest {
	
	private OrderDao orderDao;
	private FoodDao foodDao;
	private Order savedOrder;
	private Food savedFood1;
	private Food savedFood2;
	private CanteenManager canteenManager;
	
	public DefaultCanteenManagerTest() {
		DaoFactory.INSTANCE.testing();
		orderDao = DaoFactory.INSTANCE.getOrderDao();
		foodDao = DaoFactory.INSTANCE.getFoodDao();
		canteenManager = new DefaultCanteenManager();
	}

	@BeforeEach
	void setUp() throws Exception {
		savedFood1 = foodDao.save(new Food("testCanteenManager1"));
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
	}

	@Test
	void test() throws EntityUndeletableException {
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

}
