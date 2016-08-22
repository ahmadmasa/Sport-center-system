package sport.center.terminal.constants;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import resources.sounds.SoundsReferer;

/**
 *
 * @author Asendar
 */
public class Sounds {

    /**
     * @param soundName
     */
    public static synchronized void playSound(final String soundName) {
        new Thread(() -> {
            URL fileUrl = SoundsReferer.class.getResource("/sounds/"+soundName + ".mp3");
            Media sound = new Media(fileUrl.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

        }).start();
    }
}
