/**
 * 
 */
package sport.center.terminal.forms.session;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import sport.center.terminal.data.model.SessionMissDataModel;
import sport.center.terminal.jpa.entities.SessionMissEntity;

/**
 * @author Asendar
 *
 */
public class SessionMissFormController implements Initializable{

	/**
	 * 
	 */
	@FXML
	private TextField txtComment;
	/**
	 * 
	 */
	@FXML
	private CheckBox checkNotify;

	/**
	 * 
	 */
	@FXML
	private Button btnSave;
	
	/**
	 * @param sessionMiss
	 */
	@PostConstruct
	public void init(SessionMissEntity sessionMiss){
		
		txtComment.setText(sessionMiss.getComment());
		
		checkNotify.setSelected(sessionMiss.isNotify());
		
		btnSave.setOnAction((e)->{
			sessionMiss.setComment(txtComment.getText());
			sessionMiss.setNotify(checkNotify.isSelected());
			
			SessionMissDataModel.instance.update(sessionMiss);
		});
	}

	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
