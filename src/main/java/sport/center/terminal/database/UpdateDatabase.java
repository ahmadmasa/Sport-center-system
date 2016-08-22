package sport.center.terminal.database;

import sport.center.terminal.jpa.entities.*;

/**
 * @author Asendar
 *
 */
public class UpdateDatabase extends RepositoryStore {

	/**
	 * 
	 */
	public static final UpdateDatabase instance = new UpdateDatabase();

	/**
	 * @param client
	 */
	public void updateClient(ClientEntity client) {
		clientRepo.update(client);
	}

	/**
	 * @param session
	 */
	public void updateSession(SessionEntity session) {
		sessionRepo.update(session);
	}

	/**
	 * @param eventClient
	 */
	public void updateEventClient(SportEventClientEntity eventClient) {
		sportEventClientRepo.update(eventClient);
	}

	/**
	 * @param event
	 */
	public void updateSportEvent(SportEventEntity event) {
		sportEventRepo.update(event);
	}

	/**
	 * @param sport
	 */
	public void updateSport(SportEntity sport) {
		sportRepo.update(sport);
	}

	/**
	 * @param sessionMiss
	 */
	public void updateSessionMiss(SessionMissEntity sessionMiss) {
		sessionMissRepo.update(sessionMiss);
	}

	/**
	 * @param payment
	 */
	public void updatePayment(PaymentEntity payment) {
		paymentRepo.update(payment);
	}

	/**
	 * @param receivable
	 */
	public void updateReceivable(ReceivableEntity receivable) {
		receivableRepo.update(receivable);
	}

	/**
	 * @param theme
	 */
	public void updateTheme(String theme) {
		TerminalUtilsEntity utilsObject = utilsRepo.getObject(1);
		utilsObject.setTheme(theme);
		utilsRepo.update(utilsObject);
	}
	
	/**
	 * @param id
	 */
	public void updateLastLogin(long id) {
		TerminalUtilsEntity utilsObject = utilsRepo.getObject(1);
		utilsObject.setLastLogin(id);
		utilsRepo.update(utilsObject);
	}

	/**
	 * @param account
	 */
	public void updateAccount(AccountEntity account) {
		accountRepo.update(account);
	}
	
	/**
	 * @param note
	 */
	public void updateNote(NoteEntity note) {
		noteRepo.update(note);
	}
}
