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
import sport.center.terminal.database.DeleteFromDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.ReceivableEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class ReceivableDataModel extends DataModelChangeSupport implements AbstractDataModel<ReceivableEntity> {

	/**
	 * 
	 */
	public static final ReceivableDataModel instance = new ReceivableDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(ReceivableDataModel.class);

	/**
	 * 
	 */
	Map<Long, ReceivableEntity> allReceivables = new HashMap<>();

	/**
	 * 
	 */
	private ReceivableDataModel() {
		List<ReceivableEntity> recievables = GetFromDatabase.instance.getReceivables();
		
		Collections.sort(recievables, new Comparator<ReceivableEntity>() {

			@Override
			public int compare(ReceivableEntity o1, ReceivableEntity o2) {
				if(o1.isPayed() && o2.isPayed())
					return o1.getReceivableDate().compareTo(o2.getReceivableDate());
				
				if(o1.isPayed() && !o2.isPayed())
					return 1;
				
				if(!o1.isPayed() && o2.isPayed())
					return -1;

				return 0;
			}
		});
		
		for (ReceivableEntity receivables : recievables) {
			allReceivables.put(receivables.getId(), receivables);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(ReceivableEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allReceivables.put(data.getId(), data);
			AddToDatabase.instance.addReceivable(data);
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
	public boolean delete(ReceivableEntity data) {
		try {

			this.allReceivables.remove(data.getId());
			DeleteFromDatabase.instance.deleteReceivable(data);
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
	public boolean update(ReceivableEntity data) {
		try {

			this.allReceivables.put(data.getId(), data);
			UpdateDatabase.instance.updateReceivable(data);
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
	public List<ReceivableEntity> getReceivables(SportEntity sportEntity) {
		List<ReceivableEntity> filteredReceivables = new ArrayList<>();
		filteredReceivables.addAll(this.allReceivables.values());
		
		CollectionUtils.filter(filteredReceivables, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ReceivableEntity receivable = (ReceivableEntity) object;
				return(receivable.getSportId() == sportEntity.getId());
			}
		});
		
		return filteredReceivables;
	}
	
	/**
	 * @return
	 */
	public List<ReceivableEntity> getReceivables() {
		List<ReceivableEntity> filteredReceivables = new ArrayList<>();
		filteredReceivables.addAll(this.allReceivables.values());
		
		return filteredReceivables;
	}

	/**
	 * @param client
	 * @return
	 */
	public List<ReceivableEntity> getReceivables(ClientEntity client) {
		List<ReceivableEntity> filteredReceivables = new ArrayList<>();
		filteredReceivables.addAll(getReceivables());
		
		CollectionUtils.filter(filteredReceivables, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ReceivableEntity receivable = (ReceivableEntity) object;
				return(receivable.getClientId()==client.getId());
			}
		});
		
		return filteredReceivables;
	}
	
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(ReceivableEntity data) {
		this.fireDataModelChanged();
		return false;
	}

}
