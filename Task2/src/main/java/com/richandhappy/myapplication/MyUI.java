package com.richandhappy.myapplication;

import javax.servlet.annotation.WebServlet;
import com.richandhappy.myapplication.Views.CategoryView;
import com.richandhappy.myapplication.Views.HotelView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("mytheme")
public class MyUI<T> extends UI implements View {

	final MenuBar menu = new MenuBar();
	public static final String HOTEL = "Hotel";
	public static final String CATEGORY = "Category";

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		final VerticalLayout layout = new VerticalLayout();
		final CssLayout viewLayout = new CssLayout();
		final CssLayout menuLayout = new CssLayout();

		layout.addComponent(menuLayout);
		layout.addComponent(viewLayout);

		menuLayout.addComponent(menu);

		layout.setMargin(new MarginInfo(false, false, false, true));

		setContent(layout);

		final Navigator navigator = new Navigator(this, viewLayout);
		navigator.addView(CATEGORY, CategoryView.class);
		navigator.addView(HOTEL, HotelView.class);
		navigator.addView("", HotelView.class);

		// menu
		MenuBar.Command command = new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				String selected = selectedItem.getText();
				if (selected.equals(CATEGORY)) {
					navigator.navigateTo(CATEGORY);
				}
				if (selected.equals(HOTEL)) {
					navigator.navigateTo(HOTEL);
				}
			}
		};

		MenuItem hotelItem = menu.addItem(HOTEL, VaadinIcons.SMILEY_O, command);
		MenuItem categoryItem = menu.addItem(CATEGORY, VaadinIcons.ADOBE_FLASH, command);
		menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
