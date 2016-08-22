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
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.PaymentDataModel;
import sport.center.terminal.forms.client.ClientViewController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.PaymentEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.support.DragAndDropSupport;
import sport.center.terminal.support.Messages;

/**
 *
 * @author Asendar
 */
public class PaymentTable  extends TableView<PaymentEntity>{
    
	/**
	 * @author Asendar
	 *
	 */
	public static enum TableType {MONTHLY,REGULAR,ALL}
	
	/**
	 * @param client
	 */
	@Setter @Getter ClientEntity client;
	/**
	 * @param sport
	 */
	@Setter @Getter SportEntity sport;
	/**
	 * 
	 */
	TableType tableType;
	
	
	/**
	 * @param tableType
	 */
	public PaymentTable(TableType tableType) {
		this.tableType = tableType;
		init();
		addListener();
	}
	
	/**
	 * @param tableType
	 * @param client
	 */
	public PaymentTable(TableType tableType,ClientEntity client) {
		this.tableType = tableType;
		this.client=client;
		init();
	}
	
	/**
	 * 
	 */
	private void init(){
		prepareTable();
		fillTable(tableType);
		addChangeListener();
		
		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	}

	/**
	 * @param tableType
	 */
	private void fillTable(TableType tableType) {
		this.getItems().clear();
		
		if (this.client != null) {
			
			if(tableType.equals(TableType.REGULAR))
				this.getItems().addAll(PaymentDataModel.instance.getRegularPayments(client));
			
			if(tableType.equals(TableType.MONTHLY))
				this.getItems().addAll(PaymentDataModel.instance.getMonthlyPayments(client));
			
			return;
		}
		
		if(tableType.equals(TableType.REGULAR))
			this.getItems().addAll(PaymentDataModel.instance.getRegularPayments());
		
		if(tableType.equals(TableType.MONTHLY))
			this.getItems().addAll(PaymentDataModel.instance.getMonthlyPayments());
		
		if(tableType.equals(TableType.ALL))
			this.getItems().addAll(PaymentDataModel.instance.getPayments());
	}

	/**
	 * 
	 */
	private void addChangeListener() {

		PaymentDataModel.instance.addDataModelChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				fillTable(tableType);
			}
		});
	}
	
	/**
	 * 
	 */
	private void prepareTable() {
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		this.getColumns().add(generateColumn("رقم الوصل",
				new Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<PaymentEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getId()+"");
						return selectedContinent;
					}
				}));

		if (this.client == null)
			this.getColumns().add(generateColumn("رقم المشترك",
					new Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>>() {
						public ObservableValue<String> call(TableColumn.CellDataFeatures<PaymentEntity, String> item) {

							SimpleStringProperty selectedContinent = new SimpleStringProperty();
							
							ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());
							
							selectedContinent.setValue(client.getHumanReadableId()+"");
							
							return selectedContinent;
						}
					}));

		if (this.client == null)
			this.getColumns().add(generateColumn("اسم المشترك",
					new Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>>() {
						public ObservableValue<String> call(TableColumn.CellDataFeatures<PaymentEntity, String> item) {

							SimpleStringProperty selectedContinent = new SimpleStringProperty();

							ClientEntity client = ClientDataModel.instance.getClientbyId(item.getValue().getClientId());
							
							selectedContinent.setValue(client!=null?client.getName():" - ");
							
							return selectedContinent;
						}
					}));

		this.getColumns().add(generateColumn("تاريخ الدفع",
				new Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<PaymentEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getPaymentDate().toString());
						
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("القيمه",
				new Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<PaymentEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getAmmount()+"");
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("وذلك عن",
				new Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<PaymentEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getComment());
						return selectedContinent;
					}
				}));

	}

	/**
	 * 
	 */
	private void addListener(){
		this.setRowFactory(tv -> {
			TableRow<PaymentEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				
				PaymentEntity selected = this.getSelectionModel().getSelectedItem();
				
				if(selected==null)
					return;

				if (event.getClickCount()==2) {
					if (client==null &&event.getButton().equals(MouseButton.PRIMARY) 
							&& event.getClickCount() == 2 && (!row.isEmpty())) {

						ClientEntity client = ClientDataModel.instance.getClientbyId(selected.getClientId());

						if (client == null)
							return;
						
						
						FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_VIEW);
						Stage stage = manager.getStage(client.getName());
						ClientViewController controller =  manager.getController(ClientViewController.class);
						controller.init(client);
						stage.show();
					}
					
				}
				
				
			});
			return row;
		});
	}
	
	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<PaymentEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<PaymentEntity, String>, ObservableValue<String>> propertyValueFactory) {
			
		TableColumn<PaymentEntity, String> column;
		column = new TableColumn<>();
		column.setCellValueFactory(propertyValueFactory);
		column.setStyle("-fx-alignment: BASELINE_CENTER;");
		column.setText(title);
		return column;

	}
	
	
	/**
	 * @param img
	 */
	public void addDragAndDropAction(ImageView img){
		DragAndDropSupport<PaymentEntity> dragAndDrop = new DragAndDropSupport<PaymentEntity>() {

			@Override
			public void targetOnDragDroped(DragEvent dragEvent, ImageView image,
					TableView<PaymentEntity> tableView) {
				Platform.runLater(() -> {

					Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
							Messages.getString("MainTabController.DELETE_BODY"));
					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {
						PaymentEntity selected = tableView.getSelectionModel().getSelectedItem();
						if (selected == null)
							return;

						PaymentDataModel.instance.delete(selected);

					}

				});
				dragEvent.setDropCompleted(true);
				
			}

			@Override
			public void ownerOnDragDetected(ImageView image, TableView<PaymentEntity> tableView) {
				image.setVisible(true);

				PaymentEntity selected = tableView.getSelectionModel().getSelectedItem();
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
