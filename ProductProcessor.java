import java.io.*;
import java.util.*;
import java.text.*;

/**
 * @author      Parth Mehta <pmeh3907@uni.sydney.edu.au>
 * @version     1.0
 * @since       2014-09-09
 */

public class ProductProcessor
{
    private static ArrayList<Product> discardedInventory =  new ArrayList<Product>();
    /** 
     * <p>
     * Adds a <code>Product</code> to the inventory.
     * </p>
     * 
     * @param productMap the mapping from description to value for a <code>Product</code> instance
     * @param inventoryList the current list of <code>Product</code>s in the store
     * 
     * @return <code>ArrayList</code>
     */
    public static ArrayList<Product> buy(Map<String,String> productMap, ArrayList<Product> inventoryList) throws Exception
    {
        
        if(productMap.containsKey("product") && productMap.containsKey("quantity"))
        {
            String productName = productMap.get("product");
            int quantity = 0;
            boolean parsable = true;
            
            try
            {
                quantity = Integer.parseInt(productMap.get("quantity"));
            }
            catch(Exception e)
            {
                return inventoryList;
            }
            
            quantity = Integer.parseInt(productMap.get("quantity")); 
            
            //System.out.println(quantity);
            Product product = new Product(); 
            product.setProductName(productName); // set name
            product.setQuantity(quantity);
            
            
            // checking for all key values - boughton, soldon, boughtat, soldat, useby
            if(productMap.containsKey("boughton"))
            {
                // converting string to date
                String string = productMap.get("boughton");
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    sdf.setLenient(false);
                    date = sdf.parse(string);
                }catch(Exception e) {
                    // date not in correct format
                    // => ignore this product
                }
                if(date != null)
                    product.setPurchaseDate(date);
            }
            
            if(productMap.containsKey("soldon"))
            {
                // converting string to date
                String string = productMap.get("soldon");
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    sdf.setLenient(false);
                    date = sdf.parse(string);
                }catch(Exception e) {
                    // date not in correct format
                    // => ignore this product
                }
                if(date != null)
                    product.addSaleDate(date);
            }
            if(productMap.containsKey("useby"))
            {
                // converting string to date
                String string = productMap.get("useby");
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    sdf.setLenient(false);
                    date = sdf.parse(string);
                }catch(Exception e) {
                    // date not in correct format
                    // ignore this field
                }
                if(date != null)
                    product.setExpiryDate(date);
            }
            if(productMap.containsKey("boughtat"))
            {
                // converting string to date
                String string = productMap.get("boughtat");
                double price = Double.parseDouble(string.substring(1,string.length()));
                if(price > 0)
                    product.setPurchasePrice(price);
            }
            if(productMap.containsKey("soldat"))
            {
                // converting string to date
                String string = productMap.get("soldat");
                double price = Double.parseDouble(string.substring(1,string.length()));
                if(price > 0)
                    product.setSellPrice(price);
            }
            
            // second add the product to the inventoryList list
            inventoryList.add(product);

            return inventoryList;
        }
        
        return inventoryList;   
    }
    
    /** 
     * <p>
     * Sells a specified quantity of a specified <code>Product</code> from the inventory in the shop
     * </p>
     * 
     * @param productMap The mapping from description to value for a <code>Product</code>
     * @param inventoryList The current list of <code>Product</code>s in the store
     * 
     * @return <code>ArrayList</code>
     */
    public static ArrayList<Product> sell(Map<String,String> productMap, ArrayList<Product> inventoryList) throws Exception
    {
        // product, soldon, soldat, quantity
        if(productMap.containsKey("product") && productMap.containsKey("quantity"))
        {
            String productName = productMap.get("product");
            int quantity = 0;
            try
            {
                quantity = Integer.parseInt(productMap.get("quantity"));
            }
            catch(Exception e)
            {
                return inventoryList;
            }
            
            Product product = new Product(); 
            product.setProductName(productName); // set name
            product.setQuantity(quantity);
            
            if(productMap.containsKey("soldon"))
            {
                // converting string to date
                String string = productMap.get("soldon");
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    sdf.setLenient(false);
                    date = sdf.parse(string);
                }catch(Exception e) {
                    // date not in correct format
                    // ignore this field
                }
                if(date != null)
                    product.addSaleDate(date);
            }
            
            
            if(productMap.containsKey("soldat"))
            {
                // converting string to date
                String string = productMap.get("soldat");
                double price = Double.parseDouble(string.substring(1,string.length()));
                product.setSellPrice(price);
            }
            
            
            // get inventoryList product names in arraylist
            ArrayList<String> products = new ArrayList<String>();
            for(Product p : inventoryList)
                products.add(p.getProductName());
            
            // sorting in ascending order of useby date for sales
            inventoryList = sort(inventoryList,"useby");
            
            // selling logic
            // for each sell instruction find product.                
            int stock,sellQuantity;
            double sellPrice;
            Date sellDate;
            sellQuantity = product.getQuantity();
            for(Product p : inventoryList)
            {
                if((p.getProductName()).equalsIgnoreCase(product.getProductName()))
                {
                    stock = p.getQuantity(); // currrent stock
                    sellDate = product.getSellDate().get(0); // sell date
                    sellPrice = product.getSellPrice().get(0); // sell price
                    
                    if(sellDate.compareTo(p.getPurchaseDate()) >= 0)
                    {
                        if(stock >= sellQuantity)
                        {
                            p.editProductQuantity(sellQuantity); // decrease the product quantity available
                            sellQuantity = 0;
                            p.setSellPrice(sellPrice); // set the selling price
                            p.addSaleDate(sellDate); // set the sale date
                            break;
                        }
                        else
                        {
                            p.editProductQuantity(stock); // decrease product quantity available
                            sellQuantity -= stock;
                            
                            p.setSellPrice(product.getSellPrice().get(0)); // set the selling price
                            p.addSaleDate(product.getSellDate().get(0)); // set the sale date
                        }
                    }    
                }
            }
        }

        return inventoryList;
    }
    
    /** 
     * <p>
     * Sorts the shop inventory according to the specified field
     * </p>
     * 
     * @param inventoryList The current list of <code>Product</code>s in the store
     * @param field The field according to which inventory is to be sorted e.g. useby, product, boughtat, etc.
     * 
     * @return <code>ArrayList</code>
     */
    public static ArrayList<Product> sort(ArrayList<Product> inventoryList, String field) throws Exception
    {
        // Bubble sort used for all types of sorting
        switch(field)
        {
            case "useby" : {
                                for(int i=inventoryList.size()-1; i>=0; i--)
                                {
                                    for(int j=0; j<i; j++)
                                    {
                                        Date d1 = inventoryList.get(j).getExpiryDate();
                                        Date d2 = inventoryList.get(j+1).getExpiryDate();
                                        
                                        if((d1 == null && d2 == null) || (d1 != null && d2 == null))
                                            continue;
                                        else if(d1 == null && d2 != null)
                                        {
                                            Product temp = inventoryList.get(j);
                                            inventoryList.set(j,inventoryList.get(j+1));
                                            inventoryList.set(j+1,temp);
                                        }
                                        else if(d1.compareTo(d2) > 0)
                                        {
                                            Product temp = inventoryList.get(j);
                                            inventoryList.set(j,inventoryList.get(j+1));
                                            inventoryList.set(j+1,temp);
                                        }
                                    }
                                }
                                break;
                           }
            case "product" : {
                                for(int i=inventoryList.size()-1; i>=0; i--)
                                {
                                    for(int j=0; j<i; j++)
                                    {
                                        String s1 = inventoryList.get(j).getProductName();
                                        String s2 = inventoryList.get(j+1).getProductName();
                                        
                                        if(s1.compareTo(s2) > 0)
                                        {
                                            Product temp = inventoryList.get(j);
                                            inventoryList.set(j,inventoryList.get(j+1));
                                            inventoryList.set(j+1,temp);
                                        }
                                    }
                                }
                                break;
                             }
            case "quantity" : {
                                    for(int i=inventoryList.size()-1; i>=0; i--)
                                    {
                                        for(int j=0; j<i; j++)
                                        {
                                            int q1 = inventoryList.get(j).getQuantity();
                                            int q2 = inventoryList.get(j+1).getQuantity();
                                            
                                            if(q1 > q2)
                                            {
                                                Product temp = inventoryList.get(j);
                                                inventoryList.set(j,inventoryList.get(j+1));
                                                inventoryList.set(j+1,temp);
                                            }
                                        }
                                    }
                                    break;
                              }
            default : break;                  
        }
        
        return inventoryList;    
    }
    
    /**
     * <p>
     * Discards the <code>Product</code>s which will expire by the given date.
     * </p>
     * 
     * @param inventoryList The current inventory from which expired <code>Product</code>s need to be removed
     * @param date The date against which the <code>Product</code>s need to be checked for expiration
     * 
     * @return ArrayList
     */
    public static ArrayList<Product> discard(ArrayList<Product> inventoryList, Date date)
    {
        for(Product p : inventoryList)
        {
            if(!p.isDiscarded())
                if(p.hasExpiryDate())
                    if((p.getExpiryDate()).compareTo(date) <= 0)
                    {
                        discardedInventory.add(p);
                        p.discarded();
                    }
        }
       
        return inventoryList;
    }
    
    /**
     * <p>
     * Prints the current shop inventory
     * </p>
     * 
     * @param inventoryList current shop inventory
     * @param date Date according which the inventory is to be checked and printed
     */
    public static ArrayList<Product> query(ArrayList<Product> inventoryList, Date date, String reportFile) throws Exception // query type 1
    {
        inventoryList = sort(inventoryList,"useby"); // sorting inventory
        inventoryList = discard(inventoryList,date); // discarding useless inventory
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        PrintWriter writer = new PrintWriter(new FileWriter(reportFile,true));
        
        writer.println("--------   query " + sdf.format(date) + " --------");
        writer.println("\n");
        
        Set<String> productsNeeded = new HashSet<String>();
        /*for(Product p : inventoryList)
        {
            int q = 0;
            for(Product subp : inventoryList)
            {
                if((subp.getProductName()).equalsIgnoreCase(p.getProductName()) && !p.isDiscarded())
                    q += subp.getQuantity();
            }
            
            System.out.println(p.getProductName() + " : " + q);
            if(q <= 10)
                productsNeeded.add(p.getProductName());
        }*/
        for(Product p : inventoryList)
        {
            if(p.isDiscarded())
                productsNeeded.add(p.getProductName());
            else
            {
                if(p.hasExpiryDate())
                {
                    if(p.getQuantity() <= 10)
                        productsNeeded.add(p.getProductName());
                }
                else
                {
                    int q = 0;
                    for(Product subp : inventoryList)
                        q += subp.getQuantity();
                    
                    if(q <= 10)
                        productsNeeded.add(p.getProductName());
                }
            }    
        }
        
        for(Product p : inventoryList)
        {
            if(p.getExpiryDate() == null && (p.getPurchaseDate()).compareTo(date) <= 0 && p.getQuantity() > 0)
            {
                writer.println("product    :  " + p.getProductName());
                writer.println("quantity   :  " + p.getQuantity());
                writer.println();
            }
            else if((p.getExpiryDate()).compareTo(date) >= 0 && (p.getPurchaseDate()).compareTo(date) <= 0 && p.getQuantity() > 0)
            {
                writer.println("product    :  " + p.getProductName());
                writer.println("quantity   :  " + p.getQuantity());
                writer.println("useby      :  " + sdf.format(p.getExpiryDate()));
                writer.println();
            }
        }
        
        writer.println("\nSuggestion : \n");
        for(String product : productsNeeded)
            writer.println("product       " + product);
            
        writer.println("\n--------------   End  --------------\n\n");
        writer.close();
        return inventoryList;
    }
    
    /**
     * <p>
     * Calculates the best and worst ROA for a particular period of sales
     * </p>
     * 
     * @param inventoryList current shop inventory
     * @param type "bestsales" or "worstsales" are the acceptable options
     * @param start Start date of the sale period
     * @param end End date of the sale period
     */
    public static void query(ArrayList<Product> inventoryList, String type, Date start, Date end, String reportFile) throws Exception // query type 2
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        PrintWriter writer = new PrintWriter(new FileWriter(reportFile,true));
        writer.println("------- query " + type + " " + sdf.format(start) + " " + sdf.format(end) + " -------\n");
        double roa = roaLimit(inventoryList,type,start,end);
        
        Set<String> productList = new HashSet<String>();
        for(Product p : inventoryList)
            productList.add(p.getProductName()); // a list of all products in the shop
        
        for(String product : productList)
        {
            //double temproa = productROA(inventoryList,product,start,end);
            double temproa = productROA(inventoryList,product,start,end);
            //System.out.println(product + " : " + temproa);
            if(temproa == roa)
            {
                writer.println("product : " + product);
                writer.println("ROA     : $" + roa);
            }
            writer.println();
        }
        
        writer.println("----------------------- End --------------------------\n\n");
        writer.close();
    }
    
    /**
     * <p>
     * Prints the current shop inventory on the date as specified
     * </p>
     * 
     * @param inventoryList current shop inventory
     * @param date <code>Date</code> on which the inventory is to be checked and printed
     */
    public static ArrayList<Product> query(ArrayList<Product> inventoryList, Date start, Date end, String reportFile) throws Exception // query type 3
    {
        inventoryList = discard(inventoryList,end);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        PrintWriter writer = new PrintWriter(new FileWriter(reportFile,true));
        writer.println("------- query profit " + sdf.format(start) + " " + sdf.format(end) + " -------");
        writer.println();
        double netsales = 0.0, purchasecost = 0.0;
        for(Product p : inventoryList)
        {
            if(p.hasSellDate())
            {
                ArrayList<Date> sellDates = p.getSellDate();
                ArrayList<Double> sellPrices = p.getSellPrice();
                ArrayList<Integer> sellQuantity = p.getQuantitySold();
                
                for(int i=0; i<sellDates.size(); i++)
                {
                    if((sellDates.get(i)).compareTo(start) >= 0 && (sellDates.get(i)).compareTo(end) <= 0)
                    {
                        netsales += (sellPrices.get(i) * sellQuantity.get(i));
                        purchasecost += (p.getPurchasePrice() * sellQuantity.get(i));
                    }
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#.00");
        writer.println("Net Income  : $" + df.format(netsales-purchasecost));
        writer.println();
        
        // printing the loss data
        double loss = 0.0;
        int size = discardedInventory.size(), i=0;
        double costs[] = new double[size];
        int quantity[] = new int[size];
        for(Product p : discardedInventory)
        {
            if(p.hasExpiryDate())
            {
                if((p.getExpiryDate()).compareTo(end) <= 0)
                {
                    quantity[i] = p.getQuantity();
                    for(int j=0; j<p.getSellDate().size(); j++)
                        if((p.getSellDate().get(j)).compareTo(end) > 0)
                            quantity[i] += p.getQuantitySold().get(j);
                            
                    costs[i] = quantity[i] * p.getPurchasePrice();
                    //loss += (p.getQuantity() * p.getPurchasePrice());
                    loss += costs[i];
                    i++;
                }   
            }
        }
        Arrays.sort(costs);
        
        writer.println("Loss        : $" + df.format(loss) + "\n");
        
        i=size-1;
        for(;i>=0;i--)
        {
            for(Product p : discardedInventory)
            {
                double cost = (p.getQuantity() * p.getPurchasePrice());
                if(cost == costs[i])
                {
                    if(p.getQuantity() > 0)
                    {
                       writer.println("product     : " + p.getProductName());
                       writer.println("quantity    : " + quantity[i]);
                        
                       // for the rest of the items - check and print
                       if(p.hasPurchaseDate())
                           writer.println("boughton    : " + sdf.format(p.getPurchaseDate()));
                            
                       if(p.hasPurchasePrice())
                           writer.println("boughtat    : $" + p.getPurchasePrice());
                            
                       if(p.hasExpiryDate())
                           writer.println("useby       : " + sdf.format(p.getExpiryDate()));
                        
                       writer.println();
                    }
                }   
            }
        }
        writer.println("--------------------  End   ----------------------\n");
        writer.close();
        return inventoryList;
    }
    
    /*
     * Calculates the ROA based on the formula and values supplied
     */
    private static double getROA(int totalQuantity, double netSales, double purchaseCost)
    {
        return 2 * (netSales - purchaseCost) / totalQuantity;
    }
    
    /*
     * Calculates the best or worst ROA possible according to the values supplied
     */
    private static double roaLimit(ArrayList<Product> inventoryList, String type, Date start, Date end) throws Exception
    {
        double roa = 0.0;
        String productName = "";
        Set<String> productList = new HashSet<String>();
        for(Product p : inventoryList)
            productList.add(p.getProductName()); // a list of all products in the shop
        
        DecimalFormat df = new DecimalFormat("#.00");    
        if(type.equals("bestsales"))
        {
            for(String s : productList)
            {
                double temproa = productROA(inventoryList,s,start,end);
                
                //temproa = Double.parseDouble(df.format(temproa));
                if(temproa > roa)
                {
                    roa = temproa;
                    productName = s;
                }
            }
        }
        else if(type.equals("worstsales"))
        {
            for(String s : productList)
            {
                double temproa = productROA(inventoryList,s,start,end);
                if(temproa < roa)
                {
                    roa = temproa;
                    productName = s;
                }
            }
        }
            
        return roa;
    }
    
    /*
     * Calculates the ROA of the specified product during the sale period supplied
     */
    private static double productROA(ArrayList<Product> inventoryList, String product, Date start, Date end)
    {
        double roa = 0.0;
        double netsales = 0.0;
        double purchasecost = 0.0;
        int qBegin = 0, qEnd = 0;
        String productName = product;
        
        for(Product p : inventoryList)
        {
            if(p.hasSellDate())
            {
                if(productName.equals(p.getProductName()))
                {
                    // getting the initial quantity
                    if((p.getPurchaseDate()).compareTo(start) <= 0)
                        qBegin += p.getOriginalQuantity();
                    
                    double purchasePrice = p.getPurchasePrice();
                    
                    ArrayList<Date> sellDates = p.getSellDate();
                    ArrayList<Double> sellPrices = p.getSellPrice();
                    ArrayList<Integer> sellQuantity = p.getQuantitySold();
                    
                    for(int i=0; i<sellDates.size(); i++)
                    {
                        if((sellDates.get(i)).compareTo(start) >= 0 && (sellDates.get(i)).compareTo(end) <= 0)
                        {
                            netsales += (sellPrices.get(i) * sellQuantity.get(i)); // calculating net sales
                            purchasecost += (purchasePrice * sellQuantity.get(i)); // calculating purchase cost
                        }
                    }
                }
            }
            else if(p.hasExpiryDate())
            {
                if(productName.equals(p.getProductName()))
                {
                    return 0.0;
                }
            }    
        }
        
        for(Product p : inventoryList)
        {
            if((p.getProductName()).equals(productName))
            {
                if(p.getExpiryDate() == null)
                    qEnd += p.getQuantity();
                else if((p.getExpiryDate()).compareTo(end) > 0)
                    qEnd += p.getQuantity();    
            }
        }
        
        if(netsales <= purchasecost)
            roa = getROA(qBegin+qEnd,0,0);
        else    
            roa = getROA(qBegin+qEnd,netsales,purchasecost);
        return roa;
    }
}