import java.util.*;

public class Automate{

  private List<Etat> etatsInitiaux;
  private List<Etat> etats;

  public Automate()
  {
    this.etats = new ArrayList<>();
    this.etatsInitiaux = new ArrayList<>();
  }

  public void addEtat(Etat e)
  {
    this.etats.add(e);
  }

  public void constructAutomate(List<Etat> etatsInit)
  {
    this.etatsInitiaux = etatsInit;
    for(int i=0;i<etatsInit.size();i++)
    {
      //this.etats.add(etatsInit.get(i));
      this.addAllSuivants(etatsInit.get(i));
    }
  }

  public void addAllSuivants(Etat e)
  {
    this.etats.add(e);
    if(!e.isFinal())
    {
      for(Etat etatsSuivants : e.getAllVoisins())
      {
        if(!etatsSuivants.isContained(etats))
          addAllSuivants(etatsSuivants);
      }
    }
  }

  public void addEtatList(List<Etat> etatsToLoad)
  {
    for(int i=0;i<etatsToLoad.size();i++)
    {
      this.etats.add(etatsToLoad.get(i));
    }
  }

  public boolean checkStr(String str)
  {
    char[] tabChar = str.toCharArray();
    Etat etatcourant = etatsInitiaux.get(0);

    for(int i=0;i<tabChar.length;i++)
    {
      System.out.println("Lettre Courante : "+tabChar[i]+" Etat : "+etatcourant.getNum());
      if(etatcourant.isInTransitions(tabChar[i]))
      {
        etatcourant = etatcourant.retSuivant(tabChar[i]);
        System.out.println("====> "+etatcourant.getNum());
      }
      else
      {
        return false;
      }
    }
    System.out.println("Plus de characteres, Etat Final : "+etatcourant.getNum());
    return etatcourant.isFinal();
  }

}
