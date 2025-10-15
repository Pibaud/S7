import java.util.ArrayList;

public class Directory extends ElementStockage {
    ArrayList<File> files;
    ArrayList<Link> liens;

    public Directory(String nom, Directory dossierParent){
        super(nom, dossierParent);
        this.basicSize = 4;
        this.files = new ArrayList<File>();
        this.liens = new ArrayList<Link>();
    }

    @Override
    public int size() {
        int size = basicSize;

    }


}
