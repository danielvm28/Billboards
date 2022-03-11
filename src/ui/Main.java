package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import exception.AlreadyLoadedException;
import model.BillboardSystem;

public class Main {
	public static Scanner s = new Scanner(System.in);
	public static BillboardSystem bs = new BillboardSystem();
	
	public static void importCSV() throws NumberFormatException {
		try {
			String path = "";
			System.out.print("\nWrite the down the CSV path: ");
			s.nextLine();
			path = s.nextLine();
			bs.loadBaseData(path);
			bs.saveDataJSON();

			System.out.println("CSV imported successfully");
			System.out.println("Press enter to continue...");
			s.nextLine();
			
		} catch (FileNotFoundException e) {
			System.out.println("The specified file does not exist");
			System.out.println("Press enter to continue...");
			s.nextLine();
			
		} catch (AlreadyLoadedException e) {
			System.out.println(e.getMessage());
			System.out.println("Press enter to continue...");
			s.nextLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addBillboard() {
		Double height = 0.0;
		Double width = 0.0;
		String inUseString = "";
		boolean inUse = false;
		String brand = "";
		
		System.out.print("\nHeight of the billboard: ");
		height = s.nextDouble();
		
		System.out.print("Width of the billboard: ");
		width = s.nextDouble();
		
		System.out.print("Is the billboard in use? (Y/N): ");
		s.nextLine();
		inUseString = s.nextLine();
		
		if(inUseString.equals("Y") || inUseString.equals("y")) inUse = true;
		
		System.out.print("Brand of the billboard: ");
		brand = s.nextLine();
		
		bs.addBillboard(width, height, inUse, brand);
		bs.saveDataJSON();
	}
	
	public static void showBillboards() {
		System.out.println(bs.printBillboards());
		
		System.out.println("\nPress enter to continue...");
		s.nextLine();
		s.nextLine();
	}
	
	public static void showAndExportDangerous() {
		System.out.println(bs.exportDanger());
		
		System.out.println("\nPress enter to continue...");
		s.nextLine();
		s.nextLine();
	}

	public static void main(String[] args) {
		boolean exit = false;
		int selection = 0;

		bs.loadDataJSON();

		while (!exit) {
			System.out.println("\n---------------------------------------------------\n");
			System.out.println("BILLBOARD SYSTEM");
			System.out.println("(1) Import CSV Data indicating path");
			System.out.println("(2) Add Billboard");
			System.out.println("(3) Show Billboards");
			System.out.println("(4) Show and export dangerous billboards report");
			System.out.println("(5) Clear Billboards");
			System.out.println("(6) Exit");
			System.out.println("\n---------------------------------------------------\n");
			selection = s.nextInt();
			
			switch (selection) {
			case 1:
				importCSV();
				break;
			case 2:
				addBillboard();
				break;
			case 3:
				showBillboards();
				break;
			case 4:
				showAndExportDangerous();
				break;
			case 5:
				bs.clearBillboards();
				bs.saveDataJSON();
				System.out.println("Billboards cleared");
				System.out.println("Press enter to continue...");
				s.nextLine();
				s.nextLine();
				break;
			case 6:
				exit = true;
				break;
			default:
				System.out.println("Invalid Input");
				System.out.println("Press enter to continue...");
				s.nextLine();
				s.nextLine();
				break;
			}
		}	
	}
}
