import ADT.*;

public class main {

    public static void main(String[] args) {


            System.out.println("Scott Canfield\n");
            String fileAndPath = "/Users/tehese/Desktop/School/Spring 2022/CS 4100 Compiler/Programming/Parts2_4 Lexical/LexicalTestSP22.txt";
            System.out.println("Lexical for " + fileAndPath);
            boolean traceOn = true;
            // Create a symbol table to store appropriate3 symbols found
            SymbolTable symbolList;
            symbolList = new SymbolTable(150);
            Lexical myLexer = new Lexical(fileAndPath, symbolList, traceOn);
            Lexical.token currToken;
            currToken = myLexer.GetNextToken();
            //Mnemonic | Code | Lexeme
            while (currToken != null) {
                    System.out.println("\t" + currToken.mnemonic + " | \t" +
                            String.format("%04d", currToken.code)
                            + " | \t" + currToken.lexeme);
                    currToken = myLexer.GetNextToken();
            }
            symbolList.PrintSymbolTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/symboltable2.txt");
            System.out.println("Done.");
            /*
            Interpreter interp = new Interpreter();
            SymbolTable st;
            QuadTable qt;

            System.out.println("Scott Canfield");

            // interpretation FACTORIAL
            st = new SymbolTable(20);     //Create an empty SymbolTable
            qt = new QuadTable(20);       //Create an empty QuadTable
            interp.initializeFactorialTest(st,qt);  //Set up for FACTORIAL
            interp.InterpretQuads(qt, st, true, "/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/traceFact.txt");
            st.PrintSymbolTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/symbolTableFact.txt");
            qt.PrintQuadTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/quadTableFact.txt");

            // interpretation SUMMATION
            st = new SymbolTable(20);     //Create an empty SymbolTable
            qt = new QuadTable(20);       //Create an empty QuadTable
            interp.initializeSummationTest(st,qt);  //Set up for SUMMATION
            interp.InterpretQuads(qt, st, true, "/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/traceSum.txt");
            st.PrintSymbolTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/symbolTableSum.txt");
            qt.PrintQuadTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/quadTableSum.txt");


        System.out.print("Author: Scott Canfield    SP 2022\n\n");

        //Create the tables
        SymbolTable symbols = new SymbolTable(25);
        QuadTable quads = new QuadTable(50);
        int index, qvalue;
        System.out.println("Testing the Symbol Table\n");


        symbols.AddSymbol("TestInt", 'V', 27);
        symbols.AddSymbol("TestDouble", 'V', 42.25);
        symbols.AddSymbol("TestString", 'V', "These are the times...");
        symbols.AddSymbol("135",'C', 135);
        symbols.AddSymbol("3.1415",'C', 3.1415);
        symbols.AddSymbol("Please Enter A Value",'C', "Please Enter A Value");
        index = symbols.LookupSymbol("testint");
        System.out.println("testint is located at "+index);
        index = symbols.LookupSymbol("3.1415");
        System.out.println("PI is located at "+index);
        System.out.println("  the KIND for PI is "+symbols.GetKind(index));
        System.out.println("The KIND for slot 5 is "+symbols.GetKind(5)+", data type is "+
                symbols.GetDataType(5)+" and the value: "+symbols.GetString(5));
        index = symbols.LookupSymbol("BadVal");
        System.out.println("BadVal search returned "+index+'\n');
        symbols.PrintSymbolTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/Symbol.txt");
        System.out.println("Testing the Quad Table\n");


        System.out.println("At the start, NextQuad is: "+ quads.NextQuad());
        quads.AddQuad(4,3,2,1);
        System.out.println("After one add, NextQuad is: "+ quads.NextQuad());
        quads.AddQuad(1,2,3,4);
        quads.AddQuad(2,2,2,2);
        quads.AddQuad(0,0,0,0);
        quads.AddQuad(1,3,5,9);

        qvalue = quads.GetQuad(3, 2);
        System.out.println("Quad value at (4,3) is: "+ quads.GetQuad(4,3));

        quads.UpdateQuad(4,11,13,15,17);
        System.out.println("Quad value at (4,3) is: "+ quads.GetQuad(4,3));

        quads.PrintQuadTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/Quad.txt ");
        System.out.println("Finally NextQuad is: "+ quads.NextQuad());


        // Create the table
        ReserveTable reserve = new ReserveTable(25);

        // Add to the table
        reserve.Add("cat", 15);
        reserve.Add("APPLE", 11);
        reserve.Add("Dog", 5);
        reserve.Add("DOnE", 21);
        reserve.Add("Over", 8);

        // Search the table
        System.out.println("The Code for 'over' is " + reserve.LookupName("over"));
        System.out.println("The Code for 'DOG' is " + reserve.LookupName("DOG"));
        System.out.println("The Code for 'Cat' is " + reserve.LookupName("Cat"));
        System.out.println("The Code for 'gone' is " + reserve.LookupName("gone"));
        System.out.println();
        System.out.println("The Name for 11 is " + reserve.LookupCode(11));
        System.out.println("The Name for 5 is " + reserve.LookupCode(5));
        System.out.println("The Name for 8 is " + reserve.LookupCode(8));
        System.out.println("The Name for 28 is " + reserve.LookupCode(28));

        // Print table to file
        System.out.println("Saving Printed Table to /Users/tehese/IdeaProjects/ReserveTable1/src/ADT/Reserve.txt");

        reserve.PrintReserveTable("/Users/tehese/IdeaProjects/ReserveTable1/src/ADT/Reserve.txt");
        */
    }
}
