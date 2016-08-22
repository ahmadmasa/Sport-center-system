package sport.center.terminal.production.support;

import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.support.Messages;
import tray.notification.NotificationType;

public class Constants {
	/**
	 * 
	 */
	public static Constants instance = new Constants();
	/**
	 * 
	 */
	private boolean demo = false;

	/**
	 * @return
	 */
	public boolean isDemo() {
		return this.demo;
	}

	/**
	 * @param body
	 */
	public void showNotificationTry(String body) {
		Dialogs.getNotificationTray(Messages.getString("Terminal.DEMO"), //$NON-NLS-1$
				body + " " + Messages.getString("Terminal.DEMO_MESSAGE"), NotificationType.ERROR) //$NON-NLS-1$
				.showAndDismiss(Duration.seconds(8));

		Dialogs.getMessageDialog(Messages.getString("Terminal.DEMO_TITLE"), Messages.getString("Terminal.DEMO_HEADER"),
				Messages.getString("Terminal.DEMO_BODY")).showAndWait();
	}
}
