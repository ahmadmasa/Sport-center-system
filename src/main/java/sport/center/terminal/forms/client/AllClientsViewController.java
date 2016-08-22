/**
 * 
 */
package sport.center.terminal.forms.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.tables.ClientTable;

/**
 * @author Asendar
 *
 */
public class AllClientsViewController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private TextField txtFilter;
	/**
	 * 
	 */
	@FXML
	private BorderPane pnlActiveMembers, pnlInactiveMembers;

	/**
	 * 
	 */
	@FXML
	private Button btnActivate;

	/**
	 * 
	 */
	@FXML
	private TabPane membersTabPane;

	/**
	 * 
	 */
	@FXML
	private Label lblActiveClientsCount, lblInactiveClientsCount;

	/**
	 * 
	 */
	private ClientTable activeClientTable = new ClientTable();
	/**
	 * 
	 */
	private ClientTable inactiveClientTable = new ClientTable();

	/**
	 * 
	 */
	@PostConstruct
	public void init() {

		pnlActiveMembers.setCenter(activeClientTable);
		pnlInactiveMembers.setCenter(inactiveClientTable);

		final List<ClientEntity> activeClients = ClientDataModel.instance.getActiveClients();
		final List<ClientEntity> inactiveClients = ClientDataModel.instance.getInactiveClients();

		activeClientTable.fillTable(activeClients);
		activeClientTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				activeClients.clear();
				activeClients.addAll(ClientDataModel.instance.getActiveClients());
				doFiltering(activeClients, activeClientTable);
			}
		});

		inactiveClientTable.fillTable(inactiveClients);
		inactiveClientTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				inactiveClients.clear();
				inactiveClients.addAll(ClientDataModel.instance.getInactiveClients());
				doFiltering(inactiveClients, inactiveClientTable);
			}
		});

		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			doFiltering(activeClients, activeClientTable);
			doFiltering(inactiveClients, inactiveClientTable);

			lblActiveClientsCount.setText(activeClientTable.getItems().size() + " Active Member(s)");
			lblInactiveClientsCount.setText(inactiveClientTable.getItems().size() + " Inactive Member(s)");
		});

		lblActiveClientsCount.setText(activeClientTable.getItems().size() + " Active Member(s)");
		lblInactiveClientsCount.setText(inactiveClientTable.getItems().size() + " Inactive Member(s)");

		initMainAction();

	}

	/**
	 * @param allClients
	 * @param filterableTable
	 */
	private void doFiltering(List<ClientEntity> allClients, ClientTable filterableTable) {
		String filterText = txtFilter.getText().replace(" ", "");

		List<ClientEntity> clients = new ArrayList<>(allClients);

		CollectionUtils.filter(clients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return client.contains(filterText);
			}
		});

		filterableTable.getItems().clear();
		filterableTable.getItems().addAll(clients);
	}

	/**
	 * 
	 */
	private void initMainAction() {

		btnActivate.setOnAction((e) -> {
			ClientEntity client;

			if (membersTabPane.getSelectionModel().getSelectedIndex() == 0)
				client = activeClientTable.getSelectionModel().getSelectedItem();
			else
				client = inactiveClientTable.getSelectionModel().getSelectedItem();

			if (client == null)
				return;
			if (membersTabPane.getSelectionModel().getSelectedIndex() == 0)
				client.setActive(false);
			else
				client.setActive(true);

			ClientDataModel.instance.update(client);

			SessionDataModel.instance.fireDataModelChanged();
			SportEventDataModel.instance.fireDataModelChanged();
			SportEventClientDataModel.instance.fireDataModelChanged();

			lblActiveClientsCount.setText(activeClientTable.getItems().size() + " Active Member(s)");
			lblInactiveClientsCount.setText(inactiveClientTable.getItems().size() + " Inactive Member(s)");

		});
		btnActivate.setText("Deactivate");

		membersTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {

			if (membersTabPane.getSelectionModel().getSelectedIndex() == 0) {

				btnActivate.setText("Deactivate");
			}

			if (membersTabPane.getSelectionModel().getSelectedIndex() == 1) {

				btnActivate.setText("Activate");
			}

		});

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
