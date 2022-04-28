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
                System.out.println("CS 4100 Compilers");
                System.out.println("Last 4 of Student ID: 2339");
                System.out.println("Spring 2022");
                System.out.println("IDE: IntelliJ\n");


                String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/CodeGenBASICsp22.txt";
                //String filePath = "/Users/tehese/IdeaProjects/CS-4100-Compilers/src/Test Files/CodeGenFULL-SP22-2.txt"; Full Test File
                System.out.println("Parsing "+filePath);
                boolean traceon = false; //true; //false;
                Syntactic parser = new Syntactic(filePath, traceon);
                parser.parse();
                System.out.println("Done.");

        }

}
