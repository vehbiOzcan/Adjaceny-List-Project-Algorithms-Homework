import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		ArrayList<Node> nodes = splitAndAdjacencyList(fileRead("./src/veri.txt")); // dosyay� okuyup ard�ndan kom�uluk
																					// listesi yapt�k

		Scanner scann = new Scanner(System.in);
		
		String input = "1";
		while(input.equals("1")) {
			System.out.print("��lem yapmak istedi�iniz plakay� giriniz: ");
			String plate = scann.nextLine();
			
			if(isPlateAvailable(plate, nodes)) {

				printAdjacencyList(nodes); //Kom�uluk listesini yazd�rma

				inDegree(plate, nodes); //Giri� derecesi
				outDegree(plate, nodes);//��k�� derecesi

				graphEdgeCounter(nodes); //Graf�n kenar say�s�

				whereToGo(plate, nodes); ////Hangi d���mlere gidilir
				
				whereToCome(plate, nodes); //Hangi d���mlerden gelinir
			}else {
				System.out.println(plate + " Plaka numaras�na ait bir d���m yok ge�erli bir plaka giriniz ! ");
				
			}
			System.out.print("Devam etmek i�in 1 e ��kmak i�in 0 a bas�n�z: ");
			input = scann.nextLine(); 
			System.out.println();
			System.out.println("************************************************************************************************");
			System.out.println();
		}
		System.out.println("Ho��akal�n :) ");

	}

	public static String fileRead(String path) { // dosyay� okur ve yap�y� bozmadan string olarak d�ner
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

	public static ArrayList<Node> splitAndAdjacencyList(String str) { // Okunan verileri par�alar ve Kom�uluk listesi
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

	public static void printAdjacencyList(ArrayList<Node> nodes) { // Kom�uluk listesini yazd�ran metot
		ArrayList<Node> temp = nodes;
		String str = "";
		System.out.println("Kom�uluk Listesi:");
		
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

	public static void graphEdgeCounter(ArrayList<Node> nodes) { // Graftaki kenar say�s�n� sayan metot
		ArrayList<Node> temp = nodes;
		int edge = 0;
		for (Node node : temp) {
			Node t = node;
			while (t.getNext() != null) {
				edge++;
				t = t.getNext();
			}
		}
		System.out.println("Graftaki Toplam Kenar Say�s� :" + edge);
	}

	public static void outDegree(String plate, ArrayList<Node> nodes) { // Giri� derecesini hesaplayan metot
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
		System.out.println(plate + " Plakal� d���m�n ��k�� derecesi: " + outdegree);

	}

	public static void inDegree(String plate, ArrayList<Node> nodes) { // Giri� derecesini hesaplayan metot
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
		System.out.println(plate + " Plakal� d���m�n giri� derecesi: " + indegree);

	}

	public static void whereToGo(String plate, ArrayList<Node> nodes) { // Plakadan hangi �ehir d���mlerine gidildi�ini yazan metot
		ArrayList<Node> temp = nodes;
		String str = "";
		for (Node node : temp) {

			if (node.getPlate().equals(plate)) {
				System.out.print(node.getPlate() + " Plakal� " + node.getName() + " �ehrinden �u �ehir d���mlerine gidilir: ");

				Node t = node.getNext();
				if (t == null) {
					str = "Hi�bir �ehir d���m�ne gidi� yok ";
				}
				while (t != null) {
					str += t.getName() + "/" + t.getPlate() + ",";
					t = t.getNext();
				}
			}
		}
		str = str.substring(0, str.length() - 1); // en son fazla virg�l� sildik veya bo�lu�u sildik
		System.out.println(str);
	}

	public static void whereToCome(String plate, ArrayList<Node> nodes) {  // Hangi �ehir d���mlerinden verilen plakal� �ehir d���m�ne gelindi�ini yazan metot

		ArrayList<Node> temp = nodes;
		String str = "";
		String strErr = "Hi� bir �ehir d���m�nden geli� yok ";
		String city ="";
		boolean control = false;
		
		for (Node node : temp) {
			String name = node.getName();
			String plateNum = node.getPlate();
			
			if(city.equals("")) { //�ehirin ad�n� plakadan ald�k
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
		System.out.print(plate + " Plakal� " + city + " �ehrine �u �ehir d���mlerinden gelinir: ");
		System.out.println(str.equals("") ? strErr : str);

	}
	
	public static boolean isPlateAvailable(String plate, ArrayList<Node> nodes) { // plaka var m� yok mu kontrol eden fonksiyon
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
