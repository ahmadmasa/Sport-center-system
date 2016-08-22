package sport.center.terminal.fxml.manager;

import javafx.stage.Stage;
import sport.center.terminal.jpa.repositories.JPAConfigurations;
import sport.center.terminal.login.LoginController;

/**
 * @author Asendar
 *
 */
public class FXMLConstants {


	/**
	 * 
	 */
	public static final String ADD_ACCOUNT                =  "AddAccount.fxml";
	/**
	 * 
	 */
	public static final String ADD_CLIENT_TO_EVENT        =  "AddClientToEvent.fxml";
	/**
	 * 
	 */
	public static final String ADD_RECEIVABLE_FORM        =  "AddReceivableForm.fxml";
	/**
	 * 
	 */
	public static final String ADD_SESSION_FORM           =  "AddSessionForm.fxml";
	/**
	 * 
	 */
	public static final String ADD_UPDATE_EVENT           =  "AddUpdateEvent.fxml";
	/**
	 * 
	 */
	public static final String ALL_CLIENTS_VIEW           =  "AllClientsView.fxml";

	/**
	 * 
	 */
	public static final String CLIENT_ADD                 =  "ClientAdd.fxml";
	/**
	 * 
	 */
	public static final String CLIENT_DETAILS_FORM        =  "ClientDetailsForm.fxml";
	/**
	 * 
	 */
	public static final String CLIENT_MAIN                =  "ClientMain.fxml";
	
	/**
	 * 
	 */
	public static final String SESSION_MISS_FORM           =  "SessionMissForm.fxml";
	
	/**
	 * 
	 */
	public static final String CLIENT_PAYMENT_FORM        =  "ClientPaymentForm.fxml";
	/**
	 * 
	 */
	public static final String EVENT_CLIENTS_VIEW         =  "EventClientsView.fxml";

	/**
	 * 
	 */
	public static final String SPORT_EVENT_DETAILS        =  "SportEventDetails.fxml";
	
	/**
	 * 
	 */
	public static final String LOADING_SCREEN             =  "LoadingScreen.fxml";
	/**
	 * 
	 */
	public static final String LOGIN                      =  "Login.fxml";
	/**
	 * 
	 */
	public static final String MAIN_SCREEN                =  "MainScreen.fxml";
	/**
	 * 
	 */
	public static final String SESSION_VIEW               =  "SessionView.fxml";
	/**
	 * 
	 */
	public static final String CLIENT_VIEW                =  "ClientView.fxml";
	/**
	 * 
	 */
	public static final String CLIENT_CHOOSER             =  "ClientsChooser.fxml";
	/**
	 * 
	 */
	public static final String ADD_SPORT_FORM             =  "AddSportForm.fxml";

	/**
	 * 
	 */
	public static final String MAIN                       =  "Main.fxml";
	/**
	 * 
	 */
	public static final String MAIN_TAB                   =  "MainTab.fxml";
	/**
	 * 
	 */
	public static final String REPORT_FORM                =  "ReportForm.fxml";
	/**
	 * 
	 */
	public static final String RESET_PASSWORD             =  "ResetPassword.fxml";
	/**
	 * 
	 */
	public static final String SPORT_MANAGER              =  "SportManager.fxml";
	/**
	 * 
	 */
	public static final String TRAY_NOTIFICATION          =  "TrayNotification.fxml";
	/**
	 * 
	 */
	public static final String UPDATE_SESSION_FORM        =  "UpdateSessionForm.fxml";

	/**
	 * 
	 */
	public static final String VIEW_ALL_RECEIVABLES_FORM  =  "ViewAllReceivablesForm.fxml";
	
	/**
	 * 
	 */
	public static final String PAYMENT_NOTIFICATION  =  "PaymentNotification.fxml";
	
	/**
	 * 
	 */
	public static final String ADD_UPDATE_NOTE  =  "AddUpdateNote.fxml";
	
	/**
	 * 
	 */
	public static final String LOGIN_ACCOUNT_CHOOSER  =  "LoginAccountChooser.fxml";
	
	/**
	 * 
	 */
	public static final String ACCOUNT_LIST_ITEM  =  "AccountListItem.fxml";
	
	/**
	 * 
	 */
	public static final String CREATE_ACCOUNT_AVATAR_CHOOSER  =  "CreateAccountAvatarChooser.fxml";
	
	/**
	 * 
	 */
	public static final String CLIENT_CARD  =  "ClientCard.fxml";
	
	/**
	 * 
	 */
	public static final String FINANCIAL_MANAGER  =  "FinancialManager.fxml";

	/**
	 * 
	 */
	private static Stage loginCommonStage;
	/**
	 * 
	 */
	private static FXMLManager manager = new FXMLManager(FXMLConstants.LOGIN);
	/**
	 * @return
	 */
	public static Stage getLoginCommonStage() {
		if (loginCommonStage == null)
			loginCommonStage = manager.getStage("Terminal Login");
		
		manager.getController(LoginController.class).init();
		
		loginCommonStage.setResizable(false);

		loginCommonStage.setOnCloseRequest((e) -> {
			JPAConfigurations.instance.kill();
		});

		return loginCommonStage;
	}
	
	
	
}
