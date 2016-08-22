package sport.center.terminal.database;


import sport.center.terminal.jpa.repositories.*;

/**
 * @author Asendar
 *
 */
public class RepositoryStore {
	/**
	 * 
	 */
	final ClientRepository clientRepo = new ClientRepository();
	/**
	 * 
	 */
	final SessionRepository sessionRepo = new SessionRepository();
	/**
	 * 
	 */
	final SessionMissRepository sessionMissRepo = new SessionMissRepository();
	/**
	 * 
	 */
	final PaymentRepository paymentRepo = new PaymentRepository();
	/**
	 * 
	 */
	final PaymentNotificationRepository paymentNotificationRepo = new PaymentNotificationRepository();
	/**
	 * 
	 */
	final ReceivableRepository receivableRepo = new ReceivableRepository();
	/**
	 * 
	 */
	final SportEventRepository sportEventRepo = new SportEventRepository();
	/**
	 * 
	 */
	final SportEventClientRepository sportEventClientRepo = new SportEventClientRepository();
	/**
	 * 
	 */
	final AccountRepository accountRepo = new AccountRepository();
	/**
	 * 
	 */
	final SportRepository sportRepo = new SportRepository();
	/**
	 * 
	 */
	final TerminalUtilsRepository utilsRepo = new TerminalUtilsRepository();
	/**
	 * 
	 */
	final NoteRepository noteRepo = new NoteRepository();
}
