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

 */
package ADT;

public class Syntactic {

    private String filein;              //The full file path to input file
    private SymbolTable symbolList;     //Symbol table storing ident/const
    private Lexical lex;                //Lexical analyzer
    private Lexical.token token;        //Next Token retrieved
    private boolean traceon;            //Controls tracing mode
    private int level = 0;              //Controls indent for trace mode
    private boolean anyErrors;          //Set TRUE if an error happens

    private final int symbolSize = 250;

    public Syntactic(String filename, boolean traceOn) {
        filein = filename;
        traceon = traceOn;
        symbolList = new SymbolTable(symbolSize);
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
    }

    //Non Terminal
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

    //Non Terminals
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

    //Non Terminal
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
        //have ident already in order to get to here, handle as Variable
        //recur = Variable();  //Variable moves ahead, next token ready
        recur = Program();  //Variable moves ahead, next token ready

        if (token.code == lex.codeFor("ASNMT")) {
            token = lex.GetNextToken();
            recur = SimpleExpression();
        } else {
            error(lex.reserveFor("ASNMT"), token.lexeme);
        }

        trace("handleAssignment", false);
        return recur;
    }

    // NT This is dummied in to only work for an identifier.
//  It will work with the SyntaxAMiniTest file.
// SimpleExpression MUST BE
//  COMPLETED TO IMPLEMENT CFG for <simple expression>
    private int SimpleExpression() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("SimpleExpression", true);
        if (token.code == lex.codeFor("IDENT")) {
            token = lex.GetNextToken();
        }
        trace("SimpleExpression", false);
        return recur;
    }

    //Non Terminal
    private int Statement() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Statement", true);

        if (token.code == lex.codeFor("IDENT")) {  //must be an ASSUGNMENT
            recur = handleAssignment();
        } else {
            if (token.code == lex.codeFor("_I_f_")){  //must be an ASSUGNMENT
                // this would handle the rest of the IF statment IN PART B
            } else // if/elses should look for the other possible statement starts...
            //  but not until PART B
            {
                error("Statement start", token.lexeme);
            }
        }

        trace("Statement", false);
        return recur;
    }

    /**
     * *************************************************
     */
    /*     UTILITY FUNCTIONS USED THROUGHOUT THIS CLASS */
// error provides a simple way to print an error statement to standard output
//     and avoid reduncancy
    private void error(String wanted, String got) {
        anyErrors = true;
        System.out.println("ERROR: Expected " + wanted + " but found " + got);
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

  //Template for all the non-terminal method bodies
    private int exampleNonTerminal(){
            int recur = 0;
            if (anyErrors) {
                return -1;
            }

            trace("NameOfThisMethod", true);
    // unique non-terminal stuff
            trace("NameOfThisMethod", false);
            return recur;

    }


}
