import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		ArrayList<Node> nodes = splitAndAdjacencyList(fileRead("./src/veri.txt")); // dosyayý okuyup ardýndan komþuluk
																					// listesi yaptýk

		Scanner scann = new Scanner(System.in);
		
		String input = "1";
		while(input.equals("1")) {
			System.out.print("Ýþlem yapmak istediðiniz plakayý giriniz: ");
			String plate = scann.nextLine();
			
			if(isPlateAvailable(plate, nodes)) {

				printAdjacencyList(nodes); //Komþuluk listesini yazdýrma

				inDegree(plate, nodes); //Giriþ derecesi
				outDegree(plate, nodes);//Çýkýþ derecesi

				graphEdgeCounter(nodes); //Grafýn kenar sayýsý

				whereToGo(plate, nodes); ////Hangi düðümlere gidilir
				
				whereToCome(plate, nodes); //Hangi düðümlerden gelinir
			}else {
				System.out.println(plate + " Plaka numarasýna ait bir düðüm yok geçerli bir plaka giriniz ! ");
				
			}
			System.out.print("Devam etmek için 1 e çýkmak için 0 a basýnýz: ");
			input = scann.nextLine(); 
			System.out.println();
			System.out.println("************************************************************************************************");
			System.out.println();
		}
		System.out.println("Hoþçakalýn :) ");

	}

	public static String fileRead(String path) { // dosyayý okur ve yapýyý bozmadan string olarak döner
		// path = "./src/veri.txt";
		String line = "";
		String str = "";
		try {
			FileReader file = new FileReader(path);
			BufferedReader br = new BufferedReader(file);
			line = br.readLine();

			while (line != null) {
				
				str += line;

				line = br.readLine();
				if (line != null)
					str += "\n";
			}
			file.close();
			br.close();
			
		} catch (IOException e) {

			e.printStackTrace();
		}

		return str;
	}

	public static ArrayList<Node> splitAndAdjacencyList(String str) { // Okunan verileri parçalar ve Komþuluk listesi
																		// yapar
		ArrayList<Node> nodes = new ArrayList<Node>();

		String[] lines = str.split("\n");

		String[] element = new String[2];
		for (String line : lines) {
			String[] dugumler = line.split("->");

			element = dugumler[0].split("/");

			Node root = new Node(element[1].trim(), element[0].trim(), null);
			Node temp = root;

			for (int i = 1; i < dugumler.length; i++) {

				element = dugumler[i].split("/");

				while (temp.getNext() != null) {
					temp = temp.getNext();
				}

				temp.setNext(new Node(element[1].trim(), element[0].trim(), null));

			}
			nodes.add(root);
		}

		return nodes;
	}

	public static void printAdjacencyList(ArrayList<Node> nodes) { // Komþuluk listesini yazdýran metot
		ArrayList<Node> temp = nodes;
		String str = "";
		System.out.println("Komþuluk Listesi:");
		
		for (Node node : temp) {
			Node t = node;
			while (t != null) {
				
				str += "->" + t.getName()+"/"+t.getPlate();
				t = t.getNext();
			}
			str = str.substring(2);
			System.out.println(str);
			str = "";
		}
		System.out.println();
	}

	public static void graphEdgeCounter(ArrayList<Node> nodes) { // Graftaki kenar sayýsýný sayan metot
		ArrayList<Node> temp = nodes;
		int edge = 0;
		for (Node node : temp) {
			Node t = node;
			while (t.getNext() != null) {
				edge++;
				t = t.getNext();
			}
		}
		System.out.println("Graftaki Toplam Kenar Sayýsý :" + edge);
	}

	public static void outDegree(String plate, ArrayList<Node> nodes) { // Giriþ derecesini hesaplayan metot
		int outdegree = 0;

		ArrayList<Node> temp = nodes;

		for (Node node : temp) {

			if (node.getPlate().equals(plate)) {
				Node t = node;
				while (t.getNext() != null) {
					outdegree++;
					t = t.getNext();
				}
			}
		}
		System.out.println(plate + " Plakalý düðümün çýkýþ derecesi: " + outdegree);

	}

	public static void inDegree(String plate, ArrayList<Node> nodes) { // Giriþ derecesini hesaplayan metot
		int indegree = 0;

		ArrayList<Node> temp = nodes;

		for (Node node : temp) {

			Node t = node.getNext() != null ? node.getNext() : null;
			while (t != null) {
				if (t.getPlate().equals(plate)) {
					indegree++;
				}

				t = t.getNext();
			}

		}
		System.out.println(plate + " Plakalý düðümün giriþ derecesi: " + indegree);

	}

	public static void whereToGo(String plate, ArrayList<Node> nodes) { // Plakadan hangi þehir düðümlerine gidildiðini yazan metot
		ArrayList<Node> temp = nodes;
		String str = "";
		for (Node node : temp) {

			if (node.getPlate().equals(plate)) {
				System.out.print(node.getPlate() + " Plakalý " + node.getName() + " þehrinden þu þehir düðümlerine gidilir: ");

				Node t = node.getNext();
				if (t == null) {
					str = "Hiçbir þehir düðümüne gidiþ yok ";
				}
				while (t != null) {
					str += t.getName() + "/" + t.getPlate() + ",";
					t = t.getNext();
				}
			}
		}
		str = str.substring(0, str.length() - 1); // en son fazla virgülü sildik veya boþluðu sildik
		System.out.println(str);
	}

	public static void whereToCome(String plate, ArrayList<Node> nodes) {  // Hangi þehir düðümlerinden verilen plakalý þehir düðümüne gelindiðini yazan metot

		ArrayList<Node> temp = nodes;
		String str = "";
		String strErr = "Hiç bir þehir düðümünden geliþ yok ";
		String city ="";
		boolean control = false;
		
		for (Node node : temp) {
			String name = node.getName();
			String plateNum = node.getPlate();
			
			if(city.equals("")) { //þehirin adýný plakadan aldýk
				city = node.getPlate().equals(plate) ? node.getName() : "";
			}
			
			Node t = node.getNext();
			while (t != null) {
				if (t.getPlate().equals(plate)) {
					control = true;
					
				}

				t = t.getNext();
			}
			if(control) {
				str += name + "/" + plateNum + ","; 
				control = false;
			}
			
		}
		str = str.equals("") ? "" : str.substring(0, str.length() - 1);
		System.out.print(plate + " Plakalý " + city + " þehrine þu þehir düðümlerinden gelinir: ");
		System.out.println(str.equals("") ? strErr : str);

	}
	
	public static boolean isPlateAvailable(String plate, ArrayList<Node> nodes) { // plaka var mý yok mu kontrol eden fonksiyon
		ArrayList<Node> temp = nodes;
		boolean control = false;
		for(Node node : temp) {
			if(node.getPlate().equals(plate)) {
				control = true;
			}
		}
		
		return control;
	}

}
