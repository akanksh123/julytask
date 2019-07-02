package databasedemo.salesforcedemo;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Hello world!
 *
 */
public class App {

	public static final String path = "C:/Users/LENOVO/Downloads/Account.csv";

	public static void main(String[] args) {
		String sql = "select * from Account";

		try {

			Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sales?user=tech&password=");
			PreparedStatement mystat = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = mystat.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int n = md.getColumnCount();
			String[] column = new String[n + 1];
			String[] columnname = new String[n + 1];
			File file = new File("C:/Users/LENOVO/Downloads/Account.csv");
			FileReader in = new FileReader(file);
			CSVParser parse = CSVParser.parse(file, Charset.forName("UTF-8"),
					CSVFormat.RFC4180.withFirstRecordAsHeader());
			Iterable<CSVRecord> records = parse.getRecords();
			/*
			 * BufferedReader br = new BufferedReader(new FileReader(path)); String header =
			 * br.readLine(); if (header != null) { columnname = header.split(","); }
			 */

			for (CSVRecord record : records) {
				rs.moveToInsertRow();
				for (int i = 0; i < n - 1; i++) {
					column[i] = record.get(i);
					if (column[i].isEmpty()) {
						continue;
					}
					columnname[i] = md.getColumnName(i + 1);
					// System.out.println("Hello" + columnname[i]);
					// System.out.println(column[i]);

					rs.updateString(columnname[i], column[i]);

				} /*
					 * for(int i=1;i<66;i++) { column[i]=record.get(i); }
					 */
				rs.insertRow();
				rs.moveToCurrentRow();
				// ResultSet rs2=mystat.executeQuery("INSERT INTO Account (columnname)VALUES
				// (column[])");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
