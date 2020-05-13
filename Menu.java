import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class Menu
{
  public String printQuestion(String question)
  {
    System.out.println(question);
    Scanner sc = new Scanner(System.in);
    return sc.nextLine();
  }

  public String makeChoice()
  {
    return printQuestion("Que voulez vous faire? \n1.Créer une grammaire manuellement \n2.Créer une grammaire à partir du fichier Grammaire.txt \n3.Analyser le programme ALGO.txt");
  }

  public boolean checkChar(char c,char a,char z)
  {
    for(char i=a;i<z;i++)
    {
      if(c==i)
        return true;
    }
    System.out.println("Entrée non correcte ! (non-terminal Majuscule) (terminal miniscule)");
    return false;
  }

  public String doWhileNotCase(String question,char a,char z)
  {
    String ret = "";
    do {
      ret = printQuestion(question);
    } while (!checkChar(ret.charAt(0),a,z));

    return ret;
  }

  public String doWhileNotYn(String question)
  {
    String ret="";

    do {
      ret = printQuestion(question);
      if(!(ret.equals("y") || ret.equals("n")))
        System.out.println("Veuillez répondre par 'y' ou 'n'");
    } while (!(ret.equals("y") || ret.equals("n")));

    return ret;
  }

  public List<String> ajoutProdRule(String nonT)
  {
    List<String> rules = new ArrayList<String>();
    String term="";
    String reponse="";
    do {
      term = printQuestion("Regle pour "+nonT+" ('epsilon' pour mot vide) : ");
      rules.add(term.equals("epsilon")?Grammaire.epsilon:term);
      reponse = doWhileNotYn("Ajouter d'autres règles? (y/n)");
    } while (reponse.equals("y"));

    return rules;
  }

  public Grammaire constructGrammaire()
  {
    //Création Grammaire
    String term="";
    String reponse="";
    String axiome = doWhileNotCase("Definir un non terminal pour axiome :",'A','Z');
    Grammaire gram = new Grammaire(axiome);

    //Ajout des terminaux
    List<String> listTerm = new ArrayList<String>();
    do {
      term = doWhileNotCase("Ajouter un terminal :",'a','z');
      listTerm.add(term);
      reponse = doWhileNotYn("Voulez vous ajouter d'autres terminaux? (y/n)");
      System.out.println("");
    } while (reponse.equals("y"));
    gram.defineTerm(listTerm);

    //Ajout des non terminaux
    List<String> listNonTerm = new ArrayList<String>();
    do {
      term = doWhileNotCase("Ajouter un non-terminal :",'A','Z');
      listNonTerm.add(term);
      reponse = doWhileNotYn("Voulez vous ajouter d'autres non-terminaux? (y/n)");
      System.out.println("");
    } while (reponse.equals("y"));
    gram.defineNonTerm(listNonTerm);
    if(listNonTerm.contains(axiome))
      listNonTerm.remove(axiome);
    listNonTerm.add(0,axiome);

    //Ajout des règles de production
    Map<String,List<String>> prodRules = new HashMap<>();
    for(String nonT : listNonTerm)
    {
      List<String> listRules = ajoutProdRule(nonT);
      prodRules.put(nonT,listRules);
      System.out.println("");
    }
    gram.defineReglesProd(prodRules);


    System.out.println("Grammaire créée !");

    return gram;



  }

}
