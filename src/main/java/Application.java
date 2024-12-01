import org.automation.DelockSearch;
import org.automation.EdacSearch;
import org.automation.LabfacilitySearch;


public class Application {
    //private final static String SUPPLIER_NAME = "edac";
     //private final static String SUPPLIER_NAME = "delock";
    private final static String SUPPLIER_NAME = "labfacility";
    //private final static String SUPPLIER_NAME = "skyworks";

    public static void main(String[] args) {
        
        switch (SUPPLIER_NAME) {
            case "delock":
                DelockSearch delockSearch = new DelockSearch();
                delockSearch.search();
                break;
            case "edac":
                EdacSearch edacSearch = new EdacSearch();
                edacSearch.search();
                break;
            case "labfacility":
                LabfacilitySearch labfacilitySearch = new LabfacilitySearch();
                labfacilitySearch.search();
                break;
            case "skyworks":
                SkyworksSearch skyworksSearch = new SkyworksSearch();
                skyworksSearch.search();

        }


    }
}
