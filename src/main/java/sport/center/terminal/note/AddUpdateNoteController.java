/**
 * 
 */
package sport.center.terminal.note;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sport.center.terminal.data.model.NoteDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.NoteEntity;

/**
 * @author Asendar
 *
 */
public class AddUpdateNoteController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private Button btnUpdate;

	/**
	 * 
	 */
	@FXML
	private TextField txtComment, txtSource;

	/**
	 * @param client
	 */
	@PostConstruct
	public void init(ClientEntity client){
		btnUpdate.setText("Add");
		btnUpdate.setOnAction((e) -> {

			if (txtComment.getText().replace(" ", "").equals(""))
				return;

			if (txtSource.getText().replace(" ", "").equals(""))
				return;
			
			
			NoteEntity note = new NoteEntity(0, LocalDate.now(), txtComment.getText(), txtSource.getText(), client.getId());

			NoteDataModel.instance.add(note);
			
			((PopOver)btnUpdate.getScene().getWindow()).hide();
			
		});
	}
	
	/**
	 * @param note
	 */
	@PostConstruct
	public void init(NoteEntity note){
		txtComment.setText(note.getComment());
		txtSource.setText(note.getSource());
		
		btnUpdate.setOnAction((e) -> {

			if (txtComment.getText().replace(" ", "").equals(""))
				return;

			if (txtSource.getText().replace(" ", "").equals(""))
				return;

			note.setSource(txtSource.getText());
			note.setComment(txtComment.getText());

			NoteDataModel.instance.update(note);
			
			((PopOver)btnUpdate.getScene().getWindow()).hide();
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
