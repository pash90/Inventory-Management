import java.util.*;
import java.text.*;
public class Product
{
    private String productName; // name of the product
    private Date boughtOn; // bought on date
    private Date useBy; // use by date
    private ArrayList<Date> soldOn; // date product is sold
    private int quantity; // number of such items
    private int originalQuantity; // number of such items - never edited
    private double boughtAt; // buying price
    private ArrayList<Double> soldAt; // selling price of the product at the instance
    private ArrayList<Integer> quantitySold; // quantity sold in the instance
    
    // boolean variables for checks
    private boolean purchaseDateSet;
    private boolean purchasePriceSet;
    private boolean sellDateSet;
    private boolean sellPriceSet;
    private boolean expiryDateSet;
    private boolean discarded;
    
    /**
     * <p>
     * Initialises a <code>Product</code> instance
     * </p>
     */
    public Product()
    {
        productName = "";
        boughtOn = null;
        useBy = null;
        boughtAt = 0.0;
        quantity = 0;
        originalQuantity = 0;
        soldOn = new ArrayList<Date>();
        soldAt = new ArrayList<Double>();
        quantitySold = new ArrayList<Integer>();

        // setting the bool values
        purchaseDateSet = false;
        purchasePriceSet = false;
        sellPriceSet = false;
        sellDateSet = false;
        expiryDateSet = false;
        discarded = false;
    }
    
    /**
     * <p>
     * Sets the name of the <code>Product</code> to specified String
     * </p>
     * 
     * @param name The name of the product
     */
    public void setProductName(String name)
    {
        productName = name.toLowerCase();
    }
    
    /**
     * <p>
     * Sets the quantity of the <code>Product</code> to specified <code>int</code> number
     * </p>
     * 
     * @param itemQuantity The quantity of the product
     */
    public void setQuantity(int itemQuantity)
    {
        quantity = itemQuantity;
        originalQuantity = itemQuantity;
    }
    
    /**
     * <p>
     * Sets the purchase date of the <code>Product</code> to specified <code>Date</code>
     * </p>
     * 
     * @param date The purchase date of the product
     */
    public void setPurchaseDate(Date date)
    {
        boughtOn = date;
        purchaseDateSet = true;
    }
    
    /**
     * <p>
     * Sets the purchase price of the <code>Product</code> to specified <code>double</code> number
     * </p>
     * 
     * @param price The purchase price of the product
     */
    public void setPurchasePrice(double price)
    {
        boughtAt = price;
        purchasePriceSet = true;
    }
    
    /**
     * <p>
     * Sets the expiry date of the <code>Product</code> to specified <code>Date</code>
     * </p>
     * 
     * @param date The expiry date of the product
     */
    public void setExpiryDate(Date date)
    {
        useBy = date;
        expiryDateSet = true;
    }
    
    /**
     * <p>
     * Sets the sell date of the <code>Product</code> to specified <code>Date</code>
     * </p>
     * 
     * @param date The sell date of the product
     */
    public void addSaleDate(Date date)
    {
        soldOn.add(date);
        sellDateSet = true;
    }
    
    /**
     * <p>
     * Sets the selling price of the <code>Product</code> to specified <code>double</code> number
     * </p>
     * 
     * @param price The selling price of the product
     */
    public void setSellPrice(double price)
    {
        soldAt.add(price);
        sellPriceSet = true;
    }
    
    /**
     * <p>
     * Decreases the quantity of the <code>Product</code> to by the <code>int</code> number
     * </p>
     * 
     * @param decreaseBy The quantity of the product to be reduced by
     */
    public void editProductQuantity(int decreaseBy)
    {
        quantitySold.add(decreaseBy);
        quantity -= decreaseBy;
    }
    
    /**
     * <p>
     * Sets the <code>Product</code> as expired and discarded
     * </p>
     * 
     */
    public void discarded()
    {
        discarded = true;
    }
    
    // Getting information
    
    /**
     * <p>Gives the name of the <code>Product</code> </p>
     * 
     * @return String
     */
    String getProductName() {
        return productName;
    }
    
    /**
     * <p>Gives the purchase date of the <code>Product</code> </p>
     * 
     * @return Date
     */
    Date getPurchaseDate() {
        return boughtOn;
    }
    
    /**
     * <p>Gives the expiry date of the <code>Product</code> </p>
     * 
     * @return Date
     */
    Date getExpiryDate() {
        return useBy;
    }
    
    /**
     * <p>Gives the purchase price of the <code>Product</code> </p>
     * 
     * @return double
     */
    double getPurchasePrice() {
        return boughtAt;
    }
    
    /**
     * <p>Gives the remaining quantity of the <code>Product</code> </p>
     * 
     * @return int
     */
    int getQuantity() {
        return quantity;
    }
    
    /**
     * <p>Gives the original quanitity of the <code>Product</code> </p>
     * 
     * @return int
     */
    int getOriginalQuantity() {
        return originalQuantity;
    }
    
    /**
     * <p>Gives the date on which the <code>Product</code> was sold</p>
     * 
     * @return ArrayList - The list of all dates on which each particular quantity was sold
     */
    ArrayList<Date> getSellDate() {
        return soldOn;
    }

    /**
     * <p>Gives the price at which the <code>Product</code> was sold</p>
     * 
     * @return ArrayList - The list of all prices at which each particular quantity was sold
     */
    ArrayList<Double> getSellPrice() {
        return soldAt;
    }
    
    /**
     * <p>Gives the quantities of the <code>Product</code> sold</p>
     * 
     * @return ArrayList - The list of all individual quantities sold
     */
    ArrayList<Integer> getQuantitySold() {
        return quantitySold;
    }
    
    // checking whether properties are set or not
    
    /**
     * <p>Checks whether purchase date is available</p>
     * 
     * @return boolean
     */
    boolean hasPurchaseDate()
    {
        return purchaseDateSet;
    }

    /**
     * <p>Checks whether purchase price is available</p>
     * 
     * @return boolean
     */
    boolean hasPurchasePrice()
    {
        return purchasePriceSet;
    }

    /**
     * <p>Checks whether selling date is available</p>
     * 
     * @return boolean
     */
    boolean hasSellDate()
    {
        return sellDateSet;
    }

    /**
     * <p>Checks whether selling price is available</p>
     * 
     * @return boolean
     */
    boolean hasSellPrice()
    {
        return sellPriceSet;
    }

    /**
     * <p>Checks whether expiry date is available</p>
     * 
     * @return boolean
     */
    boolean hasExpiryDate()
    {
        return expiryDateSet;
    }
    
    /**
     * <p>Checks whether the <code>Product</code> is discarded</p>
     * 
     * @return boolean
     */
    boolean isDiscarded()
    {
        return discarded;
    }
}
