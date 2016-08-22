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
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SportEventClientEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;

/**
 * @author Asendar
 *
 */
public class SportEventClientDataModel extends DataModelChangeSupport implements AbstractDataModel<SportEventClientEntity> {

	/**
	 * 
	 */
	public static final SportEventClientDataModel instance = new SportEventClientDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(SportEventClientDataModel.class);

	/**
	 * 
	 */
	Map<Long, SportEventClientEntity> allSportEventClients = new HashMap<>();

	/**
	 * 
	 */
	private SportEventClientDataModel() {
		
		for (SportEventClientEntity SportEventClients : GetFromDatabase.instance.getSportEventClients()) {
			allSportEventClients.put(SportEventClients.getId(), SportEventClients);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(SportEventClientEntity data) {
		try {
			
			SportEventClientEntity duplicate = getSportEventClient(ClientDataModel.instance.getClientbyId(data.getClientId()), 
					SportEventDataModel.instance.getSportEvent(data.getEventId()));
			
			if(duplicate!=null)
				return false;
			
			data.setId(CommonIDGenerator.newID());
			this.allSportEventClients.put(data.getId(), data);
			AddToDatabase.instance.addSportEventClient(data);
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
	public boolean delete(SportEventClientEntity data) {
		try {
			this.allSportEventClients.remove(data.getId());
			DeleteFromDatabase.instance.deleteSportEventClient(data);
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
	public boolean update(SportEventClientEntity data) {
		return false;
	}

	/**
	 * @return
	 */
	public List<SportEventClientEntity> getSportEventClients() {
		return new ArrayList<>(allSportEventClients.values());
	}

	/**
	 * @param sportEvent
	 * @return
	 */
	public List<SportEventClientEntity> getSportEventClients(SportEventEntity sportEvent) {
		List<SportEventClientEntity> filteredSportEventClients = new ArrayList<>();
		filteredSportEventClients.addAll(this.allSportEventClients.values());

		CollectionUtils.filter(filteredSportEventClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventClientEntity SportEventClient = (SportEventClientEntity) object;
				return (SportEventClient.getEventId() == sportEvent.getId());
			}
		});

		return filteredSportEventClients;
	}

	/**
	 * @param client
	 * @return
	 */
	public List<SportEventClientEntity> getSportEventClients(ClientEntity client) {

		List<SportEventClientEntity> filteredSportEventClients = new ArrayList<>();
		filteredSportEventClients.addAll(this.allSportEventClients.values());

		CollectionUtils.filter(filteredSportEventClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventClientEntity SportEventClient = (SportEventClientEntity) object;
				return (SportEventClient.getClientId() == client.getId());
			}
		});

		return filteredSportEventClients;
	}

	/**
	 * @param client
	 * @param sportEvent
	 * @return
	 */
	public SportEventClientEntity getSportEventClient(ClientEntity client, SportEventEntity sportEvent) {

		List<SportEventClientEntity> filteredSportEventClients = new ArrayList<>();
		filteredSportEventClients.addAll(this.allSportEventClients.values());

		CollectionUtils.filter(filteredSportEventClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventClientEntity SportEventClient = (SportEventClientEntity) object;
				return (SportEventClient.getClientId() == client.getId());
			}
		});

		CollectionUtils.filter(filteredSportEventClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventClientEntity SportEventClient = (SportEventClientEntity) object;
				return (SportEventClient.getEventId() == sportEvent.getId());
			}
		});

		try {
			return filteredSportEventClients.get(0);
		} catch (Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(SportEventClientEntity data) {
		this.fireDataModelChanged();
		return false;
	}

}
