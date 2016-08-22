/**
 * 
 */
package sport.center.terminal.data.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Asendar
 *
 */
public class DataModelChangeSupport {

	/**
	 * 
	 */
	private final PropertyChangeSupport dataModelChangeSupport = new PropertyChangeSupport(this);
	/**
	 * 
	 */
	private static final String EVN_DATA_CHANGED = "evnDataChanged";
	
	
	/**
	 * @param propertyChangeListener
	 */
	public void addDataModelChangeListener(PropertyChangeListener propertyChangeListener){
		dataModelChangeSupport.addPropertyChangeListener(EVN_DATA_CHANGED,propertyChangeListener);
	}
	
	/**
	 * 
	 */
	public void fireDataModelChanged(){
		dataModelChangeSupport.firePropertyChange(EVN_DATA_CHANGED, true, false);
	}
	
}
