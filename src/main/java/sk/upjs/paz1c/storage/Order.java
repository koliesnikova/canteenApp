package sk.upjs.paz1c.storage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Order {

	private Long id;
	private LocalDateTime day;
	private boolean prepared;
	private Map<Food, Integer> portions;

	public Order(LocalDateTime day, boolean prepared, Map<Food, Integer> portions) {
		this.day = day;
		this.prepared = prepared;
		this.portions = portions;
	}

	public Order(Long id, LocalDateTime day, boolean prepared, Map<Food, Integer> portions) {
		super();
		this.id = id;
		this.day = day;
		this.prepared = prepared;
		this.portions = portions;
	}

	public Order(LocalDateTime day, Map<Food, Integer> portions) {
		this.day = day;
		this.portions = portions;
		this.prepared = false;
	}

	public Order(LocalDateTime day) {
		this.day = day;
		this.portions = new HashMap<>();
		this.prepared = false;
	}
	
	public Order(Long id, LocalDateTime day) {
		this.id = id;
		this.day = day;
		this.portions = new HashMap<>();
		this.prepared = false;
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
		return id + " " + day + " portions: " + portions.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(day, id, portions, prepared);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(day, other.day) && Objects.equals(id, other.id)
				&& Objects.equals(portions.size(), other.portions.size()) && prepared == other.prepared;
	}
	
	public int hasFoodOnList(Long id) {
		for (Food f : portions.keySet()) {
			if (f.getId().equals(id)) {
				return portions.get(f);
			}
		}
		return 0;
	}


}
