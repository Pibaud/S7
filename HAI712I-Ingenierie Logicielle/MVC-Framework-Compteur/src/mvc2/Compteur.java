package mvc2;

import java.util.ArrayList;

public class Compteur extends Model {
	
	protected int valeur;
    static private Compteur instance1;
    static private Compteur instance2;
	
	private Compteur() {
        this(0);
        this.instance1 = this;
    }
	
	public Compteur(int init) { valeur = init; }

	//implantation de Observer ("notify" remplacé par "changed"
	//"j'ai changé" dit le compteur
	
	protected void changeValeur(int i){
	  valeur = valeur + i;
	  this.changed("changed valeur with " + valeur);}

	//vérifiez que les méthodes sont correctes
	public int getValeur(){return valeur;}

	public void incr() {
        this.changeValeur(+1);
    }

	public void decr() {
        this.changeValeur(-1);
    }

	public void raz()  { //à écrire
        valeur = 0;
        this.changeValeur(0);
    }

    static public Model getInstance1(){
        if(instance1 == null){
            instance1 = new Compteur();
        }
        return instance1;
    }

    static public Model getInstance2(){
        if(instance2 == null){
            instance2 = new Compteur();
        }
        return instance2;
    }
}