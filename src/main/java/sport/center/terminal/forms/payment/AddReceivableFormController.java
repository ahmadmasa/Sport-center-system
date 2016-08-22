/**
 * 
 */
package sport.center.terminal.forms.payment;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sport.center.terminal.constants.EventListenSupport;
import sport.center.terminal.data.model.ReceivableDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.ReceivableEntity;
import sport.center.terminal.production.support.Constants;
import sport.center.terminal.support.Messages;

/**
 * @author Asendar
 *
 */
public class AddReceivableFormController implements Initializable {

	/**
	 * 
	 */
	private ClientEntity client;

	/**
	 * 
	 */
	private ReceivableEntity receivable;

	/**
	 * 
	 */
	@FXML
	private TextField txtAmmount, txtPayedAmmount, txtComment;

	/**
	 * 
	 */
	@FXML
	private DatePicker date;

	/**
	 * 
	 */
	@FXML
	private Button btnAdd;

	/**
	 * @param client
	 * @param receivable
	 */
	public void init(ClientEntity client, ReceivableEntity receivable) {
		this.receivable = receivable;
		this.client = client;

		addListener(txtAmmount, txtPayedAmmount, txtComment);

		date.setValue(new Date(new java.util.Date().getTime()).toLocalDate());

		btnAdd.setDisable(true);

		updateCheck();

	}

	/**
	 * 
	 */
	private void updateCheck() {
		if (receivable != null) {
			btnAdd.setText("Update");
			txtAmmount.setText(receivable.getReceivableAmmount() + "");
			txtPayedAmmount.setText(receivable.getPayedAmmount() + "");
			txtComment.setText(receivable.getComment());
			date.setValue(receivable.getReceivableDate().toLocalDate());
		}
	}

	/**
	 * @param txtFields
	 */
	private void addListener(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			txtField.textProperty().addListener((observable, oldValue, newValue) -> {

				if (newValue.replace(" ", "").equals("") || isEmpty(txtAmmount, txtPayedAmmount, txtComment)) {
					btnAdd.setDisable(true);
				} else {
					btnAdd.setDisable(false);
				}
			});
		}

	}

	/**
	 * @param txtFields
	 * @return
	 */
	private boolean isEmpty(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			if (txtField.getText().equals("")) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	/**
	 * 
	 */
	@FXML 
	private void addReceivable() {

		if (!canDoAction()) {
			Constants.instance.showNotificationTry(Messages.getString("Terminal.DEMO_ADD_RECEIVABLE"));
			return;
		}

		ReceivableEntity r = new ReceivableEntity(0,Date.valueOf(date.getValue()), client.getId(), Double.parseDouble(txtAmmount.getText()),
				Double.parseDouble(txtPayedAmmount.getText()), txtComment.getText(), false,client.getSportId());

		if (receivable != null) {
			r.setId(receivable.getId());
			ReceivableDataModel.instance.update(r);
			receivable = null;
			EventListenSupport.instance.firePropertyChange(EventListenSupport.EVN_RECEIVABLE_CHANGED);
			((PopOver) date.getScene().getWindow()).hide();
			return;
		}

		ReceivableDataModel.instance.add(new ReceivableEntity(0,Date.valueOf(date.getValue()), client.getId(),
				Double.parseDouble(txtAmmount.getText()), Double.parseDouble(txtPayedAmmount.getText()), txtComment.getText(), false,client.getSportId()));

		

		((PopOver) date.getScene().getWindow()).hide();

	}

	/**
	 * @return
	 */
	private boolean canDoAction() {
		return true;
	}
}