package sk.upjs.paz1c.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

class MysqlIngredientDaoTest {
	private IngredientDao ingredientDao;
	private Ingredient savedIngr;

	public MysqlIngredientDaoTest() {
		DaoFactory.INSTANCE.testing();
		ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Ingredient i = new Ingredient("testing", 0.01, "100 g");
		savedIngr = ingredientDao.save(i);
	}

	@AfterEach
	void tearDown() throws Exception {
		ingredientDao.delete(savedIngr.getId());
	}

	@Test
	void testGetAll() {
		// vsetko zbehlo ok
		List<Ingredient> ingredients = ingredientDao.getAll();
		assertNotNull(ingredients);
		assertTrue(ingredients.size() > 0);
		System.out.println(ingredients);
	}

	@Test
	void testGetById() {
		// vsetko zbehlo ok
		Ingredient byId = ingredientDao.getById(savedIngr.getId());
		assertEquals(savedIngr.getName(), byId.getName());
		assertEquals(savedIngr.getPrice().toString(), byId.getPrice().toString());
		assertEquals(savedIngr.getId(), byId.getId());
		assertEquals(savedIngr.getAmount(), byId.getAmount());
		assertEquals(savedIngr.getAmountAvailiable(), byId.getAmountAvailiable());
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				ingredientDao.getById(-1L);
			}
		});
	}

//	@Test
//	void testSave() {
//		
//		fail("Not yet implemented");
//	}
//	
	@Test
	void testDelete() throws EntityUndeletableException {
		Ingredient ingredientToDelete = new Ingredient("delete", 0.90, "8 L");
		Ingredient saved = ingredientDao.save(ingredientToDelete);
		Ingredient saved2 = ingredientDao.delete(saved.getId());
		assertEquals(saved, saved2);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				ingredientDao.getById(saved.getId());
			}
		});
		// TODO otestovat ked pridame ingredienciu do jedla
//		assertThrows(EntityUndeletableException.class, new Executable() {
//			@Override
//			public void execute() throws Throwable {
//				ingredientDao.delete(1L);
//			}
//		});
//	}

	}
}
