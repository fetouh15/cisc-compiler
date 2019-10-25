/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;



/**
 *
 * @author fetouh
 */
public class TokenTable {
    public static final String[] tokentable= new String[18];
   public static void create(){
    tokentable[0] = "PROGRAM";
    tokentable[1] ="VAR";
    tokentable[2] ="BEGIN";
    tokentable[3] ="END";
            tokentable[4] ="END.";
            tokentable[5] ="FOR";
            tokentable[6] ="READ";
            tokentable[7] ="WRITE";
            tokentable[8] ="TO";
            tokentable[9] ="DO";
            tokentable[10] =";";
            tokentable[11] =":=";
            tokentable[12] ="+";
            tokentable[13] ="=";
            tokentable[14] ="(";
            tokentable[15] =")";
            tokentable[16] ="id";
            tokentable[17] ="*";
           
           
   
   }
  public static int findMatch(String X){ 
      int flag=0;
      for(int i=0;i<tokentable.length;i++)
  
      {
      
      if(X.equalsIgnoreCase(tokentable[i]))
      {
      return flag=1;
      }}
  
  
  return flag=0;
  }
  
}
