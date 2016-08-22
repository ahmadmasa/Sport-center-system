package sport.center.terminal.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.ReceivableDataModel;
import sport.center.terminal.forms.payment.AddReceivableFormController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.ReceivableEntity;
import sport.center.terminal.support.DragAndDropSupport;
import sport.center.terminal.support.Messages;

/**
 *
 * @author Asendar
 */
public class ReceivableTable extends TableView<ReceivableEntity>{
    
	/**
	 * @author Asendar
	 *
	 */
	public static enum TableType {REPORT,VIEW}

	/**
	 * @param client
	 */
	@Setter @Getter ClientEntity client;
	/**
	 * 
	 */
	TableType tableType;

	/**
	 * @param tableType
	 */
	public ReceivableTable(TableType tableType) {
		this.tableType = tableType;
		init();
	}
	
	/**
	 * @param tableType
	 * @param client
	 */
	public ReceivableTable(TableType tableType,ClientEntity client) {
		this.tableType = tableType;
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
		addListener();
		
		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	}
	
	/**
	 * 
	 */
	private void fillTable() {
		this.getItems().clear();
		
		if (this.client == null) {
			this.getItems().addAll(ReceivableDataModel.instance.getReceivables());
			return;
		}
		
		this.getItems().addAll(ReceivableDataModel.instance.getReceivables(client));
	}

	/**
	 * 
	 */
	private void addChangeListener() {

		ReceivableDataModel.instance.addDataModelChangeListener(new PropertyChangeListener() {
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

		this.getColumns().add(generateColumn("رقم الذمه",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getId()+"");
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("رقم المشترك",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());
						
						selectedContinent.setValue(client.getHumanReadableId()+"");
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("اسم المشترك",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());

						selectedContinent.setValue(client.getName());
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("المبلغ المطلوب",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getReceivableAmmount()+"");
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("المبلغ المدفوع",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getPayedAmmount()+"");
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("الباقي",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue((item.getValue().getReceivableAmmount()-item.getValue().getPayedAmmount())+"");
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("التاريخ",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getReceivableDate().toString());
						return selectedContinent;
					}
				}));
		this.getColumns().add(generateColumn("دفع",
				new Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ReceivableEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().isPayed()?"YES":"NO");
						return selectedContinent;
					}
				}));
		
	}

	
	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<ReceivableEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<ReceivableEntity, String>, ObservableValue<String>> propertyValueFactory) {
		
		TableColumn<ReceivableEntity, String> column;
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
			TableRow<ReceivableEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				
				ReceivableEntity selectedReceivable = this.getSelectionModel().getSelectedItem();
				
				if(selectedReceivable==null)
					return;

				if (event.getClickCount()==2) {
					
					ClientEntity client = ClientDataModel.instance.getClientbyId(selectedReceivable.getClientId());
					
					FXMLManager manager = new FXMLManager(FXMLConstants.ADD_RECEIVABLE_FORM);
					manager.getController(AddReceivableFormController.class).init(client, selectedReceivable);
					Dialogs.showPopOver("Update Receivable", manager.getRoot(), row);
					
					getSelectionModel().clearSelection();
				}
				
				
			});
			return row;
		});
	}
	
	/**
	 * @param img
	 */
	public void addDragAndDropAction(ImageView img){
		DragAndDropSupport<ReceivableEntity> dragAndDrop = new DragAndDropSupport<ReceivableEntity>() {

			@Override
			public void targetOnDragDroped(DragEvent dragEvent, ImageView image,
					TableView<ReceivableEntity> tableView) {
				Platform.runLater(() -> {

					Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
							Messages.getString("MainTabController.DELETE_BODY"));
					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {
						ReceivableEntity selected = tableView.getSelectionModel().getSelectedItem();
						if (selected == null)
							return;

						ReceivableDataModel.instance.delete(selected);

					}

				});
				dragEvent.setDropCompleted(true);
				
			}

			@Override
			public void ownerOnDragDetected(ImageView image, TableView<ReceivableEntity> tableView) {
				image.setVisible(true);

				ReceivableEntity selected = tableView.getSelectionModel().getSelectedItem();
				if (selected != null)
					image.setVisible(true);

				Dragboard dragBoard = tableView.startDragAndDrop(TransferMode.MOVE);

				ClipboardContent content = new ClipboardContent();

				content.putString(tableView.getSelectionModel().getSelectedItem().getId() + "");

				dragBoard.setContent(content);
				
			}

		};
		dragAndDrop.addDragAndDropActions(img, this);
		
	}
	
}
