package com.richandhappy.myapplication.Services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richandhappy.myapplication.Category;
import com.richandhappy.myapplication.Hotel;

public class HotelService {

	private static HotelService instance;
	private final HashMap<Long, Hotel> hotels = new HashMap<>();
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());
	private static long nextId = 0;
	private List<Category> categoryList = new ArrayList<>();
	final CategoryService categoryService = CategoryService.getInstatnce();

	public HotelService() {
	}

	public static HotelService getInstance() {
		if (instance == null) {
			instance = new HotelService();
			instance.composeData();
		}
		return instance;
	}

	public void composeData() {
		if (findAll().isEmpty()) {
			final String[] hotelData = new String[] {
					"Emerald Patio Suites;4;https://www.booking.com/hotel/es/emerald-patio-suites.ru.html?aid=397642;Calle Lajas de Chapín B-5, 38683 Пуерто-де-Сантьяго, Испания;12-December-2012;The townhouse is located in a quiet location with a swimming pool in the complex.",
					"Vincci Tenerife Golf;5;https://www.booking.com/hotel/es/vincci-tenerife-golf.ru.html?aid=397642;Urbanización Golf del Sur, 38639 Сан-Мигель-де-Абона, Испания;10-December-2007;Large ground floor terrace has a barbecue along with the table for outside dining for 6 persons.",
					"Adrián Hoteles Roca Nivaria;5;https://www.booking.com/hotel/es/roca-nivaria-gran.ru.html?aid=397642;Avenida Adeje, 300, 38678 Адехе, Испания;12-November-2012;The townhouse has 4 floors, two bathrooms and three bedrooms.",
					"Iberostar Sábila - Adults Only;4;https://www.booking.com/hotel/es/torviscas-playa.ru.html?aid=397642;Avenida Ernesto Sartí, 5, 38660 Адехе, Испания;12-April-2012; Large supermarket Mercadona is only 100 meters away from the townhouse.",
					"Apartamentos La Casa Verde;5;https://www.booking.com/hotel/es/apartamentos-la-casa-verde.ru.html?aid=397642;Carretera General Las Arenas, 94, 38400 Пуэрто-де-ла-Круз, Испания;13-December-2007;On the ground floor there is a room which can be used as an additional bedroom." };

			Random r = new Random(0);

			for (String hotel : hotelData) {
				String split[] = hotel.split(";");
				Hotel h = new Hotel();
				h.setName(split[0]);
				h.setRating(Integer.parseInt(split[1]));
				h.setUrl(split[2]);
				h.setAddress(split[3]);
				h.setCategory(categoryService.findAllNames().get(r.nextInt(categoryService.findAllNames().size())));
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					Date date = (Date) formatter.parse(split[4]);
					long mills = date.getTime();
					h.setOperatesFrom(mills);
				} catch (java.text.ParseException ex) {
					Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
				}

				h.setDescription(split[5]);

				save(h);
			}
		}
	}

	public synchronized List<Hotel> findAll() {
		return findAll(null, null);
	}

	public synchronized List<Hotel> findAll(String nameFilte, String addressFilter) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		categoryList = categoryService.findAll();

		for (Hotel hotel : hotels.values()) {
			int i = 0;
			for (Category c : categoryList) {
				if (hotel.getCategory().equals(c.getName())) {
					i++;
				}
			}
			if (i == 0) {
				hotel.setCategory("No category");
			}

			try {
				boolean passesFilter = (nameFilte == null || nameFilte.isEmpty())
						& (addressFilter == null || addressFilter.isEmpty())
						|| hotel.getName().toString().toLowerCase().contains(nameFilte.toLowerCase())
								& hotel.getAddress().toString().toLowerCase().contains(addressFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(hotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized void save(Hotel entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Hotel) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotels.put(entry.getId(), entry);
	}

	public synchronized long count() {
		return hotels.size();
	}

	public synchronized void delete(Hotel value) {
		hotels.remove(value.getId());
	}

	public synchronized void delete(Set<Hotel> selectedItems) {
		List<Hotel> hotelList = new LinkedList<>(selectedItems);
		for (Hotel h : hotelList) {
			hotels.remove(h.getId());
		}
	}
}
