package com.drawing.utils;

public class Utils {
    public static final int SERVER_PORT = 5555;
    public static final String IMAGE_PATH = "./src/main/resources/images/";
    public static final String ATTENTE = "Attente";
    public static final String CEDER = "Ceder";
    public static final String ACTIF = "Actif";
    public static final String DESSINER = "Dessiner";

    private Utils() throws IllegalAccessException{
        throw new IllegalAccessException("Utility class");
    }
}
