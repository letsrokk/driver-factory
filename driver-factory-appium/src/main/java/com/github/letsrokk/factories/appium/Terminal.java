package com.github.letsrokk.factories.appium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {

    public static String execute(String... command) throws IOException, InterruptedException{
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine())!= null) {
            sb.append(line);
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

}
