package sport.center.terminal.forms.account;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.AccountDataModel;
import sport.center.terminal.jpa.entities.AccountEntity;
import sport.center.terminal.support.Messages;
import tray.notification.NotificationType;

/**
 * FXML Controller class
 *
 * @author Asendar
 */
public class ResetPasswordController implements Initializable {

	/**
	 * 
	 */
	private String VCode;

	/**
	 * 
	 */
	@FXML
	private TextField txtVCode;

	/**
	 * 
	 */
	@FXML
	private PasswordField txtPassword, txtReTypePassword;

	/**
	 * 
	 */
	@FXML
	private Button btnReset;
	/**
	 * 
	 */
	@FXML
	private Label lblConfirmEmail, lblEmail, lblVerification, lblPassword;

	/**
	 * 
	 */
	private AccountEntity userAccount;

	/**
	 * @param userAccount
	 * @param VCode
	 */
	public void init(AccountEntity userAccount, String VCode) {

		this.userAccount = userAccount;
		this.VCode = VCode;
		txtPassword.setDisable(true);
		txtReTypePassword.setDisable(true);
		btnReset.setDisable(true);

		addPasswordListener();
		addVCodeListener();
	}

	/**
	 * 
	 */
	private void addVCodeListener() {
		txtVCode.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.replace(" ", "").equals("") && newValue.equals(VCode)) {
				txtPassword.setDisable(false);
				lblVerification.setText("");
			} else {
				txtPassword.setDisable(true);
				lblVerification.setText("Invalid");
			}
		});
	}

	/**
	 * 
	 */
	private void addPasswordListener() {

		txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.replace(" ", "").equals("")) {
				txtReTypePassword.setDisable(false);
			} else {
				txtReTypePassword.setDisable(true);
			}
		});

		txtReTypePassword.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.replace(" ", "").equals("") && txtReTypePassword.getText().equals(txtPassword.getText())) {
				btnReset.setDisable(false);
				lblPassword.setText("");
			} else {
				btnReset.setDisable(true);
				lblPassword.setText("not matched");
			}
		});
	}

	/**
	 * 
	 */
	@FXML
	private void resetPassword() {
		if (txtPassword.getText().equals(txtReTypePassword.getText())) {

			userAccount.setPassword(txtPassword.getText());
			AccountDataModel.instance.update(userAccount);

			Dialogs.getNotificationTray(Messages.getString("ResetPasswordController.DONE"), //$NON-NLS-1$
					Messages.getString("ResetPasswordController.PASSWORD_RECOVERED"), NotificationType.SUCCESS) //$NON-NLS-1$
					.showAndDismiss(Duration.seconds(5));

			((PopOver) txtPassword.getScene().getWindow()).hide();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

}
