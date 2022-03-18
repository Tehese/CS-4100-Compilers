package ADT;

import java.util.*;
import java.io.*;


/*
Author: Scott Canfield
Assignment: Create a Reserve Table ADT
 */

class ReserveTable {

    public ArrayList<String> name; //array list for the names
    public ArrayList<Integer> code; //arraylist for the codes
    int total, maxSize;

    //Constructor for the arraylists of names and codes
    public ReserveTable(int maxSize){
        name = new ArrayList<String>(maxSize); //Creates the list "listName" with size maxSize
        code = new ArrayList<Integer>(maxSize); //Creates the list "listCode" with size maxSize
        total = 0; //To keep track of how many items are in the list, might delete later
        this.maxSize = maxSize;
    }


    //Adds a new row to the Table with the variables as the contents.
    //Returns the index of where the data was placed
    //Only adds to the end of the list nothing else.
    public int Add(String name, int code){
        if (total < maxSize) {
            this.name.add(name); //Adds name to the end of the listCode
            this.code.add(code); //Adds the code to the end of the listCode
            total++;
            return name.indexOf(name);
        } else {
            return -1; //Returns -1 if the list is full
        }
    }

    //Searches through the Table case to compare the names case insensitive
    //and returns the name if there is one, else a -1
    public int LookupName(String name){

        for (int i = 0; i < this.name.size(); i ++){ //For loop going through all the members in listName
            if(name.compareToIgnoreCase(this.name.get(i)) == 0){ //Comparing the string name to the value in the list ignoring cases
                return code.get(i); //If the IF statement was successful return the code for that spot
            }
        }
        return -1; //If the name is not found return -1
    }

    //Lookups through the list for an associated code and returns the name with it
    //If there are no listed codes return an empty string
    public String LookupCode(int code){

        for(int i = 0; i <this.code.size(); i++){ //For loop for the size of listCode
            if(code == this.code.get(i)){ //Compares the int code vs the value in the list
                return name.get(i); //If successful return the name at position I
            }
        }
        return ""; //Else return an empty string
    }

    //Uses a for loop or a while loop
    // Prints the ASCII text file with the current contents of the Reserve table
    public void PrintReserveTable(String filename) {

        PrintWriter outputList = null;
        try {
            outputList = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        outputList.printf("Author: Scott Canfield\n\n");
        for (int i = 0; i <name.size(); i ++){
            outputList.printf("Index: %d\t  Name: %s\t Code:%d\n", i, name.get(i), code.get(i));
        }

        outputList.close();
    }
}
