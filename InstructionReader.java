import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
class InstructionReader
{
    private ArrayList<Product> inventory = new ArrayList<Product>();
    
    /**
     * Constructor
     * 
     * <p>
     * Sends the instructions to their respective handlers to be executed
     * </p>
     * 
     * @param instructionFile The text file which contains the executable instructions
     * @param reportFile The text file in which the reports are to be displayed
     * @param inventoryList The shop inventory on which the instructions are to be performed
     * 
     */
    public InstructionReader(String instructionFile, String reportFile, ArrayList<Product> inventoryList) throws Exception
    {
        inventory = inventoryList;
        Scanner fileScan = new Scanner(new File(instructionFile));
        
        while(fileScan.hasNextLine())
        {
            String instruction = fileScan.nextLine();
            Scanner inputScan = new Scanner(instruction);
            //System.out.println(instruction);
            if(!instruction.isEmpty())
            {
                String s = inputScan.next(); // getting the first word
                s = s.toLowerCase();

                //if the first word is not an instruction, then move to next instruction
                switch(s)
                {
                    case "buy" : {
                                    Map<String,String> productMap = new HashMap<String,String>();
                                    String line = instruction.substring(s.length()); // getting the buy specifications
                                    
                                    while(!line.isEmpty())
                                    {
                                        int index = line.indexOf(";");
                                        
                                        if(index < 0)
                                            index = line.length();
                                            
                                        Scanner specificationScan = new Scanner(line);
                                        
                                        String property = specificationScan.next();
                                        property = property.trim().toLowerCase();
                                        //System.out.print(property + " : ");
                                        String value = line.substring(property.length()+1,index);
                                        value = value.trim().toLowerCase();
                                        //System.out.println(value);
                                        productMap.put(property,value);
                                        
                                        if(index == line.length())
                                            line = "";
                                        else    
                                            line = line.substring(index + 1);
                                    }
                                    
                                    inventory = ProductProcessor.buy(productMap,inventory);
                                    
                                    //System.out.println();
                                    break;
                                 }
                    
                    case "sell" :{
                                    Map<String,String> productMap = new HashMap<String,String>();
                                    String line = instruction.substring(s.length()); // getting the sale specifications
                                    
                                    while(!line.isEmpty())
                                    {
                                        int index = line.indexOf(";");
                                        
                                        if(index < 0)
                                            index = line.length();
                                            
                                        Scanner specificationScan = new Scanner(line);
                                        
                                        String property = specificationScan.next();
                                        property = property.trim().toLowerCase();
                                        //System.out.print(property + " : ");
                                        String value = line.substring(property.length()+1,index);
                                        value = value.trim().toLowerCase();
                                        //System.out.println(value);
                                        productMap.put(property,value);
                                        
                                        if(index == line.length())
                                            line = "";
                                        else    
                                            line = line.substring(index + 1);
                                    }   
                                    
                                    inventory = ProductProcessor.sell(productMap,inventory);
                                    
                                    break;
                                 }
                                 
                    case "discard" : {
                                        String dateString = inputScan.next(); // date in string
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        Date date = null;
                                        try {
                                            sdf.setLenient(false);
                                            date = sdf.parse(s);
                                        }catch(Exception e) {
                                            // => not correct date format
                                            // => nothing to do, just ignore
                                        }
                                        if(date != null)
                                        {
                                            inventory = ProductProcessor.discard(inventory,date);
                                        }
                                        
                                        break;
                                     }
                                     
                    case "sort" : {
                                     // implemented for product name, quantity and useby date
                                     String string = inputScan.next();
                                     string = string.trim().toLowerCase(); // removing whitespaces
                                     
                                     inventory = ProductProcessor.sort(inventory,string);
                                     break;
                                  }
                    
                    case "query" : {
                                        s = inputScan.next().toLowerCase();
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        if(s.equals("worstsales") || s.equals("bestsales"))
                                        {
                                            //Date start = (new FormatDate()).formatDate(inputScan.next());
                                            Date start = null;
                                            try {
                                                sdf.setLenient(false);
                                                start = sdf.parse(inputScan.next());
                                            }catch(Exception e) {
                                                // => not correct date format
                                                // => nothing to do, just ignore
                                            }
                                            
                                            //Date end = (new FormatDate()).formatDate(inputScan.next());
                                            Date end = null;
                                            try {
                                                sdf.setLenient(false);
                                                end = sdf.parse(inputScan.next());
                                            }catch(Exception e) {
                                                // => not correct date format
                                                // => nothing to do, just ignore
                                            }
                                            
                                            if(start == null || end == null)
                                                break;
                                            ProductProcessor.query(inventory,s,start,end,reportFile);
                                        }
                                        else if(s.equals("profit"))
                                        {
                                            //Date start = (new FormatDate()).formatDate(inputScan.next());
                                            Date start = null;
                                            try {
                                                sdf.setLenient(false);
                                                start = sdf.parse(inputScan.next());
                                            }catch(Exception e) {
                                                // => not correct date format
                                                // => nothing to do, just ignore
                                            }
                                            
                                            //Date end = (new FormatDate()).formatDate(inputScan.next());
                                            Date end = null;
                                            try {
                                                sdf.setLenient(false);
                                                end = sdf.parse(inputScan.next());
                                            }catch(Exception e) {
                                                // => not correct date format
                                                // => nothing to do, just ignore
                                            }
                                            
                                            if(start == null || end == null)
                                                break;
                                            inventory = ProductProcessor.query(inventory,start,end,reportFile);
                                        }
                                        else
                                        {
                                            Date date = null;
                                            try {
                                                sdf.setLenient(false);
                                                date = sdf.parse(s);
                                            }catch(Exception e) {
                                                // => not correct date format
                                                // => nothing to do, just ignore
                                            }
                                            if(date != null)
                                            {
                                                inventory = ProductProcessor.query(inventory,date,reportFile);
                                            }
                                            
                                        }
                                        break;
                                   }
                    default : break; // invalid instruction so ignore and move on
                }
            }
            else
                continue;
        }
    }
    
    /**
     * <p>
     * Returns the inventory after all the specified instructions have been carried out
     * </p>
     * 
     * @return ArrayList - The inventory after all the instructions have been performed
     */
    public ArrayList<Product> getInventory()
    {
        return inventory;
    }
}