
public class Node {
	private String plate;
	private String name;
	private Node next;
	
	public Node() {
		this.plate = "";
		this.name = "";
		this.next = null;
	}
	
	public Node(String plate,String name, Node next) {
		this.plate = plate;
		this.name = name;
		this.next = next;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}
	
	


}
