package sport.center.terminal.forms.payment;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.constants.EventListenSupport;
import sport.center.terminal.constants.NodePrinter;
import sport.center.terminal.data.model.PaymentDataModel;
import sport.center.terminal.data.model.ReceivableDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.PaymentEntity;
import sport.center.terminal.jpa.entities.ReceivableEntity;
import sport.center.terminal.production.support.Constants;
import sport.center.terminal.support.Messages;

/**
 * FXML Controller class
 *
 * @author Asendar
 */
public class ClientPaymentFormController implements Initializable {

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
	private boolean monthlyPayment;

	/**
	 * 
	 */
	@FXML
	private Button btnPrintPreview, btnBack, btnPrint;

	/**
	 * 
	 */
	@FXML
	private TextField txtAmmountDigits, txtName, txtAmmountText, txtComment;

	/**
	 * 
	 */
	@FXML
	private DatePicker txtDate;

	/**
	 * 
	 */
	@FXML
	private Label lblAmmountDigits, lblDate, lblName, lblAmmountText, lblComment;

	/**
	 * 
	 */
	@FXML
	private Pane printPane;

	/**
	 * @param client
	 * @param receivable
	 * @param monthlyPayment
	 */
	public void init(ClientEntity client, ReceivableEntity receivable,boolean monthlyPayment) {
		this.client = client;
		this.receivable = receivable;
		this.monthlyPayment=monthlyPayment;

		btnPrint.setDisable(true);
		btnPrintPreview.setDisable(true);
		btnBack.setVisible(false);
		fillData();

		addListeners(txtAmmountDigits, txtName, txtAmmountText, txtComment);
	}

	/**
	 * @param txtFields
	 */
	private void addListeners(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			txtField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (txtField.getText().equals(" ")) {
					txtField.setText(""); 
				}

				if (!isEmpty(txtAmmountDigits, txtName, txtAmmountText, txtComment)) {
					btnPrintPreview.setDisable(false);
					btnPrint.setDisable(false);
				} else {
					btnPrintPreview.setDisable(true);
					btnPrint.setDisable(true);
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

	/**
	 * 
	 */
	private void fillData() {
		if (monthlyPayment) { 
			txtAmmountDigits.setText(client.getMonthlyPayment() + ""); 
		} else {
			txtAmmountDigits.setText(0 + ""); 
		}
		txtDate.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		txtName.setText(client.getName());
		txtAmmountText.setText(""); 
		txtComment.setText(""); 
	}

	/**
	 * @param txtFields
	 */
	private void hideAllTxtFields(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			txtField.setVisible(false);
		}
	}

	/**
	 * @param txtFields
	 */
	private void showAllTxtFields(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			txtField.setVisible(true);
		}
	}

	/**
	 * @param e
	 */
	@FXML 
	private void printPreviewAction(ActionEvent e) {
		hideAllTxtFields(txtAmmountDigits, txtName, txtAmmountText, txtComment);
		txtDate.setVisible(false);

		lblName.setText(lblName.getText() + " " + txtName.getText()); 
		lblAmmountText.setText(lblAmmountText.getText() + " " + txtAmmountText.getText()); 
		lblComment.setText(lblComment.getText() + " " + txtComment.getText()); 
		lblAmmountDigits.setText(lblAmmountDigits.getText() + " " + txtAmmountDigits.getText()); 
		lblDate.setText(lblDate.getText() + " " + txtDate.getValue()); 

		btnPrintPreview.setVisible(false);
		btnBack.setVisible(true);
	}

	/**
	 * @return
	 */
	@Getter private boolean sucess;
	
	/**
	 * @param e
	 */
	@FXML 
	private void printAction(ActionEvent e) {
		
		sucess = false;
		
		if (!canDoAction()) {
			Constants.instance.showNotificationTry(Messages.getString("Terminal.DEMO_ADD_PAYMENT"));
			return;
		}

		Alert alert = Dialogs.getConfirmDialog(Messages.getString("ClientPaymentFormController.WARNING"), "",
				Messages.getString("ClientPaymentFormController.CANT_BE_UNDONE")); 

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.CANCEL) {
			return;
		}

		backAction(null);
		printPreviewAction(null);

		if (NodePrinter.printForm(printPane,(Stage)btnBack.getScene().getWindow(),NodePrinter.getPaymentPaper())) {

			PaymentEntity payment = new PaymentEntity();
			payment.setClientId(this.client.getId());
			payment.setAmmount(Double.parseDouble(txtAmmountDigits.getText()));
			payment.setPaymentDate(java.sql.Date.valueOf(txtDate.getValue()));
			payment.setComment(txtComment.getText());
			payment.setPaymentType(this.monthlyPayment?PaymentEntity.PaymentType.MONTHLY:PaymentEntity.PaymentType.REGULAR);
			payment.setNotify(false);
			payment.setSportId(this.client.getSportId());
			
			PaymentDataModel.instance.add(payment);

			sucess = true;
			
			((Stage) txtAmmountDigits.getScene().getWindow()).close();

			if (receivable != null) {
				receivable.setPayed(true);
				ReceivableDataModel.instance.update(receivable);
			}
			receivable = null;

			EventListenSupport.instance.firePropertyChange(EventListenSupport.EVN_PAYMENT_CHANGED);
			
			
		}
	}

	/**
	 * @param e
	 */
	@FXML 
	private void backAction(ActionEvent e) {
		btnBack.setVisible(false);
		btnPrintPreview.setVisible(true);

		txtDate.setVisible(true);
		showAllTxtFields(txtAmmountDigits, txtName, txtAmmountText, txtComment);
		lblName.setText(lblName.getText().replace(" " + txtName.getText(), ""));  
		lblAmmountText.setText(lblAmmountText.getText().replace(" " + txtAmmountText.getText(), ""));  
		lblComment.setText(lblComment.getText().replace(" " + txtComment.getText(), "")); 
		lblAmmountDigits.setText(lblAmmountDigits.getText().replace(" " + txtAmmountDigits.getText(), ""));  
		lblDate.setText(lblDate.getText().replace(" " + txtDate.getValue(), ""));  

	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	/**
	 * @return
	 */
	private boolean canDoAction() {

		return true;

	}

}
