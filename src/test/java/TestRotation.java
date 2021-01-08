/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @Test
    public void testDocx() throws Exception {
        //not a real test, yet
        Parser p = new AutoDetectParser(getTestConfig("tika-config-preprocess-ocr-auto.xml"));
        String xml = getXML("SkewedDickens.docx", p).xml;
        System.out.println(xml);
    }

    private TikaConfig getTestConfig(String name) throws Exception {
        try (InputStream is =
                TestRotation.class.getResourceAsStream("/"+name)) {
            return new TikaConfig(is);
        }
    }
}
