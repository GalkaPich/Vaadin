package com.richandhappy.myapplication.Views;

import java.util.List;

import com.richandhappy.myapplication.Category;
import com.richandhappy.myapplication.EditForms.CategoryEditForm;
import com.richandhappy.myapplication.Services.CategoryService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

public class CategoryView extends FormLayout implements View {

	final CategoryService categoryService = CategoryService.getInstatnce();
	final Grid<Category> categoryGrid = new Grid<>(Category.class);
	final VerticalLayout layout = new VerticalLayout();
	final HorizontalLayout controls = new HorizontalLayout();
	final HorizontalLayout forms = new HorizontalLayout();
	final Button addCategory = new Button("Add category");
	public final Button deleteCategory = new Button("Delete category");
	public final Button editCategory = new Button("Edit category");
	final CategoryEditForm form = new CategoryEditForm(this);

	public CategoryView() {

		layout.addComponents(controls, forms);
		forms.addComponents(categoryGrid, form);

		categoryGrid.setWidth(80, Unit.PERCENTAGE);
		categoryGrid.setHeight("100%");
		categoryGrid.setColumnOrder("id", "name");

		categoryGrid.setSelectionMode(SelectionMode.MULTI);
		MultiSelect<Category> selection = categoryGrid.asMultiSelect();

		forms.setWidth("100%");
		forms.setHeight(500, Unit.PIXELS);
		forms.setComponentAlignment(categoryGrid, Alignment.MIDDLE_LEFT);
		forms.setComponentAlignment(form, Alignment.TOP_CENTER);

		form.setVisible(false);
		deleteCategory.setEnabled(false);
		editCategory.setEnabled(false);

		controls.addComponents(addCategory, deleteCategory, editCategory);

		this.addComponent(layout);

		addCategory.addClickListener(e -> {
			form.setVisible(true);
			updateList();

		});

		deleteCategory.addClickListener(e -> {
			categoryService.delete(categoryGrid.getSelectedItems());
			updateList();
			deleteCategory.setEnabled(false);
			editCategory.setEnabled(false);
			form.setVisible(false);
		});

		editCategory.addClickListener(e -> {
			form.setVisible(true);
			deleteCategory.setEnabled(false);
			updateList();
		});

		categoryGrid.asMultiSelect().addSelectionListener(e -> {
			if (e.getValue() != null) {
				if (e.getAllSelectedItems().size() == 1) {
					deleteCategory.setEnabled(true);
					editCategory.setEnabled(true);
					form.setCategory(e.getValue());
					form.setVisible(false);
				}
				if (e.getAllSelectedItems().size() > 1) {
					deleteCategory.setEnabled(true);
					editCategory.setEnabled(false);
					form.setVisible(false);
				}
			}
		});

		updateList();
	}

	public void updateList() {
		List<Category> categoryList = categoryService.findAll();
		categoryGrid.setItems(categoryList);
	}

	public void enter(ViewChangeEvent event) {
	}
}
