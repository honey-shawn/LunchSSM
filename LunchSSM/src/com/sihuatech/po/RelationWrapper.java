package com.sihuatech.po;

import java.util.Date;

public class RelationWrapper {
	// private Relation relation;
	private String PersonName;
	private String menuName;

	private Integer id;

	private Integer menuId;

	private Integer personId;

	private Date time;

	private Double priceEnd;

	private Integer enable;

	/*
	 * public Relation getRelation() { return relation; } public void
	 * setRelation(Relation relation) { this.relation = relation; }
	 */
	public String getPersonName() {
		return PersonName;
	}

	public void setPersonName(String personName) {
		PersonName = personName;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getPriceEnd() {
		return priceEnd;
	}

	public void setPriceEnd(Double priceEnd) {
		this.priceEnd = priceEnd;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}
