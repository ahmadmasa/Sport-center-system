package sport.center.terminal.forms.account;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.AccountDataModel;
import sport.center.terminal.data.model.TerminalUtilsDataModel;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.AccountEntity;
import sport.center.terminal.support.AvatarUtils;

/**
 * FXML Controller class
 *
 * @author Asendar
 */
public class AddAccountController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private TextField txtEmail, txtUsername;

	/**
	 * 
	 */
	@FXML
	private PasswordField txtPassword, txtRetypePassword, txtPasswordOld;

	/**
	 * 
	 */
	@FXML
	private ImageView imgAvatar;

	/**
	 * 
	 */
	@FXML
	private Button btnSignUp, btnProfilePicture;

	/**
	 * 
	 */
	private AccountEntity account;

	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		txtPasswordOld.setVisible(false);

		account = new AccountEntity();
		account.setId(0);
		account.setActive(true);
		account.setAvatar(AvatarUtils.getAvatarTitles().get(0));

		btnSignUp.setDisable(true);

		initValidator();
		iniAvatarMenu();
		addCreateAction();
	}

	/**
	 * @param account
	 */
	@PostConstruct
	public void init(AccountEntity account) {

		this.account = account;
		txtUsername.setText(account.getUsername());
		txtEmail.setText(account.getEmail());

		imgAvatar.setImage(account.getAvatar());

		btnSignUp.setText("Update");

		initValidator();
		iniAvatarMenu();
		addUpdateAction();
	}

	/**
	 * 
	 */
	private void addUpdateAction() {
		btnSignUp.setOnAction((e) -> {

			if (!account.getPassword().equals(txtPasswordOld.getText())) {
				txtPasswordOld.requestFocus();
				return;
			}

			account.setUsername(txtUsername.getText());
			account.setPassword(txtPassword.getText());
			account.setEmail(txtEmail.getText());

			TerminalUtilsDataModel.instance.setLoggedInUser(account);

			AccountDataModel.instance.update(account);

			((Stage) txtEmail.getScene().getWindow()).hide();

		});

	}

	/**
	 * 
	 */
	private void addCreateAction() {
		btnSignUp.setOnAction((e) -> {
			account.setUsername(txtUsername.getText());
			account.setPassword(txtPassword.getText());
			account.setEmail(txtEmail.getText());

			AccountDataModel.instance.add(account);

			((Stage) txtEmail.getScene().getWindow()).hide();

		});

	}

	/**
	 * 
	 */
	private void iniAvatarMenu() {
		imgAvatar.setImage(account.getAvatar());

		FXMLManager manager = new FXMLManager(FXMLConstants.CREATE_ACCOUNT_AVATAR_CHOOSER);

		ListView<String> avatarListView = new ListView<>();
		avatarListView.getItems().addAll(AvatarUtils.getAvatarTitles());

		avatarListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				ListCell<String> cell = new ListCell<String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setGraphic(null);

						if (item == null)
							return;

						ImageView img = new ImageView();
						img.setImage(AvatarUtils.getAvatar(item));

						setGraphic(img);

					}
				};
				return cell;
			}
		});

		avatarListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				account.setAvatar(newValue);
				imgAvatar.setImage(account.getAvatar());
			}
		});

		manager.getController(CreateAccountAvatarChooserController.class).init(avatarListView);

		btnProfilePicture.setOnAction((e) -> {
			Dialogs.showPopOver("Choose Profile Picture", manager.getRoot(), btnProfilePicture);
		});

	}

	/**
	 * 
	 */
	private void initValidator() {

		ValidationSupport support = new ValidationSupport();

		Validator<String> passwordValidator = new Validator<String>() {
			@Override
			public ValidationResult apply(Control control, String value) {
				boolean condition = !txtPassword.getText().replace(" ", "").equals("")
						&& txtPassword.getText().equals(txtRetypePassword.getText());

				if (condition && isValidated()) {
					btnSignUp.setDisable(false);
				} else {
					btnSignUp.setDisable(true);
				}

				return ValidationResult.fromMessageIf(control, "not matched", Severity.ERROR, !condition);
			}
		};

		support.registerValidator(txtRetypePassword, false, passwordValidator);

		txtEmail.textProperty().addListener((l) -> {
			if (isValidated()) {
				btnSignUp.setDisable(false);
			} else {
				btnSignUp.setDisable(true);
			}
		});

		txtUsername.textProperty().addListener((l) -> {
			if (isValidated()) {
				btnSignUp.setDisable(false);
			} else {
				btnSignUp.setDisable(true);
			}
		});
	}

	/**
	 * @return
	 */
	private boolean isValidated() {
		boolean isValid = true;
		isValid = !txtUsername.getText().replace(" ", "").equals("");

		if (!isValid)
			return false;

		isValid = !txtEmail.getText().replace(" ", "").equals("");

		if (!isValid)
			return false;

		isValid = !txtPassword.getText().replace(" ", "").equals("");

		if (!isValid)
			return false;

		isValid = !txtRetypePassword.getText().replace(" ", "").equals("");

		if (!isValid)
			return false;

		isValid = txtRetypePassword.getText().equals(txtPassword.getText());

		if (!isValid)
			return false;

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
