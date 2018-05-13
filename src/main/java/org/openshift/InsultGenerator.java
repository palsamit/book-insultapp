package org.openshift;

import java.sql.*;
import java.util.Random;

public class InsultGenerator {
	public String generateInsult() {
		String words[][] = {{"Artless", "Bawdy", "Beslubbering"}, {"Base-court", "Bat-fowling", "Beef-witted"}, {"Apple-john", "Baggage", "Barnacle"}};
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";

		try {
			String databaseUrl = "jdbc:postgresql://"
					+ System.getenv("POSTGRESQL_SERVICE_HOST")
					+ "/" + System.getenv("POSTGRESQL_DATABASE");
			String username = System.getenv("POSTGRESQL_USER");
			String password = System.getenv("PGPASSWORD");

			Connection connection = DriverManager.getConnection(databaseUrl, username, password);
			if( connection != null ) {
				String sqlQuery = "select a.string as first, b.string as second, c.string as third from short_adjective a"
						+ ", long_adjective b, noun c order by random() limit 1";
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sqlQuery);
				while( rs.next()) {
					if( vowels.indexOf(rs.getString("first").charAt(0)) == -1){
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!"
							, article, rs.getString("first"), rs.getString("second")
							, rs.getString("third"));
				}
				rs.close();
				statement.close();
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return theInsult;
	}

}
