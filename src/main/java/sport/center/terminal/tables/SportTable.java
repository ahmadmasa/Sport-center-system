package sport.center.terminal.tables;

import java.beans.PropertyChangeListener;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.jpa.entities.SportEntity;

/**
 *
 * @author Asendar
 */
public class SportTable extends TableView<SportEntity> {

	/**
	 * 
	 */
	public SportTable() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		prepareTable();
		addListener();
		fillTable();

		this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	}

	/**
	 * 
	 */
	public void fillTable() {
		this.getItems().clear();
		if (SportDataModel.instance.getAllSports() != null)
			this.getItems().addAll(SportDataModel.instance.getAllSports());
	}

	/**
	 * @param propertyChangeListener
	 */
	public void addChangeListener(PropertyChangeListener propertyChangeListener) {

		SportDataModel.instance.addDataModelChangeListener(propertyChangeListener);
	}

	/**
	 * 
	 */
	private void prepareTable() {
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		this.getColumns().add(generateColumn("رقم الرياضه",
				new Callback<TableColumn.CellDataFeatures<SportEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getId() + "");
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("اسم الرياضه",
				new Callback<TableColumn.CellDataFeatures<SportEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().getSportName());
						return selectedContinent;
					}
				}));

		this.getColumns().add(generateColumn("فعال",
				new Callback<TableColumn.CellDataFeatures<SportEntity, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(TableColumn.CellDataFeatures<SportEntity, String> item) {

						SimpleStringProperty selectedContinent = new SimpleStringProperty();

						selectedContinent.setValue(item.getValue().isActive()?"YES":"NO");

						return selectedContinent;
					}
				}));

	}

	/**
	 * @param title
	 * @param propertyValueFactory
	 * @return
	 */
	private TableColumn<SportEntity, String> generateColumn(String title,
			Callback<TableColumn.CellDataFeatures<SportEntity, String>, ObservableValue<String>> propertyValueFactory) {

		TableColumn<SportEntity, String> column;
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
		SportDataModel.instance.addDataModelChangeListener((e)->{
			fillTable();
		});
	}

}
