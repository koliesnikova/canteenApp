package sk.upjs.paz1c.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MySqlOrderDaoTest {
	
	private OrderDao orderDao;
	private FoodDao foodDao;
	private Order savedOrder;
	
	public MySqlOrderDaoTest() {
		DaoFactory.INSTANCE.testing();
		orderDao = DaoFactory.INSTANCE.getOrderDao();
		foodDao = DaoFactory.INSTANCE.getFoodDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Map<Food, Integer> portions = new HashMap<>();
		//portions.put(DaoFactory.INSTANCE.getFoodDao().getById(7L), 3);
		Order order = new Order(LocalDateTime.of(2021, 1, 2, 0, 0), false, portions);
		savedOrder = orderDao.save(order);
	}

	@AfterEach
	void tearDown() throws Exception {
		// len pre test lebo som chcel mazat order co ma v sebe nejake jedlo
//		assertThrows(EntityUndeletableException.class, new Executable() {
//			
//			@Override
//			public void execute() throws Throwable {
//				orderDao.delete(savedOrder.getId());
//			}
//		});
		orderDao.delete(savedOrder.getId());
	}

	@Test
	void testGetAll() throws EntityUndeletableException {
		List<Order> orders = orderDao.getAll();
		int size = orders.size();
		Order o = orderDao.save(new Order(LocalDateTime.of(2021, 11, 20, 0, 0)));
		assertEquals(size + 1, orderDao.getAll().size());
		assertNotNull(orders);
		assertTrue(size > 0);
		orderDao.delete(o.getId());
	}
	
	@Test
	void testGetByPrepared() {
		List<Order> orders = orderDao.getByPrepared(true);
		int size = orders.size();
		Order o = new Order(LocalDateTime.of(2021, 11, 20, 0, 0));
		o.setPrepared(true);
		o = orderDao.save(o);
		assertEquals(size + 1 , orderDao.getByPrepared(true).size());
		o = new Order(LocalDateTime.of(2021, 11, 20, 0, 0));
		o = orderDao.save(o);
		assertEquals(size + 1, orderDao.getByPrepared(true).size());
		
	}
	
	@Test
	void testDelete() {
		Order newOrder = orderDao.save(new Order(LocalDateTime.of(21, 11, 25, 0, 0)));
		int size = orderDao.getAll().size();
		try {
			orderDao.delete(newOrder.getId());
		} catch (EntityUndeletableException e) {
			e.printStackTrace();
		}
		assertEquals(size - 1, orderDao.getAll().size());
	}
	
	@Test
	void testGetById() {
		Order o = orderDao.getById(savedOrder.getId());
		assertTrue(o.equals(savedOrder));
		
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				orderDao.getById(-1L);
			}
		});
	}
	
	@Test
	void testGetByDay() {
		Order o = orderDao.getByDay(savedOrder.getDay());
		assertTrue(o.equals(savedOrder));
		
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				orderDao.getById(-1L);
			}
		});
		
	}
	
	@Test
	void testInsert() throws EntityUndeletableException {
		int initialSize = orderDao.getAll().size();
		Order newOrder = new Order(LocalDateTime.of(21, 11, 25, 0, 0), true, new HashMap<>());
		Order savedNewOrder = orderDao.save(newOrder);
		assertEquals(LocalDateTime.of(21, 11, 25, 0, 0), savedNewOrder.getDay());
		assertTrue(savedNewOrder.isPrepared());
		assertEquals(0, savedNewOrder.getPortions().size());
		assertNotNull(savedNewOrder.getId());
		List<Order> all = orderDao.getAll();
		assertEquals(initialSize + 1, all.size());

		boolean found = false;
		for(Order order : all) {
			if (order.getId().equals(savedNewOrder.getId())) {
				found = true;
				assertEquals(LocalDateTime.of(21, 11, 25, 0, 0), savedNewOrder.getDay());
				assertEquals(true, savedNewOrder.isPrepared());
				assertEquals(0, savedNewOrder.getPortions().size());
				break;
			}
		}
		assertTrue(found);	
		orderDao.delete(savedNewOrder.getId());
		
		Order newOrder2 = new Order(-1L, LocalDateTime.now(), found, null);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				orderDao.save(newOrder2);
			}
		});
	}
	
	@Test
	void testUpdate() {
		Order changedOrder = new Order(savedOrder.getId(), LocalDateTime.of(15, 11, 25, 0, 0), true, new HashMap<>());
		Order savedChangedOrder = orderDao.save(changedOrder);
		assertEquals(changedOrder.getId(), savedChangedOrder.getId());
		assertEquals(LocalDateTime.of(15, 11, 25, 0, 0), savedChangedOrder.getDay());
		assertTrue(savedChangedOrder.isPrepared());
		assertEquals(0, savedChangedOrder.getPortions().size());
		
		List<Order> all = orderDao.getAll();
		boolean found = false;
		for(Order order : all) {
			if (order.getId().equals(changedOrder.getId())) {
				found = true;
				assertEquals(LocalDateTime.of(15, 11, 25, 0, 0), savedChangedOrder.getDay());
				assertTrue(savedChangedOrder.isPrepared());
				assertEquals(0, savedChangedOrder.getPortions().size());
				break;
			}
		}
		assertTrue(found);
		changedOrder.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				orderDao.save(changedOrder);
			}
		});
		
		assertThrows(NullPointerException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				orderDao.save(null);
			}
		});
	}
	
	@Test
	void testInsertFoods() {
		List<Food> foods = foodDao.getAll();
		Map<Food, Integer> portions = new HashMap<Food, Integer>();
		for (Food f : foods) {
			portions.put(f, (int)(Math.random() * 100));
		}
		orderDao.insertFoods(savedOrder);
		Order orderWithNewFoods = orderDao.getById(savedOrder.getId());
		
		for (Food f : orderWithNewFoods.getPortions().keySet()) {
			assertEquals(portions.get(f), orderWithNewFoods.getPortions().get(f));
		}
		
		savedOrder.setPortions(null);
		orderDao.insertFoods(savedOrder);
		orderWithNewFoods = orderDao.getById(savedOrder.getId());
		assertNotNull(orderWithNewFoods.getPortions());
		assertEquals(0, orderWithNewFoods.getPortions().keySet().size());
	}
	
	@Test
	void testRemoveFood() {
		Food f = new Food("pecene stehno");
		Food f2 = new Food("ostro-kysla polievka");
		f = foodDao.save(f);
		f2 = foodDao.save(f2);
		Map<Food, Integer> portions = new HashMap<>();
		portions.put(f, 4);
		portions.put(f2, 5);
		Order order = new Order(LocalDateTime.of(15, 11, 25, 0, 0), portions);
		Order newSaved = orderDao.save(order);
		int countOfFoods = newSaved.getPortions().size();
		newSaved = orderDao.removeFood(newSaved, f2);
		assertEquals(countOfFoods - 1, newSaved.getPortions().size());
	}
	
	
}
