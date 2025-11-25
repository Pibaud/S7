package mvc2;
import javax.swing.*;
import java.awt.*;

public class CompteurView2 extends View {

    JProgressBar progressbar = new JProgressBar();

    public CompteurView2(Model m, Controller c) {
        super(m, c);
        this.add(progressbar);
        this.update("valeur");
    }

    public void update(Object how) {
        // how n'est pas utilisé dans cet exemple
        int newVal = ((Compteur) m).getValeur();
        progressbar.setValue(newVal);
        //setText déclenche le ré-affichage
    }


}
