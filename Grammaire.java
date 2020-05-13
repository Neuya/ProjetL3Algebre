import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Set;

public class Grammaire
{
  private List<String> termList;
  private List<String> nonTermList;
  private String axiome;
  private Map<String,List<String>> reglesProd;
  private Map<String,List<String>> ensPremier;
  private Map<String,List<String>> ensSuivant;
  private Map<MyKey,String> tableAnalyse;
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
    tableAnalyse = new HashMap<MyKey,String>();
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
    if(nt.contains(axiome) && this.nonTermList.contains(axiome))
      this.nonTermList.remove(axiome);
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
    String t = ens.get((ens.size()-1>0?ens.size()-1:0)).equals(epsilon)?"epsilon":ens.get(ens.size()-1);
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
          this.ensPremier.put(prod,calculSousPremier(prod));
      }
        printEnsPremier(stringNt,ensNt);
        this.ensPremier.put(stringNt,ensNt);
      }
    }

    public void printGrammaire()
    {
      System.out.println("Grammaire : ");
      System.out.println("Terminaux : ");
      for(String s : this.termList)
      {
        System.out.println(s);
      }
      System.out.println("Non terminaux");
      for(String s: this.nonTermList)
      {
        System.out.println(s);
      }
      System.out.println("Axiome : "+axiome);
      System.out.println("Regles : ");
      for(String s: this.nonTermList)
      {
        System.out.println(s+"->");
        for(String rule : this.reglesProd.get(s))
        {
          System.out.println("| "+(rule.equals(epsilon)?"epsilon":rule));
        }
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
          do {
            premierFirst = ensPremier.get(firstTerm);
            if(premierFirst==null)
            {
              List<String> intermediate = new ArrayList<>();
              for(String regles : this.reglesProd.get(firstTerm))
              {
                intermediate.addAll(calculSousPremier(regles));
              }
              this.ensPremier.put(firstTerm,intermediate);
            }
          } while (premierFirst==null);


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

    public boolean addAllIfAbsent(List<String> listA,List<String> listB)
    {
      boolean ret = false;
      for(String s : listB)
      {
        if(!listA.contains(s))
          ret = true;
      }

      if(!ret)
        return false;

      listA.addAll(listB.stream()
                        .filter(o -> !listA.contains(o))
                        .collect(Collectors.toList()));

      return true;
    }




    public void calculSuivant()
    {
      ensSuivant.get(axiome).add("$");
      boolean termToAdd = true;
      do{
          for(int i=0;i<this.nonTermList.size();i++)
          {
            String stringNt = this.nonTermList.get(i);
            List<String> prodNt = this.reglesProd.get(stringNt);

            for(int j=0;j<prodNt.size();j++)
            {
              String prod = prodNt.get(j);
              String firstNT = "";
              do {
                firstNT = getFirstNonTerm(prod);
                if(firstNT != null)
                {
                  prod = prod.substring(prod.indexOf(firstNT)+firstNT.length());
                  List<String> sousPrem = new ArrayList<>();
                  if(!ensPremier.containsKey(prod))
                    sousPrem = calculSousPremier(prod);
                  else
                    sousPrem = ensPremier.get(prod);
                  termToAdd = addAllIfAbsent(this.ensSuivant.get(firstNT),sousPrem);
                  if(sousPrem.contains(epsilon) || prod.length()==0)
                  {
                    this.ensSuivant.get(firstNT).remove(epsilon);
                    termToAdd = addAllIfAbsent(this.ensSuivant.get(firstNT),this.ensSuivant.get(stringNt));
                  }
                }
              } while (firstNT!=null);
            }
          }

        }while(termToAdd);

    }

    public void printTableAnalyse()
    {
      Set<MyKey> keys = this.tableAnalyse.keySet();
      for(String nTerm : this.nonTermList)
      {
        keys.stream()
            .filter(t -> t.getNonTerm().equals(nTerm))
            .forEach(r -> System.out.println(r.getNonTerm()+" | "+(r.getTerm().equals(epsilon)?"£":r.getTerm())+" --- "+(this.tableAnalyse.get(r).equals(epsilon)?"epsilon":this.tableAnalyse.get(r))));
      }
    }

    public void construireTableAnalyse()
    {
      for(String nTerm : this.nonTermList)
      {
        List<String> listProd = this.reglesProd.get(nTerm);
        for(String prod: listProd)
        {
          List<String> premierProd = this.ensPremier.get(prod);
          //printEnsPremier(prod,premierProd);
          for(String terminal : premierProd)
          {
            if(!terminal.equals(epsilon))
            {
              MyKey key = new MyKey(nTerm,terminal);
              this.tableAnalyse.put(key,prod);
            }
            if(premierProd.contains(epsilon))
            {
              for(String term : this.ensSuivant.get(nTerm))
              {
                //System.out.println("TERM "+term);
                MyKey key2 = new MyKey(nTerm,term);
                this.tableAnalyse.put(key2,prod);
              }
            }

          }

        }
      }
    }



  }
