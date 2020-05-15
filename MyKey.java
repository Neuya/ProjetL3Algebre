/*
Classe utile à Grammaire pour créer une clé
nonTerminal/Terminal pour la map de la table d'analyse
*/

public class MyKey
{
    private String nonTerm; //Non terminal
    private String term; //Terminal

    public MyKey(String a,String b)
    {
      this.nonTerm = a;
      this.term = b;
    }

    //Deux fonctions utiles pour la reconnaissance via
    //Une map

    @Override
    public int hashCode(){
      return 1;
    }

    @Override
    public boolean equals(Object o)
    {
      MyKey m = (MyKey) o;
      return this.nonTerm.equals(m.nonTerm) && this.term.equals(m.term);
    }

    public String getTerm()
    {
      return this.term;
    }

    public String getNonTerm()
    {
      return this.nonTerm;
    }
}
