// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLOutputter.java

package cn.com.info21.jdom.output;

import cn.com.info21.jdom.*;
import java.io.*;
import java.util.*;

// Referenced classes of package cn.com.info21.jdom.output:
//            NamespaceStack

public class XMLOutputter
    implements Cloneable
{
    protected class NamespaceStack extends cn.com.info21.jdom.output.NamespaceStack
    {

        protected NamespaceStack()
        {
        }
    }


    public XMLOutputter()
    {
        omitDeclaration = false;
        encoding = "UTF-8";
        omitEncoding = false;
        indent = null;
        expandEmptyElements = false;
        newlines = false;
        lineSeparator = "\r\n";
        textNormalize = false;
    }

    public XMLOutputter(String indent)
    {
        omitDeclaration = false;
        encoding = "UTF-8";
        omitEncoding = false;
        this.indent = null;
        expandEmptyElements = false;
        newlines = false;
        lineSeparator = "\r\n";
        textNormalize = false;
        this.indent = indent;
    }

    public XMLOutputter(String indent, boolean newlines)
    {
        omitDeclaration = false;
        encoding = "UTF-8";
        omitEncoding = false;
        this.indent = null;
        expandEmptyElements = false;
        this.newlines = false;
        lineSeparator = "\r\n";
        textNormalize = false;
        this.indent = indent;
        this.newlines = newlines;
    }

    public XMLOutputter(String indent, boolean newlines, String encoding)
    {
        omitDeclaration = false;
        this.encoding = "UTF-8";
        omitEncoding = false;
        this.indent = null;
        expandEmptyElements = false;
        this.newlines = false;
        lineSeparator = "\r\n";
        textNormalize = false;
        this.indent = indent;
        this.newlines = newlines;
        this.encoding = encoding;
    }

    public XMLOutputter(XMLOutputter that)
    {
        omitDeclaration = false;
        encoding = "UTF-8";
        omitEncoding = false;
        indent = null;
        expandEmptyElements = false;
        newlines = false;
        lineSeparator = "\r\n";
        textNormalize = false;
        omitDeclaration = that.omitDeclaration;
        omitEncoding = that.omitEncoding;
        indent = that.indent;
        expandEmptyElements = that.expandEmptyElements;
        newlines = that.newlines;
        encoding = that.encoding;
        lineSeparator = that.lineSeparator;
        textNormalize = that.textNormalize;
    }

    public void setLineSeparator(String separator)
    {
        lineSeparator = separator;
    }

    public void setNewlines(boolean newlines)
    {
        this.newlines = newlines;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public void setOmitEncoding(boolean omitEncoding)
    {
        this.omitEncoding = omitEncoding;
    }

    public void setOmitDeclaration(boolean omitDeclaration)
    {
        this.omitDeclaration = omitDeclaration;
    }

    public void setExpandEmptyElements(boolean expandEmptyElements)
    {
        this.expandEmptyElements = expandEmptyElements;
    }

    public void setTextNormalize(boolean textNormalize)
    {
        this.textNormalize = textNormalize;
    }

    /**
     * @deprecated Method setTrimText is deprecated
     */

    public void setTrimText(boolean textTrim)
    {
        textNormalize = textTrim;
    }

    public void setIndent(String indent)
    {
        if("".equals(indent))
            indent = null;
        this.indent = indent;
    }

    public void setIndent(boolean doIndent)
    {
        if(doIndent)
            indent = "  ";
        else
            indent = null;
    }

    public void setIndentSize(int indentSize)
    {
        StringBuffer indentBuffer = new StringBuffer();
        for(int i = 0; i < indentSize; i++)
            indentBuffer.append(" ");

        indent = indentBuffer.toString();
    }

    protected void indent(Writer out, int level)
        throws IOException
    {
        if(indent != null && !indent.equals(""))
        {
            for(int i = 0; i < level; i++)
                out.write(indent);

        }
    }

    protected void maybePrintln(Writer out)
        throws IOException
    {
        if(newlines)
            out.write(lineSeparator);
    }

    protected Writer makeWriter(OutputStream out)
        throws UnsupportedEncodingException
    {
        return makeWriter(out, encoding);
    }

    protected Writer makeWriter(OutputStream out, String enc)
        throws UnsupportedEncodingException
    {
        if("UTF-8".equals(enc))
            enc = "UTF8";
        Writer writer = new OutputStreamWriter(new BufferedOutputStream(out), enc);
        return writer;
    }

    public void output(Document doc, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(doc, writer);
    }

    public void output(Document doc, Writer out)
        throws IOException
    {
        printDeclaration(doc, out, encoding);
        if(doc.getDocType() != null)
            printDocType(doc.getDocType(), out);
        for(Iterator i = doc.getContent().iterator(); i.hasNext(); maybePrintln(out))
        {
            Object obj = i.next();
            if(obj instanceof Element)
                printElement(doc.getRootElement(), out, 0, createNamespaceStack());
            else
            if(obj instanceof Comment)
                printComment((Comment)obj, out);
            else
            if(obj instanceof ProcessingInstruction)
                printProcessingInstruction((ProcessingInstruction)obj, out);
        }

        out.write(lineSeparator);
        out.flush();
    }

    public void output(Element element, Writer out)
        throws IOException
    {
        printElement(element, out, 0, createNamespaceStack());
        out.flush();
    }

    public void output(Element element, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(element, writer);
    }

    public void outputElementContent(Element element, Writer out)
        throws IOException
    {
        List eltContent = element.getContent();
        printElementContent(element, out, 0, createNamespaceStack(), eltContent);
        out.flush();
    }

    public void outputElementContent(Element element, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        outputElementContent(element, writer);
    }

    public void output(CDATA cdata, Writer out)
        throws IOException
    {
        printCDATA(cdata, out);
        out.flush();
    }

    public void output(CDATA cdata, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(cdata, writer);
    }

    public void output(Comment comment, Writer out)
        throws IOException
    {
        printComment(comment, out);
        out.flush();
    }

    public void output(Comment comment, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(comment, writer);
    }

    public void output(String string, Writer out)
        throws IOException
    {
        printString(string, out);
        out.flush();
    }

    public void output(String string, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(string, writer);
    }

    public void output(Text text, Writer out)
        throws IOException
    {
        printString(text.getValue(), out);
        out.flush();
    }

    public void output(Text text, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(text, writer);
    }

    public void output(EntityRef entity, Writer out)
        throws IOException
    {
        printEntityRef(entity, out);
        out.flush();
    }

    public void output(EntityRef entity, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(entity, writer);
    }

    public void output(ProcessingInstruction pi, Writer out)
        throws IOException
    {
        printProcessingInstruction(pi, out);
        out.flush();
    }

    public void output(ProcessingInstruction pi, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(pi, writer);
    }

    public void output(DocType doctype, Writer out)
        throws IOException
    {
        printDocType(doctype, out);
        out.flush();
    }

    public void output(DocType doctype, OutputStream out)
        throws IOException
    {
        Writer writer = makeWriter(out);
        output(doctype, writer);
    }

    public String outputString(Document doc)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(doc, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    public String outputString(Element element)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(element, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    public String outputString(Comment comment)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(comment, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    public String outputString(CDATA cdata)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(cdata, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    public String outputString(ProcessingInstruction pi)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(pi, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    public String outputString(DocType doctype)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(doctype, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    public String outputString(EntityRef entity)
    {
        StringWriter out = new StringWriter();
        try
        {
            output(entity, out);
        }
        catch(IOException ioexception) { }
        return out.toString();
    }

    protected void printDeclaration(Document doc, Writer out, String encoding)
        throws IOException
    {
        if(!omitDeclaration)
        {
            out.write("<?xml version=\"1.0\"");
            if(!omitEncoding)
                out.write(" encoding=\"" + encoding + "\"");
            out.write("?>");
            out.write(lineSeparator);
        }
    }

    protected void printDocType(DocType docType, Writer out)
        throws IOException
    {
        if(docType == null)
            return;
        String publicID = docType.getPublicID();
        String systemID = docType.getSystemID();
        boolean hasPublic = false;
        out.write("<!DOCTYPE ");
        out.write(docType.getElementName());
        if(publicID != null && !publicID.equals(""))
        {
            out.write(" PUBLIC \"");
            out.write(publicID);
            out.write("\"");
            hasPublic = true;
        }
        if(systemID != null && !systemID.equals(""))
        {
            if(!hasPublic)
                out.write(" SYSTEM");
            out.write(" \"");
            out.write(systemID);
            out.write("\"");
        }
        out.write(">");
        out.write(lineSeparator);
    }

    protected void printComment(Comment comment, Writer out)
        throws IOException
    {
        out.write("<!--");
        out.write(comment.getText());
        out.write("-->");
    }

    protected void printProcessingInstruction(ProcessingInstruction pi, Writer out)
        throws IOException
    {
        String target = pi.getTarget();
        String rawData = pi.getData();
        if(!"".equals(rawData))
        {
            out.write("<?");
            out.write(target);
            out.write(" ");
            out.write(rawData);
            out.write("?>");
        } else
        {
            out.write("<?");
            out.write(target);
            out.write("?>");
        }
    }

    protected void printCDATA(CDATA cdata, Writer out)
        throws IOException
    {
        out.write("<![CDATA[");
        out.write(cdata.getText());
        out.write("]]>");
    }

    protected void printElement(Element element, Writer out, int indentLevel, NamespaceStack namespaces)
        throws IOException
    {
        List eltContent = element.getContent();
        out.write("<");
        out.write(element.getQualifiedName());
        int previouslyDeclaredNamespaces = namespaces.size();
        printElementNamespace(element, out, namespaces);
        printAdditionalNamespaces(element, out, namespaces);
        printAttributes(element.getAttributes(), element, out, namespaces);
        boolean stringOnly = true;
        for(Iterator itr = eltContent.iterator(); itr.hasNext();)
        {
            Object o = itr.next();
            if(!(o instanceof String) && !(o instanceof CDATA))
            {
                stringOnly = false;
                break;
            }
        }

        boolean empty = false;
        if(stringOnly)
        {
            String elementText = textNormalize ? element.getTextNormalize() : element.getText();
            if(elementText == null || elementText.equals(""))
                empty = true;
        }
        if(empty)
        {
            if(!expandEmptyElements)
            {
                out.write(" />");
            } else
            {
                out.write("></");
                out.write(element.getQualifiedName());
                out.write(">");
            }
        } else
        {
            out.write(">");
            printElementContent(element, out, indentLevel + 1, namespaces, eltContent);
            out.write("</");
            out.write(element.getQualifiedName());
            out.write(">");
        }
        for(; namespaces.size() > previouslyDeclaredNamespaces; namespaces.pop());
    }

    protected void printElementContent(Element element, Writer out, int indentLevel, NamespaceStack namespaces, List eltContent)
        throws IOException
    {
        boolean empty = eltContent.size() == 0;
        boolean stringOnly = true;
        if(!empty)
            stringOnly = isStringOnly(eltContent);
        if(stringOnly)
        {
            Class justOutput = null;
            boolean endedWithWhite = false;
            for(Iterator itr = eltContent.iterator(); itr.hasNext();)
            {
                Object content = itr.next();
                if(content instanceof String)
                {
                    String scontent = (String)content;
                    if(justOutput == (cn.com.info21.jdom.CDATA.class) && (textNormalize && startsWithWhite(scontent)))
                        out.write(" ");
                    printString(scontent, out);
                    endedWithWhite = endsWithWhite(scontent);
                    justOutput = java.lang.String.class;
                } else
                {
                    if(justOutput == (java.lang.String.class) && (textNormalize && endedWithWhite))
                        out.write(" ");
                    printCDATA((CDATA)content, out);
                    justOutput = cn.com.info21.jdom.CDATA.class;
                }
            }

        } else
        {
            Object content = null;
            Class justOutput = null;
            boolean endedWithWhite = false;
            boolean wasFullyWhite = false;
            for(Iterator itr = eltContent.iterator(); itr.hasNext();)
            {
                content = itr.next();
                if(content instanceof Comment)
                {
                    if(justOutput != (java.lang.String.class) || !wasFullyWhite)
                    {
                        maybePrintln(out);
                        indent(out, indentLevel);
                    }
                    printComment((Comment)content, out);
                    justOutput = cn.com.info21.jdom.Comment.class;
                } else
                if(content instanceof String)
                {
                    String scontent = (String)content;
                    if(justOutput == (cn.com.info21.jdom.CDATA.class) && (textNormalize && startsWithWhite(scontent)))
                        out.write(" ");
                    else
                    if(justOutput != (cn.com.info21.jdom.CDATA.class) && justOutput != (java.lang.String.class))
                    {
                        maybePrintln(out);
                        indent(out, indentLevel);
                    }
                    printString(scontent, out);
                    endedWithWhite = endsWithWhite(scontent);
                    justOutput = java.lang.String.class;
                    wasFullyWhite = scontent.trim().length() == 0;
                } else
                if(content instanceof Element)
                {
                    if(justOutput != (java.lang.String.class) || !wasFullyWhite)
                    {
                        maybePrintln(out);
                        indent(out, indentLevel);
                    }
                    printElement((Element)content, out, indentLevel, namespaces);
                    justOutput = cn.com.info21.jdom.Element.class;
                } else
                if(content instanceof EntityRef)
                {
                    if(justOutput != (java.lang.String.class) || !wasFullyWhite)
                    {
                        maybePrintln(out);
                        indent(out, indentLevel);
                    }
                    printEntityRef((EntityRef)content, out);
                    justOutput = cn.com.info21.jdom.EntityRef.class;
                } else
                if(content instanceof ProcessingInstruction)
                {
                    if(justOutput != (java.lang.String.class) || !wasFullyWhite)
                    {
                        maybePrintln(out);
                        indent(out, indentLevel);
                    }
                    printProcessingInstruction((ProcessingInstruction)content, out);
                    justOutput = cn.com.info21.jdom.ProcessingInstruction.class;
                } else
                if(content instanceof CDATA)
                {
                    if(justOutput == (java.lang.String.class) && (textNormalize && endedWithWhite))
                        out.write(" ");
                    else
                    if(justOutput != (java.lang.String.class) && justOutput != (cn.com.info21.jdom.CDATA.class))
                    {
                        maybePrintln(out);
                        indent(out, indentLevel);
                    }
                    printCDATA((CDATA)content, out);
                    justOutput = cn.com.info21.jdom.CDATA.class;
                }
            }

            maybePrintln(out);
            indent(out, indentLevel - 1);
        }
    }

    protected void printString(String s, Writer out)
        throws IOException
    {
        s = escapeElementEntities(s);
        if(textNormalize)
        {
            for(StringTokenizer tokenizer = new StringTokenizer(s); tokenizer.hasMoreTokens();)
            {
                String token = tokenizer.nextToken();
                out.write(token);
                if(tokenizer.hasMoreTokens())
                    out.write(" ");
            }

        } else
        {
            out.write(s);
        }
    }

    protected void printEntityRef(EntityRef entity, Writer out)
        throws IOException
    {
        out.write("&" + entity.getName() + ";");
    }

    private void printNamespace(Namespace ns, Writer out)
        throws IOException
    {
        out.write(" xmlns");
        String prefix = ns.getPrefix();
        if(!prefix.equals(""))
        {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(ns.getURI());
        out.write("\"");
    }

    protected void printAttributes(List attributes, Element parent, Writer out, NamespaceStack namespaces)
        throws IOException
    {
        java.util.Set prefixes = new HashSet();
        for(Iterator itr = attributes.iterator(); itr.hasNext(); out.write("\""))
        {
            Attribute attribute = (Attribute)itr.next();
            Namespace ns = attribute.getNamespace();
            if(ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE)
            {
                String prefix = ns.getPrefix();
                String uri = namespaces.getURI(prefix);
                if(!ns.getURI().equals(uri))
                {
                    printNamespace(ns, out);
                    namespaces.push(ns);
                }
            }
            out.write(" ");
            out.write(attribute.getQualifiedName());
            out.write("=");
            out.write("\"");
            out.write(escapeAttributeEntities(attribute.getValue()));
        }

    }

    private void printElementNamespace(Element element, Writer out, NamespaceStack namespaces)
        throws IOException
    {
        Namespace ns = element.getNamespace();
        if(ns != Namespace.XML_NAMESPACE && (ns != Namespace.NO_NAMESPACE || namespaces.getURI("") != null))
        {
            String prefix = ns.getPrefix();
            String uri = namespaces.getURI(prefix);
            if(!ns.getURI().equals(uri))
            {
                namespaces.push(ns);
                printNamespace(ns, out);
            }
        }
    }

    private void printAdditionalNamespaces(Element element, Writer out, NamespaceStack namespaces)
        throws IOException
    {
        List additionalNamespaces = element.getAdditionalNamespaces();
        if(additionalNamespaces != null)
        {
            for(Iterator itr = additionalNamespaces.iterator(); itr.hasNext();)
            {
                Namespace additional = (Namespace)itr.next();
                String prefix = additional.getPrefix();
                String uri = namespaces.getURI(prefix);
                if(!additional.getURI().equals(uri))
                {
                    namespaces.push(additional);
                    printNamespace(additional, out);
                }
            }

        }
    }

    protected String escapeAttributeEntities(String st)
    {
        StringBuffer buff = new StringBuffer();
        char block[] = st.toCharArray();
        String stEntity = null;
        int i = 0;
        int last = 0;
        for(; i < block.length; i++)
        {
            switch(block[i])
            {
            case 60: // '<'
                stEntity = "&lt;";
                break;

            case 62: // '>'
                stEntity = "&gt;";
                break;

            case 34: // '"'
                stEntity = "&quot;";
                break;

            case 38: // '&'
                stEntity = "&amp;";
                break;
            }
            if(stEntity != null)
            {
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }

        if(last < block.length)
            buff.append(block, last, i - last);
        return buff.toString();
    }

    protected String escapeElementEntities(String st)
    {
        StringBuffer buff = new StringBuffer();
        char block[] = st.toCharArray();
        String stEntity = null;
        int i = 0;
        int last = 0;
        for(; i < block.length; i++)
        {
            switch(block[i])
            {
            case 60: // '<'
                stEntity = "&lt;";
                break;

            case 62: // '>'
                stEntity = "&gt;";
                break;

            case 38: // '&'
                stEntity = "&amp;";
                break;
            }
            if(stEntity != null)
            {
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }

        if(last < block.length)
            buff.append(block, last, i - last);
        return buff.toString();
    }

    public int parseArgs(String args[], int i)
    {
        for(; i < args.length; i++)
            if(args[i].equals("-omitDeclaration"))
                setOmitDeclaration(true);
            else
            if(args[i].equals("-omitEncoding"))
                setOmitEncoding(true);
            else
            if(args[i].equals("-indent"))
                setIndent(args[++i]);
            else
            if(args[i].equals("-indentSize"))
                setIndentSize(Integer.parseInt(args[++i]));
            else
            if(args[i].startsWith("-expandEmpty"))
                setExpandEmptyElements(true);
            else
            if(args[i].equals("-encoding"))
                setEncoding(args[++i]);
            else
            if(args[i].equals("-newlines"))
                setNewlines(true);
            else
            if(args[i].equals("-lineSeparator"))
                setLineSeparator(args[++i]);
            else
            if(args[i].equals("-textNormalize"))
                setTextNormalize(true);
            else
                return i;

        return i;
    }

    private boolean startsWithWhite(String s)
    {
        return s.length() > 0 && s.charAt(0) <= ' ';
    }

    private boolean endsWithWhite(String s)
    {
        return s.length() > 0 && s.charAt(s.length() - 1) <= ' ';
    }

    private boolean isWhitespace(String s)
    {
        char c[] = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            if(" \t\n\r".indexOf(c[i]) == -1)
                return false;

        return true;
    }

    private String getElementText(Element element)
    {
        return textNormalize ? element.getTextNormalize() : element.getText();
    }

    private boolean isEmpty(Element element)
    {
        List eltContent = element.getContent();
        if(eltContent.size() == 0)
            return true;
        if(isStringOnly(eltContent))
        {
            String elementText = getElementText(element);
            if(elementText == null || elementText.equals(""))
                return true;
        }
        return false;
    }

    private boolean isStringOnly(List eltContent)
    {
        for(Iterator itr = eltContent.iterator(); itr.hasNext();)
        {
            Object o = itr.next();
            if(!(o instanceof String) && !(o instanceof CDATA))
                return false;
        }

        return true;
    }

    protected NamespaceStack createNamespaceStack()
    {
        return new NamespaceStack();
    }

    /**
     * @deprecated Method setPadText is deprecated
     */

    public void setPadText(boolean flag)
    {
    }

    /**
     * @deprecated Method setIndentLevel is deprecated
     */

    public void setIndentLevel(int i)
    {
    }

    /**
     * @deprecated Method setSuppressDeclaration is deprecated
     */

    public void setSuppressDeclaration(boolean suppressDeclaration)
    {
        omitDeclaration = suppressDeclaration;
    }

    private static final String CVS_ID = "@(#) $RCSfile: XMLOutputter.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:30 $ $Name:  $";
    protected static final String STANDARD_INDENT = "  ";
    private boolean omitDeclaration;
    private String encoding;
    private boolean omitEncoding;
    private String indent;
    private boolean expandEmptyElements;
    private boolean newlines;
    private String lineSeparator;
    private boolean textNormalize;
}
