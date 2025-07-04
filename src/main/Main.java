package main;

import java.util.Locale;

import view.TelaLogin;
//import view.ExemploTabbedPane;

public class Main {
    public static void main(String[] args) {
    	Locale.setDefault(Locale.of("pt", "BR"));

    	new TelaLogin();
    	//new ExemploTabbedPane();
    }
}