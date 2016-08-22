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

import sport.center.terminal.database.AddToDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;

/**
 * @author Asendar
 *
 */
public class ClientDataModel extends DataModelChangeSupport implements AbstractDataModel<ClientEntity> {

	/**
	 * 
	 */
	public static final ClientDataModel instance = new ClientDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(ClientDataModel.class);

	/**
	 * 
	 */
	Map<Long, ClientEntity> allClients = new HashMap<>();
	
	/**
	 * 
	 */
	private ClientDataModel() {
		for (ClientEntity Client : GetFromDatabase.instance.getClients()) {
			allClients.put(Client.getId(), Client);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(ClientEntity data) {
		try {
			this.allClients.put(data.getId(), data);
			AddToDatabase.instance.addClient(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}
	
	/**
	 * @param data
	 * @return
	 */
	public ClientEntity addClient(ClientEntity data) {
		try {

			data = AddToDatabase.instance.addClient(data);
			this.allClients.put(data.getId(), data);

			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return null;
		}
		return data;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(ClientEntity data) {
		return false;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#update(java.lang.Object)
	 */
	@Override
	public boolean update(ClientEntity data) {
		try {

			this.allClients.put(data.getId(), data);
			UpdateDatabase.instance.updateClient(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/**
	 * @param sportEntity
	 * @param id
	 * @return
	 */
	public ClientEntity getClientbyId(SportEntity sportEntity, long id) {
		for (ClientEntity client : getClients(sportEntity)) {
			if (client.getId() == id) {
				return client;
			}
		}
		return null;
	}
	/**
	 * @return
	 */
	public List<ClientEntity> getActiveClients() {
		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());
		
		CollectionUtils.filter(filteredClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return(client.isActive());
			}
		});
		return filteredClients;
	}


	/**
	 * @param ids
	 * @return
	 */
	public List<ClientEntity> getClientbyIds(List<Long> ids) {
		List<ClientEntity> filteredClients = new ArrayList<>();
		for(Long id : ids){
			filteredClients.add(allClients.get(id));
		}
		return filteredClients;
	}

	/**
	 * @param id
	 * @return
	 */
	public ClientEntity getClientbyId(long id) {
		return this.allClients.get(id);
	}

	/**
	 * @param sportEntity
	 * @return
	 */
	public List<ClientEntity> getActiveClients(SportEntity sportEntity) {

		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());

		CollectionUtils.filter(filteredClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return (client.isActive() && client.getSportId() == sportEntity.getId());
			}
		});

		return filteredClients;
	}
	/**
	 * @param event
	 * @return
	 */
	public List<ClientEntity> getAllClients(SportEventEntity event) {

		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());

		
		
		CollectionUtils.filter(filteredClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return (client.isActive() && SportEventDataModel.instance.getSportEvents(client).contains(event));
			}
		});

		return filteredClients;
	}

	/**
	 * @param sportEntity
	 * @return
	 */
	public List<ClientEntity> getInactiveClients(SportEntity sportEntity) {
		
		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());
		
		CollectionUtils.filter(filteredClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return(!client.isActive() && client.getSportId() == sportEntity.getId());
			}
		});
		
		return filteredClients;
	}

	/**
	 * @param sportEntity
	 * @return
	 */
	public List<ClientEntity> getClients(SportEntity sportEntity) {

		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());
		
		CollectionUtils.filter(filteredClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return(client.getSportId() == sportEntity.getId());
			}
		});
		
		return filteredClients;
	}

	/**
	 * @return
	 */
	public List<ClientEntity> getAllClients() {
		return new ArrayList<>(allClients.values());
	}
	
	/**
	 * @param sessionEntity
	 * @return
	 */
	public List<ClientEntity> getActiveClients(SessionEntity sessionEntity) {
		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());
		
		CollectionUtils.filter(filteredClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return(client.isActive() && client.getSession() == sessionEntity.getId());
			}
		});
		return filteredClients;
	}
	
	/**
	 * @param sessionEntity
	 * @return
	 */
	public List<ClientEntity> getInactiveClients(SessionEntity sessionEntity) {
		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());
		
		CollectionUtils.filter(filteredClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return(!client.isActive() && client.getSession() == sessionEntity.getId());
			}
		});
		return filteredClients;
	}
	
	/**
	 * @return
	 */
	public List<ClientEntity> getInactiveClients() {

		List<ClientEntity> filteredClients = new ArrayList<>();
		filteredClients.addAll(allClients.values());

		CollectionUtils.filter(filteredClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return (!client.isActive());
			}
		});

		return filteredClients;
	}
	
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(ClientEntity data) {
		this.fireDataModelChanged();
		return true;
	}

}
