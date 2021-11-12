package sk.upjs.paz1c.storage;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {

	INSTANCE;

	private JdbcTemplate jdbcTemplate;
	private boolean testing = false;
	private IngredientDao ingredientDao;
	private FoodDao foodDao;

	public void testing() {
		testing = true;
	}

	public IngredientDao getIngredientDao() {
		if (ingredientDao == null) {
			ingredientDao = new MysqlIngredientDao(getJdbcTemplate());
		}
		return ingredientDao;
	}
	
	public FoodDao getFoodDao() {
		if(foodDao==null) {
			foodDao = new MySqlFoodDao(getJdbcTemplate());
		} 
		return foodDao;
	}

	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser("canteen");
			dataSource.setPassword("canteenApp2021");
			if (!testing) {
				dataSource.setUrl("jdbc:mysql://localhost:3306/projekt_jedalen?" + "serverTimezone=Europe/Bratislava");
			}else {
				dataSource.setUrl("jdbc:mysql://localhost:3306/projekt_jedalen_test?" + "serverTimezone=Europe/Bratislava");
			}

			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

}
