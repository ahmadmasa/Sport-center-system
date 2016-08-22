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
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class SessionDataModel extends DataModelChangeSupport implements AbstractDataModel<SessionEntity> {

	/**
	 * 
	 */
	public static final SessionDataModel instance = new SessionDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(SessionDataModel.class);

	/**
	 * 
	 */
	Map<Long, SessionEntity> allSessions = new HashMap<>();

	/**
	 * 
	 */
	private SessionDataModel() {
		
		for (SessionEntity Session : GetFromDatabase.instance.getSessions()) {
			allSessions.put(Session.getId(), Session);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(SessionEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allSessions.put(data.getId(), data);
			AddToDatabase.instance.addSession(data);
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
	public boolean delete(SessionEntity data) {
		try {

			this.allSessions.remove(data.getId());
			DeleteFromDatabase.instance.deleteSession(data);
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
	public boolean update(SessionEntity data) {
		try {

			this.allSessions.put(data.getId(), data);
			UpdateDatabase.instance.updateSession(data);
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
	public List<SessionEntity> getSessions(SportEntity sportEntity) {
		
		List<SessionEntity> filteredSessions = new ArrayList<>();
		filteredSessions.addAll(allSessions.values());
		
		CollectionUtils.filter(filteredSessions, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				SessionEntity session = (SessionEntity) object;
				return(session.getSportId()==sportEntity.getId());
			}
		});
		
		return filteredSessions;
	}
	
	/**
	 * @return
	 */
	public List<SessionEntity> getSessions() {

		List<SessionEntity> filteredSessions = new ArrayList<>();
		filteredSessions.addAll(allSessions.values());

		CollectionUtils.filter(filteredSessions, new Predicate() {

			@Override
			public boolean evaluate(Object object) {

				SessionEntity session = (SessionEntity) object;

				SportEntity sport = SportDataModel.instance.getSport(session.getSportId());

				return sport.isActive();
			}
		});
		
		return filteredSessions;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public SessionEntity getSessionById(long id) {
		return this.allSessions.get(id);
	}

	/**
	 * @param sportEntity
	 * @param client
	 * @return
	 */
	public SessionEntity getSessionByClient(SportEntity sportEntity, ClientEntity client) {

		for (SessionEntity session : getSessions(sportEntity)) {
			if (session.getId() == client.getSession()) {
				return session;
			}
		}
		return new SessionEntity(0, "none", "", 0, 0);
	}
	
	
	/**
	 * @param session
	 * @return
	 */
	public int getSessionCurrentNumber(SessionEntity session) {
		List<ClientEntity> clients = new ArrayList<>();
		for (ClientEntity client : ClientDataModel.instance.getActiveClients()) {
			if (client.getSession() == session.getId()) {
				clients.add(client);
			}
		}
		return clients.size();
	}

	/**
	 * @return
	 */
	public SessionEntity getBlankSession(){
		SessionEntity blankSession = new SessionEntity();
		blankSession.setId(0);
		blankSession.setName("بدون حصه");
		return blankSession;
	}
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(SessionEntity data) {
		this.fireDataModelChanged();
		return false;
	}

}
