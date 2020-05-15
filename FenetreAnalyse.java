import java.awt.GraphicsConfiguration;
import javax.swing.*;

/*
Classe servant à afficher une fenêtre externe
pour la table d'analyse
*/
public class FenetreAnalyse extends JFrame {

  public FenetreAnalyse(Object[][] data, String  entetes[]){
    this.setLocationRelativeTo(null);
    this.setTitle("Table d'analyse");
    this.setSize(500, 400);

    JTable tableau = new JTable(data, entetes);
    this.getContentPane().add(new JScrollPane(tableau));
  }

  public void makeVisible() //Affiche la fenêtre
  {
    this.setVisible(true);
  }
}
