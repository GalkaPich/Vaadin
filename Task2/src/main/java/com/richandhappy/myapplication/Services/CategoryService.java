package com.richandhappy.myapplication.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richandhappy.myapplication.Category;

public class CategoryService {

	public static CategoryService instance;
	private static final List<Category> categories = new LinkedList<>();
	private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
	private static long nextId = 0;

	public CategoryService() {
	}

	public static CategoryService getInstatnce() {
		if (instance == null) {
			instance = new CategoryService();
			instance.composeData();
		}
		return instance;
	}

	private void composeData() {
		if (categories.isEmpty()) {
			final String[] categoryData = new String[] { "Hotel", "Hostel", "GuestHouse", "Appartments" };
			for (String s : categoryData) {
				Category c = new Category();
				c.setName(s);
				save(c);
			}
		}
	}

	public synchronized List<Category> findAll() {
		Collections.sort(categories, new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return categories;
	}

	public synchronized List<String> findAllNames() {
		List<Category> categoryList = findAll();
		List<String> categoryListString = new ArrayList<>();
		for (Category c : categoryList) {
			categoryListString.add(c.getName());
		}
		return categoryListString;
	}

	public synchronized void save(Category entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Category is null");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Category) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		categories.add(entry);
	}

	public synchronized long count() {
		return categories.size();
	}

	public synchronized void delete(Category value) {
		categories.remove(value);
	}

	public void delete(Set<Category> selectedItems) {
		List<Category> categoryList = new LinkedList<>(selectedItems);
		for (Category c : categoryList) {
			categories.remove(c);
		}
	}

}
