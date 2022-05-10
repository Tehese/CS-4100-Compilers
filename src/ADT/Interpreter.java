/*
Scott Canfield
CS 4100 Compilers
Homework #3 Interpreter
 */

package ADT;

import java.io.*;
import java.util.Scanner;

public class Interpreter {


    //Static Global Variables

    //Terminate
    static final int STOP = 0;

    //Math
    static final int DIV = 1;
    static final int MUL = 2;
    static final int SUB = 3;
    static final int ADD = 4;

    //Data Storage
    static final int MOV = 5;

    //Utility
    static final int PRINT = 6;
    static final int READ = 7;

    //Branch Instructions
    static final int JMP = 8;
    static final int JZ = 9;
    static final int JP = 10;
    static final int JN = 11;
    static final int JNZ = 12;
    static final int JNP = 13;
    static final int JNN = 14;
    static final int JINDR = 15;

    private final ReserveTable opTable;

    //Constructor that initialzes the Interpretor, and its ReserveTable/Opcodes, ext..
    public Interpreter() {
        opTable = new ReserveTable(17);
        initReserve(opTable);
    }

    //Accept a Constructed/Initialzed Symboltable and QuadTable and add the nexessary variables
    public boolean initializeFactorialTest(SymbolTable stable, QuadTable qtable){
        stable.SymbolTableFactorial(stable);
        qtable.QuadTableFactorial(qtable);
        return true;
    }

    //Accepts a Constructed/Initialized Symboltable and QuadTable and adds the necessary variables
    public boolean initializeSummationTest(SymbolTable stable, QuadTable qtable){
        stable.SymbolTableSummation(stable);
        qtable.QuadTableSummation(qtable);
        return true;
    }

    //Checks to see if the opCode is valid
    public boolean isValid(int opCode){
        return (opTable.LookupCode(opCode) != ""); //Returns False if Opcode in reserveTable is ""
    }

    //Goes through the table of Opcodes in order to determine which function to perform.
    public void InterpretQuads(QuadTable Q, SymbolTable S, boolean TraceOn, String filename){
        int PC = 0; //Program Counter
        int opcode, op1, op2, op3; //opcodes for quadTable Referenced as q.op


        //Checks to make sure there isn't a current file is open already, if not then opens a new file and writes the the file location
        PrintWriter outputList = null;
        try {
            outputList = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(PC <  Q.maxSize()){
            //Gets OpCodes from QuadTable
            opcode = Q.GetQuad(PC,0);
            op1 = Q.GetQuad(PC,1);
            op2 = Q.GetQuad(PC,2);
            op3 = Q.GetQuad(PC,3);

            if(TraceOn){
                System.out.println(makeTraceString(PC,opcode,op1,op2,op3));//Print to console
                outputList.println(makeTraceString(PC,opcode,op1,op2,op3));//Print to file
            } //End TraceOn

            if (isValid(opcode)) { //Checks to make sure the opcode is within existence

                switch(opcode){ //All of the possible functions in a switch

                    //Indirect Unconditional Branch
                    case JINDR:
                        PC = S.GetInteger(op3);
                        break;

                    //Division op1/op2
                    case DIV:
                        int result = S.GetInteger(op1) / S.GetInteger(op2);
                        S.UpdateSymbol(op3,'v',result); //Updates the result into the op3
                        PC++; //Increment
                        break;

                    //Multiplication op1*op2
                    case MUL:
                        result = S.GetInteger(op1) * S.GetInteger(op2);
                        S.UpdateSymbol(op3,'v',result); //Updates the result into op3
                        PC++; //Increments
                        break;

                    //Subtraction op1-op2
                    case SUB:
                        result = (S.GetInteger(op1)-S.GetInteger(op2));
                        S.UpdateSymbol(op3,'v',result); //Updates the result into op3
                        PC++; //Increments
                        break;

                    //Addition op1+op2
                    case ADD:
                        result = (S.GetInteger(op1)+S.GetInteger(op2));
                        S.UpdateSymbol(op3,'v',result); //Updates the result into op3
                        PC++; //Increments
                        break;

                    //Assigns op1 into op3
                    case MOV:
                        S.UpdateSymbol(op3,'v',S.GetInteger(op1)); //Updates op1 into op3
                        PC++; //Increments
                        break;

                    //Unconditional Redirect
                    case JMP:
                        PC=op3;
                        break;

                    //Branch Zero Op1 = 0
                    case JZ: if (S.GetInteger(op1) == 0) {
                            PC = op3;
                        }
                        else
                            PC++;
                        break;

                    //Branch is Op1 Positive
                    case JP: if (S.GetInteger(op1) > 0) {
                            PC = op3;
                        }
                        else
                            PC++;
                        break;

                    //Branch is Op1 is negative
                    case JN:
                        if (op1 < 0) {
                            PC = op3;
                        }
                        else
                            PC++;
                        break;

                    //Branch if not Zero
                    case JNZ:
                        if (S.GetInteger(op1) != 0) {
                            PC = op3;
                        }
                        else
                            PC++;
                        break;

                    //Branch if not Positive
                    case JNP:
                        if (S.GetInteger(op1) <= 0) {
                            PC = op3;
                        }
                        else
                            PC++;
                        break;

                    //Branch if not Negative
                    case JNN:
                        if (S.GetInteger(op1) >= 0) {
                            PC = op3;
                        }
                        else
                            PC++;
                        break;

                    //prints the Symbol Table name and Op3 to console
                    case PRINT:
                        if(S.GetDataType(op3) == 's'){ //Checks to see if it is a string
                            System.out.println(S.GetSymbol(op3));
                        }else //Else print the Integer
                            System.out.println(S.GetInteger(op3));
                        PC++;
                        break;

                    case STOP:
                        System.out.println("Execution terminated by program STOP.");
                        PC = Q.maxSize();
                        break;

                    case READ:
                        //Assuming operand must be an integer
                        Scanner sc = new Scanner(System.in);
                        System.out.print('>');
                        //Reading only 1 integer
                        int readval = sc.nextInt();
                        //place in op3
                        S.UpdateSymbol(op3, 'i', readval);
                        sc = null;
                        PC++;
                        break;

                }//End Switch
            } //End isValid
        }// End While

        outputList.close(); //Closes File
    } // End of Interpret Quads

    //Prints in the correct format
    private String makeTraceString(int pc, int opcode,int op1,int op2,int op3 ){
        String result = "";
        result = "PC = "+String.format("%04d", pc)+": "+(opTable.LookupCode(opcode)+"     ").substring(0,6)+String.format("%02d",op1)+
                ", "+String.format("%02d",op2)+", "+String.format("%02d",op3);
        return result;
    } // End of makeTraceString

    //Initialized/Populate the ReserveTable with Reserved words
    private void initReserve(ReserveTable optable){
        optable.Add("STOP", 0);
        optable.Add("DIV", 1);
        optable.Add("MUL", 2);
        optable.Add("SUB", 3);
        optable.Add("ADD", 4);
        optable.Add("MOV", 5);
        optable.Add("PRINT", 6);
        optable.Add("READ", 7);
        optable.Add("JMP", 8);
        optable.Add("JZ", 9);
        optable.Add("JP", 10);
        optable.Add("JN", 11);
        optable.Add("JNZ", 12);
        optable.Add("JNP", 13);
        optable.Add("JNN", 14);
        optable.Add("JINDR", 15);
    }

    public int opcodeFor(String print) {

        return opTable.LookupName(print);


    }
} // End of Interpreter