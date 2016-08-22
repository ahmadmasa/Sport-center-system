package sport.center.terminal.constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Asendar
 *
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class EventListenSupport {
	/**
	 * 
	 */
	public static final EventListenSupport instance = new EventListenSupport();

	/**
	 * 
	 */
	public static final String EVN_CLIENT_CHANGED = "evn_client_changed";
	/**
	 * 
	 */
	public static final String EVN_SESSION_MISS_CHANGED = "evn_client_miss_changed";
	/**
	 * 
	 */
	public static final String EVN_SESSION_CHANGED = "evn_session_changed";
	/**
	 * 
	 */
	public static final String EVN_SPORT_CHANGED = "evn_sport_changed";
	/**
	 * 
	 */
	public static final String EVN_SPORT_EVENT_CHANGED = "evn_sport_event_changed";
	/**
	 * 
	 */
	public static final String EVN_RECEIVABLE_CHANGED = "evn_receivable_changed";
	/**
	 * 
	 */
	public static final String EVN_PAYMENT_CHANGED = "evn_payment_changed";
	/**
	 * 
	 */
	public static final String EVN_ACCOUNT_CHANGED = "evn_account_changed";

	/**
	 * 
	 */
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	/**
	 * @param propertyName
	 * @param propertyChangeListener
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		changes.addPropertyChangeListener(propertyName, propertyChangeListener);
	}

	/**
	 * @param propertyChangeListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		changes.removePropertyChangeListener(propertyChangeListener);
	}

	/**
	 * @param propertyName
	 */
	public void firePropertyChange(String propertyName) {
		changes.firePropertyChange(propertyName, true, false);
	}
}
