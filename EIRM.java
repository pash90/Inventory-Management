import java.io.*;
import java.util.*;
import java.text.*;
class EIRM
{
    public static void main(String[] args) throws Exception
    {
        String inventoryFile   = args[0];
        String instructionFile = args[1];
        String outputFile      = args[2];
        String reportFile      = args[3];
        
        InventoryReader inventoryReader = new InventoryReader(inventoryFile);
        ArrayList<Product> inventory = inventoryReader.getInventory();
        //inventory = inventoryReader.getInventory(); // current inventory
        InstructionReader instructionReader = new InstructionReader(instructionFile,reportFile,inventory);
        inventory = instructionReader.getInventory();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        // printing output to file
        PrintWriter writer = new PrintWriter(outputFile,"UTF-8");
        for(Product p : inventory)
        {
           if(p.getQuantity() > 0 && !p.isDiscarded())
           {
               writer.println("product  : " + p.getProductName());
               writer.println("quantity : " + p.getQuantity());
                
               // for the rest of the items - check and print
               if(p.hasPurchaseDate())
                   writer.println("boughton : " + sdf.format(p.getPurchaseDate()));
                    
               if(p.hasPurchasePrice())
                   writer.println("boughtat : $" + p.getPurchasePrice());
                    
               if(p.hasExpiryDate())
                   writer.println("useby    : " + sdf.format(p.getExpiryDate()));
                
               writer.println();
            }    
        }
        writer.close();
    }  
}