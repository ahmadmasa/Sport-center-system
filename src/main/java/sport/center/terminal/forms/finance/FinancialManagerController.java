/**
 * 
 */
package sport.center.terminal.forms.finance;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import sport.center.terminal.tables.PaymentTable;
import sport.center.terminal.tables.ReceivableTable;
import sport.center.terminal.tables.PaymentTable.TableType;

/**
 * @author Asendar
 *
 */
public class FinancialManagerController implements Initializable{

	/**
	 * 
	 */
	@FXML
	private BorderPane pnlRevenue,pnlReceivables;
	
	/**
	 * 
	 */
	@FXML
	private TextField txtFilter;
	
	/**
	 * 
	 */
	private final ReceivableTable receivableTable = new ReceivableTable(null);
	/**
	 * 
	 */
	private final PaymentTable PaymentTable = new PaymentTable(TableType.ALL);
	
	
	/**
	 * 
	 */
	@PostConstruct
	public void init(){
		pnlRevenue.setCenter(PaymentTable);
		pnlReceivables.setCenter(receivableTable);
	}
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
