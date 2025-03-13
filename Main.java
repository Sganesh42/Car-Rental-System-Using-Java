package CarRentalSystem;

import java.util.ArrayList;
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
	private String customerName;

	public Customer(String customerId, String customerName) {
		this.customerId = customerId;
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

}

//Rental Class
class Rental {
	private Car car;
	private Customer customer;
	private int days;

	public Rental(Car car, Customer customer, int days) {
		this.car = car;
		this.customer = customer;
		this.days = days;
	}

	public Car getCar() {
		return car;
	}

	public Customer getCustomer() {
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
		customers.add(customer);
	}

	public void rentCar(Car car, Customer customer, int days) {
		if (car.isAvailable()) {
			car.rent();
			rentals.add(new Rental(car, customer, days));
		} else {
			System.out.println("Car is Unavailable for Rent!");
		}
	}

	public void returnCar(Car car) {
		car.returnCar();
		Rental rentalToRemove = null;
		for (Rental rental : rentals) {
			if (rental.getCar() == car) {
				rentalToRemove = rental;
				break;
			}
		}
		if (rentalToRemove != null) {
			rentals.remove(rentalToRemove);
			System.out.println("Car Returned Successfully");
		} else {
			System.out.println("Car was not Rented");
		}
	}

	public void menu() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("*****CAR RENTAL SYSTEM*****");
			System.out.println("Enter Your Choice:");
			System.out.println("1.Rent a Car");
			System.out.println("2.Return a Car");
			System.out.println("3.Exit");
			int choice = sc.nextInt();
			sc.nextLine();

			if (choice == 1) {
				System.out.println("\n===Rent a Car===\n");
				System.out.println("Enter Your Name: ");
				String customerName = sc.next();

				System.out.println("\nAvailable Cars:\n");
				for (Car car : cars) {
					if (car.isAvailable()) {
						System.out.println(car.getCarId() + " - " + car.getCarBrand() + " - " + car.getCarModel());
					}
				}
				System.out.println("\nEnter the car Id you want to rent: ");
				String carId = sc.next();

				System.out.println("Enter the days you want for rental: ");
				int noOfDays = sc.nextInt();
				sc.nextLine();

				Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
				addCustomer(newCustomer);

				Car selectedCar = null;
				for (Car car : cars) {
					if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
						selectedCar = car;
						break;
					}
				}

				if (selectedCar != null) {
					double totalPrice = selectedCar.calculatePrice(noOfDays);
					System.out.println("\n===Rental Information===\n");
					System.out.println("Customer Id: " + newCustomer.getCustomerId());
					System.out.println("Customer Name: " + newCustomer.getCustomerName());
					System.out.println("Car: " + selectedCar.getCarBrand() + " " + selectedCar.getCarModel());
					System.out.println("Rental Days: " + noOfDays);
					System.out.printf("Total Price: $%.2f%n", totalPrice);

					System.out.println("\nConfirm rental(y/n): ");
					String confirm = sc.next();

					if (confirm.equalsIgnoreCase("y")) {
						rentCar(selectedCar, newCustomer, noOfDays);
						System.out.println("Car Rental Successful :)");
					} else {
						System.out.println("Car Rental Cancelled.");
					}
				} else {
					System.out.println("\nInvalid car selection or car is unavailable for rent.");
				}
			} else if (choice == 2) {
				System.out.println("\n===Return a Car===\n");
				System.out.println("Enter the Car Id you want to return: ");
				String carId = sc.next();

				Car carToReturn = null;
				for (Car car : cars) {
					if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
						carToReturn = car;
						break;
					}
				}
				if (carToReturn != null) {
					Customer customer = null;
					for (Rental rental : rentals) {
						if (rental.getCar() == carToReturn) {
							customer = rental.getCustomer();
							break;
						}
					}
					if (customer != null) {
						returnCar(carToReturn);
						System.out.println("Car Returned Successfull by: " + customer.getCustomerName());
					} else {
						System.out.println("Car was not rented or rental information is missing");
					}
				} else {
					System.out.println("Invalid car Id or car was not rented");
				}
			} else if (choice == 3) {
				break;
			} else {
				System.out.println("Invalid choice. Enter valid option.");
			}
		}

		System.out.println("Thank You for Using Car Rental System :)");
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
