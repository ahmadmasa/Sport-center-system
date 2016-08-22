package sport.center.terminal.tables;

import java.beans.PropertyChangeListener;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Callback;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.forms.session.SessionViewController;
import sport.center.terminal.forms.session.UpdateSessionFormController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.support.Messages;

/**
 *
 * @author Asendar
 */
public class SessionTable  extends TableView<SessionEntity>{

	/**
	 * 
	 */
	public SessionTable() {
		prepareTable();
		addListener();

		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		this.getStylesheets().clear();
		this.getStylesheets().add("styles/theme.css");
	}

	/**
	 * @param sessions
	 */
	public void fillTable(List<SessionEntity> sessions) {
		this.getItems().clear();
		this.getItems().addAll(sessions);
	}

	/**
	 * @param propertyChangeListener
	 */
	public void addChangeListener(PropertyChangeListener propertyChangeListener) {

		SessionDataModel.instance.addDataModelChangeListener(propertyChangeListener);
	}
	
	

	/**
	 * 
	 */
	private void prepareTable() {
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		
		
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.SESSION_NAME"),
				new Callback<TableColumn.CellDataFeatures<SessionEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getName());
						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.INSTRUCTOR"),
				new Callback<TableColumn.CellDataFeatures<SessionEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getInstructorName());

						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.CURRENT"),
				new Callback<TableColumn.CellDataFeatures<SessionEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(SessionDataModel.instance.getSessionCurrentNumber(item.getValue())+"");
						
						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("MainTabController.MAX"),
				new Callback<TableColumn.CellDataFeatures<SessionEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();
						
						selectedContinent.setValue(item.getValue().getSessionMaxCapacity()+"");
						
						return selectedContinent;
					}
				}));
		
		this.getColumns().add(generateColumn(Messages.getString("SportManagerController.SPORT_NAME"),
				new Callback<TableColumn.CellDataFeatures<SessionEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SessionEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();
						
						selectedContinent.setValue(SportDataModel.instance.getSport(item.getValue().getSportId()).getSportName());
						
						return selectedContinent;
					}
				}));
		
	}
	
	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<SessionEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<SessionEntity, String>, ObservableValue<String>> propertyValueFactory) {
		
		TableColumn<SessionEntity, String> column;
		column = new TableColumn<>();
		column.setCellValueFactory(propertyValueFactory);
		column.setStyle("-fx-alignment: BASELINE_CENTER;");
		column.setText(title);
		return column;

	}	
	
	
	/**
	 * 
	 */
	private void addListener(){
		this.setRowFactory(tv -> {
			TableRow<SessionEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				
				SessionEntity selectedSession = this.getSelectionModel().getSelectedItem();
				
				if(selectedSession==null)
					return;
				
				if (event.getClickCount() == 2 && (!row.isEmpty()) && !event.getButton().equals(MouseButton.SECONDARY)) {
					FXMLManager manager = new FXMLManager(FXMLConstants.SESSION_VIEW);
					Stage stage = manager.getStage(this.getSelectionModel().getSelectedItem().getName());
					SessionViewController controller =  manager.getController(SessionViewController.class);
					controller.init(selectedSession);
					stage.setMaximized(true);
					stage.show();
				}

				if (event.getButton().equals(MouseButton.SECONDARY)) {
					
					FXMLManager manager = new FXMLManager(FXMLConstants.UPDATE_SESSION_FORM);
					manager.getController(UpdateSessionFormController.class).init(selectedSession);
					
					Dialogs.showPopOver("Update Session", manager.getRoot(), row);

					getSelectionModel().clearSelection();
				}
				
				
			});
			return row;
		});
	}
}
