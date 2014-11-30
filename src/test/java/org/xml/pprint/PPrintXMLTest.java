package org.xml.pprint;

import org.junit.Test;

import javax.xml.transform.TransformerException;
import java.io.File;

import static org.junit.Assert.*;

public class PPrintXMLTest {

    @Test
    public void testToString() throws Exception {
        PPrintXML pp = new PPrintXML();
        String expected = "org.xml.pprint.PPrintXML with indentation true using 2 spaces and charset 'UTF-8'";
        assertEquals(expected, pp.toString());
    }

    @Test
    public void testPPrintDefault() throws Exception {
        String in = "<a><b/><c>C</c></a>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a>\n" +
                "  <b/>\n" +
                "  <c>C</c>\n" +
                "</a>\n";
        assertEquals(expected, new PPrintXML().pprint(in));
    }

    @Test
    public void testPPrint4Spaces() throws Exception {
        String in = "<a><b/><c>C</c></a>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a>\n" +
                "    <b/>\n" +
                "    <c>C</c>\n" +
                "</a>\n";
        assertEquals(expected, new PPrintXML().withIndentation(4).pprint(in));
    }

    @Test
    public void testPPrintFlat() throws Exception {
        String in = "<a><b/><c>C</c></a>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b/><c>C</c></a>";
        assertEquals(expected, new PPrintXML().flat().pprint(in));
    }

    @Test
    public void testPPrintFile() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b/><c>C</c></a>";
        assertEquals(expected, new PPrintXML().flat().pprint(new File("./src/test/resources/file.xml")));
    }

    @Test
    public void testInvalidXML() {
        String xin = "<a><b><c></b></c></a>";
        try {
            assertEquals("?", new PPrintXML().pprint(xin));
            fail();
        } catch (TransformerException e) {
            assertEquals("org.xml.sax.SAXParseException", e.getCause().getClass().getName());
        }
    }
}