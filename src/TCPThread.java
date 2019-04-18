import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/* 
 * System zarz¹dzania ankietami
 * @author Piotr Bajorek 
 * @version 1.0
 * @since 08.01.2019
 * */
public class TCPThread extends Thread {
	Socket mySocket;
	Database db;

	public TCPThread(Socket socket, Database db) {
		super();
		mySocket = socket;
		this.db = db;
	}
	public void fillQuestionnaire(BufferedReader in, PrintWriter out, int questionnaireId, int numberOfQuestions)
			throws IOException, InterruptedException {
		String question, answer;
		for (int i = 1; i <= numberOfQuestions; i++) {
			question = db.readQuestions(questionnaireId, i);
			out.println(i+"."+question);
			out.flush();
			while ((answer = in.readLine()) == null) {
				Thread.sleep(500);
			}
			this.db.addAnswer(questionnaireId, i, Integer.parseInt(answer));
		}
	}

	public void run() {
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			String answer, question = "";

			question += "1.Wypelnij \n";
			question += "2.Dodaj \n";
			question += "3.Wykres\n";
			out.println(question);
			out.flush();
			// czeka na odpowiedz o wybranej opcji
			while ((answer = in.readLine()) == null) {
				Thread.sleep(500);
			}
			switch (answer) {
			case "1":
				// wysyla liste ankiet
				String questionnaires = this.db.readQuestionnaires();
				out.println(questionnaires);
				out.flush();
				// czeka na wybrana ankiete
				while ((answer = in.readLine()) == null) {
					Thread.sleep(500);
				}
				// sprawdz ilosc pytan w ankiecie
				int questionnaireId = Integer.parseInt(answer);
				int numberOfQuestions = this.db.numberOfQuestions(questionnaireId);
				// wyslij ilosc pytan w ankiecie
				out.println(numberOfQuestions);
				out.flush();
				// wypelniaj ankiete
				this.fillQuestionnaire(in, out, questionnaireId, numberOfQuestions);
				break;
			case "2":
				// nazwa ankiety
				question = "Podaj nazwe ankiety \n";
				out.println(question);
				out.flush();
				String questionnaireName;
				while ((questionnaireName = in.readLine()).equals("")) {
					Thread.sleep(500);
				}
				this.db.addQuestionnaire(questionnaireName);
				// ilosc pytan do ankiety
				question = "Podaj ilosc pytan \n";
				out.println(question);
				out.flush();
				while ((answer = in.readLine()) == null) {
					Thread.sleep(500);
				}
				int numberOfQuestionsToAdd = Integer.parseInt(answer);
				String[] answers = new String[4];
				String questionToAdd;
				for (int i = 0; i < numberOfQuestionsToAdd; i++) {
					question = "Podaj pytanie numer " + (i + 1) + "\n";
					out.println(question);
					out.flush();
					while ((questionToAdd = in.readLine()) == null) {
						Thread.sleep(500);
					}
					for (int j = 0; j < 4; j++) {
						question = "Podaj Odpowiedz " + (j + 1) + "\n";
						out.println(question);
						out.flush();
						while ((answers[j] = in.readLine()) == null) {
							Thread.sleep(500);
						}
					}
					this.db.addQuestion(questionToAdd, answers, i + 1);
				}
				break;

			case "3":
				String questionnaires2 = this.db.readQuestionnaires();
				out.println("Wybierz ankiete:\n" + questionnaires2);
				out.flush();
				// czeka na wybrana ankiete
				while ((answer = in.readLine()) == null) {
					Thread.sleep(500);
				}
				String qst = answer;
				questionnaires2 = this.db.allQuestionToChart(Integer.parseInt(answer));
				out.println("Wybierz pytanie:\n" + questionnaires2);
				out.flush();
				while ((answer = in.readLine()) == null) {
					Thread.sleep(500);
				}
				String countA, countB, countC, countD;
				String questnumber = answer;
				questionnaires2 = this.db.readQuestionsToChart(Integer.parseInt(qst), Integer.parseInt(questnumber), 1);
				out.print(questionnaires2);
				out.flush();
				questionnaires2 = this.db.readQuestionsToChart(Integer.parseInt(qst), Integer.parseInt(questnumber), 2);
				countA = this.db.countAnswersToChart(1, Integer.parseInt(questnumber), Integer.parseInt(qst));
				out.print(questionnaires2);
				out.flush();
				questionnaires2 = this.db.readQuestionsToChart(Integer.parseInt(qst), Integer.parseInt(questnumber), 3);
				countB = this.db.countAnswersToChart(2, Integer.parseInt(questnumber), Integer.parseInt(qst));
				out.print(questionnaires2);
				out.flush();
				questionnaires2 = this.db.readQuestionsToChart(Integer.parseInt(qst), Integer.parseInt(questnumber), 4);
				countC = this.db.countAnswersToChart(3, Integer.parseInt(questnumber), Integer.parseInt(qst));
				out.print(questionnaires2);
				out.flush();
				questionnaires2 = this.db.readQuestionsToChart(Integer.parseInt(qst), Integer.parseInt(questnumber), 5);
				countD = this.db.countAnswersToChart(4, Integer.parseInt(questnumber), Integer.parseInt(qst));
				out.print(questionnaires2);
				out.flush();
				out.print(countA);
				out.flush();
				out.print(countB);
				out.flush();
				out.print(countC);
				out.flush();
				out.print(countD);
				out.flush();
				break;
			case "4":
				in.close();
				out.close();
				mySocket.close();
				break;
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}



}
