import java.util.*;

public class Main
{

  public static void main(String[] args) {

    /*  Etat e1 = new Etat(1);
      Etat e2 = new Etat(2);
      Etat e3 = new Etat(3);
      e3.makeFinal();
      e1.addSuivantTransition('a',e2);
      e2.addSuivantTransition('b',e3);
      e3.addSuivantTransition('c',e3);
      e3.addSuivantTransition('d',e1);*/

      String test = "program p1\nbegin\nz <- x ;\nx <- y ;\ny <- z\nend.";

      Automate autoEntier = Fonctions.makeAutomateEntier();
      Automate autoIdent = Fonctions.makeAutomateIdent();


      /*if(autoEntier.checkStr("-123456992"))
        System.out.println("L'automate a reconnu le mot !");
      else
        System.out.println("L'automate n'a pas reconnu le mot...");

      if(autoIdent.checkStr("program"))
        System.out.println("L'automate a reconnu le mot !");
      else
        System.out.println("L'automate n'a pas reconnu le mot...");*/

      System.out.println(Fonctions.TransformAlgo(autoEntier,autoIdent,test));




  }
}
