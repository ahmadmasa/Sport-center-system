package sport.center.terminal.fxml.manager;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;


/**
 * @author Asendar
 *
 */
public class FXMLManager{

	/**
	 * 
	 */
	private FXMLLoader fxmlLoader;

	/**
	 * @return
	 */
	@Getter private Parent root;
	/**
	 * 
	 */
	String stageFXMLName;

	
	/**
	 * @param stageFXMLName
	 */
	public FXMLManager(String stageFXMLName){
		try {
			
			this.stageFXMLName = stageFXMLName;
			this.fxmlLoader = constructFXMLLoader(stageFXMLName);
			this.fxmlLoader.setLocation(getURL("/fxml/" + stageFXMLName));
			this.root = (Parent) this.fxmlLoader.load();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param stageFXMLName
	 * @return
	 */
	private FXMLLoader constructFXMLLoader(String stageFXMLName) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getURL(stageFXMLName));
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

		return fxmlLoader;
	}


	/**
	 * @param controllerType
	 * @return
	 */
	public <T> T getController(Class<T> controllerType) {
		T controller = controllerType.cast(this.fxmlLoader.getController());
		return controller;
	}

	/**
	 * @param url
	 * @return
	 */
	public URL getURL(String url) {
		return FXMLManager.class.getResource(url);
	}
	
	
	/**
	 * @param title
	 * @return
	 */
	public Stage getStage(String title) {
		Scene scene =new Scene(this.root);
		Stage stage = new Stage();
//		scene.getStylesheets().add("/styles/" + TerminalUtilsDataModel.instance.getTheme() + ".css");
		stage.setScene(scene);
		stage.setTitle(title);
		stage.getIcons().add(new Image("/images/stage_logo.png"));
		return stage;
	}
	/**
	 * @param title
	 * @return
	 */
	public Stage getStageNoStyle(String title) {
		Scene scene =new Scene(this.root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle(title);
		stage.getIcons().add(new Image("/images/stage_logo.png"));
		return stage;
	}
	
	/**
	 * @param styleName
	 * @return
	 */
	public static String getStyle(String styleName){
		return FXMLManager.class.getResource("/styles/" + styleName + ".css").toExternalForm();
	}

}
