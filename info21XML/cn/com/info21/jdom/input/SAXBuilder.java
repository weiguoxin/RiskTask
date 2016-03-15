// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SAXBuilder.java

package cn.com.info21.jdom.input;

import org.apache.crimson.parser.*;
import cn.com.info21.jdom.Document;
import cn.com.info21.jdom.JDOMException;
import org.xml.sax.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

// Referenced classes of package cn.com.info21.jdom.input:
//            SAXHandler, BuilderErrorHandler, JDOMFactory

public class SAXBuilder {

	public SAXBuilder() {
		this(false);
	}

	public SAXBuilder(boolean validate) {
		expand = true;
		saxErrorHandler = null;
		saxEntityResolver = null;
		saxDTDHandler = null;
		saxXMLFilter = null;
		factory = null;
		ignoringWhite = false;
		this.validate = validate;
	}

	public SAXBuilder(String saxDriverClass) {
		this(saxDriverClass, false);
	}

	public SAXBuilder(String saxDriverClass, boolean validate) {
		expand = true;
		saxErrorHandler = null;
		saxEntityResolver = null;
		saxDTDHandler = null;
		saxXMLFilter = null;
		factory = null;
		ignoringWhite = false;
		this.saxDriverClass = saxDriverClass;
		this.validate = validate;
	}

	public void setFactory(JDOMFactory factory) {
		this.factory = factory;
	}

	public void setValidation(boolean validate) {
		this.validate = validate;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		saxErrorHandler = errorHandler;
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		saxEntityResolver = entityResolver;
	}

	public void setDTDHandler(DTDHandler dtdHandler) {
		saxDTDHandler = dtdHandler;
	}

	public void setXMLFilter(XMLFilter xmlFilter) {
		saxXMLFilter = xmlFilter;
	}

	public void setIgnoringElementContentWhitespace(boolean ignoringWhite) {
		this.ignoringWhite = ignoringWhite;
	}

	public Document build(InputSource in) throws JDOMException {
		SAXHandler contentHandler = null;
		try {
			contentHandler = createContentHandler();
			configureContentHandler(contentHandler);
			XMLReader parser = createParser();
			configureParser(parser, contentHandler);
			parser.parse(in);
			Document document = contentHandler.getDocument();
			return document;
		} catch (Exception e) {
			if (e instanceof SAXParseException) {
				SAXParseException p = (SAXParseException) e;
				String systemId = p.getSystemId();
				if (systemId != null)
					throw new JDOMException(
						"Error on line "
							+ p.getLineNumber()
							+ " of document "
							+ systemId,
						e);
				else
					throw new JDOMException(
						"Error on line " + p.getLineNumber(),
						e);
			}
			if (e instanceof JDOMException)
				throw (JDOMException) e;
			else
				throw new JDOMException("Error in building", e);
		} finally {
			contentHandler = null;
		}
	}

	protected SAXHandler createContentHandler() throws Exception {
		SAXHandler contentHandler = new SAXHandler(factory);
		return contentHandler;
	}

	protected void configureContentHandler(SAXHandler contentHandler)
		throws Exception {
		contentHandler.setExpandEntities(expand);
		contentHandler.setIgnoringElementContentWhitespace(ignoringWhite);
	}

	protected XMLReader createParser() throws Exception {
		XMLReader parser = new XMLReaderImpl();
		parser.setFeature("http://xml.org/sax/features/validation", validate);
		if (saxXMLFilter != null) {
			XMLFilter root;
			for (root = saxXMLFilter;
				root.getParent() instanceof XMLFilter;
				root = (XMLFilter) root.getParent());
			root.setParent(parser);
			parser = saxXMLFilter;
		}
		return parser;
	}

	protected void configureParser(XMLReader parser, SAXHandler contentHandler)
		throws Exception {
		parser.setContentHandler(contentHandler);
		if (saxEntityResolver != null)
			parser.setEntityResolver(saxEntityResolver);
		if (saxDTDHandler != null)
			parser.setDTDHandler(saxDTDHandler);
		if (saxErrorHandler != null)
			parser.setErrorHandler(saxErrorHandler);
		else
			parser.setErrorHandler(new BuilderErrorHandler());
		boolean lexicalReporting = false;
		try {
			parser.setProperty(
				"http://xml.org/sax/handlers/LexicalHandler",
				contentHandler);
			lexicalReporting = true;
		} catch (SAXNotSupportedException saxnotsupportedexception) {
		} catch (SAXNotRecognizedException saxnotrecognizedexception1) {
		}
		if (!lexicalReporting)
			try {
				parser.setProperty(
					"http://xml.org/sax/properties/lexical-handler",
					contentHandler);
				lexicalReporting = true;
			} catch (SAXNotSupportedException saxnotsupportedexception1) {
			} catch (SAXNotRecognizedException saxnotrecognizedexception2) {
			}
		if (!expand)
			try {
				parser.setProperty(
					"http://xml.org/sax/properties/declaration-handler",
					contentHandler);
			} catch (SAXNotSupportedException saxnotsupportedexception2) {
			} catch (SAXNotRecognizedException saxnotrecognizedexception3) {
			}
		try {
			internalSetFeature(
				parser,
				"http://xml.org/sax/features/validation",
				validate,
				"Validation");
		} catch (JDOMException e) {
			if (validate)
				throw e;
		}
		internalSetFeature(
			parser,
			"http://xml.org/sax/features/namespaces",
			true,
			"Namespaces");
		internalSetFeature(
			parser,
			"http://xml.org/sax/features/namespace-prefixes",
			false,
			"Namespace prefixes");
		try {
			if (parser
				.getFeature("http://xml.org/sax/features/external-general-entities")
				!= expand)
				parser.setFeature(
					"http://xml.org/sax/features/external-general-entities",
					expand);
		} catch (SAXNotRecognizedException saxnotrecognizedexception) {
		} catch (SAXNotSupportedException saxnotsupportedexception3) {
		}
	}

	private void internalSetFeature(
		XMLReader parser,
		String feature,
		boolean value,
		String displayName)
		throws JDOMException {
		try {
			parser.setFeature(feature, value);
		} catch (SAXNotSupportedException saxnotsupportedexception) {
			throw new JDOMException(
				displayName
					+ " not supported for SAX driver "
					+ saxDriverClass);
		} catch (SAXNotRecognizedException saxnotrecognizedexception) {
			throw new JDOMException(
				displayName
					+ " feature not recognized for SAX driver"
					+ saxDriverClass);
		}
	}

	public Document build(InputStream in) throws JDOMException {
		return build(new InputSource(in));
	}

	public Document build(File file) throws JDOMException {
		try {
			URL url = fileToURL(file);
			return build(url);
		} catch (MalformedURLException e) {
			throw new JDOMException("Error in building", e);
		}
	}

	public Document build(URL url) throws JDOMException {
		String systemID = url.toExternalForm();
		return build(new InputSource(systemID));
	}

	public Document build(InputStream in, String systemId)
		throws JDOMException {
		InputSource src = new InputSource(in);
		src.setSystemId(systemId);
		return build(src);
	}

	public Document build(Reader characterStream) throws JDOMException {
		return build(new InputSource(characterStream));
	}

	public Document build(Reader characterStream, String SystemId)
		throws JDOMException {
		InputSource src = new InputSource(characterStream);
		src.setSystemId(SystemId);
		return build(src);
	}

	public Document build(String systemId) throws JDOMException {
		return build(new InputSource(systemId));
	}

	protected URL fileToURL(File f) throws MalformedURLException {
		String path = f.getAbsolutePath();
		if (File.separatorChar != '/')
			path = path.replace(File.separatorChar, '/');
		if (!path.startsWith("/"))
			path = "/" + path;
		if (!path.endsWith("/") && f.isDirectory())
			path = path + "/";
		return new URL("file", "", path);
	}

	public void setExpandEntities(boolean expand) {
		this.expand = expand;
	}

	private static final String CVS_ID =
		"@(#) $RCSfile: SAXBuilder.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:31 $ $Name:  $";
	private static final String DEFAULT_SAX_DRIVER =
		"org.apache.xerces.parsers.SAXParser";
	private boolean validate;
	private boolean expand;
	private String saxDriverClass;
	private ErrorHandler saxErrorHandler;
	private EntityResolver saxEntityResolver;
	private DTDHandler saxDTDHandler;
	private XMLFilter saxXMLFilter;
	protected JDOMFactory factory;
	private boolean ignoringWhite;
}
