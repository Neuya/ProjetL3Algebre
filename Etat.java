import java.util.*;

/*
Classe représentant un état utile pour la classe Automate
*/
public class Etat
{
  private int num; //Numéro de l'état
  //Map servant à stocker les couples Character/Etat pour les différentes transitions
  private Map<Character,Etat> mapTransitionEtat;
  private boolean isFinal;

  public Etat(int i)
  {
    this.num = i;
    this.mapTransitionEtat = new HashMap<Character,Etat>();
    this.isFinal = false;
  }

  public void addSuivantTransition(char t,Etat e)
  {
    this.mapTransitionEtat.put(t,e);
  }

  /*
    Retourne tous les états voisins
    (ie) s'il existe une transition
  */

  public List<Etat> getAllVoisins()
  {
    List<Etat> listRet = new ArrayList<Etat>();
    for(char t : mapTransitionEtat.keySet())
    {
      listRet.add(mapTransitionEtat.get(t));
    }
    return listRet;
  }

  /*
  Fonction pour savoir si un état est contenu dans une liste d'états
  */

  public boolean isContained(List<Etat> list)
  {
    boolean cont = false;
    for(Etat etatList : list)
    {
      if(this.equals(etatList))
      {
        cont = true;
      }
    }

    return cont;
  }

  public int getNum()
  {
    return this.num;
  }

  public boolean isFinal()
  {
    return isFinal;
  }

  public void makeFinal()
  {
    this.isFinal = true;
  }

  public boolean isInTransitions(char s)
  {
    return this.mapTransitionEtat.containsKey(s);
  }

  public Etat retSuivant(char s)
  {
    return this.mapTransitionEtat.get(s);
  }

  public boolean equals(Etat e)
  {
    return this.num == e.num;
  }
}
