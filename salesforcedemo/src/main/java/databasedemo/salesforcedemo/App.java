package databasedemo.salesforcedemo;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class App {
	static String url = "jdbc:mariadb://localhost:3306/sales";
	static String user = "tech";
	static String password = "";
	static String col, tabname;
	static String sql;
	static Scanner s = new Scanner(System.in);
	static int n;
	static ArrayList<String> columnname = new ArrayList<String>();
	static String[] cname, ctype;

	public static void main(String[] args) throws SQLException {
		tabname = getData();
		System.out.println(tabname);
		sql = "Select * from " + tabname;
		Connection conn = DriverManager.getConnection(url, user, password);

		PreparedStatement mystat = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = mystat.executeQuery();
		ResultSetMetaData md = rs.getMetaData();
		n = md.getColumnCount();
		String[] column = new String[n + 1];
		cname = new String[n + 1];
		ctype = new String[n + 1];
		checkRecords(md, mystat);

		addData(conn, mystat, rs);

	}

	public static String getData() {
		System.out.println("Enter the table name");
		String name = s.nextLine();
		return name;

	}

	public static void checkRecords(ResultSetMetaData md, PreparedStatement stat) {
		try {
			getRecords();
			for (int i = 0, j = 0; i < n && j < n; i++) {
				cname[i] = md.getColumnName(i + 1);
				ctype[i] = md.getColumnTypeName(i + 1);
				if (cname[i].equalsIgnoreCase(columnname.get(j)) != true) {
					ResultSet rs1 = stat
							.executeQuery("ALTER TABLE " + tabname + " MODIFY " + cname[i] + " " + ctype[i] + " NULL");
				} else {
					j++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<CSVRecord> getRecords() throws Exception {

		File file = new File("C:/Users/LENOVO/Downloads/" + tabname + ".csv");
		FileReader in = new FileReader(file);
		CSVParser parse = CSVParser.parse(file, Charset.forName("UTF-8"), CSVFormat.RFC4180);
		List<CSVRecord> records = parse.getRecords();
		CSVRecord label = records.get(0);
		Iterator<String> itr = label.iterator();

		while (itr.hasNext()) {
			columnname.add(itr.next());
		}

		return records;
	}

	public static void addData(Connection conn, PreparedStatement statement, ResultSet rs) {
		try {
			List<CSVRecord> records = getRecords();
			int k = 0;
			for (CSVRecord record : records) {
				if (k == 0) {
					k++;
					continue;
				}
				rs.moveToInsertRow();
				for (int i = 0, j = 0; i < n && j < n; i++) {
					if (record.get(j).isEmpty()) {
						continue;
					}

					if (cname[i].equalsIgnoreCase(columnname.get(j)) != true) {
						;
					} else {

						System.out.println(record.get(j));
						System.out.println(cname[i]);
						rs.updateString(cname[i], record.get(j));
						j++;
					}

				}
				rs.insertRow();
				rs.moveToCurrentRow();
			}
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}