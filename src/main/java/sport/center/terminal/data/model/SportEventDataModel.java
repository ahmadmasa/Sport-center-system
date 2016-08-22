/**
 * 
 */
package sport.center.terminal.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import sport.center.terminal.data.model.support.CommonIDGenerator;
import sport.center.terminal.database.AddToDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.jpa.entities.SportEventClientEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;

/**
 * @author Asendar
 *
 */
public class SportEventDataModel extends DataModelChangeSupport implements AbstractDataModel<SportEventEntity> {

	/**
	 * 
	 */
	public static final SportEventDataModel instance = new SportEventDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(SportEventDataModel.class);

	/**
	 * 
	 */
	Map<Long, SportEventEntity> allSportEvents = new HashMap<>();

	/**
	 * 
	 */
	private SportEventDataModel() {
		
		for (SportEventEntity SportEvents : GetFromDatabase.instance.getSportEvents()) {
			allSportEvents.put(SportEvents.getId(), SportEvents);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(SportEventEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allSportEvents.put(data.getId(), data);
			AddToDatabase.instance.addSportEvent(data);
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
	public boolean delete(SportEventEntity data) {
		try {
			data.setActive(false);
			this.allSportEvents.put(data.getId(),data);
			
			UpdateDatabase.instance.updateSportEvent(data);
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
	public boolean update(SportEventEntity data) {
		try {
			this.allSportEvents.put(data.getId(),data);
			UpdateDatabase.instance.updateSportEvent(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/**
	 * @param id
	 * @return
	 */
	public SportEventEntity getSportEvent(long id) {
		return this.allSportEvents.get(id);
	}
	
	/**
	 * @param sportEntity
	 * @return
	 */
	public List<SportEventEntity> getSportEvents(SportEntity sportEntity) {
		List<SportEventEntity> filteredSportEvents = new ArrayList<>();
		filteredSportEvents.addAll(this.allSportEvents.values());

		CollectionUtils.filter(filteredSportEvents, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventEntity SportEvent = (SportEventEntity) object;
				return (SportEvent.getSportId() == sportEntity.getId());
			}
		});

		return filteredSportEvents;
	}

	/**
	 * @return
	 */
	public List<SportEventEntity> getActiveSportEvents() {
		List<SportEventEntity> filteredSportEvents = new ArrayList<>();
		filteredSportEvents.addAll(getSportEvents());

		CollectionUtils.filter(filteredSportEvents, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventEntity SportEvent = (SportEventEntity) object;
				return (SportEvent.isActive());
			}
		});

		return filteredSportEvents;
	}

	/**
	 * @return
	 */
	public List<SportEventEntity> getInactiveSportEvents() {
		List<SportEventEntity> filteredSportEvents = new ArrayList<>();
		filteredSportEvents.addAll(getSportEvents());

		CollectionUtils.filter(filteredSportEvents, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventEntity SportEvent = (SportEventEntity) object;
				return (!SportEvent.isActive());
			}
		});

		return filteredSportEvents;
	}
	/**
	 * @param sport
	 * @return
	 */
	public List<SportEventEntity> getInactiveSportEvents(SportEntity sport) {
		List<SportEventEntity> filteredSportEvents = new ArrayList<>();
		filteredSportEvents.addAll(getSportEvents());

		CollectionUtils.filter(filteredSportEvents, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventEntity SportEvent = (SportEventEntity) object;
				SportEntity eventSport = SportDataModel.instance.getSport(SportEvent.getSportId());
				
				return (!SportEvent.isActive() &&(eventSport!=null && eventSport.isActive()&& eventSport.equals(eventSport)) );
			}
		});

		return filteredSportEvents;
	}
	
	/**
	 * @param sport
	 * @return
	 */
	public List<SportEventEntity> getActiveSportEvents(SportEntity sport) {
		List<SportEventEntity> filteredSportEvents = new ArrayList<>();
		filteredSportEvents.addAll(getSportEvents());

		CollectionUtils.filter(filteredSportEvents, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				SportEventEntity SportEvent = (SportEventEntity) object;
				SportEntity eventSport = SportDataModel.instance.getSport(SportEvent.getSportId());
				
				return (SportEvent.isActive() &&(eventSport!=null && eventSport.isActive() && eventSport.equals(sport)) );
			}
		});

		return filteredSportEvents;
	}

	/**
	 * @param client
	 * @return
	 */
	public List<SportEventEntity> getSportEvents(ClientEntity client) {
		List<SportEventEntity> filteredSportEvents = new ArrayList<>();
		for (SportEventClientEntity sportEventClient :SportEventClientDataModel.instance.getSportEventClients(client)) {
			for (SportEventEntity sportEvent : getSportEvents())
				if (sportEvent.getId() == sportEventClient.getEventId())
					filteredSportEvents.add(sportEvent);
		}

		return filteredSportEvents;
	}

	/**
	 * @return
	 */
	public List<SportEventEntity> getSportEvents() {
		List<SportEventEntity> events = new ArrayList<>(this.allSportEvents.values());
		Collections.sort(events, new Comparator<SportEventEntity>() {
			@Override
			public int compare(SportEventEntity o1, SportEventEntity o2) {
				int valid1 = o1.isActive() ? 1 : 0;
				int valid2 = o2.isActive() ? 1 : 0;
				if (valid1 > valid2)
					return -1;
				else
					return 1;

			}

		});
		
		CollectionUtils.filter(events, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				SportEventEntity sportEvent = (SportEventEntity)object;

				SportEntity sport = SportDataModel.instance.getSport(sportEvent.getSportId());
				
				return sport==null || sport.isActive();
			}
		});

		return events;

	}
	/**
	 * @param client
	 * @param event
	 * @return
	 */
	public boolean isClientInSportEvent(ClientEntity client, SportEventEntity event) {
		for (SportEventClientEntity eventClient : SportEventClientDataModel.instance.getSportEventClients(client)) {
			if (eventClient.getEventId() == event.getId())
				return true;
		}
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	public int getSportEventCurrentNumber(SportEventEntity event) {
		return SportEventClientDataModel.instance.getSportEventClients(event).size();
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(SportEventEntity data) {
		this.fireDataModelChanged();
		return false;
	}

}
