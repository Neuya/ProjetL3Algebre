import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

public class Menu
{
  private Grammaire gram;

  public Menu()
  {
    gram = new Grammaire("");
  }
  public String printQuestion(String question)
  {
    System.out.println(question);
    Scanner sc = new Scanner(System.in);
    return sc.nextLine();
  }

  public boolean checkChoice(String n,int a,int b)
  {
    Integer in = Integer.parseInt(n.trim());
    return in>=a && in<=b;
  }

  public void menuPrincipal()
  {
    String choice="";
    do
    {
      choice = this.makeChoicePrincipal();
    }while(!checkChoice(choice,1,3));
    if(choice.equals("1"))
       gram = this.constructGrammaire();
    if(choice.equals("2"))
    {
      try{
        gram = this.constructGrammaireFile();
      }catch(Exception e){
        e.printStackTrace();
      }
    }
    System.out.println("Grammaire bien créée!");
    if(doWhileNotYn("Voulez vous afficher la grammaire?").equals("y"))
      gram.printGrammaire();

    gram.calculPremier();
    //gram.printAllPremier();
    gram.calculSuivant();
    //gram.printAllSuivant();
    gram.construireTableAnalyse();
      //gram.printTableAnalyse();
    this.menuGrammaire();
  }

  public void menuGrammaire()
  {
    String choice = this.makeChoiceGrammaire();
    while(!choice.equals("4"))
    {
      if(choice.equals("1"))
      {
        gram.printAllPremier();
        gram.printAllSuivant();
      }
      if(choice.equals("2"))
      {
        gram.printTableAnalyse();
      }
      if(choice.equals("3"))
      {
        String chaine = printQuestion("Entrez la chaîne à analyser : ");
        System.out.println(gram.analyseChaine(chaine)?"Cette chaîne appartient bien à L(grammaire)!\n":"Cette chaîne n'appartient pas à L(grammaire)...\n");
      }
      choice = this.makeChoiceGrammaire();
    }
  }

  public String makeChoiceGrammaire()
  {
    String question = "Quelle action souhaitez vous effectuer sur votre grammaire?\n";
    question+= "  1. Afficher les ensembles premiers et suivants\n";
    question+= "  2. Afficher la table d'analyse (générée à la construction de la grammaire)\n";
    question+= "  3. Analyser si une chaîne de caractères appartient à L(grammaire)\n";
    question+= "  4. Quitter le programme";
    return printQuestion(question);
  }

  public String makeChoicePrincipal()
  {
    return printQuestion("Que voulez vous faire?\n  1.Créer une grammaire manuellement\n  2.Créer une grammaire à partir du fichier Grammaire.txt\n  3.Analyser le programme ALGO.txt");
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

  public Grammaire constructGrammaireFile() throws Exception
  {
    File file = new File("Grammaire2.txt");

    BufferedReader br = new BufferedReader(new FileReader("Grammaire.txt"));

    String line;
    //axiome
    String st = br.readLine();
    Grammaire gram = new Grammaire(st);

    List<String> listTerm = new ArrayList<>();
    while(!st.equals("."))
    {
      st = br.readLine();
      if(!st.equals("."))
      listTerm.add(st);
    }

    List<String> listNonTerm = new ArrayList<>();
    Map<String,List<String>> mapRegles = new HashMap<>();
    st = br.readLine();
    while(!st.equals(".end"))
    {
      String nTerm = st.replace(" ->","").replace("->","").replace(" ","");
      listNonTerm.add(nTerm);
      st = br.readLine();
      List<String> listRules = new ArrayList<>();
      while(st.charAt(0)=='|')
      {
        String rule = st.substring(1).replace(" ","");
        rule = rule.equals("epsilon")?Grammaire.epsilon:rule;
        listRules.add(rule);
        st=br.readLine();
      }
      mapRegles.put(nTerm,listRules);
    }

    gram.defineTerm(listTerm);
    gram.defineNonTerm(listNonTerm);
    gram.defineReglesProd(mapRegles);
    return gram;
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
