package sport.center.terminal.database;

import sport.center.terminal.jpa.entities.*;

/**
 * @author Asendar
 *
 */
public class DeleteFromDatabase extends RepositoryStore {

	/**
	 * 
	 */
	public static final DeleteFromDatabase instance = new DeleteFromDatabase();

	/**
	 * @param session
	 */
	public void deleteSession(SessionEntity session) {
		sessionRepo.delete(session);
	}

	/**
	 * @param entry
	 */
	public void deletePaymentNotification(PaymentNotificationEntity entry) {
		paymentNotificationRepo.delete(entry);
	}
	
	/**
	 * @param entry
	 */
	public void deletePayment(PaymentEntity entry) {
		paymentRepo.delete(entry);
	}
	
	/**
	 * @param entry
	 */
	public void deleteReceivable(ReceivableEntity entry) {
		receivableRepo.delete(entry);
	}

	/**
	 * @param eventClient
	 */
	public void deleteSportEventClient(SportEventClientEntity eventClient) {
		sportEventClientRepo.delete(eventClient);
	}

	/**
	 * @param account
	 */
	public void deleteAccount(AccountEntity account) {
		accountRepo.delete(account);
	}

	/**
	 * @param sport
	 */
	public void deleteSport(SportEntity sport) {
		sportRepo.delete(sport);
	}
	
	/**
	 * @param note
	 */
	public void deleteNote(NoteEntity note) {
		noteRepo.delete(note);
	}

}
