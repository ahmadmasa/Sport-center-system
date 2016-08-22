package sport.center.terminal.database;

import sport.center.terminal.jpa.entities.*;

/**
 * @author Asendar
 *
 */
public class AddToDatabase extends RepositoryStore {

	/**
	 * 
	 */
	public static final AddToDatabase instance = new AddToDatabase();


	/**
	 * @param client
	 * @return
	 */
	public ClientEntity addClient(ClientEntity client) {
		return clientRepo.saveAndFlush(client);
	}

	/**
	 * @param session
	 * @return
	 */
	public SessionEntity addSession(SessionEntity session) {
		return sessionRepo.saveAndFlush(session);
	}

	/**
	 * @param sessionMisss
	 * @return
	 */
	public SessionMissEntity addSessionMiss(SessionMissEntity sessionMisss) {
		return sessionMissRepo.saveAndFlush(sessionMisss);
	}

	/**
	 * @param payment
	 * @return
	 */
	public PaymentEntity addPayment(PaymentEntity payment) {
		return paymentRepo.saveAndFlush(payment);
	}

	/**
	 * @param paymentNotification
	 * @return
	 */
	public PaymentNotificationEntity addPaymentNotification(PaymentNotificationEntity paymentNotification) {
		return paymentNotificationRepo.saveAndFlush(paymentNotification);
	}

	/**
	 * @param receivable
	 * @return
	 */
	public ReceivableEntity addReceivable(ReceivableEntity receivable) {
		return receivableRepo.saveAndFlush(receivable);
	}

	/**
	 * @param event
	 * @return
	 */
	public SportEventEntity addSportEvent(SportEventEntity event) {
		return sportEventRepo.saveAndFlush(event);
	}

	/**
	 * @param eventClient
	 * @return
	 */
	public SportEventClientEntity addSportEventClient(SportEventClientEntity eventClient) {
		return sportEventClientRepo.saveAndFlush(eventClient);
	}

	/**
	 * @param timeStamp
	 * @return
	 */
	public TerminalUtilsEntity addTimeStamp(java.sql.Date timeStamp) {
		TerminalUtilsEntity utilsObject = utilsRepo.getObject(1);
		utilsObject.setTimeStamp(timeStamp);
		return utilsRepo.update(utilsObject);
	}

	/**
	 * @param account
	 * @return
	 */
	public AccountEntity addAccount(AccountEntity account) {
		return accountRepo.saveAndFlush(account);
	}

	/**
	 * @param sport
	 * @return
	 */
	public SportEntity addSport(SportEntity sport) {
		return sportRepo.saveAndFlush(sport);
	}
	
	/**
	 * @param note
	 * @return
	 */
	public NoteEntity addNote(NoteEntity note) {
		return noteRepo.saveAndFlush(note);
	}
}
