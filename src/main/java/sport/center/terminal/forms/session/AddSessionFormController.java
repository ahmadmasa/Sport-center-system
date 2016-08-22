package sport.center.terminal.forms.session;

import java.net.URL;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.controlsfx.control.PopOver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.support.SportChangeFilter;

/**
 * FXML Controller class
 *
 * @author Asendar
 */
public class AddSessionFormController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private TextField txtMax, txtName, txtInstructor;

	/**
	 * 
	 */
	@FXML
	private Button btnAdd;

	/**
	 * 
	 */
	@FXML
	private ComboBox<SportEntity> cmSport;

	/**
	 * 
	 */
	@PostConstruct
    public void init() {

        btnAdd.setDisable(true);

        addListeners(txtMax, txtName, txtInstructor);
        
       cmSport.getItems().addAll(SportDataModel.instance.getAllActiveSports());
		if (SportChangeFilter.instance.getSportFilter() != null)
			cmSport.getSelectionModel().select(SportChangeFilter.instance.getSportFilter());
		else
			cmSport.getSelectionModel().select(0);
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

                if (txtField == txtMax) {
                    if (!isInteger(newValue)) {
                        txtField.setText(oldValue);
                    }
                }
                if (!isEmpty(txtMax, txtName, txtInstructor)) {
                    btnAdd.setDisable(false);
                } else {
                    btnAdd.setDisable(true);
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
     * @param text
     * @return
     */
    private boolean isInteger(String text) {
        try {
            if (text.equals("")) {
                return true;
            }

            if (Integer.parseInt(text) <= 500) {
                return true;
            }
            return false;

        } catch (Exception e) {

            return false;
        }

    }

    /**
     * 
     */
    @FXML 
    private void addAction() {
        SessionDataModel.instance.add(new SessionEntity(0,txtName.getText(), txtInstructor.getText(), Integer.parseInt(txtMax.getText()),
        		cmSport.getSelectionModel().getSelectedItem().getId()));
        ((PopOver) txtName.getScene().getWindow()).hide();
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
