/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author fetouh
 */
public class Compiler {

    /**
     * @param args the command line arguments
     */
    static ArrayList<String> tokens = new ArrayList<String>();

    public static int find(String X) {
        int flag = 0, i = 0;
        if (X.matches("[0-9]+")) {
            flag = 1;
            return flag;
        }

        int beg, end;
        beg = tokens.indexOf("VAR");
        end = tokens.indexOf("BEGIN");
        for (i = beg + 1; i < end; i++) {
            if (X.equalsIgnoreCase(tokens.get(i))) {
                flag = 1;
                return flag;
            }
        }
        return flag;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
       long start = System.currentTimeMillis();
        //String hlFile="";
        //System.out.println(args.length); 
     // Scanner sc = new Scanner(System.in);
     // hlFile=sc.next();
        
        
        //args[0]="input.txt";
    BufferedReader br = new BufferedReader(new FileReader(args[0]));
    
        StringBuilder sb = new StringBuilder();
        String linefile = br.readLine();

        while (linefile != null) {
            sb.append(linefile);
            sb.append("\n");
            linefile = br.readLine();
        }
    String  line=  sb.toString();
   
        br.close();
    
    

	


        
        
        
        
        
        
        int flagmaybe = 0, flag2 = 0;
        // String backup = "PROGRAM|VAR|BEGIN|END|END.|FOR|READ|WRITE|TO|DO|;|=|\\+|FOR|\\(|\\)|\\*)";
        /* String line = "PROGRAM BASICS VAR\n"
                + "X,A,B,C,Z BEGIN\n"
                + "READ(X,Z,B) A := X+B;\n"
                + "C := X+ Z;\n"
                + "C := C * B;\n"
                + "Z := A+B+C;\n"
                + "WRITE(A,C,Z) END.";*/

        //String line = "PROGRAM STATS VAR SUM,SUMSQ,I,VALUE,MEAN,VARIANCEBEGIN SUM:=I+SUMSQ*SUM+I+MEAN*VARIANCE*SUMSQ+I+I*VALUE+I;  END.";
       // String line = "PROGRAM STATS VAR SUM,SUMSQ,I,VALUE,MEAN,VARIANCEBEGIN SUM:=0; SUMSQ:=0; FORI:=1 TO 100 DO BEGIN READ(VALUE) SUM:=SUM+VALUE; SUMSQ:=SUMSQ+VALUE*VALUE; END FORI:=1 TO 100 DO BEGIN READ(VALUE) SUM:=SUM+VALUE; SUMSQ:=SUMSQ+VALUE*VALUE+SUMSQ+VALUE*VALUE+SUMSQ+VALUE*VALUE; END WRITE(MEAN,VARIANCE) END.";
        line = line.replaceAll("\\s", "");
        TokenTable.create();

        String linecopy = new String(line);
        String temp = new String();
        String endtoken = new String();
        Regex.create();

        String Validate = "(PROGRAM)(\\w+)(VAR)(.+)(\\BBEGIN)(.+)(END\\.)";

        Pattern r = Pattern.compile(Regex.Validate);
        Matcher m = r.matcher(line);
        if (m.find()) {
            if (m.matches()) {
                tokens.add(m.group(1).trim());
                tokens.add(m.group(2).trim());
                tokens.add(m.group(3).trim());
                String Variables = new String();
                Variables = m.group(4).trim();
                for (int i = 1; i < 4; i++) {
                    line = line.replaceFirst(m.group(i), "");
                }

                String[] parts = new String[30];
                Variables = Variables.substring(0, line.indexOf("BEGIN"));

                //Variables = Variables.replaceFirst("VAR", "");
                parts = Variables.split(",");
                for (int i = 0; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        if (!parts[i].matches("\\s*")) {
                            if (!parts[i].matches("[0-9].*")) {
                                tokens.add(parts[i]);
                            } else {
                                System.out.println("Variable can not start with a digit");
                                return;
                            }

                        }
                    }

                }
                tokens.add(m.group(5));
                endtoken = m.group(7);
                line = line.replaceFirst(Variables, "");
                line = line.replaceFirst(m.group(5), "");
            }
        }

        while (!line.equalsIgnoreCase("END.")) {
            // for (Map.Entry<String, String> entry : Regex.regex.entrySet()) {

            if (line.startsWith("WRITE") || line.startsWith("READ")) {
                Pattern r2;
                if (line.startsWith("WRITE")) {
                    r2 = Pattern.compile(Regex.WRITE);
                } else {
                    r2 = Pattern.compile(Regex.READ);
                }

                Matcher m2 = r2.matcher(line);
                if (m2.find()) {

                    tokens.add(m2.group(1).trim());
                    tokens.add(m2.group(3).trim());

                    String[] parts = new String[30];
                    String Variables = new String(m2.group(2));

                    Variables = Variables.replaceAll("\\(", "");
                    Variables = Variables.replaceAll("\\)", "");
                    parts = Variables.split(",");
                    flag2 = 0;
                    for (int k = 0; k < parts.length; k++) {
                        flag2 = find(parts[k]);
                        if (flag2 == 0) {
                            System.out.println("Undefined Variable read or write" + parts[k]);
                            return;
                        }
                    }

                    for (int i = 0; i < parts.length; i++) {
                        if (!parts[i].isEmpty()) {
                            if (!parts[i].matches("\\s*")) {
                                tokens.add(parts[i]);
                            }
                        }

                    }

                    tokens.add(m2.group(5));
                    tokens.add(m2.group(6));
                    for (int i = 1; i < 3; i++) {
                        line = line.replaceFirst(m2.group(i), "");
                    }
                    line = line.replaceFirst("\\(", "");

                    line = line.replaceFirst("\\)", "");
                     line = line.replaceFirst(";", "");

                }
            } else if (line.startsWith("FOR")) {
                Pattern r2 = Pattern.compile(Regex.FOR);
                Matcher m2 = r2.matcher(line);
                if (m2.find()) {
                    flag2 = 0;
                    flag2 = find(m2.group(2));
                    if (flag2 == 0) {
                        System.out.println("Undefined Variable3" + m2.group(2));
                        return;
                    }
                    for (int i = 1; i < 9; i++) {
                        tokens.add(m2.group(i));
                    }
                    for (int i = 1; i < 9; i++) {
                        line = line.replaceFirst(m2.group(i), "");
                    }

                }
            } else if (line.startsWith("END") && line.length() > 4) {
                Pattern r2 = Pattern.compile(Regex.END);
                Matcher m2 = r2.matcher(line);
                if (m2.find()) {
                    tokens.add(m2.group(1));

                    line = line.replaceFirst(m2.group(1), "");

                }
            } else {
                Pattern r2 = Pattern.compile(Regex.ASSIGN);
                Matcher m2 = r2.matcher(line);
                if (m2.find()) {
                    flag2 = 0;
                    flag2 = find(m2.group(1));
                    if (flag2 == 0) {
                        System.out.println("Undefined Variable1" + m2.group(1));
                        return;
                    }
                    tokens.add(m2.group(1).trim());
                    tokens.add(m2.group(2).trim());
                    for (int i = 1; i < 3; i++) {
                        line = line.replaceFirst(m2.group(i), "");
                    }
                    int i = 0;
                    while (line.charAt(i) != ';') {
                        flag2 = 0;
                        int j = 0;
                        String temp2 = new String();
                        temp2 = "";
                        if (line.charAt(0) == '(')
                        {temp2 += line.charAt(0);
                            tokens.add(temp2);
                            line = line.replaceFirst("\\(", "");}
                        temp2 = "";
                        while (line.charAt(j) != '+' && line.charAt(j) != '*' && line.charAt(j) != ';' && line.charAt(j) != '(' && line.charAt(j) != ')') {
                            temp2 += line.charAt(j);
                            j++;
                        }
                       if(!temp2.isEmpty()) { flag2 = find(temp2);
                        if (flag2 == 0) {
                            System.out.println("Undefined Variable2" + temp2);
                            return;
                        }
                        tokens.add(temp2);
                        line = line.replaceFirst(temp2, ""); }
                        j = 0;
                        temp2 = "";
                        if (line.charAt(0) == '+') {
                            temp2 += line.charAt(0);
                            tokens.add(temp2);
                            line = line.replaceFirst("\\+", "");

                        } else if (line.charAt(0) == '*') {
                            temp2 += line.charAt(0);
                            tokens.add(temp2);
                            line = line.replaceFirst("\\*", "");
                        

                        } else if (line.charAt(0) == ')') {
                            temp2 += line.charAt(0);
                            tokens.add(temp2);
                            line = line.replaceFirst("\\)", "");
                        }

                    }
                    tokens.add(m2.group(5));
                    line = line.replaceFirst(m2.group(5), "");
                }
            }
        }
        //}
        tokens.add(line);

        CG.createLines();
        CG.createFile(args[0]);

        long end = System.currentTimeMillis();
        System.out.println(end - start + "ms");
    }
}
