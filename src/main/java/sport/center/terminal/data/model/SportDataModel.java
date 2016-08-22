/**
 * 
 */
package sport.center.terminal.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import sport.center.terminal.data.model.support.CommonIDGenerator;
import sport.center.terminal.database.AddToDatabase;
import sport.center.terminal.database.DeleteFromDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class SportDataModel extends DataModelChangeSupport implements AbstractDataModel<SportEntity> {

	/**
	 * 
	 */
	public static final SportDataModel instance = new SportDataModel();

	/**
	 * 
	 */
	Map<Long, SportEntity> allSports = new HashMap<>();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(SportDataModel.class);

	/**
	 * 
	 */
	private SportDataModel() {
		
		for (SportEntity sport : GetFromDatabase.instance.getSports()) {
			allSports.put(sport.getId(), sport);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(SportEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allSports.put(data.getId(), data);
			AddToDatabase.instance.addSport(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(SportEntity data) {
		try {

			this.allSports.remove(data.getId());
			DeleteFromDatabase.instance.deleteSport(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#update(java.lang.Object)
	 */
	@Override
	public boolean update(SportEntity data) {
		try {

			this.allSports.put(data.getId(), data);
			UpdateDatabase.instance.updateSport(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}
	
	/**
	 * @return
	 */
	public List<SportEntity> getAllActiveSports() {

		List<SportEntity> sports = new ArrayList<>(this.getAllSports());
		
		CollectionUtils.filter(sports, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				SportEntity sport = (SportEntity)object;
				
				return sport.isActive();
			}
		});
		return sports;
	}

	/**
	 * @param sportId
	 * @return
	 */
	public SportEntity getSport(long sportId) {
		return allSports.get(sportId);
	}

	/**
	 * @return
	 */
	public List<SportEntity> getAllSports() {
		return new ArrayList<>(allSports.values());
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(SportEntity data) {
		this.fireDataModelChanged();
		return false;
	}
}
