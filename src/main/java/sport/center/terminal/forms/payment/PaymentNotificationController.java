/**
 * 
 */
package sport.center.terminal.forms.payment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.jpa.entities.PaymentNotificationEntity;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class PaymentNotificationController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private Label lblClientName, lblMessage, paymentAction, lblSportName;

	/**
	 * 
	 */
	@FXML
	private Pane pnlPriority;

	/**
	 * @param notification
	 */
	@PostConstruct
	public void init(PaymentNotificationEntity notification) {

		lblClientName.setText(ClientDataModel.instance.getClientbyId(notification.getClientId()).getName());

		Date date = notification.getPaymenDate();
		lblMessage.setText(new SimpleDateFormat("MM-dd-yyyy").format(date).toString());


		LocalDate Localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate now = LocalDate.now();
		
		if(Localdate.plusDays(5).isBefore(now))
			pnlPriority.setStyle("-fx-background-color: #D32F2F");
		else
			pnlPriority.setStyle("-fx-background-color: #51c93e");
		
		try {
			SportEntity sport = SportDataModel.instance.getSport(notification.getSportId());
			lblSportName.setText(sport.getSportName());
		} catch (Exception e) {
			lblSportName.setText(" - ");
		}
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}
