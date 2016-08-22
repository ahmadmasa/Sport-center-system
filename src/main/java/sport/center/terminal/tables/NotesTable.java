package sport.center.terminal.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.NoteDataModel;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.NoteEntity;
import sport.center.terminal.note.AddUpdateNoteController;

/**
 *
 * @author Asendar
 */
public class NotesTable extends TableView<NoteEntity> {

	/**
	 * 
	 */
	private ClientEntity client;

	/**
	 * @param client
	 */
	public NotesTable(ClientEntity client) {
		this.client = client;
		init();
	}

	/**
	 * 
	 */
	private void init() {
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
		this.getItems().addAll(NoteDataModel.instance.getNotes(client));

	}

	/**
	 * 
	 */
	private void addChangeListener() {

		NoteDataModel.instance.addDataModelChangeListener(new PropertyChangeListener() {
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

		this.getColumns().add(generateColumn("رقم الملاحظه",
				new Callback<TableColumn.CellDataFeatures<NoteEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<NoteEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getId() + "");
						return selectedContinent;
					}
				}));


		this.getColumns().add(generateColumn("التاريخ",
				new Callback<TableColumn.CellDataFeatures<NoteEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<NoteEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getDate().toString());

						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("المصدر",
				new Callback<TableColumn.CellDataFeatures<NoteEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<NoteEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getSource());

						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("الملاحظه",
				new Callback<TableColumn.CellDataFeatures<NoteEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<NoteEntity, String> item) {

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
	private TableColumn<NoteEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<NoteEntity, String>, ObservableValue<String>> propertyValueFactory) {

		TableColumn<NoteEntity, String> column;
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
			TableRow<NoteEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton().equals(MouseButton.SECONDARY) && (!row.isEmpty())) {

					NoteEntity note = this.getSelectionModel().getSelectedItem();

					if (note == null)
						return;

					FXMLManager manager = new FXMLManager(FXMLConstants.ADD_UPDATE_NOTE);
					manager.getController(AddUpdateNoteController.class).init(note);

					Dialogs.showPopOver("Update Note", manager.getRoot(), row);
					
					this.getSelectionModel().clearSelection();

				}
			});
			return row;
		});
	}

}
