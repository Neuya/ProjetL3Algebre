import java.util.*;

public class Fonctions
{
  //Fonction pour construire un automate reconnaissant les entiers
  public static Automate makeAutomateEntier()
  {
    Etat e1 = new Etat(1);
    Etat e2 = new Etat(2);
    Etat e3 = new Etat(3);
    Etat e4 = new Etat(4);
    e1.addSuivantTransition('0',e2);
    e4.addSuivantTransition('0',e4);
    e1.addSuivantTransition('-',e3);
    for(char i='1';i<='9';i++){
      e1.addSuivantTransition(i,e4);
      e3.addSuivantTransition(i,e4);
      e4.addSuivantTransition(i,e4);
    }
    e2.makeFinal();
    e4.makeFinal();
    Automate automate = new Automate(e1);
    return automate;
  }
  //Fonction pour construire un automate reconnaissant les identificateurs
  public static Automate makeAutomateIdent()
  {
    Etat q1 = new Etat(1);
    Etat q2 = new Etat(2);
    for(char i='a';i<='z';i++)
    {
      q1.addSuivantTransition(i,q2);
      q2.addSuivantTransition(i,q2);
    }
    for(char j='0';j<='9';j++)
    {
      q2.addSuivantTransition(j,q2);
    }
    q2.makeFinal();
    Automate automateI = new Automate(q1);
    return automateI;
  }

  //La liste des mots clés
  public static List<String> listKeyWords = Arrays.asList("program","end.","while","for","begin","true","then","false","break","if","do","not","or","and","from","end");


  /**
  *Fonction qui transforme un programme en une chaine analysable
  *@return String le programme transformé
  *@param autoEntier et autoIdent les deux automates
  *@param String programme le programme à analyser
  */
  public static String TransformAlgo(Automate autoEntier,Automate autoIdent,String programme)
  {
    StringTokenizer strTok = new StringTokenizer(programme);
    String stringRet = "";
    String stringCur;
    while(strTok.hasMoreTokens())
    {
      //System.out.println(strTok.nextToken());
      stringCur = strTok.nextToken();
      //System.out.println("Analyse de "+stringCur);
      if(!listKeyWords.contains(stringCur))
      {
        if(autoEntier.checkStr(stringCur))
          stringCur = "entier";
        else if(autoIdent.checkStr(stringCur))
          stringCur = "ident";
      }
      stringRet += stringCur + " ";
    }
    return stringRet.replace(" ; ",";");
  }
}
