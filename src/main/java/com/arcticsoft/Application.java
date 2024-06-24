package com.arcticsoft;

public class Application {
    
    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.config.file", "src/main/resources/logging.properties"); 
        var fromPage = 1;
        var toPage = 200;
        if (args.length == 2) {
            fromPage = Integer.parseInt(args[0]);
            toPage = Integer.parseInt(args[1]);
        }
        ImageService.downloadPages(fromPage, toPage, true);
    }
}