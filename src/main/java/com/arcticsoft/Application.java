package com.arcticsoft;

public class Application {
    public static void main(String[] args) throws Exception {
        var numberOfPages = 2;
        if (args.length > 0) {
            numberOfPages = Integer.parseInt(args[0]);
        }
        new Service().downloadNPages(numberOfPages);
    }
}