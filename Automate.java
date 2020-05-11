import java.util.*;

public class Automate{

  private List<Etat> etatsInitiaux;
  private List<Etat> etats;

  public Automate()
  {
    this.etats = new ArrayList<>();
    this.etatsInitiaux = new ArrayList<>();
  }

  public Automate(Etat etatInit)
  {
    this.etats=new ArrayList<>();
    this.etatsInitiaux = new ArrayList<>();
    this.etatsInitiaux.add(etatInit);
    this.constructAutomate();
  }

  public Automate(List<Etat> etatsInit)
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
    for(int i=0;i<this.etatsInitiaux.size();i++)
    {
      this.addAllSuivants(etatsInitiaux.get(i));
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
        System.out.println("non reconnu");
        return false;
      }
    }
    System.out.println("Plus de characteres, Etat Final : "+etatcourant.getNum());
    return etatcourant.isFinal();
  }

}
