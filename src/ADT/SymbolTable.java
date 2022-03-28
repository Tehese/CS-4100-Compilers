/*
Scott Canfield
CS 4100 Compilers
Part 2 SymbolTable/Quadtable
 */

package ADT;

import java.io.*;


public class SymbolTable {


    public char[] kinds;
    public char[] data_type;
    public String[] names;
    public Object[] values;
    public char[] types;
    int total, maxSize;


    public SymbolTable(int maxSize) {

        kinds = new char[maxSize]; //Array to keep track of the Kinds
        names = new String[maxSize]; //Array to keep track of the Symbol Names
        values = new Object[maxSize]; //Object Array to keep track of the values of types (int,float,String)
        data_type = new char[maxSize]; //Array to keep track of what type is in the object array
        this.maxSize = maxSize;
        total = 0; //To keep track of how many items are in the list, might delete late
    }

    public int AddSymbol(String symbol, char kind, int value) {

        if (total < maxSize) { // Making sure the List is not full, and if so return -1

            //Checks the entire array for a symbol with the same by with a non-case sensitive search
            for (int i = 0; i < total; i++) {
                if (symbol.equalsIgnoreCase(names[i]))
                    return i;
            }
            //If the symbol did not exist, create it, and add it to the list.
            values[total] = Integer.valueOf(value);
            names[total] = symbol;
            kinds[total] = kind;
            data_type[total] = 'i';

            total++;
            return (total - 1); //Returns the index of the added row
        } else {
            return -1; //Returns -1 if the List is already full
        }
    }

    public int AddSymbol(String symbol, char kind, double value) {

        if (total < maxSize) { // Making sure the List is not full, and if so return -1

            //Checks the entire array for a symbol with the same by with a non-case sensitive search
            for (int i = 0; i < total; i++) {
                if (symbol.equalsIgnoreCase(names[i]))
                    return i;
            }
            //If the symbol did not exist, create it, and add it to the list.
            values[total] = Double.valueOf(value);
            names[total] = symbol;
            kinds[total] = kind;
            data_type[total] = 'f';

            total++;
            return (total - 1); //Returns the index of the added row
        } else {
            return -1; //Returns -1 if the List is already full
        }
    }

    public int AddSymbol(String symbol, char kind, String value) {

        if (total < maxSize) { // Making sure the List is not full, and if so return -1

            //Checks the entire array for a symbol with the same by with a non-case sensitive search
            for (int i = 0; i < total; i++) {
                if (symbol.equalsIgnoreCase(names[i]))
                    return i;
            }
            //If the symbol did not exist, create it, and add it to the list.
            values[total] = new String(value);
            names[total] = symbol;
            kinds[total] = kind;
            data_type[total] = 's';

            total++;
            return (total - 1); //Returns the index of the added row
        } else {
            return -1; //Returns -1 if the List is already full
        }
    }

    //Checks to see if the Symbol matches the input case-insensitive and returns the position, if not return -1
    public int LookupSymbol(String symbol) {

        for (int i = 0; i < total; i++) {
            if (symbol.equalsIgnoreCase((names[i]))) {
                return i;
            }
        }
        return -1; //If it can't find a symbol return -1
    }

    // Returns the Symbol name at the index
    public String GetSymbol(int index) {
        return names[index];
    }

    //Returns the Kind Char
    public char GetKind(int index) {
        return kinds[index];
    }

    //Returns the data type value
    public char GetDataType(int index) {
        return data_type[index];
    }

    //Returns the string of the Value
    public String GetString(int index) {
        return values[index].toString();
    }

    //Returns the Integer of the value
    public int GetInteger(int index) {
        return (int) values[index];
    }

    //Returns the float
    public double GetFloat(int index) {return (float) values[index];}

    // Updates the Table at the correct index
    public void UpdateSymbol(int index, char kind, int value) {
        //Updates the Table at the correct Index
        values[index] = Integer.valueOf(value);
        kinds[index] = kind;
    }

    // Updates the Table at the correct index
    public void UpdateSymbol(int index, char kind, float value) {
        values[index] = Float.valueOf(value);
        kinds[index] = kind;
    }

    // Updates the Table at the correct index
    public void UpdateSymbol(int index, char kind, String value) {
        values[index] = new String(value);
        kinds[index] = kind;
    }

    //Symbol Table Factorial
    public void SymbolTableFactorial(SymbolTable stable) {
        stable.AddSymbol("n", 'v', 10);
        stable.AddSymbol("i", 'v', 0);
        stable.AddSymbol("product", 'v', 0);
        stable.AddSymbol("1", 'c', 1);
        stable.AddSymbol("$temp", 'v', 0);

    }

    //Symbol Table summation which is almost identical to Factorial, but added an extra line to make total array values 0-5
    public void SymbolTableSummation(SymbolTable stable) {
        stable.AddSymbol("n", 'v', 10);
        stable.AddSymbol("i", 'v', 0);
        stable.AddSymbol("Sum", 'v', 0);
        stable.AddSymbol("1", 'c', 1);
        stable.AddSymbol("$temp", 'v', 0);
        stable.AddSymbol("c", 'v', 0);
    }

    //Prints the list in a neat manner
    public void PrintSymbolTable(String filename) {

        //Checks to make sure there isn't a current file is open already, if not then opens a new file and writes the file location
        PrintWriter outputList = null;
        try {
            outputList = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Prints only the filled in spaces, and also prints based on what the type on Value is
        for (int i = 0; i < total; i++) {

            if (values[i] instanceof String) { //Checking to see if the Object is of type Integer
                outputList.printf("%02d" + "| " + "%-15s | %c | %c | %s\n",i, names[i], kinds[i], data_type[i], values[i].toString());
            } else if (values[i] instanceof Double) { //Checking to see if Object is of type Float
                outputList.printf("%02d" + "| " + "%-15s | %c | %c | %g\n",i, names[i], kinds[i], data_type[i], values[i]);
            } else if (values[i] instanceof Integer) { //Checking to see if the Object is of type String
                outputList.printf("%02d" + "| " + "%-15s | %c | %c | %d\n",i, names[i], kinds[i], data_type[i], values[i]);
            }
        }
            outputList.close(); //Closes the file so writing is complete
        }

    }









