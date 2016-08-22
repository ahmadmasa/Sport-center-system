/**
 * 
 */
package sport.center.terminal.forms.sport;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 * @author Asendar
 *
 */
public class AddSportFormController implements Initializable{

	/**
	 * 
	 */
	@FXML
	private Button btnAdd;

	/**
	 * 
	 */
	@FXML
	private TextField txtName;
	
	/**
	 * 
	 */
	@PostConstruct
	public void init(){
		txtName.textProperty().addListener((e)->{
			if(txtName.getText().replace(" ", "").equals(""))
				btnAdd.setDisable(true);
			else
				btnAdd.setDisable(false);
		});

		btnAdd.setOnAction((e) -> {
//			if (!txtName.getText().replace(" ", "").equals(""))
				SportDataModel.instance.add(new SportEntity(0, txtName.getText(), "", true));

			((PopOver) txtName.getScene().getWindow()).hide();
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
