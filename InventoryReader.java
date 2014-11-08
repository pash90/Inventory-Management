import java.io.*;
import java.util.*;
import java.text.*;
class InventoryReader
{
    private ArrayList<Product> inventory = new ArrayList<Product>();
    
    /**
     * Constructor
     * 
     * <p>
     * Scans the inventory and converts them into <code>Product</code> instances
     * </p>
     * 
     * @param inventoryFile The text file containing the store inventory
     */
    InventoryReader(String inventoryFile) throws Exception
    {
        Scanner scan = new Scanner(new File(inventoryFile));
        // first read each line till a blank and enter the details everytime.
        Map<String,String> productMap = new HashMap<String,String>();
        while(scan.hasNextLine()) // checking every line of the inventory file
        {
            String line = scan.nextLine();
            if(!line.isEmpty())
            {
                Scanner lineScan = new Scanner(line);
                String property = "";
                String value = "";
                //if(lineScan.hasNext())
                {
                    property = lineScan.next();
                    property = property.trim();
                    
                    value = line.substring(property.length());
                    value = value.trim();
                    
                    productMap.put(property,value);
                    //System.out.println(property + " : " + productMap.get(property));
                }
            }
            else
            {
                if(!productMap.isEmpty())
                    inventory = ProductProcessor.buy(productMap,inventory);
                productMap.clear();
            }
        }
        
        if(!productMap.isEmpty())
            inventory = ProductProcessor.buy(productMap,inventory);
    }    
    
    /**
     * <p>Gives the shop inventory as a list of <code>Product</code> instances</p>
     * 
     * @return ArrayList - The list of <code>Product</code> instances
     */
    ArrayList<Product> getInventory()
    {
        return inventory;
    }
}