// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Document.java

package cn.com.info21.jdom;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package cn.com.info21.jdom:
//            Element, IllegalAddException, Comment, ProcessingInstruction, 
//            DocType, PartialList

public class Document
    implements Serializable, Cloneable
{

    protected Document()
    {
        content = new ArrayList(5);
    }

    public Document(Element rootElement, DocType docType)
    {
        content = new ArrayList(5);
        setRootElement(rootElement);
        setDocType(docType);
    }

    public Document(Element rootElement)
    {
        this(rootElement, null);
    }

    public Document(List content, DocType docType)
    {
        this.content = new ArrayList(5);
        setContent(content);
        setDocType(docType);
    }

    public Document(List content)
    {
        this(content, null);
    }

    public Element getRootElement()
    {
        for(Iterator itr = content.iterator(); itr.hasNext();)
        {
            Object obj = itr.next();
            if(obj instanceof Element)
                return (Element)obj;
        }

        return null;
    }

    public Document setRootElement(Element rootElement)
    {
        if(rootElement == null)
            return this;
        if(rootElement.isRootElement())
            throw new IllegalAddException(this, rootElement, "The element already has an existing parent (the document root)");
        if(rootElement.getParent() != null)
            throw new IllegalAddException(this, rootElement, "The element already has an existing parent \"" + rootElement.getParent().getQualifiedName() + "\"");
        boolean hadRoot = false;
        ListIterator itr;
        for(itr = content.listIterator(); itr.hasNext();)
        {
            Object obj = itr.next();
            if(obj instanceof Element)
            {
                Element departingRoot = (Element)obj;
                departingRoot.setDocument(null);
                itr.set(rootElement);
                hadRoot = true;
            }
        }

        if(!hadRoot)
            itr.add(rootElement);
        rootElement.setDocument(this);
        return this;
    }

    public DocType getDocType()
    {
        return docType;
    }

    public Document setDocType(DocType docType)
    {
        if(docType != null && docType.getDocument() != null)
            throw new IllegalAddException(this, docType, "The docType already is attached to a document");
        if(docType != null)
            docType.setDocument(this);
        this.docType = docType;
        return this;
    }

    public Document addContent(ProcessingInstruction pi)
    {
        if(pi.getParent() != null)
            throw new IllegalAddException(this, pi, "The PI already has an existing parent \"" + pi.getParent().getQualifiedName() + "\"");
        if(pi.getDocument() != null)
        {
            throw new IllegalAddException(this, pi, "The PI already has an existing parent (the document root)");
        } else
        {
            content.add(pi);
            pi.setDocument(this);
            return this;
        }
    }

    public Document addContent(Comment comment)
    {
        if(comment.getParent() != null)
            throw new IllegalAddException(this, comment, "The comment already has an existing parent \"" + comment.getParent().getQualifiedName() + "\"");
        if(comment.getDocument() != null)
        {
            throw new IllegalAddException(this, comment, "The element already has an existing parent (the document root)");
        } else
        {
            content.add(comment);
            comment.setDocument(this);
            return this;
        }
    }

    public List getContent()
    {
        return content;
    }

    public Document setContent(List newContent)
    {
        if(newContent == null)
            return this;
        Element oldRoot = getRootElement();
        List oldContent = content;
        content = new ArrayList(5);
        RuntimeException ex = null;
        boolean didRoot = false;
        int itemsAdded = 0;
        try
        {
            for(Iterator i = newContent.iterator(); i.hasNext();)
            {
                Object obj = i.next();
                if(obj instanceof Element)
                {
                    if(!didRoot)
                    {
                        setRootElement((Element)obj);
                        if(oldRoot != null)
                            oldRoot.setDocument(null);
                        didRoot = true;
                    } else
                    {
                        throw new IllegalAddException("A Document may contain only one root element");
                    }
                } else
                if(obj instanceof Comment)
                    addContent((Comment)obj);
                else
                if(obj instanceof ProcessingInstruction)
                    addContent((ProcessingInstruction)obj);
                else
                    throw new IllegalAddException("A Document may directly contain only objects of type Element, Comment, and ProcessingInstruction: " + (obj != null ? obj.getClass().getName() : "null") + " is not allowed");
                itemsAdded++;
            }

            if(!didRoot)
                throw new IllegalAddException("A Document must contain a root element");
        }
        catch(RuntimeException e)
        {
            ex = e;
        }
        finally
        {
            if(ex != null)
            {
                content = oldContent;
                if(oldRoot != null)
                    oldRoot.setDocument(this);
                Iterator itr = newContent.iterator();
                while(itemsAdded-- > 0) 
                {
                    Object obj = itr.next();
                    if(obj instanceof Element)
                        ((Element)obj).setDocument(null);
                    else
                    if(obj instanceof Comment)
                        ((Comment)obj).setDocument(null);
                    else
                    if(obj instanceof ProcessingInstruction)
                        ((ProcessingInstruction)obj).setDocument(null);
                }
                throw ex;
            }
        }
        for(Iterator itr = oldContent.iterator(); itr.hasNext();)
        {
            Object obj = itr.next();
            if(obj instanceof Element)
                ((Element)obj).setDocument(null);
            else
            if(obj instanceof Comment)
                ((Comment)obj).setDocument(null);
            else
            if(obj instanceof ProcessingInstruction)
                ((ProcessingInstruction)obj).setDocument(null);
        }

        return this;
    }

    public String toString()
    {
        StringBuffer stringForm = (new StringBuffer()).append("[Document: ");
        if(docType != null)
            stringForm.append(docType.toString()).append(", ");
        else
            stringForm.append(" No DOCTYPE declaration, ");
        Element rootElement = getRootElement();
        if(rootElement != null)
            stringForm.append("Root is ").append(rootElement.toString());
        else
            stringForm.append(" No root element");
        stringForm.append("]");
        return stringForm.toString();
    }

    public final boolean equals(Object ob)
    {
        return ob == this;
    }

    public final int hashCode()
    {
        return super.hashCode();
    }

    public Object clone()
    {
        Document doc = null;
        try
        {
            doc = (Document)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        doc.content = new ArrayList(5);
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof Element)
                doc.setRootElement((Element)((Element)obj).clone());
            else
            if(obj instanceof Comment)
                doc.addContent((Comment)((Comment)obj).clone());
            else
            if(obj instanceof ProcessingInstruction)
                doc.addContent((ProcessingInstruction)((ProcessingInstruction)obj).clone());
        }

        if(docType != null)
            doc.docType = (DocType)docType.clone();
        return doc;
    }

    public boolean removeContent(ProcessingInstruction pi)
    {
        if(content == null)
            return false;
        else
            return content.remove(pi);
    }

    public boolean removeContent(Comment comment)
    {
        if(content == null)
            return false;
        if(content.remove(comment))
        {
            comment.setDocument(null);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        throw new RuntimeException("Document.getSerializedForm() is not yet implemented");
    }

    /**
     * @deprecated Method getProcessingInstructions is deprecated
     */

    public List getProcessingInstructions()
    {
        PartialList pis = new PartialList(content);
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof ProcessingInstruction)
                pis.addPartial(obj);
        }

        return pis;
    }

    /**
     * @deprecated Method getProcessingInstructions is deprecated
     */

    public List getProcessingInstructions(String target)
    {
        PartialList pis = new PartialList(content);
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if((obj instanceof ProcessingInstruction) && ((ProcessingInstruction)obj).getTarget().equals(target))
                pis.addPartial(obj);
        }

        return pis;
    }

    /**
     * @deprecated Method getProcessingInstruction is deprecated
     */

    public ProcessingInstruction getProcessingInstruction(String target)
    {
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if((obj instanceof ProcessingInstruction) && ((ProcessingInstruction)obj).getTarget().equals(target))
                return (ProcessingInstruction)obj;
        }

        return null;
    }

    /**
     * @deprecated Method removeProcessingInstruction is deprecated
     */

    public boolean removeProcessingInstruction(String target)
    {
        ProcessingInstruction pi = getProcessingInstruction(target);
        if(pi == null)
            return false;
        else
            return removeContent(pi);
    }

    /**
     * @deprecated Method removeProcessingInstructions is deprecated
     */

    public boolean removeProcessingInstructions(String target)
    {
        boolean deletedSome = false;
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof ProcessingInstruction)
            {
                ProcessingInstruction pi = (ProcessingInstruction)obj;
                if(pi.getTarget().equals(target))
                {
                    deletedSome = true;
                    i.remove();
                    pi.setDocument(null);
                }
            }
        }

        return deletedSome;
    }

    /**
     * @deprecated Method setProcessingInstructions is deprecated
     */

    public Document setProcessingInstructions(List pis)
    {
        List current = getProcessingInstructions();
        for(Iterator i = current.iterator(); i.hasNext(); i.remove());
        content.addAll(pis);
        return this;
    }

    /**
     * @deprecated Method getMixedContent is deprecated
     */

    public List getMixedContent()
    {
        return getContent();
    }

    /**
     * @deprecated Method setMixedContent is deprecated
     */

    public Document setMixedContent(List mixedContent)
    {
        return setContent(mixedContent);
    }

    private static final String CVS_ID = "@(#) $RCSfile: Document.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
    private static final int INITIAL_ARRAY_SIZE = 5;
    protected List content;
    protected DocType docType;
}
