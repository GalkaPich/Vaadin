package com.richandhappy.myapplication.Views;

import java.util.List;

import com.richandhappy.myapplication.Hotel;
import com.richandhappy.myapplication.EditForms.HotelEditForm;
import com.richandhappy.myapplication.Services.HotelService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

public class HotelView extends VerticalLayout implements View {

	final VerticalLayout layout = new VerticalLayout();
	final HorizontalLayout controls = new HorizontalLayout();
	final HorizontalLayout content = new HorizontalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final TextField filterName = new TextField();
	final TextField filterAddress = new TextField();
	final Button addHotel = new Button("Add hotel");
	public final Button deleteHotel = new Button("Delete hotel");
	final TextArea description = new TextArea("Description");
	final HotelEditForm form = new HotelEditForm(this);

	public HotelView() {

		layout.addComponents(controls, content);
		controls.addComponents(filterName, filterAddress, addHotel, deleteHotel);
		content.addComponents(hotelGrid, form);

		deleteHotel.setEnabled(false);

		hotelGrid.setColumnOrder("name", "address", "rating", "category", "url", "operatesFrom", "description");
		hotelGrid.getColumn("url").setRenderer(e -> "<a href='" + (String) e + "'target ='_blank'>Go to the site</a>",
				new HtmlRenderer());

		hotelGrid.setSelectionMode(SelectionMode.MULTI);
		MultiSelect<Hotel> selection = hotelGrid.asMultiSelect();

		content.setWidth("100%");
		content.setHeight(500, Unit.PIXELS);
		content.setExpandRatio(hotelGrid, 0.7f);
		content.setExpandRatio(form, 0.3f);
		content.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

		form.setWidth("80%");
		form.setHeight("100%");
		form.setVisible(false);

		hotelGrid.setWidth(100, Unit.PERCENTAGE);
		hotelGrid.setHeight("100%");

		this.addComponent(layout);

		updateList();

		// filters
		filterName.setPlaceholder("Search by name");
		filterAddress.setPlaceholder("Search by address");

		filterName.addFocusListener(e -> {
			deleteHotel.setEnabled(false);
			form.setVisible(false);
			filterName.setPlaceholder("");
		});

		filterName.addBlurListener(e -> {
			filterName.setPlaceholder("Search by address");
		});

		filterName.addValueChangeListener(e -> updateList());
		filterName.setValueChangeMode(ValueChangeMode.LAZY);

		filterAddress.addFocusListener(e -> {
			deleteHotel.setEnabled(false);
			form.setVisible(false);
			filterAddress.setPlaceholder("");
		});

		filterAddress.addBlurListener(e -> {
			filterAddress.setPlaceholder("Search by address");
		});

		filterAddress.addValueChangeListener(e -> updateList());
		filterAddress.setValueChangeMode(ValueChangeMode.LAZY);

		hotelGrid.asMultiSelect().addSelectionListener(e -> {
			if (e.getValue() != null) {
				if (e.getAllSelectedItems().size() == 0) {
					deleteHotel.setEnabled(false);
					form.setVisible(false);
				}
				if (e.getAllSelectedItems().size() == 1) {
					deleteHotel.setEnabled(true);
					form.setHotel(selection.getValue());
				}
				if (e.getAllSelectedItems().size() > 1) {
					deleteHotel.setEnabled(true);
					form.setVisible(false);
				}
			}

		});

		deleteHotel.addClickListener(e -> {

			hotelService.delete(hotelGrid.getSelectedItems());
			updateList();
			deleteHotel.setEnabled(false);

			form.setVisible(false);
		});

		addHotel.addClickListener(e -> {
			form.setVisible(true);
			updateList();
		});

	}

	public void updateList() {
		List<Hotel> hotelList = hotelService.findAll(filterName.getValue(), filterAddress.getValue());

		hotelGrid.setItems(hotelList);
	}

	public void enter(ViewChangeEvent event) {
	}
}
