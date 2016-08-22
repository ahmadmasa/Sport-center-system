package sport.center.terminal.tables;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.forms.client.ClientViewController;
import sport.center.terminal.forms.report.ReportHelper;
import sport.center.terminal.forms.report.ReportHelper.TableCustomColumn;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.support.Messages;
import tray.notification.NotificationType;

/**
 *
 * @author Asendar
 */
public class ClientTable extends TableView<ClientEntity>{
    
	/**
	 * @author Asendar
	 *
	 */
	public static enum TableType {ACTIVE,INACTIVE}

	/**
	 * 
	 */
	public ClientTable() {
		init();
	}

	/**
	 * 
	 */
	private void init(){
		prepareTable();
		addListener();
		
		this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	}

	/**
	 * @param clients
	 */
	public void fillTable(List<ClientEntity> clients){
		this.getItems().clear();
		this.getItems().addAll(clients);
	}
	
	/**
	 * @param propertyChangeListener
	 */
	public void addChangeListener(PropertyChangeListener propertyChangeListener) {

		
		ClientDataModel.instance.addDataModelChangeListener(propertyChangeListener);
	}
	
	/**
	 * @param propertyChangeListener
	 */
	public void addSportEventChangeListener(PropertyChangeListener propertyChangeListener) {

		SportEventDataModel.instance.addDataModelChangeListener(propertyChangeListener);
		SportEventClientDataModel.instance.addDataModelChangeListener(propertyChangeListener);
	}
	
	/**
	 * 
	 */
	private void prepareTable() {
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		this.getColumns().add(generateColumn("رقم المشترك",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getHumanReadableId()+"");
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("الاسم",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getName());
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("تاريخ الميلاد",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getBirthdate().toString());
						
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("تاريخ التسجيل",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getSignDate().toString());
						
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("رقم الهاتف",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getPhone1());
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("هاتف الطوارئ",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getEmergencyPhone());

						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("مشترك بالمواصلات",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().isTransportationParticipated() ? "YES" : "NO");
						return selectedContinent;
					}
				}));

		// if (this.session == null) {
		this.getColumns().add(generateColumn("الحصه",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						SessionEntity session = SessionDataModel.instance.getSessionById(item.getValue().getSession());

						selectedContinent.setValue(session != null ? session.getName() : " - ");
						return selectedContinent;
					}
				}));
		// }
		this.getColumns().add(generateColumn("الرياضه",
				new Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						SportEntity sport = SportDataModel.instance.getSport(item.getValue().getSportId());

						selectedContinent.setValue(sport != null ? sport.getSportName() : " - ");
						return selectedContinent;
					}
				}));

	}

	
	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<ClientEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<ClientEntity, String>, ObservableValue<String>> propertyValueFactory) {
		
		TableColumn<ClientEntity, String> column;
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
			TableRow<ClientEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {

					ClientEntity client = this.getSelectionModel().getSelectedItem();

					if (client == null)
						return;
					
					
					FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_VIEW);
					Stage stage = manager.getStage(this.getSelectionModel().getSelectedItem().getName());
					ClientViewController controller =  manager.getController(ClientViewController.class);
					controller.init(client);
					stage.show();
				}
			});
			return row;
		});
	}
	
	/**
	 * 
	 */
	public void exportToXLS(){
		ReportHelper helper = new ReportHelper();

		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(this.getScene().getWindow());

		if (selectedDirectory == null) {
			Dialogs.getNotificationTray(Messages.getString("Report.NOT_SAVED_HEADER"),
					Messages.getString("Report.NOT_SAVED_MESSAGE"), NotificationType.ERROR)
					.showAndDismiss(Duration.seconds(5));
			return;
		} else {
			try {
				
				
				List<List<String>> data = new ArrayList<>();
				
				for(ClientEntity client : this.getItems()){
					data.add(client.getXLSrow());
				}

				String message = helper.exportToExcel(selectedDirectory.getAbsolutePath(), ClientEntity.getXLSHeaders(), data);
				Dialogs.getNotificationTray(Messages.getString("Report.SAVED_HEADER"), message,
						NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
			} catch (IOException ex) {
				Dialogs.getNotificationTray(Messages.getString("Report.NOT_SAVED_HEADER"),
						Messages.getString("Report.ERROR_MESSAGE"), NotificationType.ERROR)
						.showAndDismiss(Duration.seconds(5));
				ex.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 */
	List<TableCustomColumn> data;
}
