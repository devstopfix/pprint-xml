Pretty-Print XML
================

Pretty-print XML from Java using the javax.xml.transform package, no
other dependencies required.

Usage
=====

```java
import org.xml.pprint.PPrintXML;

PPrintXML prettyPrinter = new PPrintXML().withIndentation(4);
String out = prettyPrinter.pprint(in);
```

Notes
=====

This library uses code that is available on many sites on the Internet. A
discussion of alternate methods can be found at [http://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java][StackOverflow - how to pretty print XML from Java].

License
=======

MIT License - no attribution required.
