/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.HashMap;

/**
 *
 * @author fetouh
 */
public class Regex {
    //String Validate="(PROGRAM)(.+)((?:(VAR)[\\s\\S]*?)VAR.+)((?:(BEGIN)[\\s\\S]*?)BEGIN.+)(END\\.)";
    //String Validate = "\\s*(PROGRAM)\\s*(.+)\\s*(VAR)\\s*(.+)\\s*(BEGIN)\\s*(\\s*.+\\s*)+\\s*(END\\.)\\s*";

    public final static String Validate = "(PROGRAM)(\\w+)(VAR)(.+)(\\BBEGIN)(.+)(END\\.)";
    public final static String READ = "\\G(READ)\\s*((\\()\\s*(\\w+,*)+\\s*(\\)))(;).*";
    public final static String WRITE = "\\G(WRITE)\\s*((\\()\\s*(\\w+,*)+\\s*(\\)))(;).*";
    public static final String ASSIGN = "\\G(\\w+)+(:=)((.+\\+*|\\**)+)(;)";
    public static final String FOR = "\\G(FOR)(\\w+)(:=)(\\d+)(TO)(\\w+)(DO)(BEGIN)";
    public static final String END = "\\G(END)...+";
    public final static HashMap<String, String> regex = new HashMap<String, String>();

    public static void create() {
        regex.put("WRITE", WRITE);
        regex.put("READ", READ);
        regex.put("ASSIGN", ASSIGN);
        regex.put("FOR", FOR);
        regex.put("END", END);

    }
}
