import org.automation.*;


public class Application {
     //private final static String SUPPLIER_NAME = "edac";
    //private final static String SUPPLIER_NAME = "delock";
    // private final static String SUPPLIER_NAME = "labfacility";
       //private final static String SUPPLIER_NAME = "mhconnectors";
      // private final static String SUPPLIER_NAME = "lemo";
      //private final static String SUPPLIER_NAME = "leviton";
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
            case "mhconnectors":
                MhconnectorsSearch mhconnectors = new MhconnectorsSearch();
                mhconnectors.search();
                break;

            case "lemo":
                LemoSearch lemo = new LemoSearch();
                lemo.search();
                break;

            case "leviton":
                LevitonSearch leviton= new LevitonSearch();
                leviton.search();
                break;

            case "murrelektronik":
                MurrelektronikSearch murrelektronikSearch= new MurrelektronikSearch();
                murrelektronikSearch.search();
                break;
        }
    }
}
