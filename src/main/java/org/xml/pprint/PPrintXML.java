package org.xml.pprint;

import org.xml.sax.InputSource;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * XML Pretty Printer with fluent interface.
 * <p/>
 * Generally you will only need to create a single instanceof this class
 * with a given configuration and re-use it throughout your program.
 * <p/>
 * Java 7.
 */
public class PPrintXML {

    /**
     * Default pretty-printer that indents output by 2 spaces,
     * outputs XML header and uses the UTF-8 Charset.
     *
     * @throws java.lang.RuntimeException if there is a serious platform
     *                                    configuration error (@see TransformerConfigurationException)
     */
    public PPrintXML() {
        this(true, 2, false, StandardCharsets.UTF_8);
    }

    /**
     * Custom pretty-printer.
     *
     * @param indent             true if output is to be indented
     * @param indentSpaces       number of spaces per change in indent level
     * @param omitXMLDeclaration use true if you wish to generate an XML fragment,
     *                           otherwise false for a complete XML document
     * @param charset            output Charset
     */
    public PPrintXML(Boolean indent, Integer indentSpaces, Boolean omitXMLDeclaration, Charset charset) {
        this.charset = charset;
        this.indent = indent;
        this.indentSpaces = indentSpaces;
        this.omitXMLDeclaration = omitXMLDeclaration;
        try {
            this.transformer = configure(TransformerFactory.newInstance().newTransformer());
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Configure the given Transformer and return it.
     *
     * @param transformer assumed to be a new instance with default properties
     * @return the input Transformer with properties modified.
     */
    private Transformer configure(Transformer transformer) {
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, this.charset.displayName());
        if (this.omitXMLDeclaration)
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        if (this.indent)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        if (this.indentSpaces >= 0)
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indentSpaces.toString());
        return transformer;
    }

    /**
     * Set the indentation.
     *
     * @param indentSpaces indentation level
     * @return a new pretty-printer
     */
    public PPrintXML withIndentation(Integer indentSpaces) {
        return new PPrintXML(true, indentSpaces, this.omitXMLDeclaration, this.charset);
    }

    /**
     * Flat pretty-printer without indentation.
     *
     * @return a new flat pretty-printer.
     */
    public PPrintXML flat() {
        return new PPrintXML(false, 0, this.omitXMLDeclaration, this.charset);
    }

    /**
     * Pretty-printer with other Charset.
     *
     * @return a new pretty-printer
     */
    public PPrintXML withCharset(Charset charset) {
        return new PPrintXML(this.indent,
                this.indentSpaces,
                this.omitXMLDeclaration,
                charset);
    }

    @Override
    public String toString() {
        return String.format("%s with indentation %b using %d spaces and charset '%s'",
                PPrintXML.class.getName(), this.indent, this.indentSpaces, this.charset.displayName());
    }

    /**
     * Default UTF-8.
     */
    public final Charset charset;

    /**
     * Default True.
     */
    public final Boolean indent;

    /**
     * Default 2.
     */
    public final Integer indentSpaces;

    /**
     * Set to true if the XML Declaration should not be
     * written to the output. Defaults to False;
     * <p/>
     * In other words, use false for an XML document and
     * true for an XML fragment.
     */
    public final Boolean omitXMLDeclaration;

    /**
     * An XML Transformer configured with properties above.
     */
    private final Transformer transformer;

    /**
     * Pretty-print a String of untidy XML.
     *
     * @param xml structurally valid yet untidy XML
     * @return pretty-printed XML
     * @throws TransformerException
     */
    public String pprint(String xml) throws TransformerException {
        return pprint(new InputSource(
                new ByteArrayInputStream(xml.getBytes())
        ));
    }

    /**
     * Pretty-print a File containing untidy XML.
     *
     * @throws IOException if the file is not found
     * @throws TransformerException if the XML is structurally invalid
     */
    public String pprint(File xml) throws IOException, TransformerException {
        try (InputStream in = new FileInputStream(xml)) {
            return pprint(in);
        }
    }

    /**
     * Pretty-print a stream containing XML.
     *
     * @throws TransformerException if the XML is structurally invalid
     */
    public String pprint(InputStream in) throws TransformerException {
        return pprint(new InputSource(in));
    }

    /**
     * Pretty-print XML.
     *
     * @param xml which is structurally valid
     * @return pretty-printed XML
     * @throws TransformerException if the input XML is structurally invalid
     */
    public String pprint(InputSource xml) throws TransformerException {
        Source xmlSource = new SAXSource(xml);
        StreamResult res = new StreamResult(new ByteArrayOutputStream());
        transformer.transform(xmlSource, res);
        return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
    }

}
