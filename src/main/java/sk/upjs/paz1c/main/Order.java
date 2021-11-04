package sk.upjs.paz1c.main;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order {
	
	private Long id;
	private LocalDateTime day;
	private boolean prepared;
	
	private Map<Food, Integer> portions;
	
	public Order(LocalDateTime day, Map<Food, Integer> portions) {
		super();
		this.day = day;
		this.portions = portions;
	}

	public Order(LocalDateTime day) {
		this.day = day;
		this.portions = new HashMap<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDay() {
		return day;
	}

	public void setDay(LocalDateTime day) {
		this.day = day;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	public Map<Food, Integer> getPortions() {
		return portions;
	}

	public void setPortions(Map<Food, Integer> portions) {
		this.portions = portions;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", day=" + day + ", prepared=" + prepared + ", portions=" + portions + "]";
	}
	
	
	
	
	

}
