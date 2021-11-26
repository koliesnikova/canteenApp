package sk.upjs.paz1c.main;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Order;

public class OrderFxModel {
	
	private Long id;
	private ObjectProperty<LocalDateTime> day = new SimpleObjectProperty<>();
	private Map<Food, IntegerProperty> portions = new HashMap<>();
	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	
	public OrderFxModel(Order order) {
		this.id = order.getId();
		setDay(order.getDay());
		for (Food f : foodDao.getAll()) {
			if (order.getPortions().keySet().contains(f))
				portions.put(f, new SimpleIntegerProperty(order.getPortions().get(f)));
			else
				portions.put(f, new SimpleIntegerProperty(0));
		}
	}
	
	public OrderFxModel() {
		for (Food f : foodDao.getAll()) {
			portions.put(f, new SimpleIntegerProperty(0));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDay() {
		return day.get();
	}

	public void setDay(LocalDateTime day) {
		this.day.set(day);
	}
	
	public ObjectProperty<LocalDateTime> dayProperty() {
		return day;
	}

	public Map<Food, IntegerProperty> getPortions() {
		return portions;
	}
	
	public Order getOrder() {
		Order o = new Order(id, getDay());
		for (Entry<Food, IntegerProperty> pair : portions.entrySet()) {
			if (pair.getValue().get() > 0) {
				o.getPortions().put(pair.getKey(), pair.getValue().get());
			}
		}
		return o;
	}


	
	
}
