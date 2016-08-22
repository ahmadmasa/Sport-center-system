package sport.center.terminal.forms.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Asendar
 *
 */
public class ReportHelper {
	/**
	 * @param path
	 * @param headers
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public String exportToExcel(String path,List<String> headers,List<List<String>> data) throws IOException {

		// create a new workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		// create a new worksheet
		HSSFSheet sheet = workbook.createSheet();

		// ***************header data******************//
		// create a header row

		HSSFRow row = sheet.createRow((short) 0);

		

		java.util.Date date = new java.util.Date();

		int i = 1;
		int cellIndex = 0;

		row = sheet.createRow((short) i++);
		for (String header : headers) {
			@SuppressWarnings("deprecation")
			HSSFCell cell = row.createCell((short) cellIndex++);
			cell.setCellValue(header);

		}

		for (List<String> col :data) {
			row = sheet.createRow((short) i++);
			cellIndex = 0;
			for (String arg : col) {
				@SuppressWarnings("deprecation")
				HSSFCell cell = row.createCell((short) cellIndex++);
				cell.setCellValue(arg);
			}
		}

		FileOutputStream stream;

		String target = path + "\\Report " + date.toString().replace(":", " ") + ".xls";
		stream = new FileOutputStream(target);

		workbook.write(stream);
		stream.close();

		return target;

	}
	
	/**
	 * @author Asendar
	 *
	 */
	@AllArgsConstructor
	public class TableCustomColumn {

		/**
		 * @param args
		 */
		@Setter @Getter private List<String> args;

		/**
		 * 
		 */
		public TableCustomColumn() {
			args = new ArrayList<>();
		}
	}


}

