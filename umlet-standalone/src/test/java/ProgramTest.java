import static org.assertj.swing.launcher.ApplicationLauncher.application;

import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.standalone.MainStandalone;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ProgramTest {
    @Test
    public void test() throws IOException {
        application(MainStandalone.class).start();
        DiagramHandler handler = new DiagramHandler(null);
        DiagramFileHandler fileHandler = DiagramFileHandler.createInstance(handler, new File("/Users/renekok/Developer/umlet/umlet-standalone/src/test/resources/class_diagram.uxf"));
        fileHandler.doSaveAs("/Users/renekok/Desktop/test", "asd");
    }

}
