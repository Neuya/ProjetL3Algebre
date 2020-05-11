import java.util.*;

public class Grammaire
{
  private List<String> termList;
  private List<String> nonTermList;
  private String axiome;
  private Map<String,List<String>> reglesProd;
  private Map<String,List<String>> ensPremier;
  private Map<String,List<String>> ensSuivant;
  public static final String epsilon = "";

  public Grammaire(String a)
  {
    this.axiome = a;
    termList = new ArrayList<String>();
    nonTermList = new ArrayList<String>();
    nonTermList.add(this.axiome);
    reglesProd = new HashMap<String,List<String>>();
    ensPremier = new HashMap<String,List<String>>();
    ensSuivant = new HashMap<String,List<String>>();

  }

  public void addAxiome(String a)
  {
    this.axiome = a;
  }

  public void defineTerm(List<String> t)
  {
    this.termList = t;
  }

  public void defineNonTerm(List<String> nt)
  {
    this.nonTermList.addAll(nt);
    for(int i=0;i<this.nonTermList.size();i++)
      ensSuivant.put(nonTermList.get(i),new ArrayList<String>());
  }

  public void defineReglesProd(Map<String,List<String>> r)
  {
    this.reglesProd = r;
  }

  public String getFirst(String s)
  {

    for(int i=0;i<this.termList.size();i++)
    {

      if(s.startsWith(this.termList.get(i)))
        return this.termList.get(i);
    }

    for(int j=0;j<this.nonTermList.size();j++)
    {

      if(s.startsWith(this.nonTermList.get(j)))
        return this.nonTermList.get(j);
    }

    return null;
  }

  public void printEnsPremier(String term,List<String> ens)
  {
    System.out.print("Premier("+term+") = {");
    for(int i=0;i<ens.size()-1;i++)
    {
      String t = ens.get(i).equals(epsilon)?"epsilon":ens.get(i);
      System.out.print(t+",");
    }
    String t = ens.get(ens.size()-1).equals(epsilon)?"epsilon":ens.get(ens.size()-1);
    System.out.println(t+"}");
  }

  public void printEnsSuivant(String term,List<String> ens)
  {
    System.out.print("Suivant("+term+") = {");
    for(int i=0;i<ens.size()-1;i++)
    {
      String t = ens.get(i).equals(epsilon)?"epsilon":ens.get(i);
      System.out.print(t+",");
    }
    String t = ens.get(ens.size()-1).equals(epsilon)?"epsilon":ens.get(ens.size()-1);
    System.out.println(t+"}");
  }

  public void printAllSuivant()
  {
    List<String> listKey = new ArrayList<String>(this.ensSuivant.keySet());
    for(int i=0;i<listKey.size();i++)
    {
      printEnsSuivant(listKey.get(i),this.ensSuivant.get(listKey.get(i)));
    }
  }

  public void calculPremier()
  {
    for(int i=0;i<this.termList.size();i++)
    {
      String stringTerm = this.termList.get(i);
      List<String> ens = new ArrayList<String>();
      ens.add(stringTerm);
      printEnsPremier(stringTerm,ens);
      ensPremier.put(stringTerm,ens);
    }
    for(int j=0;j<this.nonTermList.size();j++)
    {
      String stringNt = this.nonTermList.get(j);
      List<String> ensNt = new ArrayList<String>();
      List<String> prodNt = this.reglesProd.get(stringNt);
      for(int k=0;k<prodNt.size();k++)
      {
          String prod = prodNt.get(k);
          ensNt.addAll(calculSousPremier(prod));
      }
        printEnsPremier(stringNt,ensNt);
        this.ensPremier.put(stringNt,ensNt);
      }
    }

    public List<String> calculSousPremier(String rule)
    {
      List<String> ensReturn = new ArrayList<String>();
      if(!rule.equals(epsilon))
      {

        List<String> premierFirst = new ArrayList<String>();
        int sizeToCut = -1;

        do {

          String firstTerm = this.getFirst(rule);
          premierFirst = ensPremier.get(firstTerm);
          ensReturn.addAll(premierFirst);
          sizeToCut = firstTerm.length();
          rule = rule.substring(sizeToCut);

        }while(premierFirst.contains(epsilon) && sizeToCut!=rule.length());

        if(sizeToCut!=rule.length())
          ensReturn.remove(epsilon);
      }
      else
        ensReturn.add(epsilon);

      return ensReturn;
    }

    public String getFirstNonTerm(String rule)
    {
      String s = getFirst(rule);
      if(s==null)
        return null;
      return this.nonTermList.contains(s)?s:getFirstNonTerm(rule.substring(s.length()));
    }

    public void calculSuivant()
    {
      for(int i=0;i<this.nonTermList.size();i++)
      {
        String stringNt = this.nonTermList.get(i);
        List<String> prodNt = this.reglesProd.get(stringNt);
        if(stringNt.equals(this.axiome))
          ensSuivant.get(axiome).add("$");

        for(int j=0;j<prodNt.size();j++)
        {
          String prod = prodNt.get(j);
          String firstNT = "";
          do {
            firstNT = getFirstNonTerm(prod);
            if(firstNT != null)
            {
              List<String> sousPrem = calculSousPremier(prod);
              if(!sousPrem.contains(epsilon))
                this.ensSuivant.get(firstNT).addAll(sousPrem);
              else
                this.ensSuivant.get(firstNT).addAll(this.ensSuivant.get(stringNt));
              prod = prod.substring(prod.indexOf(firstNT));
            }
          } while (firstNT!=null);

      }
      }
    }



  }
