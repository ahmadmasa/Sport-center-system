package sport.center.terminal.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SessionMissDataModel;
import sport.center.terminal.forms.client.ClientViewController;
import sport.center.terminal.forms.session.SessionMissFormController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SessionMissEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.support.Messages;

/**
 *
 * @author Asendar
 */
public class SessionMissTable extends TableView<SessionMissEntity>{

	/**
	 * @param sportEntity
	 */
	@Setter @Getter SportEntity sportEntity;

	/**
	 * 
	 */
	private ClientEntity client;
	
	/**
	 * 
	 */
	public SessionMissTable() {
		init();
	}
	
	/**
	 * @param client
	 */
	public SessionMissTable(ClientEntity client) {
		this.client=client;
		init();
	}
	/**
	 * 
	 */
	private void init(){
		prepareTable();
		fillTable();
		addChangeListener();
		
		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		this.getStylesheets().clear();
		this.getStylesheets().add("styles/theme.css");
		
		addListener();
	}
	
	/**
	 * 
	 */
	public void fillTable() {
		this.getItems().clear();

		List<SessionMissEntity> filteredSessions = new ArrayList<>();

		if (this.client != null) {
			filteredSessions.addAll(SessionMissDataModel.instance.getSessionMisses(client));

		}

		if (this.sportEntity != null) {
			filteredSessions.addAll(SessionMissDataModel.instance.getSessionMisses(sportEntity));

		}
		if (this.sportEntity == null && this.client == null)
			filteredSessions.addAll(SessionMissDataModel.instance.getSessionMisses());

		if (this.client == null) {
			CollectionUtils.filter(filteredSessions, new Predicate() {

				@Override
				public boolean evaluate(Object object) {
					SessionMissEntity sessionMiss = (SessionMissEntity) object;
					return sessionMiss.isNotify();
				}
			});
		}

		this.getItems().addAll(filteredSessions);
	}

	/**
	 * 
	 */
	private void addChangeListener() {

		SessionMissDataModel.instance.addDataModelChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				fillTable();
			}
		});
	}
	
	

	/**
	 * 
	 */
	private void prepareTable() {
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		this.getColumns().add(generateColumn("id",
				new Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionMissEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getId()+"");
						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.DATE"),
				new Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionMissEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getSessionMissDate().toString());
						return selectedContinent;
					}
				}));
		
		if(client==null)
		this.getColumns().add(generateColumn(Messages.getString("AllClientsListController.CLIENT_ID"),
				new Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionMissEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());
						
						if(client == null)
							return selectedContinent;
						
						selectedContinent.setValue(client.getId()+"");
						return selectedContinent;
					}
				}));
		if(client==null)
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.NAME"),
				new Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionMissEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());
						
						if(client == null)
							return selectedContinent;
						
						selectedContinent.setValue(client.getName());
						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.SESSION"),
				new Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionMissEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());
						
						if(client == null)
							return selectedContinent;
						
						SessionEntity session = SessionDataModel.instance.getSessionById(client.getSession());
						
						if(session == null)
							return selectedContinent;
						
						selectedContinent.setValue(session.getName());
						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("ClientDetailsFormController.NOTES"),
				new Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionMissEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();
						
						selectedContinent.setValue(item.getValue().getComment());
						
						return selectedContinent;
					}
				}));
		
	}
	
	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<SessionMissEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<SessionMissEntity, String>, ObservableValue<String>> propertyValueFactory) {
		
		TableColumn<SessionMissEntity, String> column;
		column = new TableColumn<>();
		column.setCellValueFactory(propertyValueFactory);
		column.setStyle("-fx-alignment: BASELINE_CENTER;");
		column.setText(title);
		return column;

	}
	
	/**
	 * 
	 */
	private void addListener() {
		this.setRowFactory(tv -> {
			TableRow<SessionMissEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				SessionMissEntity selectedSessionMiss = this.getSelectionModel().getSelectedItem();

				if (selectedSessionMiss == null)
					return;

				if (event.getButton().equals(MouseButton.SECONDARY)) {

					FXMLManager manager = new FXMLManager(FXMLConstants.SESSION_MISS_FORM);
					manager.getController(SessionMissFormController.class).init(selectedSessionMiss);

					Dialogs.showPopOver("Session Miss", manager.getRoot(), row);

					getSelectionModel().clearSelection();
					return;
				}
				
				if (client==null &&event.getButton().equals(MouseButton.PRIMARY) 
						&& event.getClickCount() == 2 && (!row.isEmpty())) {

					ClientEntity client = ClientDataModel.instance.getClientbyId(selectedSessionMiss.getClientId());

					if (client == null)
						return;
					
					
					FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_VIEW);
					Stage stage = manager.getStage(client.getName());
					ClientViewController controller =  manager.getController(ClientViewController.class);
					controller.init(client);
					stage.show();
				}

			});
			return row;
		});
	}
}
