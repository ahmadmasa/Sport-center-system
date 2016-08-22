/**
 * 
 */
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
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.forms.event.AddUpdateEventForm;
import sport.center.terminal.forms.event.SportEventDetailsController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.SportEventEntity;

/**
 * @author Asendar
 *
 */
public class SportEventsTable extends TableView<SportEventEntity>{
	/**
	 * 
	 */
	public SportEventsTable() {
		init();
	}

	/**
	 * 
	 */
	private void init(){
		prepareTable();
		addListener();
		
		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		this.getStylesheets().clear();
		this.getStylesheets().add("styles/theme.css");
	}

	/**
	 * @param sportEvents
	 */
	public void fillTable(List<SportEventEntity> sportEvents){
		this.getItems().clear();
		this.getItems().addAll(sportEvents);
	}
	
	/**
	 * @param propertyChangeListener
	 */
	public void addChangeListener(PropertyChangeListener propertyChangeListener) {
		SportEventDataModel.instance.addDataModelChangeListener(propertyChangeListener);
		SportEventClientDataModel.instance.addDataModelChangeListener(propertyChangeListener);
	}
	
	/**
	 * 
	 */
	private void prepareTable() {
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		this.getColumns().add(generateColumn("النشاط",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getName()+"");
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("يبدأ بتاريخ",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getStartDate().toString());
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("ينتهي بتاريخ",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getEndDate().toString());
						
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("ملاحظات",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getComment());
						
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("العدد الحالي",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(
								SportEventDataModel.instance.getSportEventCurrentNumber(item.getValue()) + "");
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("الحد الاقصى",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getEventMaxCapacity()+"");
						
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("الرياضه",
				new Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEventEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue("جميع الرياضات");
						
						if (item.getValue().getSportId()!=-1)
							selectedContinent.setValue(
									SportDataModel.instance.getSport(item.getValue().getSportId()).getSportName());
						return selectedContinent;
					}
				}));
	}

	
	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<SportEventEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<SportEventEntity, String>, ObservableValue<String>> propertyValueFactory) {
		
		TableColumn<SportEventEntity, String> column;
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
			TableRow<SportEventEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty()) && !event.getButton().equals(MouseButton.SECONDARY)) {

					SportEventEntity sportEvent = this.getSelectionModel().getSelectedItem();

					if (sportEvent == null)
						return;

					FXMLManager manager = new FXMLManager(FXMLConstants.SPORT_EVENT_DETAILS);
					Stage stage = manager.getStage(this.getSelectionModel().getSelectedItem().getName());
					SportEventDetailsController controller = manager.getController(SportEventDetailsController.class);
					controller.init(sportEvent);
					stage.show();
				}
				
				if (event.getButton().equals(MouseButton.SECONDARY) && (!row.isEmpty())) {

					SportEventEntity sportEvent = this.getSelectionModel().getSelectedItem();

					if (sportEvent == null)
						return;

					FXMLManager eventManager = new FXMLManager(FXMLConstants.ADD_UPDATE_EVENT);
					eventManager.getController(AddUpdateEventForm.class).init(sportEvent);
					Dialogs.showPopOver("Edit Event", eventManager.getRoot(), row);
				}

				
			});
			return row;
		});
	}
	
}
