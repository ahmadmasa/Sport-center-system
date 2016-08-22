package sport.center.terminal.support;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	/**
	 * 
	 */
	private static final String BUNDLE_NAME = "bundle.messages";

	/**
	 * 
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * 
	 */
	private Messages() {
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	/**
	 * @param key
	 * @param args
	 * @return
	 */
	public static String get(String key, Object... args) {
		return String.format(getString(key), args);
	}
}
