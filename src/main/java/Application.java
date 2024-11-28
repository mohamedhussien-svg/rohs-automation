import org.automation.DelockSearch;
import org.automation.EdacSearch;


public class Application {
    private final static String SUPPLIER_NAME = "edac";
//    private final static String SUPPLIER_NAME = "delock";

    public static void main(String[] args) {
        
        switch (SUPPLIER_NAME) {
            case "delock":
                DelockSearch delockSearch = new DelockSearch();
                delockSearch.search();
                break;
            case "edac":
                EdacSearch edacSearch = new EdacSearch();
                edacSearch.search();

        }


    }
}
