package org.vaadin.viritin.grid.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.vaadin.viritin.util.BrowserCookie;
import org.vaadin.viritin.util.BrowserCookie.Callback;

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

/**
 * Use this class to save grid hidden columns to cookies.
 * Use {@link #attachToGrid(Grid, String) }
 *
 */
public class GridUtils {

	private static final String COLOMN_DELIMITER = ":";
	private static final String SEMI_COLOMN_DELIMITER = ";";
	private final String HIDDEN_SETTINGS_NAME;
	private final String SORT_ORDER_SETTINGS_NAME;
	private Grid grid;
	/**
	 * Set specified grid to save hidden columns in cookies.
	 * @param grid - grid which columns would be used
	 * @param cookieName - name of the cookie. Should be unique for every grid.
	 */
	static public void attachToGrid(Grid grid, String cookieName) {
		GridUtils utils=new GridUtils(grid, cookieName);
	}
	private GridUtils(Grid grid, String cookieName) {
		super();
		this.grid=grid;
		HIDDEN_SETTINGS_NAME=cookieName+"hiddenCols";
		SORT_ORDER_SETTINGS_NAME=cookieName+"sortOrder";
		loadSettings();
		grid.addColumnVisibilityChangeListener(e->{
			saveHidden();
		});
		grid.addSortListener(e->{
			saveSortOrder();
		});
	}
	private void loadSettings() {
		loadHidden();
		loadSortOrder();
	}

	private void loadSortOrder() {
		Callback saveFunc=new Callback() {
			@Override
			public void onValueDetected(String value) {
				List<SortOrder> sortOrderList=new ArrayList<SortOrder>();
				if(value!=null) {
					String[] sortOrder=value.split(SEMI_COLOMN_DELIMITER);
					Arrays.stream(sortOrder).forEach(so -> {
						String[] colIdWithSort=so.split(COLOMN_DELIMITER);
						Object propertyId=colIdWithSort[0];
						SortDirection direction=SortDirection.valueOf(colIdWithSort[1]);
						SortOrder soCreated=new SortOrder(propertyId, direction);
						sortOrderList.add(soCreated);
					});
				}
				grid.setSortOrder(sortOrderList);
			}
		};
		BrowserCookie.detectCookieValue(SORT_ORDER_SETTINGS_NAME, saveFunc);	
	}
	private void loadHidden() {
		Callback saveFunc=new Callback() {
			@Override
			public void onValueDetected(String value) {
				if(value!=null) {
					String[] hiddenColumns=value.split(COLOMN_DELIMITER);
					Arrays.stream(hiddenColumns).forEach(col -> {
						Column column = grid.getColumn(col);
						if(column!=null) {
							column.setHidden(true);
						}
					});
				}
			}
		};
		BrowserCookie.detectCookieValue(HIDDEN_SETTINGS_NAME, saveFunc);
	}

	private void saveSortOrder() {
		String value = grid.getSortOrder().stream().
				reduce("",(String propToOrder,SortOrder so)->propToOrder+=so.getPropertyId()+COLOMN_DELIMITER+so.getDirection(),
				(a,b)->(a+";"+b));
		BrowserCookie.setCookie(SORT_ORDER_SETTINGS_NAME, value);
	}
	private void saveHidden() {
		Optional<String> value = grid.getColumns().stream().filter(c -> c.isHidden())
				.map(a -> a.getPropertyId().toString())
				.reduce((a, b) -> a + COLOMN_DELIMITER + b);

		if(value.isPresent()) {
			BrowserCookie.setCookie(HIDDEN_SETTINGS_NAME, value.get());
		}
		else {
			BrowserCookie.setCookie(HIDDEN_SETTINGS_NAME, "");
		}
	}
	

}
