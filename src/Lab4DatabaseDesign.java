import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class Lab4DatabaseDesign {
	public static void main(String args[]) {
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter a number");
		int intInput = reader.nextInt();

		Database db = new Database();

		Connection con = db.con;

		String SELECT_STATEMENT = "SELECT EANHotelID, Name FROM Property WHERE PropertyCurrency = ? AND HighRate > ?";
		String SELECT_HOTEL_STATEMENT = "SELECT EANHotelID, Name, HighRate FROM Property WHERE EANHotelID = ?";
		String UPDATE_STATEMENT = "UPDATE property SET HighRate = ? WHERE EANHotelID = ?";

		try {
			PreparedStatement st = con.prepareStatement(SELECT_STATEMENT);
			// st.setString(1, "EANHotelID");
			// st.setString(2, "Name");
			// st.setString(1, "Property");
			st.setString(1, "USD");
			st.setInt(2, intInput);

			ResultSet rs = st.executeQuery();

			Database.printPretty(rs);

			System.out.println();
			System.out.println("Enter a hotel identifier");
			intInput = reader.nextInt();
			st = con.prepareStatement(SELECT_HOTEL_STATEMENT);
			st.setInt(1, intInput);

			rs = st.executeQuery();

			int oldHighRateRow;
			int oldHighRate = 0;
			boolean existed = false;

			if (rs.next()) {
				oldHighRateRow = rs.findColumn("HighRate");
				oldHighRate = rs.getInt(oldHighRateRow);
				existed = true;
			}

			if (existed) {
				st = con.prepareStatement(UPDATE_STATEMENT);
				st.setInt(1, (oldHighRate + 100));
				st.setInt(2, intInput);
				System.out.println("Rows affected " + st.executeUpdate());
			}

			st = con.prepareStatement(SELECT_HOTEL_STATEMENT);
			st.setInt(1, intInput);
			rs = st.executeQuery();

			Database.printPretty(rs);
			db.con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		reader.close();
	}
}

class Database {// postgresqlDB database;

	Connection con;
	// Statement st;
	// ResultSet rs;

	static final String URL = "jdbc:mysql://groucho.it.uu.se/";
	static final String DATABASE = "ht16_hoteldb_dv5";
	// static final String TABLENAME = "items";
	// DATABASE MUST BE IN LOWERCASE for some reason.
	static final String USER = "ht16_user_dv5";// "ht16_user_dv5"; // Check this
	static final String PASSWORD = "pwd_dv5"; // Check this

	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.err.println("Driver not found");
			System.err.println(e.getMessage());
		}
		// Load driver, if the driver doesn't exist, panic and crash.
		try {
			con = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);
			System.out.println("DB connected");
		} catch (SQLException e) {
			System.out.println(e.getSQLState());
			e.printStackTrace();
		}
	}

	static void printPretty(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int colomnCount = rsmd.getColumnCount();

			int[] columnWidth = new int[colomnCount];

			for (int i = 0; i < colomnCount - 1; i++) {
				columnWidth[i] = rsmd.getColumnDisplaySize(i + 1);
			}

			for (int columnIndex = 1; columnIndex <= colomnCount; columnIndex++) {
				String print = " " + rsmd.getColumnName(columnIndex);
				for (int i = print.length(); i < columnWidth[columnIndex - 1]; i++) {
					print = print + " ";
				}
				if (columnIndex < colomnCount) {
					print = print + " |";
				}
				System.out.print(print);
			}
			System.out.println();
			String printLines = "";
			for (int i = printLines.length(); i < columnWidth.length; i++) {
				for (int x = 0; x < columnWidth[colomnCount - 2]; x++) {
					printLines += "-";
				}
			}
			System.out.println(printLines);

			// The only thing that matters is here:
			while (rs.next()) {
				for (int columnIndex = 1; columnIndex <= colomnCount; columnIndex++) {
					String print = " " + rs.getString(columnIndex);
					for (int i = print.length(); i < columnWidth[columnIndex - 1]; i++) {
						print = print + " ";
					}
					if (columnIndex < colomnCount) {
						print = print + " |";
					}
					System.out.print(print);
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		System.out.println("");
	}

	static void print(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int colomnCount = rsmd.getColumnCount();

			for (int columnIndex = 1; columnIndex <= colomnCount; columnIndex++) {
				String print = " " + rsmd.getColumnName(columnIndex);
				System.out.print(print);
			}
			System.out.println();

			// The only thing that matters is here:
			while (rs.next()) {
				for (int columnIndex = 1; columnIndex <= colomnCount; columnIndex++) {
					System.out.print(rs.getString(columnIndex) + " ");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		System.out.println("");
	}
}