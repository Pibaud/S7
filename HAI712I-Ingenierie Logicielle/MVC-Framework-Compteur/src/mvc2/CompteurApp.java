package mvc2;
import java.awt.*;
import javax.swing.*;

public class CompteurApp {
    JFrame appWindow;
    Model model;
    Controller controller1;
    Controller controller2;
    // Déclarez view2 (qui sera CompteurView2)
    View view1, view2, view3;

    JButton bIncr;
    JButton bDecr;
    JButton bRAZ ;

    public CompteurApp() {
        this.initMVC();
        this.initGraphic();
    }

    public void initMVC() {
        model = Compteur.getInstance1();
        controller1 = new CompteurController(model);
        controller2 = new CompteurController(model);
        view1 = new CompteurView1(model, controller1);
        // *** CORRECTION 1 : Instancier CompteurView2 et non deux fois CompteurView1 ***
        // On suppose que CompteurView2 implémente View. Le cast est implicite ou Compteur est bien le type.
        view2 = new CompteurView2(model, controller2);
    }

    public void initGraphic() {
        appWindow = new JFrame("CompteurApp");
        //les Jbuttons pour incrémenter le compteur
        bIncr = new JButton("incr");
        bDecr = new JButton("decr");
        bRAZ  = new JButton("raz");
        //les contrôleurs de notre compteur sont des observateurs des boutons
        bIncr.addActionListener(controller1);
        bDecr.addActionListener(controller1);
        bRAZ.addActionListener(controller1);

        //rangement des choses dans la fenêtre de l'application
        //Les vues
        JPanel compteursViews = new JPanel();

        // *** CORRECTION 2 : Utiliser un layout pour empiler les vues ***
        // Utiliser BoxLayout.Y_AXIS pour empiler verticalement view1 et view2
        compteursViews.setLayout(new BoxLayout(compteursViews, BoxLayout.Y_AXIS));

        // Ajout de la première vue
        compteursViews.add((Component) view1);

        // Ajouter un petit espace entre les deux vues
        compteursViews.add(Box.createVerticalStrut(10));

        compteursViews.add((Component) view2);

        //Les boutons
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(bIncr);
        buttons.add(bDecr);
        buttons.add(bRAZ);

        //rangement
        appWindow.getContentPane().setLayout(new BorderLayout());
        appWindow.getContentPane().add(compteursViews, BorderLayout.CENTER);
        appWindow.getContentPane().add(buttons, BorderLayout.SOUTH);

        //ajuster la taille
        appWindow.pack();

    }

    public void open() { appWindow.setVisible(true); }

    public static void main(String[] args) {
        new CompteurApp().open();
    }
}