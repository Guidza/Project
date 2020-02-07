package pt.ulisboa.tecnico.learnjava.bank.domain;

public class IdCard {
	private final String[] fullName;
	private final String nif;
	private final String address;
	private int age;

	public IdCard(String fullName, String nif, String address, int age) {
		this.fullName = fullName.split(" ");
		this.nif = nif;
		this.address = address;
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String[] getFullName() {
		return fullName;
	}

	public String getNif() {
		return nif;
	}

	public String getAddress() {
		return address;
	}

}
