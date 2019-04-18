import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


/* 
 * System zarz¹dzania ankietami
 * @author Piotr Bajorek 
 * @version 1.0
 * @since 08.01.2019
 * */
public class Client extends JFrame {
	public static void init() {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 9991);
			socket.setTcpNoDelay(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String question;
			int answer, choice;
			
			
			
			// Wybor opcji
			Scanner input = new Scanner(System.in);
			while (!(question = in.readLine()).equals("")) {
				System.out.println(question);
			}
			System.out.println("4.Zamknij");
			choice = input.nextInt();
			out.println(choice);
			out.flush();
			switch (choice) {
			case 1:
				// Wybor ankiety
				while (!(question = in.readLine()).equals("")) {
					System.out.println(question);
				}
				answer = input.nextInt();
				out.println(answer);
				out.flush();
				
				
				// Odczyt ilosci pytan
				question = in.readLine();
				System.out.println("Liczba pytan w ankiecie: " + question);
				int numberOfQuestions = Integer.parseInt(question);
				
				
				
				// Lista pytan
				for (int i = 1; i <= numberOfQuestions; i++) {
					while (!(question = in.readLine()).equals("")) {
						System.out.println(question);
					}
					answer = input.nextInt();
					out.println(answer);
					out.flush();
				}
				init();
				break;
			case 2:
				//Nazwa ankiety
				while (!(question = in.readLine()).equals("")) {
					System.out.println(question);
				}
				Scanner input2 = new Scanner(System.in);
				String name = input2.nextLine();
				out.println(name);
				out.flush();
				
				
				
				//Ilosc pytan
				while (!(question = in.readLine()).equals("")) {
					System.out.println(question);
				}
				answer = input.nextInt();
				int numberOfQuestionsToAdd = answer;
				out.println(answer);
				out.flush();
				
				
				
				//Wpisa pytania
				for (int i = 0; i < numberOfQuestionsToAdd; i++) {
					while (!(question = in.readLine()).equals("")) {
						System.out.println(question);
					}
					String questionToAdd = input2.nextLine();
					out.println(questionToAdd);
					out.flush();
					
					
					
					// Dodaj pytania
					for (int j = 0; j < 4; j++) {
						while (!(question = in.readLine()).equals("")) {
							System.out.println(question);
						}
						String answerToAdd = input2.nextLine();
						out.println(answerToAdd);
						out.flush();
					}
				}
				init();
				break;
			case 3:
				// Wybor ankiety
				while (!(question = in.readLine()).equals("")) {
					System.out.println(question);
				}
				answer = input.nextInt();
				out.println(answer);
				out.flush();
				
				
				
				//Wybor pytania
				while (!(question = in.readLine()).equals("")) {
					System.out.println(question);
				}
				answer = input.nextInt();
				out.println(answer);
				out.flush();
				
				
				
				//Pobiera pytanie i tresc
				String[] questANDansw = new String[5];
				int index = 0;
				while (index < 5) {
					questANDansw[index++] = in.readLine();
				}
				
							
				
				//Zlicza ile bylo odpowiedzi
				int[] countAnswers = new int[4];
				int index2 = 0;
				while (index2 < 4) {
					countAnswers[index2] = Integer.parseInt(in.readLine());
					index2++;
				}
				SwingUtilities.invokeLater(() -> {
					Chart ex = new Chart(questANDansw, countAnswers);
					ex.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					ex.setVisible(true);
				});
				init();
				break;
			case 4:
				//Koniec dzialania
				input.close();
				out.close();
				in.close();
				socket.close();
				break;
			default:
				init();
				break;
			}

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		init();

	}
}
