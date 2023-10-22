import ApplicationLayer.GraphicInterfaceManager;
import Management.GeneralLogging;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            GraphicInterfaceManager gim = new GraphicInterfaceManager();
            gim.startMenu();
        } catch (Exception e) {
            GeneralLogging logger = new GeneralLogging();
            logger.log(e);
            System.err.println("App shutdown.");
        }
    }
        /*try {
            AccountManager am = new AccountManager();
            am.startMenu();
        } catch (Exception e){
            GeneralLogging logger = new GeneralLogging();
            logger.log(e);
            System.err.println("App shutdown.");
        }

        /*List<String> scanPackages = new ArrayList<>();
        scanPackages.add("Management");
        scanPackages.add("Context");
        scanPackages.add("Database");
        scanPackages.add("ApplicationLayer");
        List<String> hideClasses = new ArrayList<>();
        hideClasses.add("");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withHideClasses(hideClasses);
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        assertNotNull(result);
        System.out.println(result);
        PrintWriter writer = new PrintWriter("class-diagram.txt", "UTF-8");
        writer.print(result);
        writer.close();
    }*/
}
