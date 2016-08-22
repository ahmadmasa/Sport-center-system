/**
 * 
 */
package sport.center.terminal.forms.account;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

/**
 * @author Asendar
 *
 */
public class CreateAccountAvatarChooserController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private BorderPane pnlAvatar;

	/**
	 * @param list
	 */
	@PostConstruct
	public void init(ListView<String> list) {
		pnlAvatar.setCenter(list);

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
