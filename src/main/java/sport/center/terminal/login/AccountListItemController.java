/**
 * 
 */
package sport.center.terminal.login;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sport.center.terminal.jpa.entities.AccountEntity;

/**
 * @author Asendar
 *
 */
public class AccountListItemController implements Initializable{
	/**
	 * 
	 */
	@FXML
	private Label lblName, lblEmail;

	/**
	 * 
	 */
	@FXML
	private ImageView imgAvatar;

	/**
	 * @param account
	 */
	@PostConstruct
	public void init(AccountEntity account){
		lblName.setText(account.getUsername());
		lblEmail.setText(account.getEmail());
		imgAvatar.setImage(account.getAvatar());
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
