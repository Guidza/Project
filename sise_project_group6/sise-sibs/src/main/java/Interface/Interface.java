//Carlos Morais nº3
//Guilherme Costa nº5
//grupo 6
package Interface;

import java.io.BufferedInputStream;
import java.util.Random;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Interface {

	public static void main(String[] args)
			throws BankException, ClientException, AccountException, MbWayException, SibsException, OperationException {
		Services service = new Services();
		Sibs sibs = new Sibs(10000, service);
//		Bank bank1 = new Bank("CGD");
//		Bank bank2 = new Bank("CTT");
//		Client client1 = new Client(bank1, new IdCard("Dorian Grey", "123456789", "casa pia", 200), "914056607");
//		Client client2 = new Client(bank1, new IdCard("Luke Skywalker", "234567891", "Tatooine", 20), "917722338");
//		Client client3 = new Client(bank2, new IdCard("Tom Riddle", "345678912", "Hogwarts", 60), "666666666");
//		bank1.createAccount(AccountType.CHECKING, client1, 10000, 1000); // Iban CGDCK1
//		bank1.createAccount(AccountType.CHECKING, client2, 10000, 1000); // Iban CGDCK2
//		bank2.createAccount(AccountType.CHECKING, client3, 10000, 1000); // Iban CTTCK3
		MbwayAccount mbway;
		while (true) {
			String str[] = lerInput();
			String comando = str[0];

			switch (comando) {
			case "exit":
				System.exit(0);
			case "associate-mbway":
				String iban = str[1];
				String phoneNumber = str[2];

				mbway = createMbWay(phoneNumber, iban, service);
				if (mbway == null) {
					break;
				}
				Interface.setCode(mbway);
				break;

			case "confirm-mbway":
				confirmMbWay(str[1], Integer.parseInt(str[2]));
				break;
			case "mbway-transfer":
				if (checkParametersTransfer(str[1], str[2], Integer.parseInt(str[3]))) {
					System.out.println("Transfer successful!");
					break;
				} else {
					break;
				}

			case "mbway-split-bill":
				int nrFriends = Integer.parseInt(str[1]);
				int amountToSplit = Integer.parseInt(str[2]);
				int addedFriends = 0;
				MbwayAccount[] friends = new MbwayAccount[nrFriends];
				int[] amounts = new int[nrFriends];

				Boolean o = true;
				int friendsAmount = 0;
				int i = 0;
				while (o == true) {
					String str1[] = Interface.lerInput();
					String comand = str1[0];
					if (comand.equals("friend")) {
						try {
							friends[i] = MbWay.getAccount(str1[1]);
							amounts[i] = Integer.parseInt((str1[2]));
							++i;
							addedFriends++;
							friendsAmount += Integer.parseInt(str1[2]);
						} catch (MbWayException e) {
							System.out.println("Friend does not have a MBway account!");
							o = false;
							break;
						}

						if (service.getAccountByIban((MbWay.getAccount(str1[1]).getIban())).getBalance() < Integer
								.parseInt(str1[2])) {
							System.out.println("Oh no! One of your friends does not have money to pay!");
							o = false;
							break;
						}
					} else if (comand.equals("end")) {
						o = false;
					} else {
						System.out.println("It was not a friend command!");
					}

				}
				if (!checkFriends(addedFriends, nrFriends)) {
					break;
				}

				if (amountToSplit != friendsAmount) {
					System.out.println("Something is wrong. Did you set the bill amount right?");
					break;
				}
				transfers(friends, amounts, sibs);
				break;
			}

		}
	}

	// Metodos criados para melhorar a primeira heuristica
	public static void setCode(MbwayAccount mbway) {
		Random rand = new Random();
		Integer code = 1000000 + rand.nextInt(8999999);
		mbway.setCode(code);
		System.out.println("Code: " + code + " (Dont share it with anyone)");
	}

	public static void transfers(MbwayAccount[] friends, int[] amounts, Sibs sibs)
			throws SibsException, AccountException, OperationException, BankException {
		String target = friends[0].getIban();
		for (int j = 1; j < friends.length; j++) {
			String source = friends[j].getIban();
			int share = amounts[j];
			sibs.transfer(source, target, share);
		}
	}

	public static boolean confirmMbWay(String phone, int cod) throws MbWayException {
		MbwayAccount mb = null;
		try {
			mb = MbWay.getAccount(phone);
		} catch (MbWayException e) {
			System.out.println("Wrong Confirmation Code!");
		}

		if (mb.getCode().equals(cod)) {
			mb.setState(true);
			System.out.println("MBWay association confirmed successfully!");
			return true;
		} else {
			System.out.println("Wrong Confirmation Code!");
			return false;
		}
	}

	public static String[] lerInput() {
		Scanner input = new Scanner(new BufferedInputStream(System.in));
		String linha = input.nextLine();
		String str[] = linha.split(" ");
		return str;
	}

	public static MbwayAccount createMbWay(String phoneNumber, String iban, Services service) {
		try {
			MbwayAccount mb = new MbwayAccount(phoneNumber, iban, service);
			return mb;
		} catch (AccountException e) {
			System.out.println("No account with such IBAN");
			return null;
		}

	}

	public static boolean checkParametersTransfer(String sourcePhone, String targetPhone, int amount) {
		MbwayAccount targetMb;
		MbwayAccount sourceMb;
		try {
			sourceMb = MbWay.getAccount(sourcePhone);
		} catch (MbWayException e) {
			System.out.println("Could not complete transfer!");
			return false;
		}
		try {
			targetMb = MbWay.getAccount(targetPhone);
		} catch (MbWayException e) {
			System.out.println("Could not complete transfer!");
			return false;
		}
		if (targetMb.getState() == false || sourceMb.getState() == false) {
			System.out.println("Could not complete transfer!");
			return false;
		}
		return true;
	}

	public static boolean checkFriends(int addedFriends, int nrFriends) {
		if (addedFriends != nrFriends) {
			if (addedFriends < nrFriends) {
				System.out.println("Oh no! One friend is missing.");
				return false;
			} else if (addedFriends > nrFriends) {
				System.out.println("Oh no! Too many friends.");
				return false;
			}
		}
		return true;
	}

}
