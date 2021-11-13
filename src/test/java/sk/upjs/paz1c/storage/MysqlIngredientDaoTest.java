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
		assertEquals(savedIngr, byId);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				ingredientDao.getById(-1L);
			}
		});
	}

	@Test
	void testSave() throws EntityUndeletableException {
		//OK
		//INSERT
		int initialSize = ingredientDao.getAll().size();
		Ingredient newIngr = new Ingredient("TestOfSave", 0.5, "3");
		Ingredient savedNewIngr = ingredientDao.save(newIngr);
		//assertTrue(savedNewIngr.equals(newIngr)); - can not be used here, newIngr does not have ID yet
		assertEquals(savedNewIngr.getName(), newIngr.getName());
		assertEquals(savedNewIngr.getAmount(), newIngr.getAmount());
		assertEquals(savedNewIngr.getPrice(), newIngr.getPrice());
		assertEquals(savedNewIngr.getAmountAvailiable(), newIngr.getAmountAvailiable());
		assertNotNull(savedNewIngr.getId());
		
		List<Ingredient> all = ingredientDao.getAll();
		assertEquals(initialSize + 1, all.size());

		boolean found = false;
		for(Ingredient i : all) {
			if (i.getId().equals(savedNewIngr.getId())) {
				found = true;
				assertEquals("TestOfSave", i.getName());
				assertEquals(0.5, i.getPrice());
				assertEquals("3", i.getAmount());
				assertEquals(0, i.getAmountAvailiable());
				break;
			}
		}
		assertTrue(found);	
		ingredientDao.delete(savedNewIngr.getId());
		
		//UPDATE
		Ingredient changedIngr = new Ingredient(savedIngr.getId(), "Change", 8.8, "8 kg", 6);
		Ingredient savedChangedIngr = ingredientDao.save(changedIngr);
		assertEquals("Change", savedChangedIngr.getName());
		assertEquals(8.8, savedChangedIngr.getPrice());
		assertEquals("8 kg", savedChangedIngr.getAmount());
		assertEquals(6, savedChangedIngr.getAmountAvailiable());
		assertEquals(savedChangedIngr.getId(), changedIngr.getId());
		
		all = ingredientDao.getAll();
		found = false;
		for (Ingredient i : all) {
			if (i.getId().equals(changedIngr.getId())) {
				found = true;
				assertEquals("Change", i.getName());
				assertEquals(8.8, i.getPrice());
				assertEquals("8 kg", i.getAmount());
				assertEquals(6, i.getAmountAvailiable());
				break;
			}
		}
		assertTrue(found);
		changedIngr.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				ingredientDao.save(changedIngr);
			}
		});
		assertThrows(NullPointerException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				ingredientDao.save(null);
			}
		});
	}
	
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
