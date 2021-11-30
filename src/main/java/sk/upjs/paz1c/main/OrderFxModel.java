package sk.upjs.paz1c.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Order;

public class OrderFxModel {
	
	private Long id;
	private ObjectProperty<LocalDate> day = new SimpleObjectProperty<>();
	private Map<Food, Integer> portions = new HashMap<>();
	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	private boolean prepared;
	
	public OrderFxModel(Order order) {
		this.id = order.getId();
		this.prepared = order.isPrepared();
		setDay(order.getDay().toLocalDate());
		for (Food f : foodDao.getAll()) {
			portions.put(f, order.hasFoodOnList(f.getId()));
		}
	}
	
	public OrderFxModel() {
		for (Food f : foodDao.getAll()) {
			portions.put(f, 0);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	public LocalDate getDay() {
		return day.get();
	}

	public void setDay(LocalDate day) {
		this.day.set(day);
	}
	
	public ObjectProperty<LocalDate> dayProperty() {
		return day;
	}

	public Map<Food, Integer> getPortions() {
		return portions;
	}
	
	public void removePortionsOfFood(Long id) {
		for (Entry<Food, Integer> pair : portions.entrySet()) {
			if (pair.getKey().getId().equals(id)) {
				portions.put(pair.getKey(), 0);
			}
		}
	}
	
	public Order getOrderFromModel() {
		Order o = new Order(id, LocalDateTime.of(getDay(), LocalTime.of(0, 0, 0)), prepared, new HashMap<>());
		for (Entry<Food, Integer> pair : portions.entrySet()) {
			if (pair.getValue() > 0) {
				o.getPortions().put(pair.getKey(), pair.getValue());
			}
		}
		return o;
	}


	
	
}
