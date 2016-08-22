package sport.center.terminal.constants;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.controlsfx.control.PopOver;
import org.controlsfx.dialog.ProgressDialog;

import javafx.concurrent.Service;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Asendar
 */
public class Dialogs {

	/**
	 * @param ex
	 */
	public static void showExceptionDialog(Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);

		addTheme(alert);

		alert.setTitle("Exception");
		alert.setHeaderText("There was an error");
		alert.setContentText("Unknown error");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Please show this to your adminstrator:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
		
		ex.printStackTrace();
	}

	/**
	 * @param title
	 * @param header
	 * @param message
	 * @return
	 */
	public static Alert getConfirmDialog(String title, String header, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setDefaultButton(false);

		addTheme(alert);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);

		return alert;
	}

	/**
	 * @param title
	 * @param header
	 * @param message
	 * @return
	 */
	public static Alert getMessageDialog(String title, String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setDefaultButton(false);
		addTheme(alert);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);

		return alert;
	}

	/**
	 * @param title
	 * @param message
	 * @param type
	 * @return
	 */
	public static TrayNotification getNotificationTray(String title, String message, NotificationType type) {
		TrayNotification tray = new TrayNotification();
		tray.setTitle(title);
		tray.setMessage(message);
		tray.setNotificationType(type);
		tray.setAnimationType(AnimationType.POPUP);
		return tray;
	}

	/**
	 * @param txtString
	 * @param txtTitle
	 * @param txtHeader
	 * @param txtContent
	 * @return
	 */
	public static TextInputDialog getTextInputDialog(String txtString, String txtTitle, String txtHeader,
			String txtContent) {
		TextInputDialog dialog = new TextInputDialog(txtString);
		dialog.setTitle(txtTitle);
		dialog.setHeaderText(txtHeader);
		dialog.setContentText(txtContent);

		Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setDefaultButton(false);

		return dialog;
	}

	/**
	 * @param title
	 * @param content
	 * @param owner
	 * @return
	 */
	public static PopOver showPopOver(String title,Node content,Node owner){
		PopOver popOver = new PopOver(content);
		popOver.setTitle(title);
		popOver.setCornerRadius(10);
		popOver.show(owner);
		return popOver;
	}
	
	/**
	 * @param title
	 * @param content
	 * @param owner
	 * @return
	 */
	public static PopOver showSeparatedPopOver(String title,Node content,Node owner){
		PopOver popOver = new PopOver(content);
		popOver.setTitle(title);
		popOver.setCornerRadius(10);
		popOver.setDetachable(true);
		popOver.setDetached(true);
		popOver.show(owner);
		
		return popOver;
	}
	
	/**
	 * @param service
	 * @param stage
	 * @param title
	 * @param header
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ProgressDialog getProgressDialog(Service service, Stage stage, String title, String header) {
		ProgressDialog progDiag = new ProgressDialog(service);
		progDiag.setTitle(title);
		progDiag.initOwner(stage);
		progDiag.setHeaderText(header);

		return progDiag;
	}

	/**
	 * @param alert
	 */
	private static void addTheme(Alert alert) {
//		DialogPane dialogPane = alert.getDialogPane();
//		dialogPane.getStylesheets()
//				.add(Dialogs.class.getResource("/styles/" + TerminalUtilsDataModel.instance.getTheme() + ".css").toExternalForm());
	}
}
