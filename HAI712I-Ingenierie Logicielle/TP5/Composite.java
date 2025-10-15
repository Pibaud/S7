import java.util.ArrayList;

public class Composite {
    private ArrayList<AbstractComponent> components;

    public Composite() {
        this.components = new ArrayList<AbstractComponent>();
    }
    public void addComponent(AbstractComponent a){
        this.components.add(a);
    }
}
