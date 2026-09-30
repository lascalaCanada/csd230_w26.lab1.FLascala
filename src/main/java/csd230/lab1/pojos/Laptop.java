package csd230.lab1.pojos;

/**
 * Concrete domain class representing a Laptop product in the store catalog.
 * <p>
 * Extends the abstract {@link Product} class to model specific laptop features
 * including brand and RAM capacity, while fulfilling the {@link SaleableItem} contract.
 * </p>
 *
 * @author Firstname Lastname
 * @version 1.0
 */
public class Laptop extends Product {

    /**
     * The retail price of the laptop in CAD.
     */
    private double price;

    /**
     * A detailed description of the laptop hardware and usage.
     */
    private String description;

    /**
     * The manufacturing brand or vendor of the laptop (e.g., Dell, Apple).
     */
    private String brand;

    /**
     * System memory capacity allocated in Gigabytes (GB).
     */
    private int ramGb;

    /**
     * Default no-argument constructor for Java Bean compliance and reflection frameworks.
     */
    public Laptop() {
        super();
    }

    /**
     * Fully parameterized constructor to instantiate a custom Laptop object.
     *
     * @param price       The monetary cost of the laptop
     * @param description A brief overview of the laptop specifications
     * @param brand       The manufacturer name
     * @param ramGb       Total RAM available in Gigabytes
     */
    public Laptop(double price, String description, String brand, int ramGb) {
        super();
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.ramGb = ramGb;
    }

    /**
     * Gets the price of the laptop.
     *
     * @return double representing current unit price
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the laptop.
     *
     * @param price double value for new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the description of the laptop.
     *
     * @return String containing laptop details
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the laptop.
     *
     * @param description String value for laptop details
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the brand of the laptop.
     *
     * @return String representing brand name
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the laptop.
     *
     * @param brand String specifying brand name
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the RAM capacity in GB.
     *
     * @return int RAM size in GB
     */
    public int getRamGb() {
        return ramGb;
    }

    /**
     * Sets the RAM capacity in GB.
     *
     * @param ramGb int RAM size in GB
     */
    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    /**
     * Simulates selling the laptop by displaying transaction output to standard console.
     * Fulfills the {@link SaleableItem} interface contract.
     */
    @Override
    public void sellItem() {
        System.out.println("Selling Laptop: " + getBrand() + " - " + getDescription() + " (RAM: " + ramGb + "GB) for $" + getPrice());
    }

    /**
     * Interactively prompts user through standard console to modify existing laptop properties.
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
        System.out.print("Enter RAM in GB (" + getRamGb() + "): ");
        setRamGb(getInput(getRamGb()));
    }

    /**
     * Interactively prompts user through standard console to initialize brand-new laptop attributes.
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
        System.out.print("Enter RAM in GB: ");
        setRamGb(getInput(0));
    }

    /**
     * Formats object state as a String for debugging and logging purposes.
     *
     * @return String representation of Laptop fields
     */
    @Override
    public String toString() {
        return "Laptop{" +
                "price=" + price +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", ramGb=" + ramGb +
                "} " + super.toString();
    }
}