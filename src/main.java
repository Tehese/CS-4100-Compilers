/*
Scott Canfield
CS 4100
Compilers Spring 2022
 */
import ADT.*;

public class main {

        public static void main(String[] args) {

        System.out.println("Scott Canfield");
        System.out.println("Last 4 of Student Id: 2339");
        System.out.println("CS 4100 Compilers");
        System.out.println("Spring 2022");
        System.out.println("IDE: IntelliJ\n");
        //String filePath = "C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Test Files\\SyntaxAMiniTestSP22.txt";
        //String filePath = "C:\\Users\\someb\\JavaProjects\\CS4100_Compilers\\src\\Test Files\\GoodSyntaxASP22.txt";
        String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/GoodSyntaxASP22.txt";
        boolean traceon = true;
        Syntactic parser = new Syntactic(filePath, traceon);
        parser.parse();
        System.out.println("Done.");
        }

}

