/**
 * 
 */
package sport.center.terminal.data.model;

import java.util.ArrayList;
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
import sport.center.terminal.jpa.entities.SessionMissEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class SessionMissDataModel extends DataModelChangeSupport implements AbstractDataModel<SessionMissEntity> {

	/**
	 * 
	 */
	public static final SessionMissDataModel instance = new SessionMissDataModel();

	/**
	 * 
	 */
	Map<Long, SessionMissEntity> allSessionMisss = new HashMap<>();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(SessionMissDataModel.class);

	/**
	 * 
	 */
	private SessionMissDataModel() {
		
		for (SessionMissEntity SessionMiss : GetFromDatabase.instance.getSessionMisses()) {
			allSessionMisss.put(SessionMiss.getId(), SessionMiss);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(SessionMissEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allSessionMisss.put(data.getId(), data);
			AddToDatabase.instance.addSessionMiss(data);
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
	public boolean delete(SessionMissEntity data) {
		return false;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#update(java.lang.Object)
	 */
	@Override
	public boolean update(SessionMissEntity data) {
		try {

			this.allSessionMisss.put(data.getId(), data);
			UpdateDatabase.instance.updateSessionMiss(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}
	
	/**
	 * @param sportEntity
	 * @return
	 */
	public List<SessionMissEntity> getSessionMisses(SportEntity sportEntity){
		List<SessionMissEntity> filteredSessionMisses = new ArrayList<>();
		filteredSessionMisses.addAll(getSessionMisses());
		
		CollectionUtils.filter(filteredSessionMisses, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				SessionMissEntity sessionMiss = (SessionMissEntity) object;
				return(sessionMiss.getSportId() == sportEntity.getId());
			}
		});
		
		return filteredSessionMisses;
	}
	
	/**
	 * @return
	 */
	public List<SessionMissEntity> getSessionMisses(){
		List<SessionMissEntity> filteredSessionMisses = new ArrayList<>();
		filteredSessionMisses.addAll(this.allSessionMisss.values());
		
		java.util.Collections.sort(filteredSessionMisses, new Comparator<SessionMissEntity>() {

			@Override
			public int compare(SessionMissEntity o1, SessionMissEntity o2) {

				return o1.getSessionMissDate().compareTo(o2.getSessionMissDate());
			}
		});
		
		CollectionUtils.filter(filteredSessionMisses, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {

				SessionMissEntity sessionMiss = (SessionMissEntity) object;
				
				SportEntity sport = SportDataModel.instance.getSport(sessionMiss.getSportId());

				return sport.isActive();
			}
		});

		return filteredSessionMisses;
	}

	/**
	 * @param sportEntity
	 * @return
	 */
	public List<SessionMissEntity> getNotifySessionMisses(SportEntity sportEntity) {
		List<SessionMissEntity> filteredSessionMisses = new ArrayList<>();
		filteredSessionMisses.addAll(getSessionMisses(sportEntity));
		
		CollectionUtils.filter(filteredSessionMisses, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				SessionMissEntity sessionMiss = (SessionMissEntity) object;
				return(sessionMiss.isNotify());
			}
		});
		
		return filteredSessionMisses;
	}

	/**
	 * @param client
	 * @return
	 */
	public List<SessionMissEntity> getSessionMisses(ClientEntity client) {
		List<SessionMissEntity> filteredSessionMisses = new ArrayList<>();
		filteredSessionMisses.addAll(getSessionMisses());
		
		CollectionUtils.filter(filteredSessionMisses, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				SessionMissEntity sessionMiss = (SessionMissEntity) object;
				return(sessionMiss.getClientId()==client.getId());
			}
		});
		
		return filteredSessionMisses;
	}



	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(SessionMissEntity data) {
		this.fireDataModelChanged();
		return false;
	}
}
