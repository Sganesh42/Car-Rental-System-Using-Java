package CarRentalSystem;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Car Class
class Car {
	private String carId;
	private String carModel;
	private String carBrand;
	private double basePricePerDay;
	private boolean isAvailable;

	public Car(String carId, String carModel, String carBrand, double basePricePerDay) {
		this.carId = carId;
		this.carModel = carModel;
		this.carBrand = carBrand;
		this.basePricePerDay = basePricePerDay;
		this.isAvailable = true;
	}

	public String getCarId() {
		return carId;
	}

	public String getCarModel() {
		return carModel;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public double calculatePrice(int rentalDays) {
		return basePricePerDay * rentalDays;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void rent() {
		isAvailable = false;
	}

	public void returnCar() {
		isAvailable = true;
	}

}

// Customer Class
class Customer {
	private String customerId;
	private static String customerName;

	public Customer(String customerId, String customerName) {
		this.customerId = customerId;
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public static String getCustomerName() {
		return customerName;
	}

}

//Rental Class
class Rental {
	private Car car;
	private static Customer customer;
	private int days;

	public Rental(Car car, Customer customer, int days) {
		this.car = car;
		this.customer = customer;
		this.days = days;
	}

	public Car getCar() {
		return car;
	}

	public static Customer getCustomer() {
		return customer;
	}

	public int getDays() {
		return days;
	}
}

//MAIN Car Rental System Class
public class CarRentalSystem {
	private List<Car> cars;
	private List<Customer> customers;
	private List<Rental> rentals;

	public CarRentalSystem() {
		cars = new ArrayList<>();
		customers = new ArrayList<>();
		rentals = new ArrayList<>();
	}

	public void addCar(Car car) {
		cars.add(car);
	}

	public void addCustomer(Customer customer) {
		if (!customers.contains(customer)) {
			customers.add(customer);
		}
	}

	public void rentCar(Car car, Customer customer, int days) {
		if (car.isAvailable()) {
			car.rent();
			rentals.add(new Rental(car, customer, days));
			System.out.println("Car Rental Successful :)");
		} else {
			System.out.println("Car is Unavailable for Rent!");
		}
	}

	public void returnCar(Car car) {
		car.returnCar();
		Rental rentalToRemove = null;
		for (Rental rental : rentals) {
			if (rental.getCar().equals(car)) {
				rentalToRemove = rental;
				break;
			}
		}
		if (rentalToRemove != null) {
			rentals.remove(rentalToRemove);
			System.out.println("Car Returned Successfully by: " + Customer.getCustomerName());
		} else {
			System.out.println("Car was not Rented");
		}
	}

	public void menu() {
		Scanner sc = new Scanner(System.in);
		try {
			while (true) {
				System.out.println("\n===== CAR RENTAL SYSTEM =====");
				System.out.println("1. Rent a Car");
				System.out.println("2. Return a Car");
				System.out.println("3. View Available Cars");
				System.out.println("4. Exit");
				System.out.print("Enter Your Choice: ");

				int choice;
				try {
					choice = sc.nextInt();
				} catch (InputMismatchException e) {
					System.err.println("Invalid input! Please enter a number.");
					sc.nextLine(); // Clear buffer
					continue;
				}

				if (choice == 1) {
					System.out.print("Enter Your Name: ");
					String customerName = sc.next();

					System.out.println("\nAvailable Cars:");
					boolean carsAvailable = false;
					for (Car car : cars) {
						if (car.isAvailable()) {
							System.out.println(car.getCarId() + " - " + car.getCarBrand() + " - " + car.getCarModel());
							carsAvailable = true;
						}
					}
					if (!carsAvailable) {
						System.out.println("No cars available for rent.");
						continue;
					}

					System.out.print("Enter the Car ID you want to rent: ");
					String carId = sc.next();

					System.out.print("Enter rental days: ");
					int rentalDays;
					try {
						rentalDays = sc.nextInt();
						if (rentalDays <= 0) {
							throw new InputMismatchException();
						}
					} catch (InputMismatchException e) {
						System.err.println("Invalid number of days. Enter a positive integer.");
						sc.nextLine();
						continue;
					}

					Car selectedCar = null;
					for (Car car : cars) {
						if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
							selectedCar = car;
							break;
						}
					}

					if (selectedCar != null) {
						double totalPrice = selectedCar.calculatePrice(rentalDays);
						System.out.printf("Total Price: $%.2f%n", totalPrice);
						System.out.print("Confirm rental (y/n): ");
						String confirm = sc.next();

						if (confirm.equalsIgnoreCase("y")) {
							Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
							addCustomer(newCustomer);
							rentCar(selectedCar, newCustomer, rentalDays);
						} else {
							System.out.println("Car Rental Cancelled.");
						}
					} else {
						System.out.println("Invalid Car ID or Car is Unavailable.");
					}
				} else if (choice == 2) {
					System.out.print("Enter the Car ID you want to return: ");
					String carId = sc.next();

					Car carToReturn = null;
					for (Car car : cars) {
						if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
							carToReturn = car;
							break;
						}
					}

					if (carToReturn != null) {
						returnCar(carToReturn);
					} else {
						System.out.println("Invalid Car ID or Car was not rented.");
					}
				} else if (choice == 3) {
					System.out.println("\nAvailable Cars:");
					for (Car car : cars) {
						if (car.isAvailable()) {
							System.out.println(car.getCarId() + " - " + car.getCarBrand() + " - " + car.getCarModel());
						}
					}
				} else if (choice == 4) {
					break;
				} else {
					System.out.println("Invalid choice. Enter a valid option.");
				}
			}
		} finally {
			sc.close();
			System.out.println("Thank You for Using Car Rental System :)");
		}
	}

	public static void main(String[] args) {
		CarRentalSystem rentalSystem = new CarRentalSystem();

		Car car1 = new Car("C001", "Punch.ev", "TATA", 1200);
		Car car2 = new Car("C002", "Thar Roxx", "Mahindra", 1500);
		Car car3 = new Car("C003", "Swift", "Maruti Suzuki", 800);
		Car car4 = new Car("C004", "i20", "Hyundai", 900);
		rentalSystem.addCar(car1);
		rentalSystem.addCar(car2);
		rentalSystem.addCar(car3);
		rentalSystem.addCar(car4);

		rentalSystem.menu();
	}
}
