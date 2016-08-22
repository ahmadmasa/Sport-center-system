/**
 * 
 */
package sport.center.terminal.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.image.Image;

/**
 * @author Asendar
 *
 */
public class AvatarUtils {

	/**
	 * @return
	 */
	public static List<String> getAvatarTitles(){
		return Arrays.asList("char1","char2","char3","char4","char5","char6","char7","char8","char9","char10","char11","char12");
	}

	/**
	 * @return
	 */
	public static List<Image> getAvatars(){
		List<Image> avatars = new ArrayList<>();
		
		for (String title : getAvatarTitles()) {
			avatars.add(getAvatar(title));
		}
		
		return avatars;
		
	}

	/**
	 * @param title
	 * @return
	 */
	public static Image getAvatar(String title) {
		try {
			return new Image("/images/avatars/" + title + ".jpg");
		} catch (Exception e) {

			return null;
		}
	}
	
}
