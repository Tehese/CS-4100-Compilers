/*
Scott Canfield
CS 4100 Compilers
Part 2 SymbolTable/Quadtable
 */


package ADT;

import java.io.*;

public class QuadTable {

    public int[][] quadTable; //2D array to keep track of values
    int nextAvailable;  //Keeps track of how many items are in the list
    int maxSize;

    public QuadTable(int maxSize){
        quadTable = new int[maxSize][4];
        nextAvailable = 0; //To keep track of how many items are in the list
    }

    public int NextQuad(){
        return nextAvailable; //Returns the next available index which is kept track of by the Variable nextAvailable;
    }

    // Assigns the opcodes to the next correct position in the array, and then increments to the next row.
    public void AddQuad(int opcode, int op1, int op2, int op3){
        this.quadTable[nextAvailable][0] = opcode;
        this.quadTable[nextAvailable][1] = op1;
        this.quadTable[nextAvailable][2] = op2;
        this.quadTable[nextAvailable][3] = op3;
        nextAvailable++;

    }

    //Returns the int at position [index][column]
    public int GetQuad(int index, int column){return quadTable[index][column];
    }

    public void UpdateQuad(int index, int opcode, int op1, int op2, int op3){
        //Assigns the opcodes to the correct index
        this.quadTable[index][0] = opcode;
        this.quadTable[index][1] = op1;
        this.quadTable[index][2] = op2;
        this.quadTable[index][3] = op3;
    }

    //Factorial Function in Assembly
    public void QuadTableFactorial(QuadTable qtable){
        qtable.AddQuad(5, 3,0,2);
        qtable.AddQuad(5, 3,0,1);
        qtable.AddQuad(3, 1,0,4);
        qtable.AddQuad(10,4,0,7);
        qtable.AddQuad(2, 2,1,2);
        qtable.AddQuad(4, 1,3,1);
        qtable.AddQuad(8, 0,0,2);
        qtable.AddQuad(6, 2,0,0);
        qtable.AddQuad(0,0,0,0); //Stop

    }

    //Summation, modified the first line to start at 0, and then changed it to addition from OpCode 5 to 4
    public void QuadTableSummation(QuadTable qtable){
        qtable.AddQuad(5, 5,0,2);
        qtable.AddQuad(5, 3,0,1);
        qtable.AddQuad(3, 1,0,4);
        qtable.AddQuad(10,4,0,7);
        qtable.AddQuad(4, 2,1,2); //Changed to Addition OpCode
        qtable.AddQuad(4, 1,3,1);
        qtable.AddQuad(8, 0,0,2);
        qtable.AddQuad(6, 2,0,0);
        qtable.AddQuad(0, 0,0,0); // Stop
    }

    //Returns the total amount of objects in the array.
    public int maxSize() {
        return nextAvailable ;
    }

    //Neatly Prints the table into a file
    public void PrintQuadTable(String filename){

        //Checks to make sure there isn't a current file is open already, if not then opens a new file and writes the the file location
        PrintWriter outputList = null;
        try {
            outputList = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i <nextAvailable; i++){
            outputList.printf("Column: %d\t  Opcode:%d\t Op1:%d\t Op2:%d\t Op3:%d\n", i, quadTable[i][0], quadTable[i][1], quadTable[i][2], quadTable[i][3] );
        }
        outputList.close(); //Closes the file so writing is complete
    }
}





