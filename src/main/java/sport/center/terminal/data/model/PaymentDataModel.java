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
import sport.center.terminal.database.DeleteFromDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.PaymentEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class PaymentDataModel extends DataModelChangeSupport implements AbstractDataModel<PaymentEntity> {

	/**
	 * 
	 */
	public static final PaymentDataModel instance = new PaymentDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(PaymentDataModel.class);

	/**
	 * 
	 */
	Map<Long, PaymentEntity> allPayments = new HashMap<>();

	/**
	 * 
	 */
	private PaymentDataModel() {
		
		for (PaymentEntity Payment : GetFromDatabase.instance.getPayments()) {
			allPayments.put(Payment.getId(), Payment);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(PaymentEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allPayments.put(data.getId(), data);
			AddToDatabase.instance.addPayment(data);
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
	public boolean delete(PaymentEntity data) {
		try {

			this.allPayments.remove(data.getId());
			DeleteFromDatabase.instance.deletePayment(data);
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
	public boolean update(PaymentEntity data) {
		try {

			this.allPayments.put(data.getId(), data);
			UpdateDatabase.instance.updatePayment(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/**
	 * @param client
	 * @return
	 */
	public List<PaymentEntity> getMonthlyPayments(ClientEntity client) {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(getPayments());
		
		CollectionUtils.filter(filteredPayments, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentEntity payment = (PaymentEntity) object;
				return(payment.getClientId() == client.getId() && payment.getPaymentType().equals(PaymentEntity.PaymentType.MONTHLY));
			}
		});
		
		return filteredPayments;
	}

	/**
	 * @param client
	 * @return
	 */
	public List<PaymentEntity> getRegularPayments(ClientEntity client) {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(getPayments());
		
		CollectionUtils.filter(filteredPayments, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentEntity payment = (PaymentEntity) object;
				return(payment.getClientId() == client.getId() && payment.getPaymentType().equals(PaymentEntity.PaymentType.REGULAR));
			}
		});
		
		return filteredPayments;
	}
	
	/**
	 * @return
	 */
	public List<PaymentEntity> getRegularPayments() {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(getPayments());
		
		CollectionUtils.filter(filteredPayments, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentEntity payment = (PaymentEntity) object;
				return(payment.getPaymentType().equals(PaymentEntity.PaymentType.REGULAR));
			}
		});
		
		return filteredPayments;
	}

	/**
	 * @return
	 */
	public List<PaymentEntity> getMonthlyPayments( ) {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(getPayments());
		
		CollectionUtils.filter(filteredPayments, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentEntity payment = (PaymentEntity) object;
				return(payment.getPaymentType().equals(PaymentEntity.PaymentType.MONTHLY));
			}
		});
		
		return filteredPayments;
	}
	
	/**
	 * @param sportEntity
	 * @return
	 */
	public List<PaymentEntity> getPayments(SportEntity sportEntity) {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(getPayments());
		
		CollectionUtils.filter(filteredPayments, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentEntity payment = (PaymentEntity) object;
				return(payment.getSportId()==sportEntity.getId());
			}
		});
		
		return filteredPayments;
	}
	
	/**
	 * @return
	 */
	public List<PaymentEntity> getPayments() {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(allPayments.values());
		
		java.util.Collections.sort(filteredPayments, new Comparator<PaymentEntity>() {

			@Override
			public int compare(PaymentEntity o1, PaymentEntity o2) {

				return o1.getPaymentDate().compareTo(o2.getPaymentDate());
			}
		});
		
		return filteredPayments;
	}
	
	/**
	 * @param client
	 * @return
	 */
	public List<PaymentEntity> getPayments(ClientEntity client) {
		List<PaymentEntity> filteredPayments = new ArrayList<>();
		filteredPayments.addAll(getPayments());
		
		CollectionUtils.filter(filteredPayments, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				PaymentEntity payment = (PaymentEntity) object;
				return(payment.getClientId()==client.getId());
			}
		});
		
		return filteredPayments;
	}
	
	
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(PaymentEntity data) {
		this.fireDataModelChanged();
		return false;
	}

}
