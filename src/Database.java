import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

/* 
 * System zarz¹dzania ankietami
 * @author Piotr Bajorek 
 * @version 1.0
 * @since 08.01.2019
 * */
public class Database {
	Statement st;

	public Database(String url, int port) {
		if (checkDriver("com.mysql.jdbc.Driver"))
			System.out.println(" ... Dziala");
		else
			System.exit(1);

		Connection con = getConnection("jdbc:mysql://", url, port, "root", "");
		this.st = createStatement(con);

		this.createDatabase();
	}

	public static boolean checkDriver(String driver) {

		System.out.print("Sprawdzanie sterownika:");
		try {
			Class.forName(driver).newInstance();
			return true;
		} catch (Exception e) {
			System.out.println("Blad przy ladowaniu sterownika bazy!");
			return false;
		}
	}

	public static Connection connectToDatabase(String kindOfDatabase, String adress, String dataBaseName,
			String userName, String password) {
		System.out.print("\nLaczenie z baza danych:");
		String baza = kindOfDatabase + adress + "/" + dataBaseName;
		java.sql.Connection connection = null;
		try {
			connection = DriverManager.getConnection(baza, userName, password);
		} catch (SQLException e) {
			System.out.println("Blad przy polaczeniu z baza danych!");
			System.exit(1);
		}
		return connection;
	}

	public static Connection getConnection(String kindOfDatabase, String adres, int port, String userName,
			String password) {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		try {
			conn = DriverManager.getConnection(kindOfDatabase + adres + ":" + port + "/", connectionProps);
		} catch (SQLException e) {
			System.out.println("Blad polaczenia z baza danych! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(2);
		}
		System.out.println("Polaczenie z baza danych: ... OK");
		return conn;
	}

	private static Statement createStatement(Connection connection) {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Blad createStatement! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(3);
		}
		return null;
	}

	private void closeConnection(Connection connection) {
		System.out.print("\nZamykanie polaczenia z baza:");
		try {
			this.st.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Blld przy zamykaniu polaczenia z baza! " + e.getMessage() + ": " + e.getErrorCode());
			;
			System.exit(4);
		}
		System.out.print(" zamkniecie OK");
	}

	private ResultSet executeQuery(String sql) {
		try {
			return this.st.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}
		return null;
	}

	private int executeUpdate(String sql) {
		try {
			return this.st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}
		return -1;
	}

	public int numberOfRows() {
		int number = 0;
		ResultSet rs;
		try {
			rs = this.st.executeQuery("SELECT COUNT(*) FROM bazapytan");
			while (rs.next())
				number = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return number;
	}

	public String readQuestions(int id_ankiety, int id) {
		String pytanie = "";
		String sql = ("SELECT tresc, a, b, c, d FROM pytania WHERE ID_pytania= " + id + " AND id_ankiety="
				+ id_ankiety);
		System.out.println(sql);
		ResultSet rs;
		try {
			rs = this.st.executeQuery(sql);
			while (rs.next()) {
				pytanie+=rs.getString(1)+"\n";
				for (int i = 2; i < 6; i++) {
					pytanie +=(i-1)+"."+ rs.getString(i) + "\n";
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pytanie;
	}

	public String countAnswersToChart(int data, int questionID, int questionnaireID) {
		String pytanie = "";
		String sql="";
		sql = "SELECT COUNT(*) FROM odpowiedzi WHERE id_pytania=" + questionID + " AND id_ankiety =" + questionnaireID + " AND odp LIKE '" + data + "'";
		System.out.println(sql);
		ResultSet rs;
		try {
			rs = this.st.executeQuery(sql);
			while (rs.next()) {
					pytanie += rs.getString(1) + "\n";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pytanie;
	}
	
	public String readQuestionsToChart(int id_ankiety, int id, int odp) {
		String pytanie = "";
		String sql="";
		switch (odp) {
		case 1:
			 sql = ("SELECT tresc FROM pytania WHERE id_ankiety= " + id_ankiety + " AND id_pytania="
					+ id);
			break;
		case 2:
			 sql = ("SELECT a FROM pytania WHERE id_ankiety= " + id_ankiety + " AND id_pytania="
					+ id);
			break;
		case 3:
			 sql = ("SELECT b FROM pytania WHERE id_ankiety= " + id_ankiety + " AND id_pytania="
					+ id);
			break;
		case 4:
			 sql = ("SELECT c FROM pytania WHERE id_ankiety= " + id_ankiety + " AND id_pytania="
						+ id);
			break;
		case 5:
			 sql = ("SELECT d FROM pytania WHERE id_ankiety= " + id_ankiety + " AND id_pytania="
						+ id);
			break;
		}
		System.out.println(sql);
		ResultSet rs;
		try {
			rs = this.st.executeQuery(sql);
			while (rs.next()) {
					pytanie += rs.getString(1) + "\n";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pytanie;
	}

	public String readQuestions2(int id_ankiety) {
		String pytanie = "";
		String sql = ("SELECT tresc, a, b, c, d FROM pytania WHERE id_ankiety=" + id_ankiety);
		System.out.println(sql);
		ResultSet rs;
		try {
			rs = this.st.executeQuery(sql);
			while (rs.next()) {
				pytanie+=rs.getString(1);
				for (int i = 2; i < 6; i++) {
					pytanie +=(i-1)+"."+ rs.getString(i) + "\n";
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pytanie;
	}

	public String readQuestionnaires() {
		String ankieta = "";
		String sql = ("SELECT nazwa FROM Ankiety");
		ResultSet rs;
		int numer = 1;
		try {
			rs = this.st.executeQuery(sql);
			while (rs.next()) {
				ankieta += numer + "." + rs.getString(1) + "\n";
				numer++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ankieta;
	}

	public String allQuestionToChart(int questionareId) {
		String questions = "";
		String sql = ("SELECT tresc FROM pytania WHERE id_ankiety =" + questionareId);
		System.out.println(sql);
		ResultSet rs;
		try {
			rs = this.st.executeQuery(sql);
			int id = 1;
			while (rs.next()) {
				questions += id + "." + rs.getString(1) + "\n";
				id++;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}

	public void addAnswer(int questionnaireId, int questionId, int answer) {
		String sql = "INSERT INTO Odpowiedzi VALUES ('" + questionnaireId + "','" + questionId + "','" + answer + "');";
		System.out.println(sql);
		executeUpdate(sql);
	}

	public int numberOfQuestionnaires() {
		int count = 0;
		ResultSet rs;
		try {
			rs = this.st.executeQuery("SELECT COUNT(*) FROM ankiety");
			while (rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int numberOfQuestions(int questionnaireId) {
		int count = 0;
		ResultSet rs;
		try {
			rs = this.st.executeQuery("SELECT COUNT(*) FROM pytania WHERE id_ankiety=" + questionnaireId);
			while (rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int countAllQuestions() {
		int count = 0;
		ResultSet rs;
		try {
			rs = this.st.executeQuery("SELECT COUNT(*) FROM pytania");
			while (rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void addQuestionnaire(String name) {
		int countQuestionnaires = this.numberOfQuestionnaires();
		String sql = "INSERT INTO Ankiety VALUES('" + (countQuestionnaires + 1) + "','" + name + "');";
		executeUpdate(sql);
	}

	public void addQuestion(String question, String[] answers, int numberOfQuestions) {
		int countQuestionnaires = this.numberOfQuestionnaires();
		int countAllQuestions = this.countAllQuestions();
		String sql = "INSERT INTO Pytania " + "VALUES('" + (countAllQuestions + 1) + "','" + numberOfQuestions + "','"
				+ countQuestionnaires + "','" + question + "','" + answers[0] + "','" + answers[1] + "','" + answers[2]
				+ "','" + answers[3] + "');";
		System.out.println(sql);
		executeUpdate(sql);
	}

	public void createDatabase() {
		if (executeUpdate("USE Ankieta;") == 0)
			System.out.println("Baza wybrana");
		else {
			System.out.println("Baza nie istnieje! Tworzymy baze½: ");
			if (executeUpdate("create Database Ankieta;") == 1)
				System.out.println("Baza utworzona");
			else
				System.out.println("Baza nieutworzona!");
			if (executeUpdate("USE Ankieta;") == 0)
				System.out.println("Baza wybrana");
			else
				System.out.println("Baza niewybrana!");
		}

		String sql = "CREATE TABLE Ankiety ( id INT NOT NULL UNIQUE, nazwa VARCHAR(100) NOT NULL);";
		if (executeUpdate(sql) == 0) {
			System.out.println("Tabela Ankiet utworzona");
			sql = "INSERT INTO Ankiety  VALUES('1','Ankieta gastronomiczna');";
			executeUpdate(sql);
			sql = "INSERT INTO Ankiety VALUES('2','Ankieta o sporcie');";
			executeUpdate(sql);
			sql = "INSERT INTO Ankiety VALUES('3','Mobbing w pracy');";
			executeUpdate(sql);
		} else
			System.out.println("Tabela Ankiet nie utworzona!");

		sql = "CREATE TABLE Pytania ( id INT NOT NULL UNIQUE, id_pytania INT NOT NULL, id_ankiety INT NOT NULL, tresc VARCHAR(100) NOT NULL, a VARCHAR(100) NOT NULL, b VARCHAR(100) NOT NULL, c VARCHAR(100) NOT NULL, d VARCHAR(100) NOT NULL, PRIMARY KEY (id) );";
		if (executeUpdate(sql) == 0) {
			System.out.println("Tabela Pytania utworzona");
			sql = "INSERT INTO Pytania VALUES('1','1','1','Gdzie najczesciej jadasz?', 'Dom','Praca','Szkola','Na miescie');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('2','2','1','Ile srednio jesz dziennie posilkow?', 'Jeden','Dwa','Trzy','Cztery i wiecej');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('3','3','1','Jak czesto chodzisz do restauracji?', 'Raz w miesiacu','Raz w tygodniu','Wiecej niz raz w tygodniu','W ogole');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('4','4','1','Jaka kuchnie jesz najczesciej?', 'Polska','Wloska','Azjatycka','Fast Food');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('5','1','2','Czy interesujesz sie sportem?', 'Nie','Tak, uprawiam sport','Tak, jestem kibicem','Tak, jestem kibicem i uprawiam sport');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('6','2','2','Ktore z wymienionych sportow uprawiasz najczesciej? ', 'Pilka nozna','Siatkowka','Koszykowka','Skoki Narciarskie');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('7','3','2','Jak czesto uprawiasz sport?', 'Raz w miesiacu','Raz w tygodniu','Kilka razy w tygodniu','W ogole');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('8','4','2','Jak przygotowujesz sie do uprawiania sportu?', 'Robie rozgrzewke','Kupuje specjalny sprzet','Kupuje odziez sportowa','Nie przygotowuje sie');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('9','5','2','Gdzie lubisz najbardziej cwiczyc?', 'Dom','Silownia','Sala gimanstyczna','Obojetne');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('10','1','3','Jaka jest atmosfera w twoim miejscu pracy?', 'przyjazna','neutralna','nieprzyjazna','wroga');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('11','2','3','Pracownicy traktowani sa w Twoim miejscu pracy:', 'w ten sam sposób','w rozny sposob - istnieje pracownik/grupa wyró¿niana','w rozny sposob - istnieje pracownik/grupa traktowana gorzej','kazdy jest traktowany w inny sposob');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('12','3','3','Czy byles/jestes ofiara mobbingu', 'tak','nie','bycmoze','nie jestem pewien');";
			executeUpdate(sql);
			sql = "INSERT INTO Pytania VALUES('13','4','3','Kto byl sprawca takich dzialan', 'pracodawca/przelozony','wspolpracownik','podwladny','nikt');";
			executeUpdate(sql);

		} else
			System.out.println("Tabela Pytania nie utworzona!");

		sql = "CREATE TABLE Odpowiedzi (id_ankiety INT NOT NULL, id_pytania INT NOT NULL, odp INT NOT NULL );";
		if (executeUpdate(sql) == 0) {
			System.out.println("Tabela Odpowiedzi utworzona");
			for(int i=1; i<5;i++) {
				for(int j=0;j<40;j++) {
					Random generator = new Random();
					sql ="INSERT INTO odpowiedzi VALUES('1','"+i+"','"+(generator.nextInt(4)+1)+"')";
					executeUpdate(sql);
				}
			}
			
			for(int i=1; i<5;i++) {
				for(int j=0;j<40;j++) {
					Random generator = new Random();
					sql ="INSERT INTO odpowiedzi VALUES('2','"+i+"','"+(generator.nextInt(4)+1)+"')";
					executeUpdate(sql);
				}
			}
			
			for(int i=1; i<5;i++) {
				for(int j=0;j<40;j++) {
					Random generator = new Random();
					sql ="INSERT INTO odpowiedzi VALUES('3','"+i+"','"+(generator.nextInt(4)+1)+"')";
					executeUpdate(sql);
				}
			}
		}else
			System.out.println("Tabela Odpowiedzi nie utworzona!");

	}

	public static void main(String[] args) {
	}
}