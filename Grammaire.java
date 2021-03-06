import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Set;

public class Grammaire
{
  private List<String> termList; //Liste des terminaux
  private List<String> nonTermList; //Liste des non Terminaux
  private String axiome; //Axiome
  private Map<String,List<String>> reglesProd; //Regles de production
  private Map<String,List<String>> ensPremier; //Ensembles premiers
  private Map<String,List<String>> ensSuivant; //Ensembles suivants
  private Map<MyKey,String> tableAnalyse; //Table d'analyse (voir MyKey.java)
  public static final String epsilon = ""; //Utile pour lecture du code

  //Constructeur
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

  /*
  ===============================
  Constructeurs (initialisateurs)
  ===============================
  */
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
    {
      ensSuivant.put(nonTermList.get(i),new ArrayList<String>());
      ensPremier.put(nonTermList.get(i),new ArrayList<String>());
    }
  }

  public void defineReglesProd(Map<String,List<String>> r)
  {
    this.reglesProd = r;
  }

  /*
  =================================
  */

  /*
  ======================
  Fonctions d'affichage
  ======================
  */
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
    System.out.println("\nEnsembles suivants : \n");
    List<String> listKey = new ArrayList<String>(this.ensSuivant.keySet());
    for(int i=0;i<listKey.size();i++)
    {
      printEnsSuivant(listKey.get(i),this.ensSuivant.get(listKey.get(i)));
    }
    System.out.println("");
  }

  public void printAllPremier()
  {
    System.out.println("\nEnsembles premiers : \n");
    for(int i=0;i<this.nonTermList.size();i++)
    {
      if(this.ensPremier.get(this.nonTermList.get(i)).size()>0)
      printEnsPremier(this.nonTermList.get(i),this.ensPremier.get(this.nonTermList.get(i)));
    }
    System.out.println("");
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
    System.out.println("");
  }

  public void printTableAnalyse()
  {
      System.out.println("\nTable d'analyse : \n");
      Set<MyKey> keys = this.tableAnalyse.keySet();
      for(String nTerm : this.nonTermList)
      {
        keys.stream()
            .filter(t -> t.getNonTerm().equals(nTerm))
            .forEach(r -> System.out.println(r.getNonTerm()+" | "+(r.getTerm().equals(epsilon)?"£":r.getTerm())+" --- "+(this.tableAnalyse.get(r).equals(epsilon)?"epsilon":this.tableAnalyse.get(r))));
      }
      System.out.println("");
      String[][] tableAnalyseTab = new String[this.nonTermList.size()][this.termList.size()+2];
      String[] entetes = new String[termList.size()+2];
      entetes[0] = " ";
      for(int l=0;l<this.termList.size();l++)
      {
        entetes[l+1] = this.termList.get(l);
      }
      entetes[entetes.length-1]="$";

      for(int i=0;i<this.nonTermList.size();i++)
      {
        String nonTerm = this.nonTermList.get(i);
        tableAnalyseTab[i][0]=nonTerm;
        MyKey cle;
        for(int j=0;j<this.termList.size();j++)
        {
          String term = this.termList.get(j);
          cle = new MyKey(nonTerm,term);
          if(this.tableAnalyse.containsKey(cle))
            tableAnalyseTab[i][j+1]= this.tableAnalyse.get(cle).equals(epsilon)?"epsilon":this.tableAnalyse.get(cle);
          else
            tableAnalyseTab[i][j+1]=" ";
        }
        cle = new MyKey(nonTerm,"$");
        if(this.tableAnalyse.containsKey(cle))
          tableAnalyseTab[i][tableAnalyseTab[i].length-1]= this.tableAnalyse.get(cle).equals(epsilon)?"epsilon":this.tableAnalyse.get(cle);
        else
          tableAnalyseTab[i][tableAnalyseTab[i].length-1]=" ";

      }
      FenetreAnalyse fenetre = new FenetreAnalyse(tableAnalyseTab,entetes);
      fenetre.makeVisible();

    }


  /*
  =======================
  */

  /**
  *Fonction qui retourne une liste contenant tout les terminaux
  *qui commencent par s
  *@param String s le préfixe
  *@return La liste correspondante
  */

  public List<String> getAllTermStartWith(String s)
  {
    return this.termList.stream()
                        .filter(term -> term.startsWith(s))
                        .collect(Collectors.toList());
  }

  /**
  *Fonction qui retourne une liste contenant tout les non-terminaux
  *qui commencent par s
  *@param String s le préfixe
  *@return La liste correspondante
  */

  public List<String> getAllNonTermStartWith(String s)
  {
    return this.nonTermList.stream()
                        .filter(term -> term.startsWith(s))
                        .collect(Collectors.toList());
  }

  /**
  *Fonction qui check si dans une liste donnée,
  *il existe un String qui a pour préfixe "s"
  *@param String s le préfixe
  *@param list la liste à vérifier
  *@return true si la liste en contient bien un
  */

  public boolean containsStartWith(String s,List<String> list)
  {
    return list.stream().filter(t -> t.startsWith(s)).collect(Collectors.toList()).size() > 0;
  }

  /**
  *Fonction qui dans une liste donnée, renvoie le choix parfait
  *ie: si deux String dans la liste ont le même préfixe on avance jusqu'à
  *que la chaîne corresponde à un unique String dans la liste choices
  *@param String s la chaine
  *@param choices la liste dans laquelle on récupère le choix
  *@return String le bon String
  */

  public String findGoodChoice(String s,List<String> choices)
  {
    int i=1;
    boolean bool= true;
    while(bool && i<s.length())
    {
      i++;
      bool = containsStartWith(s.substring(0,i),choices);

      //System.out.println("I"+i+" substring "+s.substring(0,i));
    }
    final int j = !bool?i-1:i;
    // System.out.println("SUBSTRING "+s.substring(0,j));
     String t = choices.stream()
                       .filter(c -> c.equals(s.substring(0,j)))
                       .findAny()
                       .get();
      //System.out.println("findGoodChoice returns "+t);
      return t;
  }

  /**
  *Fonction qui renvoie le premier terminal ou non terminal d'un String
  *@param String s la chaine à analyser
  *@return String le premier de la chaine s'il existe, null sinon
  */
  public String getFirst(String s)
  {
    List<String> listPossible = new ArrayList<>();
    String inter = s;
    //System.out.println("DANS GET FIRST "+inter);
    if(s.length()>0)
    {
    listPossible = getAllTermStartWith(inter.substring(0,1));
    if(listPossible.size()>1)
    {
      //System.out.println("On est bien lo1");
      return findGoodChoice(inter,listPossible);
    }else if(listPossible.size()==1)
    {
      return listPossible.get(0);
    }

    listPossible = getAllNonTermStartWith(inter.substring(0,1));
    if(listPossible.size()>1)
    {
      //System.out.println("On est bien lo");
      return findGoodChoice(inter,listPossible);
    }
    else if(listPossible.size()==1)
    {
      return listPossible.get(0);
    }
  }
    return null;
  }

  /**
  *Fonction qui calcule Premier(rule1) et le stocke dans ensPremier
  */

  public void calculSousPremier2(String rule1)
  {
      String rule = rule1;
      List<String> ensReturn = new ArrayList<String>();
      if(!rule.equals(epsilon))
      {
        List<String> premierFirst = new ArrayList<String>();
        int sizeToCut = -1;

        do {
          //Premier terme de la règle
          String firstTerm = this.getFirst(rule);
          if(this.ensPremier.containsKey(firstTerm)) //Si Premier(terme) existe déjà
          {
            premierFirst = ensPremier.get(firstTerm);
            addAllIfAbsent(ensReturn,premierFirst);
          }
          if(!this.termList.contains(firstTerm))
          {
            for(String regles:this.reglesProd.get(firstTerm))
            {
              if(!this.ensPremier.containsKey(regles))
                calculSousPremier2(regles);
              addAllIfAbsent(ensReturn,this.ensPremier.get(regles));
            }
          }

          sizeToCut = firstTerm.length();
          rule = rule.substring(sizeToCut);

        }while(premierFirst.contains(epsilon) && sizeToCut!=rule.length());

        if(sizeToCut!=rule.length())
          ensReturn.remove(epsilon);

      }
      else
        ensReturn.add(epsilon);

      if(this.ensPremier.containsKey(rule1))
        addAllIfAbsent(ensReturn,this.ensPremier.get(rule1));
      else
        this.ensPremier.put(rule1,ensReturn);

    }

  /**
  *Fonction qui calcule l'ensemble des ensembles Premier
  *des terminaux et non-terminaux
  */

  public void calculPremier()
  {
      for(int i=0;i<this.termList.size();i++)
      {
        String stringTerm = this.termList.get(i);
        List<String> ens = new ArrayList<String>();
        ens.add(stringTerm);
        ensPremier.put(stringTerm,ens);
        //printEnsPremier(stringTerm,this.ensPremier.get(stringTerm));
      }
      boolean termToAdd = true;
      while(termToAdd)
      {
        for(int j=0;j<this.nonTermList.size();j++)
        {
          //System.out.println("Calcul premier de "+this.nonTermList.get(j));
          String stringNt = this.nonTermList.get(j);
          List<String> ensNt = new ArrayList<String>();
          List<String> prodNt = this.reglesProd.get(stringNt);
          //System.out.println("On calcule Premier de "+stringNt);
          for(int k=0;k<prodNt.size();k++)
          {
            String prod = prodNt.get(k);
            List<String> premierProd = new ArrayList<>();
            calculSousPremier2(prod);
            termToAdd = addAllIfAbsent(this.ensPremier.get(stringNt),this.ensPremier.get(prod));
          }
        }
        //System.out.println("ETAT PREMIER : ");
        //printAllPremier();

        }
    }

  /**
  *Fonction qui renvoie le premier non terminal d'un String
  *@param rule le string
  *@return String le non terminal, null sinon
  */
  public String getFirstNonTerm(String rule)
  {
      String s = getFirst(rule);
      //System.out.println("DANS NON TERM "+s);
      if(s==null)
        return null;
      return this.nonTermList.contains(s)?s:getFirstNonTerm(rule.substring(s.length()));
    }

  /**
  *Fonction qui ajoute tous les éléments <ABSENTS> d'une liste dans l'autre
  *@param listB la liste à ajouter dans
  *@param listA
  *@return booleen vrai si l'on a bien ajouté des éléments à listA
  */

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

  /**
  *Fonction qui calcule tous les ensembles Suivant
  */
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
                //System.out.println("FirstNT dans suivnat "+firstNT);
                if(firstNT != null && prod!=null)
                {
                  //System.out.println("prod avant cut "+prod+" index :"+prod.indexOf(firstNT));
                  prod = prod.substring(prod.indexOf(firstNT)+firstNT.length());
                  List<String> sousPrem = new ArrayList<>();
                  //System.out.println("Prod dans suivant "+prod);
                  if(!ensPremier.containsKey(prod))
                    calculSousPremier2(prod);
                  termToAdd = addAllIfAbsent(this.ensSuivant.get(firstNT),this.ensPremier.get(prod));
                  if(this.ensPremier.get(prod).contains(epsilon) || prod.length()==0)
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

  /**
  *Fonction qui construit la table d'analyse de la Grammaire
  */

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

  /**
  *Fonction qui check si une chaine ne contient bien uniquement que des
  *terminaux de la liste des Terminaux
  *sinon, pas besoin de l'analyser..
  */
  public boolean checkTermChaine(String chaine)
  {
      boolean check = true;
      String term ="";
      while(check && chaine.length()>0)
      {
        term = getFirst(chaine);
        if(term==null && chaine.length()>0)
          return false;
        else
          check = this.termList.contains(term);

        if(!check)
          return false;

        chaine=chaine.substring(term.length());

      }
      return true;
    }

  /**
  *Fonction qui analyse une chaine
  *@param String chaine la chaine à analyser
  *@param boolean affiche booleen true si on veut afficher les étapes
  *@return bool true si la chaine appartient à L(Grammaire)
  */

  public boolean analyseChaine(String chaine,boolean affiche)
  {
      chaine = chaine.replace(" ","");
      if(!checkTermChaine(chaine))
        return false;
      boolean appartient = true;
      chaine = chaine+"$";
      String pile = axiome+"$";
      boolean dollarPileEtChaine = false; //booleen pour savoir si la chaine et la pile sont toutes les deux à $
      while(appartient && !dollarPileEtChaine) //Tant que l'analyse n'a pas décidé de la non-appartenance
      {
          String firstTerm = getFirst(pile); //Premier terme de la pile (terminal ou non)
          if(affiche)
          System.out.println("Pile : "+pile+" --- Chaine : "+chaine);
          if(this.nonTermList.contains(firstTerm)) //Si le premier terme de la liste est un non-terminal
          {
            String firstTermChaine = chaine.equals("$")?"$":getFirst(chaine); //On prend le premier terme de la chaine

            if(this.termList.contains(firstTermChaine) || firstTermChaine.equals("$")) //Si ce terme appartient bien à la liste des terminaux
            {
              List<String> reglesProdNonTerm = this.reglesProd.get(firstTerm); //On récupère les règles de production du non Terminal
              if(reglesProdNonTerm.contains(firstTermChaine)) //Si le terminal est compris dans les règles
              {
                pile = pile.replaceFirst(firstTerm,firstTermChaine); //On remplace la première occurence du non Terminal dans la pile par ce terminal
              }
              else
              {
                MyKey myKey = new MyKey(firstTerm,firstTermChaine);
                //System.out.println(this.tableAnalyse.get(myKey));
                if(this.tableAnalyse.containsKey(myKey)) //Si la table d'analyse contient bien une entrée pour le couple firstTerm  firstTermChaine
                {
                  String replacement = this.tableAnalyse.get(myKey);
                  pile = pile.replaceFirst(firstTerm,this.tableAnalyse.get(myKey)); //Sinon on récupère le remplacement dans la table d'analyse
                }
                else //Le cas échéant, cette chaine n'appartient pas à L(Grammaire)
                  appartient = false;
              }
            }
            else //Le cas échéant, cette chaine n'appartient pas à L(Grammaire)
              appartient = false;
          }
          else if(this.termList.contains(firstTerm) || (chaine.equals("$") && !pile.equals("$"))) //Sinon on vérifie que ce premier terme est bien un de nos terminaux
          {
            //Si oui, on dépile et on avance
            if(!chaine.equals("$"))
            {
              pile=pile.substring(firstTerm.length());
              chaine = chaine.substring(firstTerm.length());
            }
            else
              appartient = false;
          }
          else if(pile.equals("$") && chaine.equals("$"))
            dollarPileEtChaine = true;
          else
            appartient = false;
      }

      return appartient && dollarPileEtChaine;
    }



  }
