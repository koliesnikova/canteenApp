package sk.upjs.paz1c.biznis;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

class OverviewManagerImplTest {
	
	private OrderDao orderDao;
	private FoodDao foodDao;
	private OverviewManager manager = new OverviewManagerImpl();
	private Order savedOrder;
	
	public OverviewManagerImplTest() {
		DaoFactory.INSTANCE.testing();
		orderDao = DaoFactory.INSTANCE.getOrderDao();
		foodDao = DaoFactory.INSTANCE.getFoodDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Map<Food, Integer> portions = new HashMap<>();
		for (Food f : foodDao.getAll()) {
			portions.put(f, 1);
		}
		savedOrder = orderDao.save(new Order(LocalDateTime.of(2021, 11, 30, 0, 0), portions));
		
	}

	@AfterEach
	void tearDown() throws Exception {
		savedOrder.setPortions(null);
		orderDao.insertFoods(savedOrder);
		orderDao.delete(savedOrder.getId());
	}

	@Test
	void testGetAll() {
		int count = manager.getAll(savedOrder.getId()).size();
		Food addedFood = foodDao.save(new Food("Prazenica"));
		savedOrder.getPortions().put(addedFood, 1);
		orderDao.save(savedOrder);
		assertEquals(count + 1, manager.getAll(savedOrder.getId()).size());
		savedOrder = orderDao.removeFood(savedOrder, addedFood);
		assertEquals(count, manager.getAll(savedOrder.getId()).size());
		
	}

}
