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
import sport.center.terminal.jpa.entities.PaymentNotificationEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class PaymentNotificationDataModel extends DataModelChangeSupport implements AbstractDataModel<PaymentNotificationEntity> {

	/**
	 * 
	 */
	public static final PaymentNotificationDataModel instance = new PaymentNotificationDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(PaymentNotificationDataModel.class);

	/**
	 * 
	 */
	Map<Long, PaymentNotificationEntity> allPaymentNotifications = new HashMap<>();

	/**
	 * 
	 */
	private PaymentNotificationDataModel() {
		for (PaymentNotificationEntity PaymentNotifications : GetFromDatabase.instance.getPaymentNotifications()) {
			allPaymentNotifications.put(PaymentNotifications.getId(), PaymentNotifications);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(PaymentNotificationEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allPaymentNotifications.put(data.getId(), data);
			AddToDatabase.instance.addPaymentNotification(data);
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
	public boolean delete(PaymentNotificationEntity data) {
		try {

			this.allPaymentNotifications.remove(data.getId());
			DeleteFromDatabase.instance.deletePaymentNotification(data);
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
	public boolean update(PaymentNotificationEntity data) {
		return false;
	}


	/**
	 * @param sportEntity
	 * @return
	 */
	public List<PaymentNotificationEntity> getPaymentNotifications(SportEntity sportEntity) {
		List<PaymentNotificationEntity> filteredPaymentNotifications = new ArrayList<>();
		filteredPaymentNotifications.addAll(getPaymentNotifications());
		
		CollectionUtils.filter(filteredPaymentNotifications, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentNotificationEntity PaymentNotification = (PaymentNotificationEntity) object;
				return(PaymentNotification.getSportId() == sportEntity.getId());
			}
		});
		
		return filteredPaymentNotifications;
	}
	
	/**
	 * @return
	 */
	public List<PaymentNotificationEntity> getPaymentNotifications() {
		List<PaymentNotificationEntity> filteredPaymentNotifications = new ArrayList<>();
		filteredPaymentNotifications.addAll(this.allPaymentNotifications.values());
		
		
		Collections.sort(filteredPaymentNotifications,new Comparator<PaymentNotificationEntity>() {

			@Override
			public int compare(PaymentNotificationEntity o1, PaymentNotificationEntity o2) {
				// TODO Auto-generated method stub
				return -o1.getPaymenDate().compareTo(o2.getPaymenDate());
			}
		});
		
		
		return filteredPaymentNotifications;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public PaymentNotificationEntity getPaymentNotification(long id) {
		return this.allPaymentNotifications.get(id);
	}

	
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(PaymentNotificationEntity data) {
		this.fireDataModelChanged();
		return false;
	}

}
