package com.richandhappy.myapplication;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class HotelEditForm extends FormLayout {

	private MyUI ui;
	private HotelService hotelService = HotelService.getInstance();
	private Hotel hotel;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("OperatesFrom");
	private NativeSelect<HotelCategry> category = new NativeSelect<>("Category");
	private TextField url = new TextField("Url");
	private TextArea description = new TextArea("Descriptin");

	private Button save = new Button("Save");
	private Button close = new Button("Close");

	public HotelEditForm(MyUI hotelUi) {
		this.ui = hotelUi;

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(save, close);
		addComponents(name, address, rating, operatesFrom, category, url, description, buttons);

		binder.bindInstanceFields(this);

		category.setItems(HotelCategry.values());

		save.addClickListener(e -> {
			save();
			hotelUi.deleteHotel.setEnabled(false);
		});

		close.addClickListener(e -> {
			hotelUi.updateList();
			setVisible(false);
			hotelUi.deleteHotel.setEnabled(false);
		});
	}

	private void save() {
		hotelService.save(hotel);
		ui.updateList();
		setVisible(false);
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(hotel);
		setVisible(true);
	}
}