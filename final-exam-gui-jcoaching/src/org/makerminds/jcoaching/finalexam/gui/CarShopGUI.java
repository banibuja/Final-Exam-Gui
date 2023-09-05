package org.makerminds.jcoaching.finalexam.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.makerminds.jcoaching.finalexam.Car;
import org.makerminds.jcoaching.finalexam.CarFileManager;
import org.makerminds.jcoaching.finalexam.CarImporter;
import org.makerminds.jcoaching.finalexam.CarShopProcessor;

@SuppressWarnings("serial")
public class CarShopGUI extends JFrame {

	private static final String CAR_LIST_PATH = "carList.txt";
	
	private JPanel mainPanel;
	private String carArray[][];
	private List<Car> carList;
	private JTable carTable;
	private String[] header;
	private JScrollPane carTableScrollPane;
	
	public CarShopGUI() {
		CarImporter carImporter = new CarImporter();
		CarFileManager carFileManager = new CarFileManager(CAR_LIST_PATH);

		String[] carListAsArray = carFileManager.importCarsFromFile();
		carList = carImporter.importToJavaObjects(carListAsArray);

		header = prepareHeaderForTable();

		carArray = prepareCarArray(carList);

		mainPanel = prepareMainPanel();

		carTable = prepareTableForCars();
		carTableScrollPane = preparePanelForDisplayCars(carTable);
		mainPanel.add(carTableScrollPane);

		preparePanelForChoseAndBuy();
	}

	private String[] prepareHeaderForTable() {
		String header[] = {
				"ID",
				"Manufacturer", 
				"Model", 
				"Horse-Power", 
				"Price", 
				"Color", 
				"Mileage", 
				"Production Year",
				"Fuel Type", 
				"Transmission" 
				};
		return header;
	}

	public static void main(String[] args) {
		CarShopGUI frame = new CarShopGUI();
		frame.setVisible(true);
	}

	private void preparePanelForChoseAndBuy() {
		JPanel panelForChoseAndBuy = new JPanel(new FlowLayout());
		panelForChoseAndBuy.setBorder(BorderFactory.createTitledBorder("Select and Sell"));
		panelForChoseAndBuy.setOpaque(true);
		mainPanel.add(panelForChoseAndBuy);

		JLabel lblWriteID = new JLabel("Select the car you want to sell");
		JButton button = new JButton("Sell");
		panelForChoseAndBuy.add(lblWriteID);
		panelForChoseAndBuy.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int column = 0;
				int selectedRow = carTable.getSelectedRow();
				String carChosenId = carTable.getModel().getValueAt(selectedRow, column).toString();
				int carChosenIdLikeInt = Integer.parseInt(carChosenId);

				CarShopProcessor carShopProcessor = new CarShopProcessor();
				carShopProcessor.sellCars(carList, carChosenIdLikeInt);
				TableModel model = carTable.getModel();
				((DefaultTableModel)model).removeRow(selectedRow);
				
				CarFileManager fileManager = new CarFileManager(CAR_LIST_PATH);
				fileManager.rewriteFile(carList);
			}
		});
	}

	private JTable prepareTableForCars() {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setDataVector(carArray, header);
		JTable table = new JTable(tableModel) {
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(800, 175);
			};
		};
		return table;
	}

	private JScrollPane preparePanelForDisplayCars(JTable table) {
		JScrollPane panelForDisplayCars = new JScrollPane(table);
		panelForDisplayCars.setBorder(BorderFactory.createTitledBorder("List of Cars"));
		return panelForDisplayCars;
	}

	private JPanel prepareMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(mainPanel);
		setSize(1280, 720);
		setVisible(true);
		setLayout(new FlowLayout()); // Layout define
		setLocationRelativeTo(null); // Display in center GUI window
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		return mainPanel;
	}

	public String[][] prepareCarArray(List<Car> carsList) {
		String cars[][] = new String[carsList.size()][10];

		for (int i=0; i < carsList.size(); i++) {
			Car car = carsList.get(i);
			cars[i][0] = Integer.toString(car.getId());
			cars[i][1] = car.getManufacturer().name();
			cars[i][2] = car.getModel();
			cars[i][3] = Integer.toString(car.getHorsePower());
			cars[i][4] = Double.toString(car.getPrice());
			cars[i][5] = car.getColor().name();
			cars[i][6] = Integer.toString(car.getMileage());
			cars[i][7] = Integer.toString(car.getProductionYear());
			cars[i][8] = car.getFuelType().name();
			cars[i][9] = car.getTransmission().name();
		}

		return cars;
	}
}
