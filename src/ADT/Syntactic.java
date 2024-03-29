/*
Scott Canfield
CS4100 Compilers Spring 2022
Last 4 Student ID: 2339
UCCS

See TEMPLATE at end of this file for the framework to be used for
ALL non-terminal methods created.

CFG below, the following conventions are used:

1) Anything prefaced by a $ is a terminal token (symbol or reserved word); anything
inside of <> pointy brackets is a non-terminal
2) An item enclosed in ‘[‘,’]’ square braces is optional
3) An item enclosed in ‘{‘,’}’ curly braces is repeatable; ‘*’ is ‘0 or more times’, while ‘+’ is
‘1 or more times’
4) Vertical bars, ‘|’, are OR connectors; any one of the items they separate may be
selected
5) Note that all named elements of the form $SOMETHING are token codes for terminals
which are defined for this language and returned by the lexical analyzer.


Overall Incomplete
However, finished enough to pass the class and accomplish basic Compiler Functions
 */

package ADT;

import javax.swing.plaf.nimbus.State;

public class Syntactic {

    private String filein;              //The full file path to input file
    private SymbolTable symbolList;     //Symbol table storing ident/const
    private Lexical lex;//Lexical analyzer
    private QuadTable quads;
    private Interpreter interp;
    private Lexical.token token;        //Next Token retrieved
    private boolean traceon;            //Controls tracing mode
    private int level = 0;              //Controls indent for trace mode
    private boolean anyErrors;          //Set TRUE if an error happens

    private int symbolSize = 250;       //Max Size
    private int quadSize = 1000;
    private int genCount = 0; //Keeps track of adding temps in Symbol Table
    private int Location;

    private int Minus1Index;
    private int Plus1Index;

    //Initializes the SymbolTable and Mneumonic Table from Lexical, and also lexical.
    public Syntactic(String filename, boolean traceOn) {
        filein = filename;
        traceon = traceOn;

        symbolList = new SymbolTable(symbolSize);
        quads = new QuadTable(quadSize);
        interp = new Interpreter();

        Minus1Index = symbolList.AddSymbol("-1", 'c', -1);
        Plus1Index = symbolList.AddSymbol("1", 'c', 1);


        lex = new Lexical(filein, symbolList, true);
        lex.setPrintToken(traceOn);
        anyErrors = false;
    }

    //The interface to the syntax analyzer, initiates parsing
// Uses variable RECUR to get return values throughout the non-terminal methods
    public void parse() {

        int recur = 0;

        // prime the pump
        token = lex.GetNextToken();

        // call PROGRAM
        recur = Program();

        //Done, so add final STOP quad
        quads.AddQuad(interp.opcodeFor("STOP"),0,0,0);

        //print ST/QT Before

        //Basic Files
        symbolList.PrintSymbolTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\CodeGenBASICST-before.txt");
        quads.PrintQuadTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\CodeGenBASICQUADS.txt");

        //Full Files
        //symbolList.PrintSymbolTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\CodeGenFULLST-before.txt");
        //quads.PrintQuadTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\CodeGenFULLQUADS.txt");

        if(anyErrors == false){
            interp.InterpretQuads(quads,symbolList,false, "C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\TRACE.txt");
        } else {
            System.out.println("Errors, Unable to run the program");
        }

        //symbolList.PrintSymbolTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\CodeGenFullST-after.txt");
        symbolList.PrintSymbolTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\CodeGenBASICST-after.txt");
    }

    // Non-Terminal which looks for an Identifer and updates the Symboltablfe
    private int ProgIdentifier() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        // This non-term is used to uniquely mark the program identifier
        if (token.code == lex.codeFor("IDENT")) {
            // Because this is the progIdentifier, it will get a 'p' type to prevent re-use as a var
            symbolList.UpdateSymbol(symbolList.LookupSymbol(token.lexeme), 'p', 0);
            //move on
            token = lex.GetNextToken();
        }
        return recur;
    }

    /*
     Checks for the beginning of a Program by looking for
    Program Mneumonic then followed by the correct components
    */
    private int Program() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Program", true);
        if (token.code == lex.codeFor("PRGRM")) {
            token = lex.GetNextToken();
            recur = ProgIdentifier();
            if (token.code == lex.codeFor("SCOLN")) {
                token = lex.GetNextToken();
                recur = Block();
                if (token.code == lex.codeFor("PEROD")) {
                    if (!anyErrors) {
                        System.out.println("Success.");
                    } else {
                        System.out.println("Compilation failed.");
                    }
                } else {
                    error(lex.reserveFor("PEROD"), token.lexeme);
                }
            } else {
                error(lex.reserveFor("SCOLN"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("PRGRM"), token.lexeme);
        }
        trace("Program", false);
        return recur;
    }

    //Non-Terminal Checking for Beginning/End
    private int Block() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Block", true);

        if (token.code == lex.codeFor("BEGIN")) {
            token = lex.GetNextToken();
            recur = Statement();

            while ((token.code == lex.codeFor("SCOLN")) && (!lex.EOF()) && (!anyErrors)) {
                token = lex.GetNextToken();
                recur = Statement();
            }
            if (token.code == lex.codeFor("_END_")) {
                token = lex.GetNextToken();
            } else {
                error(lex.reserveFor("_END_"), token.lexeme);
            }

        } else {
            error(lex.reserveFor("BEGIN"), token.lexeme);
        }

        trace("Block", false);
        return recur;
    }


    //Not a NT, but used to shorten Statement code body
    //<variable> $COLON-EQUALS <simple expression>
    private int handleAssignment() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("handleAssignment", true);
        recur = Variable();  //Variable moves ahead, next token ready

        if (token.code == lex.codeFor("ASNMT")) {
            recur = SimpleExpression();
        } else {
            error(lex.reserveFor("ASNMT"), token.lexeme);
        }

        trace("handleAssignment", false);
        return recur;
    }

//Checks for [<sign>] <Term> {<addop> <term>}
    private int SimpleExpression() {
        int left, right, signval, temp, opcode;

        signval = Sign();
        left = Term();

        trace("SimpleExpression", true);

        if(signval == -1)
            quads.AddQuad(interp.opcodeFor("MUL"), left, Minus1Index, left);

        while (token.code == lex.codeFor("SUBTR") || token.code == lex.codeFor("PLUS_")){

            opcode = Addop();
            token = lex.GetNextToken();
            right = Term();
            temp = GenSymbol(); //Creates a new temp variable in the symbol table and returns position
            quads.AddQuad(opcode,left,right,temp);
            left = temp;

        } //End while

        trace("SimpleExpression", false);

        return left;
    }

    //Non Terminals, checks for IDENT and then IFS/WHILES/DOWHILES
    private int Statement() {
        int left, right, saveTop, branchQuad, patchElse;

        if (anyErrors) {
            return -1;
        }

        trace("Statement", true);

        if (token.code == lex.codeFor("IDENT")) {  //must be an ASSIGNMENT
            left = Variable();

            //Assignments
            if (token.code == lex.codeFor("ASNMT")) {
                token = lex.GetNextToken();
                right = SimpleExpression();
                quads.AddQuad(interp.opcodeFor("MOV"), right, 0, left);
            } else {
                error("Assignment", token.lexeme);
            }
        } else {
            //IF Statement Check
            if (token.code == lex.codeFor("_I_F_")) {  //must be an ASSIGNMENT
                //move onto next token
                token = lex.GetNextToken();
                branchQuad = relexExpression();

                if (token.code == lex.codeFor("THEN_")) {
                    token = lex.GetNextToken();
                    Statement();

                    if (token.code == lex.codeFor("ELSE_")) {
                        token = lex.GetNextToken();
                        patchElse = quads.NextQuad();

                        quads.AddQuad(interp.opcodeFor("JMP"), 0, 0, 0);
                        quads.setQuadOp3(branchQuad,quads.NextQuad());
                        Statement();
                        quads.setQuadOp3(patchElse,quads.NextQuad());
                    } else {
                        quads.setQuadOp3(branchQuad,quads.NextQuad());
                    }
                } else {
                    error("IF", token.lexeme);
                }

            } else {

                //DOWHILE <Relexp> <Statement>
                if (token.code == lex.codeFor("DWHLE")) {
                    //Move onto next token
                    token = lex.GetNextToken();
                    saveTop = quads.NextQuad();
                    branchQuad = relexExpression();

                } else {
                    //Printline Check
                    if (token.code == lex.codeFor("PRTLN")) {
                        handlePrintln();

                    } else {
                        //Readline Check
                        if (token.code == lex.codeFor("READL")) {
                            handleReadN();

                        } else {
                            error("ReadL", token.lexeme);
                        }

                    }


                }

            }

        }

        trace("Statement", true);
        return 0;
    }

    // $EQ | $LSS | $GTR | $NEQ | $LEQ | $GEQ
    private int relop() {
        int recur = 0;

        if (anyErrors) {
            return -1;
        }

        trace("relop", true);

        //Use a switch if changing next time
        if (token.code == lex.codeFor("EQUAL")
                || token.code == lex.codeFor("LESS<")
                || token.code == lex.codeFor("GRTH>")
                || token.code == lex.codeFor("NEQUL")
                || token.code == lex.codeFor("LSOR=")
                || token.code == lex.codeFor("GROR=")) {
            recur = symbolList.LookupSymbol(token.lexeme);
            token = lex.GetNextToken();
        }else {
            error(lex.reserveFor("Variable"), token.lexeme);
        }
        trace("relop", false);
        return recur;
    }

    // <simple expression> <relop> <simple expression>
    private int relexExpression(){
        int recur = 0;

        if (anyErrors) {
            return -1;
        }

        trace("relexExpression", true);

        if(token.code == lex.codeFor("PLUS_")
                || token.code == lex.codeFor("SUBTR")){
            recur = SimpleExpression();
            recur = relop();
            recur = SimpleExpression();

        }else{
            //Check for a Term (Factor)
            if(token.code == lex.codeFor("NCINT")
                    || token.code == lex.codeFor("FCINT")
                    || token.code == lex.codeFor("IDENT")
                    || token.code == lex.codeFor("LPARA")){
                recur = SimpleExpression();
                recur = relop();
                recur = SimpleExpression();
            }
        }

        trace("relexExpression", false);

        return recur;
    }

    //Display Error message and set anyErrors to tue
    private void error(String wanted, String got) {
        anyErrors = true;
        System.out.println("ERROR: Expected " + wanted + " but found " + got);
    }


    //Adds a new, specially named temp symbol to the symbol table
    private int GenSymbol(){
        String temp = "@" + Integer.toString(genCount);
        genCount++;
        symbolList.AddSymbol(temp,'v', 0);
        return symbolList.LookupSymbol(temp);
    }


    // trace simply RETURNs if traceon is false; otherwise, it prints an
    // ENTERING or EXITING message using the proc string
    private void trace(String proc, boolean enter) {
        String tabs = "";

        if (!traceon) {
            return;
        }

        if (enter) {
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("--> Entering " + proc);
            level++;
        } else {
            if (level > 0) {
                level--;
            }
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("<-- Exiting " + proc);
        }
    }

    // repeatChar returns a string containing x repetitions of string s;
//    nice for making a varying indent format
    private String repeatChar(String s, int x) {
        int i;
        String result = "";
        for (i = 1; i <= x; i++) {
            result = result + s;
        }
        return result;
    }

    //Checks for an <Identifer>
    //Accoring to the CFG this is suppose to go into a redundant Identifer Function which does the same thing as thing.
    private int Variable() {
        int result = 0;

        if (anyErrors) {
            return -1;
        }

        trace("Variable", true);

        if (token.code == lex.codeFor("IDENT")) {
            result = symbolList.LookupSymbol(token.lexeme);
            token = lex.GetNextToken();
        } else {
            error(lex.reserveFor("Variable"), token.lexeme);
        }

        trace("Variable", false);
        return result;
    }


    //Checks for an Unsigned Constant | Variable | Lpar <Simple Expression> Rpar
    private int Factor() {
        int recur = 0;

        if (anyErrors) {
            return -1;
        }

        trace("Factor", true);

        if (token.code == (lex.codeFor("LPARA"))) {
            token = lex.GetNextToken(); //Move on to next token
            recur = SimpleExpression();

            if(token.code ==(lex.codeFor("RPARA"))){
                token = lex.GetNextToken();
            }
        } else if(token.code == lex.codeFor("IDENT")){
            recur = Variable();
        } else {
            recur = UnsignedConstant();
        }

        trace("Factor", false);
        return recur;
    }

    //Handles ReadN by checking for Lpara-Variable-Rpara
    private int handleReadN() {
        int recur = 0;
        int toprint = 0;

        if(anyErrors){
            return -1;
        }

        trace("handleReadN",true);

        token = lex.GetNextToken(); //Move next

        if(token.code == lex.codeFor("LPARA")){
            token = lex.GetNextToken();
            toprint = Variable();

            if(token.code == lex.codeFor("RPARA")){
                token = lex.GetNextToken(); //Move next
            }else {
                error("RPARA", token.lexeme);
            }
        } else {
            error("LPARA", token.lexeme);
        }

        quads.AddQuad(interp.opcodeFor("READ"), 0, 0, toprint);
        return recur;
    }

    //HandlePrintln by checking for a Lpara followed by a (SimpleEx or String Const) then Rpara
    private int handlePrintln() {
        int recur = 0;
        int toprint = 0;
        if (anyErrors) {
            return -1;
        }
        trace("handlePrintln", true);
        //got here from a PRINT token, move past it...
        token = lex.GetNextToken();
        //look for ( stringconst, ident, simpleexp )
        if (token.code == lex.codeFor("LPARA")) {
            //move on
            token = lex.GetNextToken();

            if ((token.code == lex.codeFor("STRGC"))
                    || (token.code == lex.codeFor("IDENT"))) {
                    // save index for string literal or identifier
                    toprint = symbolList.LookupSymbol(token.lexeme);
                    //move on
                    token = lex.GetNextToken();
                }else {
                    toprint = SimpleExpression();
                }
                quads.AddQuad(interp.opcodeFor("PRINT"), 0, 0, toprint);
                //now need right ")"
                if (token.code == lex.codeFor("RPARA")) {
                //move on
                    token = lex.GetNextToken();
                } else {
                    error(lex.reserveFor("RPARA"), token.lexeme);
                }
            } else {
                error(lex.reserveFor("LPARA"), token.lexeme);
            }// end lpar group

            trace("handlePrintn", false);
            return recur;
        }

//Checks for Factor Components followed up a possible repeating Mulop + Factor
    private int Term() {
        int left, right, temp, opcode;

        left = Factor();

        trace("Term", true);

        while (token.code == lex.codeFor("MULTI") || token.code == lex.codeFor("DIVID")){

            opcode = Mulop();
            token = lex.GetNextToken();
            right = Factor();
            temp = GenSymbol(); //Creates a new temp variable in the symbol table and returns position
            quads.AddQuad(opcode,left,right,temp);
            left = temp;

        } //End while

        trace("Term", false);

        return left;
    }

    //Checks for an Unsigned Constant then passes it to UnsignedNumber
    private int UnsignedConstant() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("UnsignedConstant", true);

        if (token.code == lex.codeFor("FCINT")) {
            recur = UnsignedNumber();
        } else if(token.code == lex.codeFor("NCINT")) {
                recur = UnsignedNumber();
            }else{
                //Handling Errors
                error(lex.reserveFor("UnsignedConstant"), token.lexeme);
            }

        trace("UnsignedConstant", false);

        return recur;
    }

    //Checks the Mneunomic for the correct reference for an Integer or Float
    private int UnsignedNumber() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("UnsignedNumber", true);

        if ((token.code == lex.codeFor("NCINT")
                || token.code == lex.codeFor("FCINT"))) {
                recur = symbolList.LookupSymbol(token.lexeme);
                token = lex.GetNextToken();
        } else {
            //Handling Errors
                error(lex.reserveFor("Unsigned Number"), token.lexeme);
        }
        trace("UnsignedNumber", false);
        return recur;
    }

    //Checks for a Multiplier or Divide then gets next token
    private int Mulop() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Mulop", true);

        if (token.code == lex.codeFor("MULTI")) {
            recur = interp.opcodeFor("MUL");
        }else if (token.code == lex.codeFor("DIVID")) {
            recur = interp.opcodeFor("DIV");
            } else {
                error(lex.reserveFor("Mulop"), token.lexeme);
            }

        trace("Mulop", false);

        return recur;
    }

    //Checks for a Plus or Minus Sign then gets next char
    private int Addop() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Addop", true);

        if (token.code == lex.codeFor("PLUS_")) {
            recur = interp.opcodeFor("ADD");

        }else if (token.code == lex.codeFor("SUBTR")) {
            recur = interp.opcodeFor("SUB");

        } else {
            error(lex.reserveFor("Addop"), token.lexeme);
        }
        trace("Addop", false);
        return recur;
    }

    //Checks for a Plus or Minus at the beginning of a statement
    private int Sign() {
        int result = 1;

        if (anyErrors) {
            return -1;
        }

        trace("Sign", true);

        if (token.code == lex.codeFor("SUBTR")){
            result = -1;
            token = lex.GetNextToken();
        } else if (token.code == lex.codeFor("PLUS_")){
            token = lex.GetNextToken();
        }
        trace("Sign", false);
        return result;
    }

    private int stringConst(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("stringConst", true);

        if (token.code == lex.codeFor("STRGC")){
            recur = symbolList.LookupSymbol(token.lexeme);
            token = lex.GetNextToken();
        } else {
            //Handling Errors
            error(lex.reserveFor("stringConst"), token.lexeme);
        }
        trace("stringConst", false);

        return recur;
    } // End stringConst

} // End Syntactic