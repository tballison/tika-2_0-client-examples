import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestOptimaizeBasic {

    @Test
    public void testBasic() throws Exception {
        LanguageDetector ld = new OptimaizeLangDetector();
        ld.loadModels();

        LanguageResult result = ld.detect("Lorem ipsum dolor " +
                "sit amet, consectetur adipiscing elit, sed do " +
                "eiusmod tempor incididunt");
        assertEquals("pt", result.getLanguage());
    }
}
