/*
 The following code is provided by the instructor for the completion of PHASE 2
of the compiler project for CS4100.

Scott Canfield
CS 4100
3-17-22
 */
package ADT;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


import java.io.*;
import java.util.Objects;

public class Lexical {

    private File file;                        //File to be read for input
    private FileReader filereader;            //Reader, Java reqd
    private BufferedReader bufferedreader;    //Buffered, Java reqd
    private String line;                      //Current line of input from file
    private int linePos;                      //Current character position
    //  in the current line
    private SymbolTable saveSymbols;          //SymbolTable used in Lexical
    //  sent as parameter to construct
    private boolean EOF;                      //End Of File indicator
    private boolean echo;                     //true means echo each input line
    private boolean printToken;               //true to print found tokens here
    private int lineCount;                    //line #in file, for echo-ing
    private boolean needLine;                 //track when to read a new line
    //Tables to hold the reserve words and the mnemonics for token codes
    private ReserveTable reserveWords = new ReserveTable(50); //a few more than # reserves
    private ReserveTable mnemonics = new ReserveTable(50); //a few more than # reserves
    //global char
    char currCh;

    //constructor
    public Lexical(String filename, SymbolTable symbols, boolean echoOn) {
        saveSymbols = symbols;  //map the initialized parameter to the local ST
        echo = echoOn;          //store echo status
        lineCount = 0;          //start the line number count
        line = "";              //line starts empty
        needLine = true;        //need to read a line
        printToken = false;     //default OFF, do not print tokens here
        //  within GetNextToken; call setPrintToken to
        //  change it publicly.
        linePos = -1;           //no chars read yet
        //call initializations of tables
        initReserveWords(reserveWords);
        initMnemonics(mnemonics);

        //set up the file access, get first character, line retrieved 1st time
        try {
            file = new File(filename);    //creates a new file instance
            filereader = new FileReader(file);   //reads the file
            bufferedreader = new BufferedReader(filereader);  //creates a buffering character input stream
            EOF = false;
            currCh = GetNextChar();
        } catch (IOException e) {
            EOF = true;
            e.printStackTrace();
        }
    }

    // token class is declared here, no accessors needed
    public class token {

        public String lexeme;
        public int code;
        public String mnemonic;

        token() {
            lexeme = "";
            code = 0;
            mnemonic = "";
        }
    }


    private final int UNKNOWN_CHAR = 99;
    public final int _GRTR = 38;
    public final int _LESS = 39;
    public final int _GREQ = 40;
    public final int _LEEQ = 41;
    public final int _EQLS = 42;
    public final int _NEQL = 43;

    // given a mnemonic, find its token code value
    public int codeFor(String mnemonic) {
        return mnemonics.LookupName(mnemonic);
    }
    // given a mnemonic, return its reserve word
    public String reserveFor(String mnemonic) {
        return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
    }

    // Public access to the current End Of File status
    public boolean EOF() {
        return EOF;
    }

    // DEBUG enabler, turns on token printing inside of GetNextToken
    public void setPrintToken(boolean on) {
        printToken = on;
    }

    /* ReserveTable with all reserved words */
    private void initReserveWords(ReserveTable reserveWords) {
        reserveWords.Add("GO_TO", 0);
        reserveWords.Add("INTEGER", 1);
        reserveWords.Add("TO", 2);
        reserveWords.Add("DO", 3);
        reserveWords.Add("IF", 4);
        reserveWords.Add("THEN", 5);
        reserveWords.Add("ELSE", 6);
        reserveWords.Add("FOR", 7);
        reserveWords.Add("OF", 8);
        reserveWords.Add("PRINTLN", 9);
        reserveWords.Add("READLN", 10);
        reserveWords.Add("BEGIN", 11);
        reserveWords.Add("END", 12);
        reserveWords.Add("VAR", 13);
        reserveWords.Add("DOWHILE", 14);
        reserveWords.Add("PROGRAM", 15);
        reserveWords.Add("LABEL", 16);
        reserveWords.Add("REPEAT", 17);
        reserveWords.Add("UNTIL", 18);
        reserveWords.Add("PROCEDURE", 19);
        reserveWords.Add("DOWNTO", 20);
        reserveWords.Add("FUNCTION", 21);
        reserveWords.Add("RETURN", 22);
        reserveWords.Add("FLOAT", 23);
        reserveWords.Add("STRING", 24);
        reserveWords.Add("ARRAY", 25);

        // add 1 and 2-char tokens here
        reserveWords.Add("/", 30);
        reserveWords.Add("*", 31);
        reserveWords.Add("+", 32);
        reserveWords.Add("-", 33);
        reserveWords.Add("(", 34);
        reserveWords.Add(")", 35);
        reserveWords.Add(";", 36);
        reserveWords.Add(":=", 37);
        reserveWords.Add(">", 38);
        reserveWords.Add("<", 39);
        reserveWords.Add(">=", 40);
        reserveWords.Add("<=", 41);
        reserveWords.Add("=", 42);
        reserveWords.Add("<>", 43);
        reserveWords.Add(",", 44);
        reserveWords.Add("[", 45);
        reserveWords.Add("]", 46);
        reserveWords.Add(":", 47);
        reserveWords.Add(".", 48);
        reserveWords.Add("Identifer", 50);
        reserveWords.Add("Numeric Constant Int", 51);
        reserveWords.Add("Float Constant Int", 52);
        reserveWords.Add("String Constant", 53);


        //Anything Else
        reserveWords.Add("UNKNOWN", 99 );

    }

    /* Mnemonics table correlating to the ReserveTable Reserved Words */
    private void initMnemonics(ReserveTable mnemonics) {
        mnemonics.Add("GOTO_", 0);
        mnemonics.Add("INTGE", 1);
        mnemonics.Add("_T_O_", 2);
        mnemonics.Add("_D_O_", 3);
        mnemonics.Add("_I_F_", 4);
        mnemonics.Add("THEN_", 5);
        mnemonics.Add("ELSE_", 6);
        mnemonics.Add("F_O_R", 7);
        mnemonics.Add("_O_F_", 8);
        mnemonics.Add("PRTLN", 9);
        mnemonics.Add("READL", 10);
        mnemonics.Add("BEGIN", 11);
        mnemonics.Add("_END_", 12);
        mnemonics.Add("_VAR_", 13);
        mnemonics.Add("DWHLE", 14);
        mnemonics.Add("PRGRM", 15);
        mnemonics.Add("LABEL", 16);
        mnemonics.Add("RPEAT", 17);
        mnemonics.Add("UNTIL", 18);
        mnemonics.Add("PROCD", 19);
        mnemonics.Add("DOWNT", 20);
        mnemonics.Add("FUNCT", 21);
        mnemonics.Add("RTURN", 22);
        mnemonics.Add("FLOAT", 23);
        mnemonics.Add("STRNG", 24);
        mnemonics.Add("ARRAY", 25);

        // 1 or 2 char mnemonics
        mnemonics.Add("SLASH", 30);
        mnemonics.Add("STAR_", 31);
        mnemonics.Add("PLUS_", 32);
        mnemonics.Add("DASH_", 33);
        mnemonics.Add("LPARA", 34);
        mnemonics.Add("RPARA", 35);
        mnemonics.Add("SCOLN", 36);
        mnemonics.Add("ASNMT", 37);
        mnemonics.Add("GRTH>", 38);
        mnemonics.Add("LESS<", 39);
        mnemonics.Add("GROR=", 40);
        mnemonics.Add("LSOR=", 41);
        mnemonics.Add("EQUAL", 42);
        mnemonics.Add("NEQUL", 43);
        mnemonics.Add("COMMA", 44);
        mnemonics.Add("RBRAK", 45);
        mnemonics.Add("LBRAK", 46);
        mnemonics.Add("COLON", 47);
        mnemonics.Add("PEROD", 48);
        mnemonics.Add("IDENT", 50);
        mnemonics.Add("NCINT", 51);
        mnemonics.Add("FCINT", 52);
        mnemonics.Add("STRGC", 53);


        //Anything else
        mnemonics.Add("UNKWN", 99);

    }

    // Character category for alphabetic chars, upper and lower case
    private boolean isLetter(char ch) {
        return (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')));
    }

    // Character category for 0..9
    private boolean isDigit(char ch) {
        return ((ch >= '0') && (ch <= '9'));
    }

    // Category for any whitespace to be skipped over
    // space, tab, and newline
    private boolean isWhitespace(char ch) {
        return ((ch == ' ') || (ch == '\t') || (ch == '\n'));
    }

    // Returns the VALUE of the next character without removing it from the
    //    input line.  Useful for checking 2-character tokens that start with
    //    a 1-character token.
    private char PeekNextChar() {
        char result = ' ';
        if ((needLine) || (EOF)) {
            result = ' '; //at end of line, so nothing
        } else //
        {
            if ((linePos + 1) < line.length()) { //have a char to peek
                result = line.charAt(linePos + 1);
            }
        }
        return result;
    }

    // Called by GetNextChar when the characters in the current line
    // buffer string (line) are used up.
    private void GetNextLine() {
        try {
            line = bufferedreader.readLine();  //returns a null string when EOF
            if ((line != null) && (echo)) {
                lineCount++;
                System.out.println(String.format("%04d", lineCount) + " " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null) {    // The readLine returns null at EOF, set flag
            EOF = true;
        }
        linePos = -1;      // reset vars for new line if we have one
        needLine = false;  // we have one, no need
        //the line is ready for the next call to get a char with GetNextChar
    }

    // Returns the next character in the input file, returning a
    // /n newline character at the end of each input line or at EOF
    public char GetNextChar() {
        char result;
        if (needLine) //ran out last time we got a char, so get a new line
        {
            GetNextLine();
        }
        //try to get char from line buff
        // if EOF there is no new char, just return endofline, DONE
        if (EOF) {
            result = '\n';
            needLine = false;
        } else {
            // if there are more characters left in the input buffer
            if ((linePos < line.length() - 1)) { //have a character available
                linePos++;
                result = line.charAt(linePos);
            } else {
                //need a new line, but want to return eoln on this call first
                result = '\n';
                needLine = true; //will read a new line on next GetNextChar call
            }
        }
        return result;
    }

    final char comment_start1 = '{';
    final char comment_end1 = '}';
    final char comment_start2 = '(';
    final char comment_startend = '*';
    final char comment_end2 = ')';

    // skips over any comment as if it were whitespace, so it is ignored
    public char skipComment(char curr) {
        // if this is the start of a comment...
        if (curr == comment_start1) {
            curr = GetNextChar();
            // loop until the end of comment or EOF is reached
            while ((curr != comment_end1) && (!EOF)) {
                curr = GetNextChar();
            }
            // if the file ended before the comment terminated
            if (EOF) {
                System.out.println("Comment not terminated before End Of File");
            } else {
                // keep getting the next char
                curr = GetNextChar();
            }
        } else {
            // this is for the 2-character comment start, different start/end
            if ((curr == comment_start2) && (PeekNextChar() == comment_startend)) {
                curr = GetNextChar(); // get the second
                curr = GetNextChar(); // into comment or end of comment
                //while comment end is not reached
                while ((!((curr == comment_startend) && (PeekNextChar() == comment_end2))) && (!EOF)) {
                    curr = GetNextChar();
                }
                // EOF before comment end
                if (EOF) {
                    System.out.println("Comment not terminated before End Of File");
                } else {
                    curr = GetNextChar();          //must move past close
                    curr = GetNextChar();          //must get following
                }
            }

        }
        return (curr);
    }

    //reads past any white space, blank lines, comments
    public char skipWhiteSpace() {
        do {
            while ((isWhitespace(currCh)) && (!EOF)) {
                currCh = GetNextChar();
            }
            currCh = skipComment(currCh);
        } while (isWhitespace(currCh) && (!EOF));
        return currCh;
    }

    // returns TRUE if ch is a prefix to a 2-character token like := or <=
    private boolean isPrefix(char ch) {
        return ((ch == ':') || (ch == '<') || (ch == '>'));
    }

    // returns TRUE if ch is the string delimiter
    private boolean isStringStart(char ch) {
        return ch == '"';
    }


// Student supplied methods
private token getIdent(char ch){
    token result = new token();
    result.lexeme = "" + ch; //have the first char
    ch = GetNextChar();
    while (isLetter(ch)||(isDigit(ch)||(ch == '$')||(ch=='_'))) {
        result.lexeme = result.lexeme + ch; //extend lexeme
        ch = GetNextChar();
    }

    // end of token, lookup or IDENT
    result.code = reserveWords.LookupName(result.lexeme);
    if (result.code == -1)
        result.code = IDENT_ID;

    return result;
}

    private token getNumber() {
        char ch;
        token result = new token();
        ch = GetNextChar();
       while(isDigit(ch) || (ch=='E') || (ch=='e')||(ch == '.')) {
           result.lexeme = result.lexeme + ch;
           if(ch =='E'|| ch =='e'|| ch=='.'){
               result.code = FLOAT_ID;
           }else
               result.code = INTEGER_ID;

           ch = GetNextChar();
       }
        return result;


        // Regex for Ints ([0-9]+[.]*[0-9]*)
        // Regex for Floats ([0-9]+[.]*[0-9]*[Ee][+-][0-9]+)
        /* a number is:   <digit>+[.<digit>*[E[+|-]<digit>+]] */
    }

    private token getString() {
        token result = new token();
        return result;
    }

    private token getOneTwoChar() {
        token result = new token();
        return result;
    }


    private final int IDENT_ID = 50;
    private final int INTEGER_ID = 51;
    private final int FLOAT_ID = 52;
    private final int STRING_ID = 53;
    private int maxIdentLength = 20;
    private int maxNumberLength = 15;

    //Checks the length of the corresponding value, and truncates it if it is to long
    public token checkTruncate(token result) {
        switch (result.code) {

            //Token Code 50, truncate if length > 20 to 20
            case IDENT_ID:
                if(result.lexeme.length() > maxIdentLength){
                    String temp = result.lexeme.substring(0, maxIdentLength);
                    System.out.println("Identifer length > 20, truncated " +result.lexeme +"to "+temp);
                    result.lexeme = temp;
                }
                //Write to symbol Table
                return result;


            //Token Code 51, truncate if length is > 15 to 15
            case INTEGER_ID:
                if(result.lexeme.length() > maxNumberLength){
                    String temp = result.lexeme.substring(0, maxNumberLength);
                    System.out.println("Integer length > 15, truncated "+result.lexeme+" to "+temp);
                    result.lexeme = temp; //truncate to 20 chars
                }
                saveSymbols.AddSymbol(result.lexeme, 'f', result.lexeme);
                //Write to Symbol Table (Symbol String, Char Kind, Value)
                return result;

            //Token Code 52, truncate if length is > 15 to 15
            case FLOAT_ID:
                if(result.lexeme.length() > maxNumberLength){
                    String temp =  result.lexeme.substring(0, maxNumberLength);
                    System.out.println("Float length > 15, truncated "+result.lexeme+" to "+temp);
                    result.lexeme = temp; //truncate to 20 chars
                }
                //Write to Symbol Table (Symbol String, Char Kind, Value)
                saveSymbols.AddSymbol(result.lexeme, 'f', result.lexeme);
                return result;

            //String consists of double quotes (""), any chars except line-terminator, and won't excede EOF

            case STRING_ID:

                //Write to Symbol Table (Symbol String, Char Kind, Value)
                return result;

            default:
                break; //don't add
        }
        return result;
    }

// numberic validation freebie code!

    // Checks to see if a string contains a valid DOUBLE
    public boolean doubleOK(String stin) {
        boolean result;
        Double x;
        try {
            x = Double.parseDouble(stin);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }
        return result;
    }

    // Checks the input string for a valid INTEGER
    public boolean integerOK(String stin) {
        boolean result;
        int x;
        try {
            x = Integer.parseInt(stin);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }
        return result;
    }

    // Main method of Lexical
    public token GetNextToken() {
        token result = new token();

        currCh = skipWhiteSpace();
        if (isLetter(currCh)) { //is ident
            result = getIdent(currCh);
        } else if (isDigit(currCh)) { //is numeric
            result = getNumber();
        } else if (isStringStart(currCh)) { //string literal
            result = getString();
        } else //default char checks
        {
            result = getOneTwoChar();
        }

        if ((result.lexeme.equals("")) || (EOF)) {
            result = null;
        }
//set the mnemonic
        if (result != null) {
            if(Objects.equals(mnemonics.LookupCode(result.code), "")){
                result.mnemonic = mnemonics.LookupCode(99); //returns UNKWN
            }else{
                result.mnemonic = mnemonics.LookupCode(result.code); //Returns correct Mnemonic is found
            }

            result = checkTruncate(result);
            if (printToken) {
                System.out.println("\t" + result.mnemonic + " | \t" + String.format("%04d", result.code) + " | \t" + result.lexeme);
            }
        }
        return result;
    }
}