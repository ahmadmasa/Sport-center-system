package sport.center.terminal.database;


import java.util.ArrayList;
import java.util.List;

import sport.center.terminal.jpa.entities.AccountEntity;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.NoteEntity;
import sport.center.terminal.jpa.entities.PaymentEntity;
import sport.center.terminal.jpa.entities.PaymentNotificationEntity;
import sport.center.terminal.jpa.entities.ReceivableEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SessionMissEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.jpa.entities.SportEventClientEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;
import sport.center.terminal.jpa.entities.TerminalUtilsEntity;


/**
 * @author Asendar
 *
 */
public class GetFromDatabase extends RepositoryStore {

	/**
	 * 
	 */
	public static GetFromDatabase instance = new GetFromDatabase();

	/**
	 * @return
	 */
	public List<SportEventEntity> getSportEvents() {
		return sportEventRepo.findAll();

	}

	/**
	 * @return
	 */
	public List<PaymentEntity> getPayments(){
		return paymentRepo.findAll();
	}
	/**
	 * @param client
	 * @return
	 */
	public List<SportEventClientEntity> getEventClient(ClientEntity client) {
		
		List<SportEventClientEntity> eventClients = new ArrayList<>();


		if(sportEventClientRepo.findAll()!=null)
		for(SportEventClientEntity event: sportEventClientRepo.findAll()){
			if(event.getClientId() == client.getId())
				eventClients.add(event);
		}

		return eventClients;
	}

	/**
	 * @param event
	 * @return
	 */
	public List<SportEventClientEntity> getSportEventClient(SportEventEntity event) {
		List<SportEventClientEntity> eventClients = new ArrayList<>();


		if(sportEventClientRepo.findAll()!=null)
		for(SportEventClientEntity eventClient: sportEventClientRepo.findAll()){
			if(eventClient.getEventId() == event.getId())
				eventClients.add(eventClient);
		}

		return eventClients;
	}
	
	/**
	 * @return
	 */
	public List<SessionMissEntity> getSessionMisses() {
		return sessionMissRepo.findAll();
	}

	/**
	 * @return
	 */
	public List<ClientEntity> getClients() {
		return clientRepo.findAll();
	}
	
	/**
	 * @return
	 */
	public List<SportEntity> getSports() {
		return sportRepo.findAll();
	}
	/**
	 * @return
	 */
	public List<SessionEntity> getSessions() {
		return sessionRepo.findAll();
	}
	
	/**
	 * @return
	 */
	public List<ReceivableEntity> getReceivables() {
		return receivableRepo.findAll();
	}
	/**
	 * @return
	 */
	public List<PaymentNotificationEntity> getPaymentNotifications() {
		return paymentNotificationRepo.findAll();
	}
	/**
	 * @return
	 */
	public List<SportEventClientEntity> getSportEventClients() {
		return sportEventClientRepo.findAll();
	}
	/**
	 * @return
	 */
	public java.sql.Date getTimeStamp() {
		return utilsRepo.getObject(1).getTimeStamp();
	}

	/**
	 * @return
	 */
	public String getTheme() {
		if (utilsRepo.getObject(1).getTheme() != null)
			return utilsRepo.getObject(1).getTheme();

		return "theme_white";

	}

	/**
	 * @return
	 */
	public TerminalUtilsEntity getTerminalutilsObject() {
		return utilsRepo.getObject(1);
	}

	/**
	 * @return
	 */
	public List<AccountEntity> getAccounts() {
		return accountRepo.findAll();
	}
	
	/**
	 * @return
	 */
	public List<NoteEntity> getNotes() {
		return noteRepo.findAll();
	}
}
