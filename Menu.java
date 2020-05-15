import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

//Classe gérant un menu permettant une navigation à l'utilisateur
public class Menu
{
  private Grammaire gram;
  private Automate autoEntier = Fonctions.makeAutomateEntier();
  private Automate autoIdent = Fonctions.makeAutomateIdent();

  public Menu()
  {
    gram = new Grammaire("");
    autoEntier = Fonctions.makeAutomateEntier();
    autoIdent = Fonctions.makeAutomateIdent();
  }

  /**
  *Fonction permettant de print une question et de récupérer l'entrée clavier
  *@param String question la question
  *@return String la réponse
  */

  public String printQuestion(String question)
  {
    System.out.println(question);
    Scanner sc = new Scanner(System.in);
    return sc.nextLine();
  }

  /**
  *Fonction permettant de savoir si une réponse est bien comprise entre
  *une palette de choix
  *@param String n le choix
  *@param int a le premier choix possible
  *@param int b le dernier choix possible
  *@return true si bien compris entre a et b
  */

  public boolean checkChoice(String n,int a,int b)
  {
    Integer in = Integer.parseInt(n.trim());
    return in>=a && in<=b;
  }

  //Fonction permettant de print le menu principal
  //Différents choix sont proposés
  public void menuPrincipal()
  {
    String choice="";
    do
    {
      choice = this.makeChoicePrincipal();
    }while(!checkChoice(choice,1,4));
    if(choice.equals("1"))
       gram = this.constructGrammaire();
    if(choice.equals("2"))
    {
      String fichier ="";
      do {
         fichier = printQuestion("Choissisez le fichier souhaité :\n 1.Grammaire1.txt\n 2.Grammaire2.txt\n 3.Grammaire3.txt");
      } while (!checkChoice(fichier,1,3));
      try{
        String grammaire = "Grammaire"+fichier+".txt";
        System.out.println(grammaire);
        gram = this.constructGrammaireFile(grammaire);
      }catch(Exception e){
        e.printStackTrace();
      }
    }
    if(choice.equals("3"))
    {
      this.afficheTransformAlgoFile();
      this.menuPrincipal();
    }
    if(choice.equals("1") || choice.equals("2"))
    {
      System.out.println("Grammaire bien créée!");
      if(doWhileNotYn("Voulez vous afficher la grammaire?(y/n)").equals("y"))
        gram.printGrammaire();
      gram.calculPremier();
      gram.calculSuivant();
      gram.construireTableAnalyse();
      this.menuGrammaire();
    }
  }

  //Fonction permettant de print le menu d'une Grammaire
  //Toutes les actions possibles sur une grammaire y sont proposés

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
        String chYn = doWhileNotYn("Afficher les étapes de l'analyse? (y/n)");
        boolean affiche = chYn.equals("y")?true:false;
        System.out.println(gram.analyseChaine(chaine,affiche)?"Cette chaîne appartient bien à L(grammaire)!\n":"Cette chaîne n'appartient pas à L(grammaire)...\n");
      }
      choice = this.makeChoiceGrammaire();
    }
    System.exit(1);
  }

  //Fonctions de menu (print..)

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
    return printQuestion("\nQue voulez vous faire?\n  1.Créer une grammaire manuellement\n  2.Créer une grammaire à partir d'un fichier\n  3.Transformer (via automates) le programme ALGO.txt\n  4.Quitter");
  }


  /**
  *Fonction utile aux différents menus pour que l'utilisateur reponde par
  * y ou n (boucle tant que la réponse n'est pas oui ou non)
  *@param String la question
  *@return String la réponse
  */

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

  /**
  *Fonction permettant d'afficher la transformation depuis les reconnaissances
  *des automates du fichier ALGO.txt
  */

  public void afficheTransformAlgoFile()
  {
    try{
    InputStream is = new FileInputStream("ALGO.txt");
    BufferedReader buf = new BufferedReader(new InputStreamReader(is));

    String line = buf.readLine();
    StringBuilder sb = new StringBuilder();

    while(line != null){
      sb.append(line).append("\n");
      line = buf.readLine();
    }

    String fileAsString = sb.toString();
    System.out.println("\nProgramme : \n\n"+fileAsString+"\nAprès passage dans l'automate : \n");
    System.out.println(Fonctions.TransformAlgo(autoEntier,autoIdent,fileAsString));
  }catch(Exception e){}



  }

  /**
  *Fonction permettant de construire une grammaire depuis un fichier
  *@param String t le fichier en question
  *@return Grammaire la grammaire générée
  */

  public Grammaire constructGrammaireFile(String t) throws Exception
  {

    BufferedReader br = new BufferedReader(new FileReader(t));

    String line;
    //axiome
    String st = br.readLine();
    Grammaire gram = new Grammaire(st);

    //terminaux
    List<String> listTerm = new ArrayList<>();
    while(!st.equals("."))
    {
      st = br.readLine();
      if(!st.equals("."))
        listTerm.add(st);
    }

    //non terminaux et leurs regles
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
        String rule = st.substring(1).replaceAll(" ","");
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

  /**
  Fonction permettant de construire une grammaire pas à pas
  @return la Grammaire générée
  **/
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
  /**
  *Fonction qui permet de vérifier qu'un char est bien compris entre deux
  *@param char c le char à vérifier
  *@param char a la première borne
  *@param char z la deuxième borne
  *@return boolean true si a<c<z
  */

  public boolean checkChar(char c,char a,char z)
  {
    for(char i=a;i<=z;i++)
    {
      if(c==i)
        return true;
    }
    System.out.println("Entrée non correcte ! (non-terminal Majuscule) (terminal miniscule)");
    return false;
  }

  /**
  *Fonction utile pour la génération de grammaire, permet de checker
  *si la première lettre correspond au type d'entrée
  *@param String question la question
  *@param char a le premier choix
  *@param char e le dernier choix
  *@return String la réponse
  */

  public String doWhileNotCase(String question,char a,char z)
  {
    String ret = "";
    do {
      ret = printQuestion(question);
    } while (!checkChar(ret.charAt(0),a,z));

    return ret;
  }


  /**
    Fonction permettant d'ajouter des règles à un non terminal (utile à la génération pas à pas)
    @return List<String> la liste des règles
    @param String le non terminal
  */
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

}
