/**
 * 
 */
package sport.center.terminal.login;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.controlsfx.control.PopOver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import sport.center.terminal.data.model.AccountDataModel;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.AccountEntity;

/**
 * @author Asendar
 *
 */
public class LoginAccountChooserController implements Initializable{

	/**
	 * 
	 */
	@FXML
	private ListView<AccountEntity> listAccounts;

	/**
	 * @param value
	 */
	@Setter @Getter private AccountEntity value;
	
	/**
	 * 
	 */
	@PostConstruct
	public void init(){
		this.listAccounts.setCellFactory(new Callback<ListView<AccountEntity>, ListCell<AccountEntity>>() {

			@Override
			public ListCell<AccountEntity> call(ListView<AccountEntity> param) {
				ListCell<AccountEntity> cell = new ListCell<AccountEntity>() {

					@Override
					protected void updateItem(AccountEntity item, boolean empty) {
						super.updateItem(item, empty);
						setGraphic(null);
						
						if(item==null)
							return;
						
						FXMLManager manager = new FXMLManager(FXMLConstants.ACCOUNT_LIST_ITEM);
						manager.getController(AccountListItemController.class).init(item);
						
						setGraphic(manager.getRoot());

					}
				};
				return cell;
			}
		});
		
		listAccounts.getItems().addAll(AccountDataModel.instance.getAccounts());
		
		
		listAccounts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AccountEntity>() {

		    @Override
		    public void changed(ObservableValue<? extends AccountEntity> observable, AccountEntity oldValue, AccountEntity newValue) {
		       setValue(newValue); 
		       
		       ((PopOver)listAccounts.getScene().getWindow()).hide();
		    }
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
