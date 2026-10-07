package com.studentservices;

/**
 * Bootstrap launcher class that bypasses the JavaFX 11+ runtime component check
 * when executing the application without modular VM arguments.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class Launcher {

    public static void main(String[] args) {
        App.main(args);
    }
}
