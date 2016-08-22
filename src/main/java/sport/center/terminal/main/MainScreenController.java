/**
 * 
 */
package sport.center.terminal.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.AccountDataModel;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.PaymentNotificationDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SessionMissDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.data.model.TerminalUtilsDataModel;
import sport.center.terminal.forms.account.AddAccountController;
import sport.center.terminal.forms.client.AllClientsViewController;
import sport.center.terminal.forms.client.ClientAddController;
import sport.center.terminal.forms.event.AddUpdateEventForm;
import sport.center.terminal.forms.finance.FinancialManagerController;
import sport.center.terminal.forms.payment.ClientPaymentFormController;
import sport.center.terminal.forms.payment.PaymentNotificationController;
import sport.center.terminal.forms.session.AddSessionFormController;
import sport.center.terminal.forms.sport.SportManagerController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.PaymentNotificationEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;
import sport.center.terminal.support.Messages;
import sport.center.terminal.support.SportChangeFilter;
import sport.center.terminal.tables.SessionMissTable;
import sport.center.terminal.tables.SessionTable;
import sport.center.terminal.tables.SportEventsTable;

/**
 * @author Asendar
 *
 */
public class MainScreenController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private Button btnAddMember, btnSearchMember, btnAddSession, btnDeleteSession, btnManageSports, btnAddSportEvent,
			btnDeleteSportEvent, btnFinancial, btnActivateSportEvent;

	/**
	 * 
	 */
	@FXML
	private BorderPane sessionsPanel, missesPanel, eventsPanel, eventsHistoryPanel;

	/**
	 * 
	 */
	@FXML
	private MenuButton menuLoggiedInUser, menuSportFilter;

	/**
	 * 
	 */
	@FXML
	private ImageView imgAvatar;

	/**
	 * 
	 */
	@FXML
	private TabPane mainTabs, eventsTabPane;

	/**
	 * 
	 */
	@FXML
	private TitledPane pnlNotifications;

	/**
	 * 
	 */
	@FXML
	private ListView<PaymentNotificationEntity> listNotifications;

	/**
	 * 
	 */
	private Stage addNewClient;

	/**
	 * 
	 */
	private final SessionMissTable sessionMissesTable = new SessionMissTable();
	/**
	 * 
	 */
	private final SessionTable sessionTable = new SessionTable();
	/**
	 * 
	 */
	private final SportEventsTable sportEventsTable = new SportEventsTable();
	/**
	 * 
	 */
	private final SportEventsTable inactiveSportEventsTable = new SportEventsTable();

	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		sessionsPanel.setCenter(sessionTable);
		missesPanel.setCenter(sessionMissesTable);
		eventsPanel.setCenter(sportEventsTable);
		eventsHistoryPanel.setCenter(inactiveSportEventsTable);

		fillTables();
		addMainActions();

		fillSportsMenu();

		SportDataModel.instance.addDataModelChangeListener((e) -> {
			menuSportFilter.getItems().clear();
			SportChangeFilter.instance.setSportFilter(null);
			fillSportsMenu();
		});

		initMenuButton();

		AccountDataModel.instance.addDataModelChangeListener((p) -> {
			initMenuButton();
		});

		addTabPaneActions();

		fillNotificationsList();

	}

	/**
	 * 
	 */
	private void initMenuButton() {

		menuLoggiedInUser.setText(TerminalUtilsDataModel.instance.getLoggedInUser().getUsername());

		menuLoggiedInUser.getItems().clear();

		FXMLManager manager = new FXMLManager(FXMLConstants.ADD_ACCOUNT);
		Stage addAccountStage = manager.getStage("Update Account");

		MenuItem createNew = new MenuItem("Update account");
		createNew.setOnAction((e) -> {
			Platform.runLater(() -> {
				manager.getController(AddAccountController.class)
						.init(TerminalUtilsDataModel.instance.getLoggedInUser());
				addAccountStage.showAndWait();

			});

		});

		menuLoggiedInUser.getItems().add(createNew);

		MenuItem logoutItem = new MenuItem("Logout");
		logoutItem.setOnAction((e) -> {
			((Stage) imgAvatar.getScene().getWindow()).close();

			FXMLConstants.getLoginCommonStage().show();
		});

		menuLoggiedInUser.getItems().add(logoutItem);

		imgAvatar.setImage(TerminalUtilsDataModel.instance.getLoggedInUser().getAvatar());

	}

	/**
	 * 
	 */
	private void fillTables() {

		sessionMissesTable.setSportEntity(SportChangeFilter.instance.getSportFilter());
		sessionMissesTable.fillTable();

		sessionTable.fillTable(SessionDataModel.instance.getSessions());

		mainTabs.getTabs().get(2).setText("Misses ( " + sessionMissesTable.getItems().size() + " )");

		SportChangeFilter.instance.addSportFilterChangedListener((ev) -> {

			sessionMissesTable.setSportEntity(SportChangeFilter.instance.getSportFilter());
			sessionMissesTable.fillTable();

			sessionTable.getItems().clear();

			if (ev.getNewValue() == null) {
				sessionTable.fillTable(SessionDataModel.instance.getSessions());

			} else {
				sessionTable
						.fillTable(SessionDataModel.instance.getSessions(SportChangeFilter.instance.getSportFilter()));

			}

			mainTabs.getTabs().get(2).setText("Misses ( " + sessionMissesTable.getItems().size() + " )");

		});

		SessionMissDataModel.instance.addDataModelChangeListener((p) -> {
			mainTabs.getTabs().get(2).setText("Misses ( " + sessionMissesTable.getItems().size() + " )");
		});

		sessionTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (SportChangeFilter.instance.getSportFilter() != null)
					sessionTable.fillTable(
							SessionDataModel.instance.getSessions(SportChangeFilter.instance.getSportFilter()));
				else
					sessionTable.fillTable(SessionDataModel.instance.getSessions());

			}
		});

		sportEventsTable.getItems().clear();
		sportEventsTable.getItems().addAll(SportEventDataModel.instance.getActiveSportEvents());
		sportEventsTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (SportChangeFilter.instance.getSportFilter() != null)
					sportEventsTable.fillTable(SportEventDataModel.instance
							.getActiveSportEvents(SportChangeFilter.instance.getSportFilter()));
				else
					sportEventsTable.fillTable(SportEventDataModel.instance.getActiveSportEvents());

			}
		});

		SportChangeFilter.instance.addSportFilterChangedListener((ev) -> {

			if (ev.getNewValue() == null) {
				sportEventsTable.fillTable(SportEventDataModel.instance.getActiveSportEvents());

			} else {
				sportEventsTable.fillTable(
						SportEventDataModel.instance.getActiveSportEvents(SportChangeFilter.instance.getSportFilter()));

			}

		});

		inactiveSportEventsTable.getItems().clear();
		inactiveSportEventsTable.getItems().addAll(SportEventDataModel.instance.getInactiveSportEvents());
		inactiveSportEventsTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (SportChangeFilter.instance.getSportFilter() != null)
					inactiveSportEventsTable.fillTable(SportEventDataModel.instance
							.getInactiveSportEvents(SportChangeFilter.instance.getSportFilter()));
				else
					inactiveSportEventsTable.fillTable(SportEventDataModel.instance.getInactiveSportEvents());

			}
		});

		SportChangeFilter.instance.addSportFilterChangedListener((ev) -> {

			if (ev.getNewValue() == null) {
				inactiveSportEventsTable.fillTable(SportEventDataModel.instance.getInactiveSportEvents());

			} else {
				inactiveSportEventsTable.fillTable(SportEventDataModel.instance
						.getInactiveSportEvents(SportChangeFilter.instance.getSportFilter()));

			}

		});

	}

	/**
	 * 
	 */
	private void addMainActions() {
		btnAddSession.setOnAction((e) -> {

			FXMLManager manager = new FXMLManager(FXMLConstants.ADD_SESSION_FORM);
			manager.getController(AddSessionFormController.class).init();

			Dialogs.showPopOver("Add Session", manager.getRoot(), btnAddSession);
		});

		btnDeleteSession.setOnAction((e) -> {
			SessionEntity selectedSession = sessionTable.getSelectionModel().getSelectedItem();

			if (selectedSession == null) {
				Dialogs.showPopOver("Can't delete", new Label("   Select a session from the table to delete    "),
						sessionTable);
				return;
			}

			Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
					Messages.getString("MainTabController.DELETE_BODY"));
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				SessionDataModel.instance.delete(selectedSession);
			}

		});

		btnAddMember.setOnAction((e) -> {

			if (addNewClient != null && addNewClient.isShowing())
				return;

			FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_ADD);
			manager.getController(ClientAddController.class).init();
			addNewClient = manager.getStage("Add new member");
			addNewClient.setResizable(false);
			addNewClient.show();

		});

		FXMLManager manager = new FXMLManager(FXMLConstants.ALL_CLIENTS_VIEW);
		Stage allClientsViewStage = manager.getStage("All Members");
		AllClientsViewController allClientsViewController = manager.getController(AllClientsViewController.class);

		btnSearchMember.setOnAction((e) -> {
			allClientsViewController.init();
			allClientsViewStage.setMaximized(true);
			allClientsViewStage.show();

		});

		manager = new FXMLManager(FXMLConstants.SPORT_MANAGER);
		Stage sortManagerStage = manager.getStage("Sport Manager");
		SportManagerController sortManagerController = manager.getController(SportManagerController.class);

		btnManageSports.setOnAction((e) -> {
			sortManagerController.init();
			sortManagerStage.setMaximized(true);
			sortManagerStage.show();

		});

		btnAddSportEvent.setOnAction((e) -> {

			FXMLManager eventManager = new FXMLManager(FXMLConstants.ADD_UPDATE_EVENT);
			eventManager.getController(AddUpdateEventForm.class).init();
			Dialogs.showPopOver("Add Event", eventManager.getRoot(), btnAddSportEvent);
		});

		btnDeleteSportEvent.setOnAction((e) -> {
			SportEventEntity selectedSportEvent = sportEventsTable.getSelectionModel().getSelectedItem();

			if (selectedSportEvent == null) {
				Dialogs.showPopOver("Can't delete", new Label("   Select an Event from the table to delete    "),
						sportEventsTable);
				return;
			}

			Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
					Messages.getString("MainTabController.DELETE_BODY"));
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				SportEventDataModel.instance.delete(selectedSportEvent);
			}

		});

		btnActivateSportEvent.setOnAction((e) -> {
			SportEventEntity sportEvent = inactiveSportEventsTable.getSelectionModel().getSelectedItem();

			mainTabs.getSelectionModel().select(1);
			eventsTabPane.getSelectionModel().select(1);

			if (sportEvent == null) {
				Dialogs.showPopOver("Cant Activate", new Label("      Select an event to re activate       "),
						btnActivateSportEvent);
				return;
			}

			sportEvent.setActive(true);
			SportEventDataModel.instance.update(sportEvent);
			eventsTabPane.getSelectionModel().select(0);

		});

		btnFinancial.setOnAction((e) -> {
			FXMLManager financeManager = new FXMLManager(FXMLConstants.FINANCIAL_MANAGER);
			financeManager.getController(FinancialManagerController.class).init();

			financeManager.getStage("Financial Manager").show();

		});

	}

	/**
	 * 
	 */
	private void fillSportsMenu() {
		MenuItem allSportsMenuItem = new MenuItem("All Sports");
		allSportsMenuItem.setOnAction((e) -> {
			SportChangeFilter.instance.fireSportFilterChangedEvent(null);
			menuSportFilter.setText(allSportsMenuItem.getText());
		});
		this.menuSportFilter.getItems().add(allSportsMenuItem);

		for (SportEntity sport : SportDataModel.instance.getAllActiveSports()) {
			MenuItem menuItem = new MenuItem(sport.getSportName());
			menuItem.setOnAction((e) -> {
				SportChangeFilter.instance.fireSportFilterChangedEvent(sport);
				menuSportFilter.setText(menuItem.getText());
			});
			this.menuSportFilter.getItems().add(menuItem);
		}
	}

	/**
	 * 
	 */
	private void addTabPaneActions() {
		setSessionTabButtons(true);
		setEventTabButtons(false);
		mainTabs.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			switch (mainTabs.getSelectionModel().getSelectedIndex()) {
			case 0:
				setSessionTabButtons(true);
				setEventTabButtons(false);
				break;
			case 1:
				setSessionTabButtons(false);
				setEventTabButtons(true);
				break;
			case 2:
				setSessionTabButtons(false);
				setEventTabButtons(false);
				break;
			}
		});
	}

	/**
	 * @param visible
	 */
	private void setSessionTabButtons(boolean visible) {
		btnDeleteSession.managedProperty().bind(btnDeleteSession.visibleProperty());
		btnAddSession.managedProperty().bind(btnAddSession.visibleProperty());

		btnDeleteSession.setVisible(visible);
		btnAddSession.setVisible(visible);

	}

	/**
	 * @param visible
	 */
	private void setEventTabButtons(boolean visible) {
		btnAddSportEvent.managedProperty().bind(btnAddSportEvent.visibleProperty());
		btnDeleteSportEvent.managedProperty().bind(btnDeleteSportEvent.visibleProperty());
		btnActivateSportEvent.managedProperty().bind(btnActivateSportEvent.visibleProperty());

		btnAddSportEvent.setVisible(visible);
		btnDeleteSportEvent.setVisible(visible);
		btnActivateSportEvent.setVisible(visible);
	}

	/**
	 * 
	 */
	private void fillNotificationsList() {
		this.listNotifications.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		this.listNotifications.setCellFactory(
				new Callback<ListView<PaymentNotificationEntity>, ListCell<PaymentNotificationEntity>>() {

					@Override
					public ListCell<PaymentNotificationEntity> call(ListView<PaymentNotificationEntity> param) {
						ListCell<PaymentNotificationEntity> cell = new ListCell<PaymentNotificationEntity>() {

							@Override
							protected void updateItem(PaymentNotificationEntity item, boolean empty) {
								super.updateItem(item, empty);
								setGraphic(null);

								if (item == null)
									return;

								FXMLManager manager = new FXMLManager(FXMLConstants.PAYMENT_NOTIFICATION);
								manager.getController(PaymentNotificationController.class).init(item);

								setGraphic(manager.getRoot());

							}
						};
						return cell;
					}
				});

		listNotifications.getItems().clear();
		listNotifications.getItems().addAll(PaymentNotificationDataModel.instance.getPaymentNotifications());

		pnlNotifications.setText("Notifications ( " + listNotifications.getItems().size() + " )");

		SportChangeFilter.instance.addSportFilterChangedListener((p) -> {
			fillNotificationList();
		});
		PaymentNotificationDataModel.instance.addDataModelChangeListener((p) -> {
			fillNotificationList();
		});

		ClientDataModel.instance.addDataModelChangeListener((p) -> {
			fillNotificationList();
		});

		initializeListeners();
	}

	/**
	 * 
	 */
	private void fillNotificationList() {
		listNotifications.getItems().clear();

		SportEntity sport = SportChangeFilter.instance.getSportFilter();

		if (sport == null)
			listNotifications.getItems().addAll(PaymentNotificationDataModel.instance.getPaymentNotifications());
		else
			listNotifications.getItems().addAll(PaymentNotificationDataModel.instance.getPaymentNotifications(sport));

		pnlNotifications.setText("Notifications ( " + listNotifications.getItems().size() + " )");
	}

	/**
	 * 
	 */
	/**
	 * 
	 */
	@FXML
	private ImageView imgRubbish, imgPayment;

	// transfare support
	/**
	 * 
	 */
	private void initializeListeners() {
		imgRubbish.setVisible(false);
		imgPayment.setVisible(false);
		imgRubbish.setImage(new Image("/icons/rubish-close.png"));
		listNotifications.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgRubbish.setVisible(true);

				List<PaymentNotificationEntity> selected = listNotifications.getSelectionModel().getSelectedItems();
				if (selected != null && selected.size() == 1)
					imgPayment.setVisible(true);

				Dragboard dragBoard = listNotifications.startDragAndDrop(TransferMode.MOVE);

				ClipboardContent content = new ClipboardContent();

				content.putString(listNotifications.getSelectionModel().getSelectedItem().getId() + "");

				dragBoard.setContent(content);
			}
		});

		listNotifications.setOnDragDone(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				imgRubbish.setVisible(false);
				imgPayment.setVisible(false);
			}
		});

		imgPayment.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				imgRubbish.setBlendMode(null);
			}
		});

		imgRubbish.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				imgRubbish.setImage(new Image("/icons/rubish-close.png"));
				imgRubbish.setBlendMode(null);
			}
		});

		imgRubbish.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				imgRubbish.setImage(new Image("/icons/rubish-open.png"));
			}
		});

		imgRubbish.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				dragEvent.acceptTransferModes(TransferMode.MOVE);
			}
		});

		imgPayment.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				dragEvent.acceptTransferModes(TransferMode.MOVE);
			}
		});

		imgRubbish.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {

				Platform.runLater(() -> {

					Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
							Messages.getString("MainTabController.DELETE_BODY"));
					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {
						List<PaymentNotificationEntity> selected = listNotifications.getSelectionModel()
								.getSelectedItems();
						if (selected == null)
							return;

						for (PaymentNotificationEntity notification : selected) {
							Platform.runLater(() -> {
								PaymentNotificationDataModel.instance.delete(notification);
							});

						}
					}

				});
				dragEvent.setDropCompleted(true);
			}
		});

		imgPayment.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				Platform.runLater(() -> {

					FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_PAYMENT_FORM);
					Stage stage = manager.getStage("Add Payment");

					PaymentNotificationEntity notification = listNotifications.getSelectionModel().getSelectedItem();

					ClientPaymentFormController controller = manager.getController(ClientPaymentFormController.class);
					controller.init(ClientDataModel.instance.getClientbyId(notification.getClientId()), null, true);
					stage.showAndWait();

					if (controller.isSucess())
						PaymentNotificationDataModel.instance.delete(notification);
				});

				dragEvent.setDropCompleted(true);
			}
		});
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stu

	}

}
