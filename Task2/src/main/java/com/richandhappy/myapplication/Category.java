package com.richandhappy.myapplication;

import java.io.Serializable;

public class Category implements Serializable, Cloneable {
	private static final long serialVersionUID = -6315335082851969131L;

	private Long id;
	private String name = "";
	
	public Category(){
	}
	
	public Category(Long id, String name){
		this.id=id;
		this.name=name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Category clone() throws CloneNotSupportedException {
		return (Category) super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
