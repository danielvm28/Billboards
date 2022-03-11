package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;

import exception.AlreadyLoadedException;

public class BillboardSystem {
	// Attributes
	private ArrayList<Billboard> savedBillboards;

	private ArrayList<Billboard> dangerousBillboards;

	private boolean dataLoaded1;

	private boolean dataLoaded2;

	// Constructor
	public BillboardSystem() {
		savedBillboards = new ArrayList<Billboard>();
		dangerousBillboards = new ArrayList<Billboard>();
		this.dataLoaded1 = false;
		this.dataLoaded2 = false;
	}

	// Methods
	public ArrayList<Billboard> getSavedBillboards() {
		return savedBillboards;
	}

	public void setSavedBillboards(ArrayList<Billboard> savedBillboards) {
		this.savedBillboards = savedBillboards;
	}

	public ArrayList<Billboard> getDangerousBillboards() {
		return dangerousBillboards;
	}

	public void setDangerousBillboards(ArrayList<Billboard> dangerousBillboards) {
		this.dangerousBillboards = dangerousBillboards;
	}

	public boolean isDataLoaded1() {
		return dataLoaded1;
	}

	public void setDataLoaded1(boolean dataLoaded1) {
		this.dataLoaded1 = dataLoaded1;
	}

	public boolean isDataLoaded2() {
		return dataLoaded2;
	}

	public void setDataLoaded2(boolean dataLoaded2) {
		this.dataLoaded2 = dataLoaded2;
	}

	/**
	 * Loads billboard data depending on the path given by the user
	 * @param path the path given
	 * @throws AlreadyLoadedException
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public void loadBaseData(String path) throws AlreadyLoadedException, NumberFormatException, IOException {

			boolean first = true;
			String[] billboardAttributes = new String[4];
			File file = new File(path);

			// Checks if the file was already loaded before
			if (path.equals("data/Datos1.csv") || path.equals("data\\Datos1.csv")) {
				if (dataLoaded1) {
					throw new AlreadyLoadedException();
				} else {
					dataLoaded1 = true;
				}
			} else {
				if (dataLoaded2) {
					throw new AlreadyLoadedException();
				} else {
					dataLoaded2 = true;
				}
			}

			// Reads the data and adds it to the saved billboards

			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null) {
				if (first) {
					first = false;
				} else {
					// Add billboards to the saved billboards and dangerous billboards ArrayLists
					
					billboardAttributes = line.split("\\|");

					Double width = Double.parseDouble(billboardAttributes[0]);
					Double height = Double.parseDouble(billboardAttributes[1]);
					boolean inUse = (billboardAttributes[2].equals("true") ? true : false);
					String brand = billboardAttributes[3];
					
					Double area = width * height;
					
					Billboard newBillboard = new Billboard(width, height, inUse, brand);
					
					savedBillboards.add(newBillboard);
					
					if (area > 200000) {
						dangerousBillboards.add(newBillboard);
					}
				}
			}

			fis.close();
	}

	/**
	 * Saves the billboards from this session
	 */
	public void saveDataJSON() {
		try {
			Gson gson = new Gson();
			String json = gson.toJson(this);

			File file = new File("data/billboardSessionData.json");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(json.getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the billboard data from the saved JSON file
	 */
	public void loadDataJSON() {
		try {
			FileInputStream fis = new FileInputStream(new File("data/billboardSessionData.json"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			String json = "";
			String line;
			while ((line = reader.readLine()) != null) {
				json += line;
			}

			// from String to object
			Gson gson = new Gson();
			BillboardSystem data = gson.fromJson(json, BillboardSystem.class);
			savedBillboards = data.savedBillboards;
			dangerousBillboards = data.dangerousBillboards;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes all the data contained in the arrays
	 */
	public void clearBillboards() {
		savedBillboards.clear();
		dangerousBillboards.clear();
		
		dataLoaded1 = false;
		dataLoaded2 = false;
	}
	
	/**
	 * Adds a new billboard to the corresponding ArrayLists
	 * @param width
	 * @param height
	 * @param inUse
	 * @param brand
	 */
	public void addBillboard(Double width, Double height, boolean inUse, String brand) {
		Billboard newBillboard = new Billboard(width, height, inUse, brand);
		savedBillboards.add(newBillboard);
		
		if (width * height > 200000) {
			dangerousBillboards.add(newBillboard);
		}
	}

	/**
	 * Prints all of the saved billboards
	 * @return billboards
	 */
	public String printBillboards() {
		String m = "";
		
		m += "W		H		in Use	Brand\n";
		m += "---		---		---	---\n";	
		
		for (Billboard billboard : savedBillboards) {
			m += billboard + "\n";
		}
		
		m += "\nTOTAL: " + savedBillboards.size() + " Billboards";
		
		return m;
	}

	/**
	 * Stores a danger report in a text file and returns its string value
	 * @return the string value
	 */
	public String exportDanger() {
		String dangerous = "";
		
		dangerous = "===========================\r\n"
				+ "DANGEROUS BILLBOARD REPORT\r\n"
				+ "===========================\r\n"
				+ "The dangerous billboards are: \n";
		
		for (int i = 0; i < dangerousBillboards.size(); i++) {
			Double area = dangerousBillboards.get(i).getHeight() * dangerousBillboards.get(i).getWidth();
			dangerous += (i+1) + ". Billboard " + dangerousBillboards.get(i).getBrand() + " with area " + area + " cm^2 \n";
		}
		
		try {
			File file = new File("data/report.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(dangerous.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dangerous;
		
	}

}
