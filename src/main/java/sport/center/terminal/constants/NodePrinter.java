/**
 * 
 */
package sport.center.terminal.constants;

import java.lang.reflect.Constructor;

import com.sun.javafx.print.Units;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Asendar
 *
 */
public abstract class NodePrinter {

	/**
	 * @param paymentPage
	 * @param stage
	 * @param paper
	 * @return
	 */
	public static boolean printForm(Pane paymentPage, Stage stage, Paper paper) {
		Printer printer = Printer.getDefaultPrinter();
		PageLayout pageLayout = printer.createPageLayout(paper, PageOrientation.LANDSCAPE,
				Printer.MarginType.HARDWARE_MINIMUM);
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null && job.showPrintDialog(stage)) {
			boolean success = job.printPage(pageLayout, paymentPage);
			if (success) {
				job.endJob();
			}

			return success;
		}

		return false;
	}
	
	/**
	 * @return
	 */
	public static Paper getPaymentPaper(){
		return Paper.A5;
	}
	
	/**
	 * @return
	 */
	public static Paper getCardPaper() {
		try {
			return getPaper("Card_Paper", 82, 110, Units.MM);
		} catch (Exception e) {
			return Paper.A5;
		}
	}
	
	/**
	 * @param paperName
	 * @param paperWidth
	 * @param paperHeight
	 * @param units
	 * @return
	 * @throws Exception
	 */
	private static Paper getPaper(String paperName, double paperWidth, double paperHeight, Units units) throws Exception {
		Constructor<Paper> c = Paper.class.getDeclaredConstructor(String.class, double.class, double.class,
				Units.class);
		c.setAccessible(true);
		Paper paper = c.newInstance(paperName, paperWidth, paperHeight, units);
		return paper;
	}
	
}
