package databasedemo.salesforcedemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {

	public static final String path = "C:/Users/LENOVO/Downloads/Account.csv";
	static String sql = "select * from Account";

	public static void main(String args[]) {

		try {
			/*
			 * ResultSetMetaData md = rs.getMetaData(); int n = md.getColumnCount();
			 * String[] column = new String[n + 1]; File file = new
			 * File("C:/Users/LENOVO/Downloads/Account.csv"); FileReader in = new
			 * FileReader(file);
			 * 
			 * CSVParser parse = CSVParser.parse(file, Charset.forName("UTF-8"),
			 * CSVFormat.RFC4180.withFirstRecordAsHeader()); Iterable<CSVRecord> records =
			 * parse.getRecords();
			 */
			Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sales?user=tech&password=");
			PreparedStatement mystat = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs1 = mystat.executeQuery("LOAD DATA LOCAL INFILE 'C:/Users/LENOVO/Downloads/Account.csv' \r\n"
					+ "INTO TABLE Account\r\n" + "FIELDS TERMINATED BY ',' \r\n" + "ENCLOSED BY '\"'\r\n"
					+ "LINES TERMINATED BY '\\n'\r\n" + "IGNORE 1 ROWS;");
			System.out.println("Successful");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * private CSVFormat(final String[] header) { this.header = header == null ?
	 * null : header.clone(); }
	 */

}
