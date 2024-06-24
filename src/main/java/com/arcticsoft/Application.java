package com.arcticsoft;

public class Application {

    public static void main(String[] args) throws Exception {
        var fromPage = 11;
        var toPage = 12;
        if (args.length == 2) {
            fromPage = Integer.parseInt(args[0]);
            toPage = Integer.parseInt(args[1]);
        }
        ImageService.downloadNPages(fromPage, toPage);
    }
}