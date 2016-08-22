package sport.center.terminal.data.model.support;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.PaymentNotificationDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.TerminalUtilsDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.PaymentNotificationEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 *
 * @author Asendar
 */
/**
 * 
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class ClientNotifyManager {

	/**
	 * 
	 */
	public static final ClientNotifyManager instance = new ClientNotifyManager();

	/**
	 * @param client
	 * @param sport
	 * @return
	 */
	private List<PaymentNotificationEntity> getClientNotifyObjects(ClientEntity client,SportEntity sport) {
		List<PaymentNotificationEntity> notifyObjects = new ArrayList<>();

		if (PaymentNotificationDataModel.instance.getPaymentNotifications(sport) != null) {
			for (PaymentNotificationEntity object : PaymentNotificationDataModel.instance
					.getPaymentNotifications(sport)) {
				if (object.getClientId() == client.getId()) {
					notifyObjects.add(object);
				}
			}
		}

		return notifyObjects;
	}

	/**
	 * 
	 */
	public void setNotifyObjectsForAllSports(){
		for(SportEntity sport : SportDataModel.instance.getAllActiveSports()){
			setNotifyObjects(sport);
		}
		
		TerminalUtilsDataModel.instance.addTimeStamp();
	}
	
	/**
	 * @param sport
	 */
	private void setNotifyObjects(SportEntity sport) {

		if (TerminalUtilsDataModel.instance.getTimeStamp() == null
				|| TerminalUtilsDataModel.instance.getTimeStamp().after(new Date())) {
			TerminalUtilsDataModel.instance.addTimeStamp();
		}

		LocalDate lastDate = TerminalUtilsDataModel.instance.getTimeStamp().toLocalDate();

		LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (ClientDataModel.instance.getActiveClients(sport) != null) {
			for (ClientEntity client : ClientDataModel.instance.getActiveClients(sport)) {
				for (LocalDate date = lastDate.plusDays(1); !date.equals(today.plusDays(1)); date = date.plusDays(1)) {
					if (client.getMonthlyPaymentDate().toLocalDate().getDayOfMonth() == date.getDayOfMonth()) {
						if (!hasNotification(client,sport,
								Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))) {

							PaymentNotificationEntity PaymentNotificationObject = new PaymentNotificationEntity();
							PaymentNotificationObject.setClientId(client.getId());
							PaymentNotificationObject.setSportId(sport.getId());
							PaymentNotificationObject.setPaymenDate(
									Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

							PaymentNotificationDataModel.instance.add(PaymentNotificationObject);

						}
					}

				}
			}
		}
	}

	/**
	 * @param client
	 * @param sport
	 * @param date
	 * @return
	 */
	private boolean hasNotification(ClientEntity client,SportEntity sport, Date date) {
		List<PaymentNotificationEntity> allObj = getClientNotifyObjects(client,sport);

		if (allObj == null) {
			return false;
		}

		for (PaymentNotificationEntity obj : allObj) {
			if (obj.getPaymenDate().equals(date)) {
				return true;
			}
		}
		return false;
	}
}
