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
public class trials {
    /*              String pattern = "(.+)";
               Pattern r = Pattern.compile(pattern);
               Matcher m = r.matcher(line);
                if (m.find( )) { System.out.println("Found value: " + m.group(0) );
                for(int i=0;i<TokenTable.tokentable.length;i++)
                {
                String pattern1 = "("+TokenTable.tokentable[i]+")";
                 String pattern2="(|[a-zA-Z]|[0-9]+)";
               Pattern r1= Pattern.compile(pattern1);
               Matcher m1 = r1.matcher(line);
               if (m1.find( )) { System.out.println("1Found value: " + m1.group(0) );  }
             if(m1.group(0).equalsIgnoreCase(TokenTable.tokentable[i])) {  tokens.add(m1.group(0));
                line=line.replaceFirst(m1.group(0), "");}
             else{r1= Pattern.compile(pattern2);
            m1 = r1.matcher(line);
            tokens.add(m1.group(0));}
             
                }
                
                
                
                
                }*/

 /*   while(!line.equals(""))
             {
             
             
             
              for(int i=0;i<TokenTable.tokentable.length;i++)
                { line=line.replaceFirst("\\s*","");
                String pattern1 = "("+TokenTable.tokentable[i]+")";
                 
               Pattern r1= Pattern.compile(backup);
               Matcher m1 = r1.matcher(line);
               if (m1.find( )) { //System.out.println("1Found value: " + m1.group(0) );  }
              
               if(m1.group(0).equalsIgnoreCase(line.substring(0, m1.group(0).length())))
              {
             tokens.add(m1.group(0));
               if(m1.group(0).equalsIgnoreCase("(")||m1.group(0).equalsIgnoreCase(")"))
               
               {String newStr="";
for(int x=1; x<line.length();x++)
{

newStr =newStr+line.charAt(x);
           }
line=newStr;
               } 
               else
             line=line.replaceFirst(m1.group(0), "");
                
              }
               else{
               
               String pattern2="\\s*(.*)\\s*"+m1.group(0);
                Pattern r2= Pattern.compile(pattern2);
               Matcher m2 = r2.matcher(line);
               
            if(m2.find()){  
                String []parts=new String[20];
               parts=m2.group(1).split(",");
               for(int j=0;j<parts.length;j++)
               {if(!parts[j].equals(""))
                   if(!parts[j].equals(" "))
                   {   parts[j]=parts[j].trim();
                       tokens.add(parts[j]);                      }
                   
                   }
                line=line.replaceFirst(m2.group(1), "");
           }
               
               }
             
            


    }
             }*/
            /*  while (!line.equals("") || line.equalsIgnoreCase("END.")) {
            flag2 = 0;            
            temp = "";
            line = line.replaceAll("\\s", "");
            for (int j = 0; j < 10; j++) {
                if (line.charAt(0) == TokenTable.tokentable[j].charAt(0)) {
                    flagmaybe = 1;
                    break;
                }
                
            }
            
            for (int i = 0; i < 10; i++) {
                temp += line.charAt(i);
                flag2 = TokenTable.findMatch(temp);
                if (flag2 == 1) {
                    tokens.add(temp);
                }
                
            }
            
        } */
    
}
