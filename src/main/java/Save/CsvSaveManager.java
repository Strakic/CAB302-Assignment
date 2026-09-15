package Save;


public class CsvSaveManager {

    private static final String HEADER = "type,key,value";
    private static final int FORMAT_VERSION = 1;

    private final Path saveDir;

    public CsvSaveManager() {
        this(Paths.get(System.getProperty("user.home"), ".trashslammers", "Save"));
    }
}
