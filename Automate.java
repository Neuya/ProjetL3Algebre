import java.util.*;

public class Automate{

  private List<Etat> etatsInitiaux; //Liste des etats initiaux
  private List<Etat> etats; //Liste complète des états de l'automate

  public Automate()
  {
    this.etats = new ArrayList<>();
    this.etatsInitiaux = new ArrayList<>();
  }

  public Automate(Etat etatInit) //Constructeur avec un seul etat initial
  {
    this.etats=new ArrayList<>();
    this.etatsInitiaux = new ArrayList<>();
    this.etatsInitiaux.add(etatInit);
    this.constructAutomate();
  }

  public Automate(List<Etat> etatsInit) //Constructeur avec une liste d'etats initiaux
  {
    this.etats = new ArrayList<>();
    this.etatsInitiaux = etatsInit;
    this.constructAutomate();
  }

  public void addEtat(Etat e)
  {
    this.etats.add(e);
  }

  public void constructAutomate()
  {
    //On construit l'automate à partir de ses états etatsInitiaux
    //Grâce à la structure des états, tout ceci est automatisé
    for(int i=0;i<this.etatsInitiaux.size();i++)
    {
      this.addAllSuivants(etatsInitiaux.get(i));
    }
  }

  //Ajoute tous les états suivants d'un état donné
  public void addAllSuivants(Etat e)
  {
    this.etats.add(e);
    List<Etat> etatsSuivants = e.getAllVoisins();
    for(Etat etatsSuivant : etatsSuivants)
    {
      if(!etatsSuivant.isContained(etats))//Si la liste des états ne contient pas l'état
        addAllSuivants(etatsSuivant); //Recursion sur l'état et ses voisins
    }

  }

  //Ajoute une liste d'états à la liste des états de l'automate
  public void addEtatList(List<Etat> etatsToLoad)
  {
    for(int i=0;i<etatsToLoad.size();i++)
    {
      this.etats.add(etatsToLoad.get(i));
    }
  }

  /**
  *Fonction permettant de passer une chaine dans l'automate
  *@param String la Chaine
  *@return boolean true si la chaine est reconnue par l'automate
  */

  public boolean checkStr(String str)
  {
    char[] tabChar = str.toCharArray(); //On transforme la chaine en tableau de char
    Etat etatcourant = etatsInitiaux.get(0); //Etat initial

    for(int i=0;i<tabChar.length;i++)
    {
      if(etatcourant.isInTransitions(tabChar[i]))//S'il existe bien une transition avec ce caractere
        etatcourant = etatcourant.retSuivant(tabChar[i]); //On passe à l'état suivant
      else
        return false;
    }

    return etatcourant.isFinal(); //On check si l'état sur lequel on se trouve est bien final
  }

}
