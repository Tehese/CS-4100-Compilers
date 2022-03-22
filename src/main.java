import ADT.*;

public class main {

    public static void main(String[] args) {

            System.out.println("Scott Canfield\n");
            System.out.println("Last 4 of Student ID: 2339");
            System.out.print("CS4100 Spring 2022 Compiler = IntellJ");



            // /Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/LexicalTestSP22.txt MAC FILE ADDRESS
            // C:\Users\someb\JavaProjects\CS4100_Compilers\src\Test Files\LexicalTestSP22.txt PC FILE ADDRESS

            String fileAndPath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/LexicalTestSP22.txt";
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

            // /Users/tehese/IdeaProjects/CS-4100-Compilers/src/Output Files/symboltable2.txt MAC File Address
            // C:\Users\someb\JavaProjects\CS4100_Compilers\src\Output Files\symboltable2.txt PC File Address
            
            symbolList.PrintSymbolTable("/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Output Files/symboltable2.txt");
            System.out.println("Done.");
    }
}
