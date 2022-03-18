import ADT.*;

public class main {

    public static void main(String[] args) {


            System.out.println("Scott Canfield\n");
            String fileAndPath = "C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Test Files\\LexicalTestSP22.txt";
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
            symbolList.PrintSymbolTable("C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Output Files\\symboltable2.txt");
            System.out.println("Done.");

    }
}
