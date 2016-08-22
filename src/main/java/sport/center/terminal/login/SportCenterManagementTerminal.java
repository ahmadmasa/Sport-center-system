package sport.center.terminal.login;

import java.io.IOException;

import com.sun.javafx.css.StyleManager;

import javafx.application.Application;
import javafx.stage.Stage;
import sport.center.terminal.data.model.AccountDataModel;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.PaymentDataModel;
import sport.center.terminal.data.model.PaymentNotificationDataModel;
import sport.center.terminal.data.model.ReceivableDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SessionMissDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.data.model.TerminalUtilsDataModel;
import sport.center.terminal.data.model.support.ClientNotifyManager;
import sport.center.terminal.fxml.manager.FXMLConstants;

/**
 *
 * @author Asendar
 */
public class SportCenterManagementTerminal extends Application {

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		StyleManager.getInstance().addUserAgentStylesheet("/styles/theme.css");
		loadData();
		showScreen();
	}

	/**
	 * @param args
	 *  the command line arguments
	 */

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * 
	 */
	private void loadData() {
		AccountDataModel.instance.getAccounts();
		ClientDataModel.instance.getAllClients();
		PaymentDataModel.instance.getPayments();
		PaymentNotificationDataModel.instance.getPaymentNotifications();
		ReceivableDataModel.instance.getReceivables();
		SessionDataModel.instance.getSessions();
		SessionMissDataModel.instance.getSessionMisses();
		SportDataModel.instance.getAllSports();
		SportEventClientDataModel.instance.getSportEventClients();
		SportEventDataModel.instance.getSportEvents();
		TerminalUtilsDataModel.instance.getTimeStamp();
		ClientNotifyManager.instance.setNotifyObjectsForAllSports();
	}

	/**
	 * @throws IOException
	 */
	private void showScreen() throws IOException {
		
		FXMLConstants.getLoginCommonStage().show();

	}

}
