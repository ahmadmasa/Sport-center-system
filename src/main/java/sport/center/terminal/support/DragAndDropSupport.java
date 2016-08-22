/**
 * 
 */
package sport.center.terminal.support;

import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 * @author Asendar
 *
 */
public abstract class DragAndDropSupport<T> {

	
	/**
	 * @param image
	 * @param tableView
	 */
	public void addDragAndDropActions (ImageView image,TableView<T> tableView){
		image.setVisible(false);
		image.setImage(new Image("/icons/rubish-close.png"));
		tableView.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ownerOnDragDetected(image,tableView);
			}
		});

		tableView.setOnDragDone(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				image.setVisible(false);
			}
		});

		tableView.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				image.setBlendMode(null);
			}
		});

		image.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				image.setImage(new Image("/icons/rubish-close.png"));
				image.setBlendMode(null);
			}
		});

		image.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				image.setImage(new Image("/icons/rubish-open.png"));
			}
		});

		image.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				dragEvent.acceptTransferModes(TransferMode.MOVE);
			}
		});

		image.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {

				targetOnDragDroped(dragEvent, image, tableView);

				dragEvent.setDropCompleted(true);
			}
		});
	}
	
	/**
	 * @param dragEvent
	 * @param image
	 * @param tableView
	 */
	public abstract void targetOnDragDroped(DragEvent dragEvent,ImageView image,TableView<T> tableView);
	/**
	 * @param image
	 * @param tableView
	 */
	public abstract void ownerOnDragDetected(ImageView image,TableView<T> tableView);

}
