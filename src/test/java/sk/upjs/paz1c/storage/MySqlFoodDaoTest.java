package sk.upjs.paz1c.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MySqlFoodDaoTest {

	private Food savedFood;
	private FoodDao foodDao;

	public MySqlFoodDaoTest() {
		DaoFactory.INSTANCE.testing();
		foodDao = DaoFactory.INSTANCE.getFoodDao();
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Food food = new Food("test food", "made for testing", "none", 10.99, 1);
		savedFood = foodDao.save(food);

	}

	@AfterEach
	void tearDown() throws Exception {
		foodDao.delete(savedFood.getId());
	}

	@Test
	void testGetAll() {
		// OK
		List<Food> foods = foodDao.getAll();
		assertNotNull(foods);
		assertTrue(foods.size() > 0);
		System.out.println(foods); // divne IDcka?? pozriet
	}

//	@Test
//	void testSave() {
//		fail("Not yet implemented");
//	}
//	
	@Test
	void testGetById() {
		// OK
		Food byId = foodDao.getById(savedFood.getId());
		assertEquals(savedFood, byId);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				foodDao.getById(-1L);
			}
		});
	}

	@Test
	void testDelete() throws EntityUndeletableException {
		//OK
		Food foodToDelete = new Food("delete", "food to be deleted", "idk", 5.00, 100);
		Food saved = foodDao.save(foodToDelete);
		Food savedToDelete = foodDao.delete(saved.getId());
		assertEquals(saved, savedToDelete);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				foodDao.getById(saved.getId());
			}
		});
		// TODO otestovat ked pridame jedlo do order
//		assertThrows(EntityUndeletableException.class, new Executable() {
//			@Override
//			public void execute() throws Throwable {
//				foodDao.delete(1L);
//			}
//		});
//	}
	}

}
