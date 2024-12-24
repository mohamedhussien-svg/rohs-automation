import org.automation.DelockSearch;
import org.automation.EdacSearch;
import org.automation.LabfacilitySearch;
import org.automation.MurrelektronikSearch;


public class Application {
    //    private final static String SUPPLIER_NAME = "edac";
    //private final static String SUPPLIER_NAME = "delock";
//    private final static String SUPPLIER_NAME = "labfacility";
    //private final static String SUPPLIER_NAME = "skyworks";
    private final static String SUPPLIER_NAME = "murrelektronik";

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
            case "murrelektronik":
                MurrelektronikSearch murrelektronikSearch = new MurrelektronikSearch();
                murrelektronikSearch.search();
                break;
        }


    }
}
