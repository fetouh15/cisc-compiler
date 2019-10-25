/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author fetouh
 */
public class CG {

    static ArrayList<String> backuplist = new ArrayList<String>(Compiler.tokens);
    static ArrayList<String> lines = new ArrayList<String>();
    static ArrayList<String> endinglines = new ArrayList<String>();
    static int[] loops = new int[10];
    static String sp = "\t\t\t";
    static String sk = "\t\t\t\t\t\t";
    static String assignstmt = "";
    static int TCount = 0, LCount = 0;
    static int firstInstruction = 4;
    static String lastloop = "";
    static int counter = 0;
static Stack st = new Stack();
static Stack st2 = new Stack();
static Stack st3 = new Stack();
    public static void start() {

        lines.add(Compiler.tokens.get(1) + sp + "START" + sp + "0");//0
        lines.add(sk + "EXTREF" + sp + "XREAD,XWRITE");//1
        lines.add(sk + "STL" + sp + "RETADR");//2
        lines.add(sk + "J" + sp + "{EXADDR}");//3
        lines.add("RETADR" + sp + "RESW" + sp + "1");//4
        endinglines.add(sk + "LDL" + sp + "RETADR");
        endinglines.add(sk + "RSUB");

        for (int i = 3; i < Compiler.tokens.indexOf("BEGIN"); i++) {
            lines.add(Compiler.tokens.get(i) + sp + "RESW" + sp + "1");
            firstInstruction++;
        }
        firstInstruction++;
        int i = 0;
        while (!Compiler.tokens.get(0).equals("BEGIN")) {
            Compiler.tokens.remove(0);
        }
        Compiler.tokens.remove(0);
    }

    public static void end() {
        lines.add(sk + "LDA" + sp + st.pop());
        lines.add(sk + "ADD" + sp + "#1");
        lines.add(sk + "J" + sp + "L" + st2.pop());

        loops[counter] = lines.size();
        counter++;
        Compiler.tokens.remove(0);
    }

    public static void forLoop() {

        int k = 0, nested = 0,imp=0;
        

            Compiler.tokens.remove(0);

            LCount++;
            st2.push(LCount);
            lines.add(sk + "LDA" + sp + "#" + Compiler.tokens.get(2)
            );
            lines.add("L" + Integer.toString(LCount) + sp + "STA" + sp + Compiler.tokens.get(0));
            if (Compiler.tokens.get(4).matches("\\d+")) {
                lines.add(sk + "COMP" + sp + "#" + Compiler.tokens.get(4));
            } else {
                lines.add(sk + "COMP" + sp + Compiler.tokens.get(4));
            }
            LCount++;
            st3.push(LCount);
            lines.add(sk + "JGT" + sp + "L" + Integer.toString(LCount));

           // lastloop = Compiler.tokens.get(0);
           st.push(Compiler.tokens.get(0));
          
        
        for (int i = 0; i < 7; i++) {
            Compiler.tokens.remove(0);
        }

    }

    public static void createLines() throws FileNotFoundException, UnsupportedEncodingException {

        start();

        int i = 0;

        while (!Compiler.tokens.get(0).equals("END.")) {
            if (Compiler.tokens.get(0).equalsIgnoreCase("READ")) {//braces stopping point
                ArrayList<String> Variables = new ArrayList<String>();
                lines.add(sk + "+JSUB" + sp + "XREAD");

                Compiler.tokens.remove(0);
                Compiler.tokens.remove(0);
                i = 0;
                while (!Compiler.tokens.get(i).equalsIgnoreCase(")")) {

                    Variables.add(Compiler.tokens.get(i));
                    i++;

                }
                i = 0;
                while (!Compiler.tokens.get(0).equalsIgnoreCase(")")) {
                    Compiler.tokens.remove(0);

                }
                Compiler.tokens.remove(0);
                Compiler.tokens.remove(0);
                lines.add(sk + "WORD" + sp + Integer.toString(Variables.size()));
                for (i = 0; i < Variables.size(); i++) {
                    lines.add(sk + "WORD" + sp + Variables.get(i));
                }

            } else if (Compiler.tokens.get(0).equalsIgnoreCase("WRITE")) {//braces stopping point
                ArrayList<String> Variables = new ArrayList<String>();
                lines.add(sk + "+JSUB" + sp + "XWRITE");
                // tokenCount += 2;

                Compiler.tokens.remove(0);
                Compiler.tokens.remove(0);
                i = 0;
                while (!Compiler.tokens.get(i).equalsIgnoreCase(")")) {

                    Variables.add(Compiler.tokens.get(i));
                    i++;
                    // tokenCount++;
                }
                //tokenCount++;

                i = 0;
                while (!Compiler.tokens.get(0).equalsIgnoreCase(")")) {
                    Compiler.tokens.remove(0);

                }
                Compiler.tokens.remove(0);
                Compiler.tokens.remove(0);
                lines.add(sk + "WORD" + sp + Integer.toString(Variables.size()));
                for (i = 0; i < Variables.size(); i++) {
                    lines.add(sk + "WORD" + sp + Variables.get(i));
                }

            } else if (Compiler.tokens.get(0).equalsIgnoreCase("FOR")) {
                forLoop();
            } else if (Compiler.tokens.get(0).equalsIgnoreCase("END")) {
                end();
            } else {
                ArrayList<String> Variables = new ArrayList<String>();
                i = 0;
                while (!Compiler.tokens.get(i).equalsIgnoreCase(";")) {

                    Variables.add(Compiler.tokens.get(i));
                    i++;

                }
                Variables.add(Compiler.tokens.get(i));
                i = 0;
                while (!Compiler.tokens.get(0).equalsIgnoreCase(";")) {

                    Compiler.tokens.remove(0);

                }
                Compiler.tokens.remove(0);
                if (Variables.size() == 4) {
                    if (Variables.get(2).matches("\\d+")) {
                        lines.add(sk + "LDA" + sp + "#" + Variables.get(2));        // ASSGN VAR TO VAR OR INT TO VAR

                    } else {
                        lines.add(sk + "LDA" + sp + Variables.get(2));
                    }

                    lines.add(sk + "STA" + sp + Variables.get(0));
                } else {
                    int plusflag = 0, timesflag = 0;
                    int bracketflagL = 0, bracketflagR = 0;
                    int duplicates = 0;
                    i = 0;

                    while (!Variables.get(i).equals(";")) {
                        if (Variables.get(i).equals("+")) {
                            plusflag = 1;
                        }
                        if (Variables.get(i).equals("*")) {
                            timesflag = 1;
                        }
                        if (Variables.get(i).equals("(")) {
                            bracketflagL = 1;
                        }
                        if (Variables.get(i).equals(")")) {
                            bracketflagR = 1;
                        }

                        i++;
                    }
                    if (bracketflagR == 1 && bracketflagL == 0) {
                        System.out.println("brackets error");
                    } else if (bracketflagR == 0 && bracketflagL == 1) {
                        System.out.println("brackets error");
                    } else if (bracketflagR == 1 && bracketflagL == 1) {
                        if ((Variables.indexOf("(") == Variables.indexOf(":=") + 1) && ((Variables.indexOf(")") == Variables.indexOf(";") - 1))) {
                            Variables.remove("(");
                            Variables.remove(")");
                        } else if (plusflag == 0 && timesflag == 1) {
                            Variables.remove("(");
                            Variables.remove(")");
                        } else if (plusflag == 1 && timesflag == 0) {
                            Variables.remove("(");
                            Variables.remove(")");
                        } else {
                            int k = 1;
                            if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                lines.add(sk + "LDA" + sp + "#" + Variables.get(Variables.indexOf("(") + k));

                            } else {
                                lines.add(sk + "LDA" + sp + Variables.get(Variables.indexOf("(") + k));
                            }
                            k++;
                            if (Variables.get(Variables.indexOf("(") + k).matches("\\+")) {
                                k++;
                                if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                    lines.add(sk + "ADD" + sp + "#" + Variables.get(Variables.indexOf("(") + k));
                                } else {
                                    lines.add(sk + "ADD" + sp + Variables.get(Variables.indexOf("(") + k));
                                }

                            } else if (Variables.get(Variables.indexOf("(") + k).matches("\\*")) {
                                k++;
                                if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                    lines.add(sk + "MUL" + sp + "#" + Variables.get(Variables.indexOf("(") + k));
                                } else {
                                    lines.add(sk + "MUL" + sp + Variables.get(Variables.indexOf("(") + k));
                                }

                            }
                            k = k + 2;
                            if (Variables.get(Variables.indexOf("(") + k).matches("\\+")) {
                                k++;
                                if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                    lines.add(sk + "ADD" + sp + "#" + Variables.get(Variables.indexOf("(") + k));
                                } else {
                                    lines.add(sk + "ADD" + sp + Variables.get(Variables.indexOf("(") + k));
                                }

                            } else if (Variables.get(Variables.indexOf("(") + k).matches("\\*")) {
                                k++;
                                if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                    lines.add(sk + "MUL" + sp + "#" + Variables.get(Variables.indexOf("(") + k));
                                } else {
                                    lines.add(sk + "MUL" + sp + Variables.get(Variables.indexOf("(") + k));
                                }

                            } else if (Variables.get(Variables.indexOf("(") + k).matches(";")) {
                                k = -1;

                                if (Variables.get(Variables.indexOf("(") + k).matches("\\+")) {
                                    k--;
                                    if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                        lines.add(sk + "ADD" + sp + "#" + Variables.get(Variables.indexOf("(") + k));
                                    } else {
                                        lines.add(sk + "ADD" + sp + Variables.get(Variables.indexOf("(") + k));
                                    }

                                } else if (Variables.get(Variables.indexOf("(") + k).matches("\\*")) {
                                    k--;
                                    if (Variables.get(Variables.indexOf("(") + k).matches("\\d+")) {
                                        lines.add(sk + "MUL" + sp + "#" + Variables.get(Variables.indexOf("(") + k));
                                    } else {
                                        lines.add(sk + "MUL" + sp + Variables.get(Variables.indexOf("(") + k));
                                    }

                                }

                            }
                            lines.add(sk + "STA" + sp + Variables.get(0));
                            continue;
                        }
                    }
                    //skip                 
                    if (plusflag == 0 && timesflag == 0) {
                        System.out.println("error this shouldnt happen");
                    } else if (plusflag == 1 && timesflag == 0) { //ADDfunction
                        i = 2;
                        while (!Variables.get(i).equals(";")) {
                            if (Variables.get(0).equals(Variables.get(i))) {
                                duplicates = i;
                                break;
                            }
                            i++;
                        }

                        if (duplicates != 0) {
                            lines.add(sk + "LDA" + sp + Variables.get(duplicates));
                            i = 2;
                            while (i < Variables.size()) {
                                if (!Variables.get(i).equals(Variables.get(duplicates))) {
                                    if (Variables.get(i).matches("\\d+")) {
                                        lines.add(sk + "ADD" + sp + "#" + Variables.get(i));

                                    } else {
                                        lines.add(sk + "ADD" + sp + Variables.get(i));
                                    }

                                } else {
                                    duplicates = 1;
                                }

                                i = i + 2;

                            }
                        } else {

                            if (Variables.get(2).matches("\\d+")) {
                                lines.add(sk + "LDA" + sp + "#" + Variables.get(2));

                            } else {
                                lines.add(sk + "LDA" + sp + Variables.get(2));
                            }
                            i = 4;

                            while (i < Variables.size()) {
                                if (Variables.get(i).matches("\\d+")) {
                                    lines.add(sk + "ADD" + sp + "#" + Variables.get(i));

                                } else {
                                    lines.add(sk + "ADD" + sp + Variables.get(i));
                                }
                                i = i + 2;
                            }
                        }
                        lines.add(sk + "STA" + sp + Variables.get(0));
                    } else if (plusflag == 0 && timesflag == 1) {
                        i = 2;                                          //MULTIPLY FUNCTION
                        while (!Variables.get(i).equals(";")) {
                            if (Variables.get(0).equals(Variables.get(i))) {
                                duplicates = i;
                                break;
                            }
                            i++;
                        }

                        if (duplicates != 0) {
                            lines.add(sk + "LDA" + sp + Variables.get(duplicates));
                            i = 2;
                            while (i < Variables.size()) {
                                if (!Variables.get(i).equals(Variables.get(duplicates))) {
                                    if (Variables.get(i).matches("\\d+")) {
                                        lines.add(sk + "MUL" + sp + "#" + Variables.get(i));

                                    } else {
                                        lines.add(sk + "MUL" + sp + Variables.get(i));
                                    }

                                } else {
                                    duplicates = 1;
                                }

                                i = i + 2;

                            }
                        } else {

                            if (Variables.get(2).matches("\\d+")) {
                                lines.add(sk + "LDA" + sp + "#" + Variables.get(2));

                            } else {
                                lines.add(sk + "LDA" + sp + Variables.get(2));
                            }
                            i = 4;
                            while (i < Variables.size()) {
                                if (Variables.get(i).matches("\\d+")) {
                                    lines.add(sk + "MUL" + sp + "#" + Variables.get(i));

                                } else {
                                    lines.add(sk + "MUL" + sp + Variables.get(i));
                                }
                                i = i + 2;
                            }
                        }
                        lines.add(sk + "STA" + sp + Variables.get(0));
                    } else if (plusflag == 1 && timesflag == 1) {
                        i = 2;                                  //MULTIPLY AND PLUS
                        int timescount = 0;
                        while (!Variables.get(i).equals(";")) {
                            if (Variables.get(i).equals("*")) {
                                timescount++;

                            }
                            i++;
                        }

                        if (timescount == 0) {
                            System.out.println("error no times signs");
                        } else if (timescount == 1) {
                            if (Variables.get((Variables.indexOf("*") - 1)).matches("\\d+")) {
                                lines.add(sk + "LDA" + sp + "#" + Variables.get(Variables.indexOf("*") - 1));
                            } else {
                                lines.add(sk + "LDA" + sp + Variables.get(Variables.indexOf("*") - 1));
                            }
                            if (Variables.get((Variables.indexOf("*") + 1)).matches("\\d+")) {
                                lines.add(sk + "MUL" + sp + "#" + Variables.get(Variables.indexOf("*") + 1));

                            } else {
                                lines.add(sk + "MUL" + sp + Variables.get(Variables.indexOf("*") + 1));
                            }
                            i = 2;
                            while (i < Variables.size()) {
                                if ((i != Variables.indexOf("*") + 1) && (i != Variables.indexOf("*")) && (i != Variables.indexOf("*") - 1)) {
                                    if (Variables.get(i).matches("\\d+")) {
                                        lines.add(sk + "ADD" + sp + "#" + Variables.get(i));

                                    } else {
                                        lines.add(sk + "ADD" + sp + Variables.get(i));
                                    }
                                }
                                i = i + 2;
                            }

                        } else {

                            while (Variables.contains("*")) {

                                int j = 2;

                                if (Variables.get(Variables.indexOf("*") - 1).matches("\\d+")) {
                                    lines.add(sk + "LDA" + sp + "#" + Variables.get(Variables.indexOf("*") - 1));

                                } else {
                                    lines.add(sk + "LDA" + sp + Variables.get(Variables.indexOf("*") - 1));
                                }
                                if (Variables.get((Variables.indexOf("*") + 1)).matches("\\d+")) {
                                    lines.add(sk + "MUL" + sp + "#" + Variables.get(Variables.indexOf("*") + 1));

                                } else {
                                    lines.add(sk + "MUL" + sp + Variables.get(Variables.indexOf("*") + 1));
                                }

                                while (!Variables.get(Variables.indexOf("*") + j).equals("+") && !Variables.get(Variables.indexOf("*") + j).equals(";")) {
                                    if (Variables.get((Variables.indexOf("*") + j + 1)).matches("\\d+")) {
                                        lines.add(sk + "MUL" + sp + "#" + Variables.get(Variables.indexOf("*") + j + 1));

                                    } else {
                                        lines.add(sk + "MUL" + sp + Variables.get(Variables.indexOf("*") + j + 1));
                                    }
                                    j = j + 2;

                                }
                                j--;
                                j = Variables.indexOf("*") + j;
                                while (j != Variables.indexOf("*")) {
                                    Variables.remove(j);
                                    j--;

                                }
                                Variables.remove(j);
                                j--;
                                TCount++;
                                Variables.set(j, "T" + Integer.toString(TCount));
                                lines.add(sk + "STA" + sp + "T" + Integer.toString(TCount));
                                endinglines.add("T" + Integer.toString(TCount) + sp + "RESW" + sp + "1");

                            }
                            i = 2; //ADD
                            while (!Variables.get(i).equals(";")) {
                                if (Variables.get(0).equals(Variables.get(i))) {
                                    duplicates = i;
                                    break;
                                }
                                i++;
                            }

                            if (duplicates != 0) {
                                lines.add(sk + "LDA" + sp + Variables.get(duplicates));
                                i = 2;
                                while (i < Variables.size()) {
                                    if (!Variables.get(i).equals(Variables.get(duplicates))) {
                                        if (Variables.get(i).matches("\\d+")) {
                                            lines.add(sk + "ADD" + sp + "#" + Variables.get(i));

                                        } else {
                                            lines.add(sk + "ADD" + sp + Variables.get(i));
                                        }

                                    } else {
                                        duplicates = 1;
                                    }

                                    i = i + 2;

                                }
                            } else {

                                if (Variables.get(2).matches("\\d+")) {
                                    lines.add(sk + "LDA" + sp + "#" + Variables.get(2));

                                } else {
                                    lines.add(sk + "LDA" + sp + Variables.get(2));
                                }
                                i = 4;

                                while (i < Variables.size()) {
                                    if (Variables.get(i).matches("\\d+")) {
                                        lines.add(sk + "ADD" + sp + "#" + Variables.get(i));

                                    } else {
                                        lines.add(sk + "ADD" + sp + Variables.get(i));
                                    }
                                    i = i + 2;
                                }
                            }

                        }
                        lines.add(sk + "STA" + sp + Variables.get(0));
                    }
                }
            }
        }
        endinglines.add(sk + "END");
        lines.set(firstInstruction, "{EXADDR}" + sp + lines.get(firstInstruction));

        for (i = 0; i < endinglines.size(); i++) {

            lines.add(endinglines.get(i));
        }
        for (i = 0; i < counter; i++) {
            lines.set(loops[i], "L" + st3.pop() + sp + lines.get(loops[i]));
        }
    }

    public static void createFile(String x) throws FileNotFoundException, UnsupportedEncodingException {
        // x = x.replaceAll("\\.txt", "");
        if (x.endsWith(".txt")) {
            x = x.replaceAll("\\.txt", "");
        } else if (x.endsWith(".pas")) {
            x = x.replaceAll("\\.pas", "");
        }
        PrintWriter writer = new PrintWriter(x + ".asm");
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, lines.get(i).replaceAll("\\s+", "\t\t\t"));

        }
        for (int i = 0; i < lines.size(); i++) {
            writer.println(lines.get(i));

        }
        writer.close();

    }

}
