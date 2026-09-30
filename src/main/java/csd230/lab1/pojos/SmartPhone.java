package csd230.lab1.pojos;

/**
 * Concrete domain class representing a SmartPhone product in the store catalog.
 * <p>
 * Extends the abstract {@link Product} class to incorporate mobile hardware
 * attributes including brand and display size, while fulfilling the {@link SaleableItem} contract.
 * </p>
 *
 * @author Firstname Lastname
 * @version 1.0
 */
public class SmartPhone extends Product {

    /**
     * The retail price of the smartphone in CAD.
     */
    private double price;

    /**
     * A detailed description of the smartphone features.
     */
    private String description;

    /**
     * The manufacturing brand or vendor (e.g., Apple, Samsung).
     */
    private String brand;

    /**
     * The physical display diagonal measure in inches.
     */
    private double screenSize;

    /**
     * Default no-argument constructor for Java Bean compliance and reflection frameworks.
     */
    public SmartPhone() {
        super();
    }

    /**
     * Fully parameterized constructor to instantiate a custom SmartPhone object.
     *
     * @param price       The monetary cost of the smartphone
     * @param description A brief overview of features
     * @param brand       The manufacturer name
     * @param screenSize  Screen dimension measured diagonally in inches
     */
    public SmartPhone(double price, String description, String brand, double screenSize) {
        super();
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.screenSize = screenSize;
    }

    /**
     * Gets the price of the smartphone.
     *
     * @return double representing unit price
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the smartphone.
     *
     * @param price double value for new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the description of the smartphone.
     *
     * @return String containing smartphone details
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the smartphone.
     *
     * @param description String value for smartphone details
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the brand of the smartphone.
     *
     * @return String representing brand name
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the smartphone.
     *
     * @param brand String specifying brand name
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the display screen size in inches.
     *
     * @return double screen size in inches
     */
    public double getScreenSize() {
        return screenSize;
    }

    /**
     * Sets the display screen size in inches.
     *
     * @param screenSize double screen size in inches
     */
    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    /**
     * Simulates selling the smartphone by displaying transaction output to standard console.
     * Fulfills the {@link SaleableItem} interface contract.
     */
    @Override
    public void sellItem() {
        System.out.println("Selling SmartPhone: " + getBrand() + " - " + getDescription() + " (Screen: " + screenSize + "\") for $" + getPrice());
    }

    /**
     * Interactively prompts user through standard console to modify existing smartphone properties.
     * Overrides {@link Editable#edit()}.
     */
    @Override
    public void edit() {
        System.out.print("Enter Price (" + getPrice() + "): ");
        setPrice(getInput(getPrice()));
        System.out.print("Enter Description (" + getDescription() + "): ");
        setDescription(getInput(getDescription()));
        System.out.print("Enter Brand (" + getBrand() + "): ");
        setBrand(getInput(getBrand()));
        System.out.print("Enter Screen Size in inches (" + getScreenSize() + "): ");
        setScreenSize(getInput(getScreenSize()));
    }

    /**
     * Interactively prompts user through standard console to initialize brand-new smartphone attributes.
     * Overrides {@link Editable#initialize()}.
     */
    @Override
    public void initialize() {
        System.out.print("Enter Price: ");
        setPrice(getInput(0.0));
        System.out.print("Enter Description: ");
        setDescription(getInput(""));
        System.out.print("Enter Brand: ");
        setBrand(getInput(""));
        System.out.print("Enter Screen Size in inches: ");
        setScreenSize(getInput(0.0));
    }

    /**
     * Formats object state as a String for debugging and logging purposes.
     *
     * @return String representation of SmartPhone fields
     */
    @Override
    public String toString() {
        return "SmartPhone{" +
                "price=" + price +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", screenSize=" + screenSize +
                "} " + super.toString();
    }
}