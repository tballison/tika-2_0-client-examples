
import org.apache.tika.TikaTest;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.ToXMLContentHandler;
import org.junit.Test;
import org.xml.sax.ContentHandler;


import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRotation extends TikaTest {
    @Test
    public void testBasicRotation() throws Exception {
        String path = "/test-documents/skewed5_image_text.png";
        try (InputStream is = TestRotation.class.getResourceAsStream(path)) {
            TesseractOCRConfig tess = new TesseractOCRConfig();
            tess.setApplyRotation(true);
            tess.setEnableImageProcessing(true);
            ParseContext pc = new ParseContext();
            pc.set(TesseractOCRConfig.class, tess);

            Parser p = new AutoDetectParser();
            Metadata m = new Metadata();
            ContentHandler contentHandler = new ToXMLContentHandler();
            p.parse(is, contentHandler, m, pc);
            String content = contentHandler.toString();
            assertTrue(content.contains("non-text-searchable PDF"));
        }
    }

    @Test
    public void testInline() throws Exception {
        Parser p = new AutoDetectParser(getTestConfig(
                "tika-config-preprocess-ocr-per-inline-image.xml"));

        Metadata m = new Metadata();
        ContentHandler contentHandler = new ToXMLContentHandler();
        ParseContext pc = new ParseContext();
        String path = "/test-documents/Simple-text-image.pdf";
        try (InputStream is = TestRotation.class.getResourceAsStream(path)) {
            p.parse(is, contentHandler, m, pc);
            String content = contentHandler.toString();
            assertTrue(content.contains("Pellentesque"));
        }
    }

    @Test
    public void testInlineRPW() throws Exception {
        Parser p = new AutoDetectParser(getTestConfig("tika-config-preprocess-ocr-auto.xml"));
        List<Metadata> metadataList = getRecursiveMetadata("testOCR.pdf", p);
        assertEquals(1, metadataList.size());

        debug(metadataList);
    }

    private TikaConfig getTestConfig(String name) throws Exception {
        try (InputStream is =
                TestRotation.class.getResourceAsStream("/"+name)) {
            return new TikaConfig(is);
        }
    }
}
