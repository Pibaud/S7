package mvc2;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Model {
	//quand un modèle change, ses vues sont prévenues

	public void changed(Object how) {
        Collection<View> views = MV_Association.getViews(this);
        for (View v : views) {
            v.update(how);
        }
  }
}
