import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Lab4DatabaseDesign {
	public static void main(String args[]) {
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter a number");
		int input = reader.nextInt();

		Database db = new Database();

		Connection con = db.con;

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM "); // Read from
																// hotels

			int columns = 0;
			while (rs.getObject(columns) != null) {
				columns++;
			}

			for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
				System.out.println();
			}

			while (rs.next()) {
				// PRINT THIS: System.out.println(rs.);
				for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
					System.out.println(rs.getObject(columnIndex));
				}
			}

			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM "); // Read from
													// hotels
			while (rs.next()) {
				// PRINT THIS: System.out.println(rs.);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Database {// postgresqlDB database;

	Connection con;
	// Statement st;
	// ResultSet rs;

	static final String URL = "jdbc:mysql://localhost/";
	static final String DATABASE = "ht16_hoteldb_dv5";
	static final String TABLENAME = "items";
	// DATABASE MUST BE IN LOWERCASE for some reason.
	static final String USER = "ht16_user_dv5"; // Check this
	static final String PASSWORD = "pwd_dv5"; // Check this

	public Database() {
		System.out.println("New database");
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
}