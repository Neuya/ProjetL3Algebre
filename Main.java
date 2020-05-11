import java.util.*;

public class Main
{
  public static final String epsi = "";

  public static void main(String[] args) {

      String test = "program p1\nbegin\nz <- x ;\nx <- y ;\ny <- z\nend.";

      Automate autoEntier = Fonctions.makeAutomateEntier();
      Automate autoIdent = Fonctions.makeAutomateIdent();


      System.out.println(Fonctions.TransformAlgo(autoEntier,autoIdent,test));



      Grammaire gram = new Grammaire("S");


      /*gram.defineTerm(Arrays.asList("a","b"));
      gram.defineNonTerm(Arrays.asList("A","B","S"));
      Map<String,List<String>> mapTest = new HashMap<String,List<String>>();
      mapTest.put("S",Arrays.asList("AaAb","BbBa"));
      mapTest.put("A",Arrays.asList(epsi));
      mapTest.put("B",Arrays.asList(epsi));*/
      gram.defineTerm(Arrays.asList("a","b"));
    //  gram.defineNonTerm(Arrays.asList("S"));
      Map<String,List<String>> mapTest = new HashMap<String,List<String>>();
      mapTest.put("S",Arrays.asList("aSbS","bSaS",epsi));

      gram.defineReglesProd(mapTest);
      gram.calculPremier();
      gram.calculSuivant();
      gram.printAllSuivant();
    //  gram.printAllEnsPremier();



  }
}
