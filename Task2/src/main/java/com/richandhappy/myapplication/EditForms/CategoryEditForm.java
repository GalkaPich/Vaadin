package com.richandhappy.myapplication.EditForms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.richandhappy.myapplication.Category;
import com.richandhappy.myapplication.Services.CategoryService;
import com.richandhappy.myapplication.Views.CategoryView;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;

public class CategoryEditForm extends FormLayout {

	private CategoryView cw;
	private Category category = new Category();
	private HorizontalLayout buttons = new HorizontalLayout();
	private Button save = new Button("Save");
	private Button close = new Button("Close");
	private TextField name = new TextField("Name");
	private Binder<Category> binder = new Binder<>(Category.class);
	private CategoryService categoryService = new CategoryService();
	private List<Category> categryList = new ArrayList<>();

	public CategoryEditForm(CategoryView categoryView) {
		this.cw = categoryView;

		buttons.addComponents(save, close);
		addComponents(name, buttons);

		prepareFields();

		// listeners
		save.addClickListener(e -> {
			category = new Category();
			save();
			categoryView.deleteCategory.setEnabled(false);
			categoryView.editCategory.setEnabled(false);
		});

		close.addClickListener(e -> {
			categoryView.updateList();
			setVisible(false);
			categoryView.deleteCategory.setEnabled(false);
			categoryView.editCategory.setEnabled(false);
		});

	}

	public void prepareFields() {
		// binders
		binder.forField(name).asRequired("Please, enter a name").bind(Category::getName, Category::setName);
		name.setDescription("Enter the category");
	}

	private void exit() {
		cw.updateList();
		setVisible(false);
		cw.deleteCategory.setEnabled(false);
	}

	private void save() {
		if (binder.isValid()) {
			try {
				binder.writeBean(category);
			} catch (Exception ex) {
				Notification.show("Unable to save " + ex.getMessage(), Type.HUMANIZED_MESSAGE);
			}
			categoryService.save(category);
			exit();
		} else {
			Notification.show("Unable to save. Review errors and fix them", Type.ERROR_MESSAGE);
		}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
		binder.readBean(this.category);
		setVisible(true);
	}

	public void setCategory(Set<Category> value) {
		categryList.clear();
		categryList.addAll(value);
		this.category = categryList.get(0);
		binder.readBean(this.category);
		setVisible(true);
	}
}
