package main;

import java.util.Locale;

import view.TelaLogin;

public class Main {
    public static void main(String[] args) {
    	Locale.setDefault(Locale.of("pt", "BR"));

    	new TelaLogin();
    }
}