/**
 * 
 */
package sport.center.terminal.support;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SportChangeFilter {

	/**
	 * @param sportFilter
	 */
	@Setter @Getter private SportEntity sportFilter;

	/**
	 * 
	 */
	public static final SportChangeFilter instance = new SportChangeFilter();
	/**
	 * 
	 */
	private static final String EVN_SPORT_FILTER_CHANGED = "evn_sport_filter_changed";
	/**
	 * 
	 */
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/**
	 * @param listener
	 */
	public void addSportFilterChangedListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(EVN_SPORT_FILTER_CHANGED, listener);
	}

	/**
	 * @param sportFilter
	 */
	public void fireSportFilterChangedEvent(SportEntity sportFilter) {
		this.setSportFilter(sportFilter);
		this.propertyChangeSupport.firePropertyChange(EVN_SPORT_FILTER_CHANGED, null, sportFilter);
	}

}
