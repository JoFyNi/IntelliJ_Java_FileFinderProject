import componenten.MainUI;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class DatasourceTest {

    @AfterEach
    public void afterEachDatasourceTest() {
        File directory = new File("file-uploads");
        if (directory.exists() && directory.isDirectory()) {
            for (File f : directory.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
        directory.delete();
    }

    @Test
    public void testSet() throws Exception {

    }

    @Test
    public void checkReader() throws Exception {
        //searchThread("test","test.txt");
    }

    @Test(expected = ArithmeticException.class)
    public void checkMain() throws Exception {
        MainUI UI = new MainUI();
        UI.getRootPanel();
    }

    @Test
    public void testPostUpload() throws Exception {
        File f = new File("src/test/resources/TestFile");
        assertTrue(f.exists());

        String expected = "Uploaded: testFile" + System.lineSeparator();
    }

    @Test
    public void testPostSet() throws Exception {
        File f = new File("src/test/resources/testFile");
        assertTrue(f.exists());

        String expected = "Uploaded: testFile" + System.lineSeparator();

    }
}