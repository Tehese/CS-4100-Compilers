/*
Scott Canfield
May 2nd, 2022
CS 4100 Compilers
Last 4 of Student ID: 2339
 */


import ADT.*;

public class main {

        public static void main(String[] args) {


                System.out.println("Scott Canfield");
                System.out.println("Last 4 of Student Id: 2339");
                System.out.println("CS 4100 Compilers");
                System.out.println("Spring 2022");
                System.out.println("IDE: IntelliJ\n");

                //String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/BadSyntax-1-ASP22.txt"; // Bad File 1
                //String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/BadSyntax-2-ASP22.txt"; // Bad File 2
                //String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/SyntaxAMiniTestSP22.txt"; //Short Good File
                String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/GoodSyntaxASP22.txt"; //Good Long File
                boolean traceon = true;
                Syntactic parser = new Syntactic(filePath, traceon);
                parser.parse();
                System.out.println("Done.");

                /*
                System.out.println("Scott Canfield");
                System.out.println("CS 4100 Compilers");
                System.out.println("Last 4 of Student ID: 2339");


                String filePath = "d:\\CodeGenPartial.txt";
                System.out.println("Parsing "+filePath);
                boolean traceon = false; //true; //false;
                Syntactic parser = new Syntactic(filePath, traceon);
                parser.parse();
                System.out.println("Done.");

                 */


        }

}
