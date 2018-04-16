package com.richandhappy.myapplication;

import java.util.List;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

@Theme("mytheme")
public class MyUI<T> extends UI {

	final VerticalLayout layout = new VerticalLayout();
	final HorizontalLayout controls = new HorizontalLayout();
	final HorizontalLayout content = new HorizontalLayout();
	final HorizontalLayout filterFilds = new HorizontalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final TextField filterName = new TextField("Search by name");
	final TextField filterAddresss = new TextField("Search by address");
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	final TextArea description = new TextArea("Description");
	final HotelEditForm form = new HotelEditForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		setContent(layout);

		layout.addComponents(filterFilds, controls, content);
		content.addComponents(hotelGrid, form);
		controls.addComponents(addHotel, deleteHotel);
		filterFilds.addComponents(filterName, filterAddresss);
		form.setVisible(false);
		deleteHotel.setEnabled(false);

		hotelGrid.setColumnOrder("name", "address", "rating", "category", "url", "operatesFrom", "description");

		hotelGrid.getColumn("url").setRenderer(e -> "<a href='" + (String) e + "'target ='_blank'>Go to the site</a>",
				new HtmlRenderer());

		// hotelGrid.setWidth(100, Unit.PERCENTAGE);

		filterName.addFocusListener(e -> {
			deleteHotel.setEnabled(false);
			form.setVisible(false);
		});
		
		filterAddresss.addFocusListener(e -> {
			deleteHotel.setEnabled(false);
			form.setVisible(false);
		});
		
		
		hotelGrid.asSingleSelect().addValueChangeListener(e -> {
			if (e.getValue() != null) {
				deleteHotel.setEnabled(true);
				form.setHotel(e.getValue());
			}
		});

		deleteHotel.addClickListener(e -> {
			Hotel delCanddidate = hotelGrid.getSelectedItems().iterator().next();
			hotelService.delete(delCanddidate);
			deleteHotel.setEnabled(false);
			updateList();

			form.setVisible(false);
		});

		filterName.addValueChangeListener(e -> updateList());
		filterName.setValueChangeMode(ValueChangeMode.LAZY);

		filterAddresss.addValueChangeListener(e -> updateList());
		filterAddresss.setValueChangeMode(ValueChangeMode.LAZY);

		updateList();

		addHotel.addClickListener(e -> form.setHotel(new Hotel()));	
	}

	public void updateList() {
		List<Hotel> hotelList = hotelService.findAll(filterName.getValue(), filterAddresss.getValue());
		hotelGrid.setItems(hotelList);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}