import java.util.*;

public class Main
{
  public static final String epsi = "";

  public static void main(String[] args) {

      String test = "program p1\nbegin\nz <- x ;\nx <- y ;\ny <- z\nend.";

      /*Grammaire gram = new Grammaire("S");


      gram.defineTerm(Arrays.asList("a","b"));
      gram.defineNonTerm(Arrays.asList("A","B","S"));
      Map<String,List<String>> mapTest = new HashMap<String,List<String>>();
      mapTest.put("S",Arrays.asList("AaAb","BbBa"));
      mapTest.put("A",Arrays.asList(epsi));
      mapTest.put("B",Arrays.asList(epsi));*/
      /*gram.defineTerm(Arrays.asList("a","b"));
      gram.defineNonTerm(Arrays.asList("S",));
      Map<String,List<String>> mapTest = new HashMap<String,List<String>>();
      mapTest.put("S",Arrays.asList("aSbS","bSaS",epsi));*/

      /*String t = "E";
      System.out.println("START"+t.startsWith("E'"));
      System.out.println("SUB "+t.substring(0,0));

      Grammaire gram = new Grammaire("E");
      gram.defineTerm(Arrays.asList("x","0","1","+","*","(",")"));
      gram.defineNonTerm(Arrays.asList("E","R","T","V","F"));
      Map<String,List<String>> mapTest = new HashMap<String,List<String>>();
      mapTest.put("E",Arrays.asList("TR"));
      mapTest.put("R",Arrays.asList("+TR",Grammaire.epsilon));
      mapTest.put("T",Arrays.asList("FV"));
      mapTest.put("V",Arrays.asList("*FV",Grammaire.epsilon));
      mapTest.put("F",Arrays.asList("x","0","1","(E)"));

      gram.defineReglesProd(mapTest);
      gram.printGrammaire();
      gram.calculPremier();
      gram.printAllPremier();
      gram.calculSuivant();
      gram.printAllSuivant();
      gram.construireTableAnalyse();
      gram.printTableAnalyse();
      System.out.println("ANALYSE : "+gram.analyseChaine("x*(x+1)",true));*/
      Menu menu = new Menu();

      menu.menuPrincipal();
    }



  }
