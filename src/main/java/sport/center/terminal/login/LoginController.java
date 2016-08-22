package sport.center.terminal.login;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.PopOver;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.AccountDataModel;
import sport.center.terminal.data.model.TerminalUtilsDataModel;
import sport.center.terminal.forms.account.AddAccountController;
import sport.center.terminal.forms.account.ResetPasswordController;
import sport.center.terminal.forms.account.SendMailSSL;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.AccountEntity;
import sport.center.terminal.jpa.repositories.JPAConfigurations;
import sport.center.terminal.main.MainScreenController;
import sport.center.terminal.support.Messages;
import tray.notification.NotificationType;

/**
 * FXML Controller class
 *
 * @author Asendar
 */
public class LoginController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private Button btnLogin, btnSwitch;

	/**
	 * 
	 */
	@FXML
	private PasswordField txtPassword;

	/**
	 * 
	 */
	@FXML
	private Label lblResetPassword, lblUsername, lblEmail, lblSignUp;

	/**
	 * 
	 */
	@FXML
	private ImageView imgAvatar;
	

	/**
	 * 
	 */
	private AccountEntity account;

	/**
	 * 
	 */
	public void init() {
		btnSwitch.requestFocus();
		
		while (AccountDataModel.instance.getAccounts() == null || AccountDataModel.instance.getAccounts().isEmpty()) {
			Dialogs.getNotificationTray("Welcome To Terminal", "Create Your Account !", NotificationType.INFORMATION)
			.showAndDismiss(Duration.seconds(10));
			
			
			FXMLManager manager = new FXMLManager(FXMLConstants.ADD_ACCOUNT);
			manager.getController(AddAccountController.class).init();
			Stage stage = manager.getStage("ِAdd new account");
			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setOnCloseRequest((final WindowEvent event) -> {
				JPAConfigurations.instance.kill();
				event.consume();
				System.exit(0);
			});

			stage.showAndWait();
		}
		
		prepareAccount();
		

		txtPassword.setOnKeyPressed((KeyEvent ke) -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				try {
					loginAction(null);
				} catch (IOException ex) {
					Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		
		
		btnSwitch.setOnAction((e)->{
			FXMLManager manager = new FXMLManager(FXMLConstants.LOGIN_ACCOUNT_CHOOSER);
			LoginAccountChooserController controller =  manager.getController(LoginAccountChooserController.class);
			controller.init();
			
			PopOver popOver = Dialogs.showPopOver("Select an account", manager.getRoot(), btnSwitch);
			
			popOver.setOnHidden((h)->{
				account = controller.getValue()!=null?controller.getValue():account;
				setAccountDetails();
			});
			
			
		});
		
		addAccountMainActions();
		
	}

	/**
	 * 
	 */
	private void prepareAccount(){
		if(TerminalUtilsDataModel.instance.getLastLogin()==null){
			this.account = AccountDataModel.instance.getAccounts().get(0);
		}else{
			this.account = TerminalUtilsDataModel.instance.getLastLogin();
		}

		setAccountDetails();
		
		
	}
	
	/**
	 * 
	 */
	private void setAccountDetails(){
		this.lblUsername.setText(account.getUsername());
		this.lblEmail.setText(account.getEmail());
		this.imgAvatar.setImage(account.getAvatar());
	}
	
	
	/**
	 * @param e
	 * @throws IOException
	 */
	@FXML 
	private void loginAction(ActionEvent e) throws IOException {
		try {
			if (isValidated()) {
				FXMLConstants.getLoginCommonStage().close();
				showMainStage();

				txtPassword.clear();


			} else {
				txtPassword.requestFocus();
			}

		} catch (Exception ex) {
			Dialogs.showExceptionDialog(ex);
		}
	}

	/**
	 * @return
	 */
	private boolean isValidated() {
		if (account.getPassword()==null || account.getPassword().equals(txtPassword.getText())) {
			TerminalUtilsDataModel.instance.setLoggedInUser(account);
			TerminalUtilsDataModel.instance.setLastLogin(account.getId());
			return true;
		}

		return false;
	}


	/**
	 * 
	 */
	private PopOver reset;
	/**
	 * 
	 */
	private void addAccountMainActions(){
		lblResetPassword.setOnMouseClicked((e)->{
			FXMLManager manager = new FXMLManager(FXMLConstants.RESET_PASSWORD);
			
			if(VCode.equals(""))
				sendResetEmail();
			
			manager.getController(ResetPasswordController.class).init(account,VCode);
			
			
			
			
			if(reset!=null){
				reset.show(lblResetPassword);
				return;
			}
			reset = Dialogs.showSeparatedPopOver("Resetpassword", manager.getRoot(), lblResetPassword);
			
		
		});
		
		
		lblSignUp.setOnMouseClicked((e)->{
			FXMLManager manager = new FXMLManager(FXMLConstants.ADD_ACCOUNT);
			manager.getController(AddAccountController.class).init();
			Stage stage = manager.getStage("Add Account");
			stage.show();
		
		});
	}

	/**
	 * @return
	 */
	private int randomCodeGenerator() {
		return (int) (Math.random() * 1000000);
	}

	/**
	 * 
	 */
	private String VCode ="";
	
	/**
	 * 
	 */
	private void sendResetEmail() {

        try {
            int code = randomCodeGenerator();
            VCode=code+"";
            new Thread(() -> {
                SendMailSSL.sendEmail(code + "", this.account.getEmail()); 

                Platform.runLater(() -> {
                    Dialogs.getNotificationTray(Messages.getString("ResetPasswordController.EMAIL_SENT"), Messages.getString("ResetPasswordController.CHECK_EMAIL"),NotificationType.NOTICE).showAndDismiss(Duration.seconds(8)); 
                });
            }).start();

        } catch (Exception ex) {
            Dialogs.getNotificationTray(Messages.getString("ResetPasswordController.ERROR"), Messages.getString("ResetPasswordController.EMAIL_NOT_SENT"),NotificationType.WARNING).showAndDismiss(Duration.seconds(8)); 
        }

    }
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	/**
	 * @throws IOException
	 */
	private void showMainStage() throws IOException {

		try {
			FXMLManager manager = new FXMLManager(FXMLConstants.MAIN_SCREEN);
			Stage stage = manager.getStage(Messages.getString("MainScreen.TERMINAL"));
			MainScreenController controller =  manager.getController(MainScreenController.class);
			controller.init();
			stage.setMaximized(true);

			stage.setOnCloseRequest((final WindowEvent event) -> {
				event.consume();
				Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainScreen.EXIT"), "",
						Messages.getString("MainScreen.EXIT_CONFIRM"));

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					System.exit(0);
				}
			});

			stage.show();

		} catch (Exception ex) {
			Dialogs.showExceptionDialog(ex);

		}

	}
}
