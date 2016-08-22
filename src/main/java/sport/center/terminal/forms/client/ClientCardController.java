/**
 * 
 */
package sport.center.terminal.forms.client;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;

/**
 * @author Asendar
 *
 */
public class ClientCardController implements Initializable {

	/**
	 * 
	 */
	@FXML
	/**
	 * @return
	 */
	@Getter private AnchorPane printPane;

	/**
	 * 
	 */
	@FXML
	private Label lblClientName, lblClientHumanReadableId, lblClientSport, lblClientBelt;

	/**
	 * @param client
	 */
	@PostConstruct
	public void init(ClientEntity client) {
		lblClientName.setText(client.getName());
		lblClientHumanReadableId.setText(client.getHumanReadableId() + "");
		lblClientSport.setText(SportDataModel.instance.getSport(client.getSportId()).getSportName());
		lblClientBelt.setText(client.getBelt());
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
