package com.richandhappy.myapplication.EditForms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.richandhappy.myapplication.Hotel;
import com.richandhappy.myapplication.Converters.MyConverter;
import com.richandhappy.myapplication.Services.CategoryService;
import com.richandhappy.myapplication.Services.HotelService;
import com.richandhappy.myapplication.Views.HotelView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;

public class HotelEditForm extends FormLayout {

	private HotelView hw;
	private HotelService hotelService = HotelService.getInstance();
	private CategoryService categoryService = CategoryService.getInstatnce();
	private Hotel hotel = new Hotel();
	private List<Hotel> hotelsList = new ArrayList<>();
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Date");
	private NativeSelect<String> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Descriptin");
	private HorizontalLayout buttons = new HorizontalLayout();
	private Button save = new Button("Save");
	private Button close = new Button("Close");

	public HotelEditForm(HotelView hotelView) {
		this.hw = hotelView;

		buttons.addComponents(save, close);
		addComponents(name, address, rating, operatesFrom, category, url, description, buttons);

		category.setItems(categoryService.findAllNames());

		prepareFields();

		// listeners
		save.addClickListener(e -> {
			save();
			hotel = new Hotel();
			hotelView.deleteHotel.setEnabled(false);
		});

		close.addClickListener(e -> {
			hotelView.updateList();
			setVisible(false);
			hotelView.deleteHotel.setEnabled(false);
		});
	}

	public boolean isInteger(String string) {
		try {
			Integer.valueOf(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void prepareFields() {

		// validators
		Validator<String> addressValidator = new Validator<String>() {
			@Override
			public ValidationResult apply(String value, ValueContext cntext) {
				if ((value == null) || (value.isEmpty())) {
					return ValidationResult.error("The address is empty");
				}
				if (value.length() < 5) {
					return ValidationResult.error("The address is too short");
				}
				return ValidationResult.ok();
			}
		};

		Validator<String> raitingValidator = new Validator<String>() {
			@Override
			public ValidationResult apply(String value, ValueContext cntext) {
				if ((value == null) || (value.isEmpty())) {
					return ValidationResult.error("The raiting is empty");
				}
				if (isInteger(value) == false) {
					return ValidationResult.error("Raiting can be 0-5 by number");
				}

				Integer i = Integer.parseInt(value);
				if ((i < 0) || (i > 5)) {
					return ValidationResult.error("Raiting can be from 0 till 5");
				}

				return ValidationResult.ok();
			}
		};

		// binders
		binder.forField(name).asRequired("Please, enter a name").bind(Hotel::getName, Hotel::setName);
		binder.forField(address).withValidator(addressValidator).asRequired("Please, enter a address")
				.bind(Hotel::getAddress, Hotel::setAddress);
		binder.forField(rating).withValidator(raitingValidator).asRequired("Please, enter a rating")
				.withConverter(new StringToIntegerConverter(Integer.valueOf(0), "integers only"))
				.bind(Hotel::getRating, Hotel::setRating);

		/*
		 * binder.forField(operatesFrom).asRequired("Please, enter a date")
		 * .asRequired().withConverter(new MyConverter())
		 * .bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
		 */

		binder.forField(category).asRequired("Please, enter a category").bind(Hotel::getCategory, Hotel::setCategory);
		binder.forField(url).asRequired("Please, enter a url").bind(Hotel::getUrl, Hotel::setUrl);
		binder.forField(description).withNullRepresentation("").bind(Hotel::getDescription, Hotel::setDescription);

		name.setDescription("Enter the hotel name");
		address.setDescription("Enter the hotel's address");
		rating.setDescription("Enter the raiting from 0 till 5");
		operatesFrom.setDescription("Choose the date of opening");
		category.setDescription("Choose the hotel category");
		url.setDescription("Enter url of hotel");
		description.setDescription("You can add description of the hotel");
	}

	private void save() {
		if (binder.isValid()) {
			try {
				binder.writeBean(hotel);
			} catch (ValidationException ex) {
				Notification.show("Unable to save" + ex.getMessage(), Type.HUMANIZED_MESSAGE);
			}
			hotelService.save(hotel);
			exit();
		} else {
			Notification.show("Unable to save. Review errors and fix them", Type.ERROR_MESSAGE);
		}
	}

	private void exit() {
		hw.updateList();
		setVisible(false);
		hw.deleteHotel.setEnabled(false);
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.readBean(this.hotel);
		setVisible(true);
	}

	public void setHotel(Set<Hotel> value) {
		hotelsList.clear();
		hotelsList.addAll(value);

		this.hotel = hotelsList.get(0);
		binder.readBean(this.hotel);
		setVisible(true);
	}
}
