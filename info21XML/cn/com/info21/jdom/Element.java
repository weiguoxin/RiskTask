// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Element.java

package cn.com.info21.jdom;

import java.io.*;
import java.util.*;

// Referenced classes of package cn.com.info21.jdom:
//            Namespace, IllegalNameException, IllegalAddException, Attribute, 
//            Document, CDATA, PartialList, Comment, 
//            ProcessingInstruction, EntityRef, Verifier

public class Element
    implements Serializable, Cloneable
{

    protected Element()
    {
    }

    public Element(String name, Namespace namespace)
    {
        setName(name);
        setNamespace(namespace);
    }

    public Element(String name)
    {
        this(name, (Namespace)null);
    }

    public Element(String name, String uri)
    {
        this(name, Namespace.getNamespace("", uri));
    }

    public Element(String name, String prefix, String uri)
    {
        this(name, Namespace.getNamespace(prefix, uri));
    }

    public String getName()
    {
        return name;
    }

    public Element setName(String name)
    {
        String reason;
        if((reason = Verifier.checkElementName(name)) != null)
        {
            throw new IllegalNameException(name, "element", reason);
        } else
        {
            this.name = name;
            return this;
        }
    }

    public Namespace getNamespace()
    {
        return namespace;
    }

    public Element setNamespace(Namespace namespace)
    {
        if(namespace == null)
            namespace = Namespace.NO_NAMESPACE;
        this.namespace = namespace;
        return this;
    }

    public String getNamespacePrefix()
    {
        return namespace.getPrefix();
    }

    public String getNamespaceURI()
    {
        return namespace.getURI();
    }

    public Namespace getNamespace(String prefix)
    {
        if(prefix == null)
            return null;
        if(prefix.equals(getNamespacePrefix()))
            return getNamespace();
        List addl = getAdditionalNamespaces();
        if(addl.size() > 0)
        {
            for(Iterator itr = addl.iterator(); itr.hasNext();)
            {
                Namespace ns = (Namespace)itr.next();
                if(prefix.equals(ns.getPrefix()))
                    return ns;
            }

        }
        if(parent instanceof Element)
            return ((Element)parent).getNamespace(prefix);
        else
            return null;
    }

    public String getQualifiedName()
    {
        if(namespace.getPrefix().equals(""))
            return getName();
        else
            return namespace.getPrefix() + ":" + name;
    }

    public void addNamespaceDeclaration(Namespace additionalNamespace)
    {
        String prefix = additionalNamespace.getPrefix();
        String uri = additionalNamespace.getURI();
        if(prefix.equals(getNamespacePrefix()) && !uri.equals(getNamespaceURI()))
            throw new IllegalAddException(this, additionalNamespace, "The namespace prefix \"" + prefix + "\" collides " + "with the element namespace prefix");
        if(additionalNamespaces != null && additionalNamespaces.size() > 0)
        {
            for(Iterator itr = additionalNamespaces.iterator(); itr.hasNext();)
            {
                Namespace ns = (Namespace)itr.next();
                if(prefix.equals(ns.getPrefix()) && !uri.equals(ns.getURI()))
                    throw new IllegalAddException(this, additionalNamespace, "The namespace prefix \"" + prefix + "\" collides with an additional namespace declared " + "by the element");
            }

        }
        if(attributes != null && attributes.size() > 0)
        {
            for(Iterator itr = attributes.iterator(); itr.hasNext();)
            {
                Namespace ns = ((Attribute)itr.next()).getNamespace();
                if(prefix.equals(ns.getPrefix()) && !uri.equals(ns.getURI()))
                    throw new IllegalAddException(this, additionalNamespace, "The namespace prefix \"" + prefix + "\" collides with an attribute namespace on " + "the element");
            }

        }
        if(additionalNamespaces == null)
            additionalNamespaces = new ArrayList(5);
        additionalNamespaces.add(additionalNamespace);
    }

    public void removeNamespaceDeclaration(Namespace additionalNamespace)
    {
        if(additionalNamespaces == null)
        {
            return;
        } else
        {
            additionalNamespaces.remove(additionalNamespace);
            return;
        }
    }

    public List getAdditionalNamespaces()
    {
        if(additionalNamespaces == null)
            return Collections.EMPTY_LIST;
        else
            return Collections.unmodifiableList(additionalNamespaces);
    }

    public Element getParent()
    {
        if(parent instanceof Element)
            return (Element)parent;
        else
            return null;
    }

    protected Element setParent(Element parent)
    {
        this.parent = parent;
        return this;
    }

    public Element detach()
    {
        if(parent instanceof Element)
            ((Element)parent).removeContent(this);
        else
        if(parent instanceof Document)
            ((Document)parent).setRootElement(new Element("root-element-was-detached"));
        return this;
    }

    public boolean isRootElement()
    {
        return parent instanceof Document;
    }

    protected Element setDocument(Document document)
    {
        parent = document;
        return this;
    }

    public Document getDocument()
    {
        if(parent instanceof Document)
            return (Document)parent;
        if(parent instanceof Element)
            return ((Element)parent).getDocument();
        else
            return null;
    }

    public String getText()
    {
        if(content == null || content.size() < 1 || content.get(0) == null)
            return "";
        if(content.size() == 1 && (content.get(0) instanceof String))
            return (String)content.get(0);
        StringBuffer textContent = new StringBuffer();
        boolean hasText = false;
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof String)
            {
                textContent.append((String)obj);
                hasText = true;
            } else
            if(obj instanceof CDATA)
            {
                textContent.append(((CDATA)obj).getText());
                hasText = true;
            }
        }

        if(!hasText)
            return "";
        else
            return textContent.toString();
    }

    public String getTextTrim()
    {
        return getText().trim();
    }

    public String getTextNormalize()
    {
        return normalizeString(getText());
    }

    private static String normalizeString(String value)
    {
        char c[] = value.toCharArray();
        char n[] = new char[c.length];
        boolean white = true;
        int pos = 0;
        for(int i = 0; i < c.length; i++)
            if(" \t\n\r".indexOf(c[i]) != -1)
            {
                if(!white)
                {
                    n[pos++] = ' ';
                    white = true;
                }
            } else
            {
                n[pos++] = c[i];
                white = false;
            }

        if(white && pos > 0)
            pos--;
        return new String(n, 0, pos);
    }

    public String getChildText(String name)
    {
        Element child = getChild(name);
        if(child == null)
            return null;
        else
            return child.getText();
    }

    public String getChildTextTrim(String name)
    {
        Element child = getChild(name);
        if(child == null)
            return null;
        else
            return child.getTextTrim();
    }

    public String getChildText(String name, Namespace ns)
    {
        Element child = getChild(name, ns);
        if(child == null)
            return null;
        else
            return child.getText();
    }

    public String getChildTextTrim(String name, Namespace ns)
    {
        Element child = getChild(name, ns);
        if(child == null)
            return null;
        else
            return child.getTextTrim();
    }

    public Element setText(String text)
    {
        if(content != null)
            content.clear();
        else
            content = new ArrayList(5);
        if(text != null)
            content.add(text);
        return this;
    }

    /**
     * @deprecated Method hasMixedContent is deprecated
     */

    public boolean hasMixedContent()
    {
        if(content == null)
            return false;
        Class prevClass = null;
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            Class newClass = obj.getClass();
            if(newClass != prevClass)
            {
                if(prevClass != null)
                    return true;
                prevClass = newClass;
            }
        }

        return false;
    }

    public List getContent()
    {
        if(content == null)
            content = new ArrayList(5);
        PartialList result = new PartialList(content, this);
        result.addAllPartial(content);
        return result;
    }

    public Element setContent(List newContent)
    {
        if(newContent == null)
        {
            content = new ArrayList(5);
            return this;
        }
        List oldContent = content;
        content = new ArrayList(5);
        RuntimeException ex = null;
        int itemsAdded = 0;
        try
        {
            for(Iterator i = newContent.iterator(); i.hasNext();)
            {
                Object obj = i.next();
                if(obj instanceof Element)
                    addContent((Element)obj);
                else
                if(obj instanceof String)
                    addContent((String)obj);
                else
                if(obj instanceof Comment)
                    addContent((Comment)obj);
                else
                if(obj instanceof ProcessingInstruction)
                    addContent((ProcessingInstruction)obj);
                else
                if(obj instanceof CDATA)
                    addContent((CDATA)obj);
                else
                if(obj instanceof EntityRef)
                    addContent((EntityRef)obj);
                else
                    throw new IllegalAddException("An Element may directly contain only objects of type String, Element, Comment, CDATA, EntityRef, and ProcessingInstruction: " + (obj != null ? obj.getClass().getName() : "null") + " is not allowed");
                itemsAdded++;
            }

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
                Iterator i = newContent.iterator();
                while(itemsAdded-- > 0) 
                {
                    Object obj = i.next();
                    if(obj instanceof Element)
                        ((Element)obj).setParent(null);
                    else
                    if(obj instanceof Comment)
                        ((Comment)obj).setParent(null);
                    else
                    if(obj instanceof ProcessingInstruction)
                        ((ProcessingInstruction)obj).setParent(null);
                    else
                    if(obj instanceof EntityRef)
                        ((EntityRef)obj).setParent(null);
                }
                throw ex;
            }
        }
        if(oldContent == null)
            return this;
        for(Iterator itr = oldContent.iterator(); itr.hasNext();)
        {
            Object obj = itr.next();
            if(obj instanceof Element)
                ((Element)obj).setParent(null);
            else
            if(obj instanceof Comment)
                ((Comment)obj).setParent(null);
            else
            if(obj instanceof ProcessingInstruction)
                ((ProcessingInstruction)obj).setParent(null);
            else
            if(obj instanceof EntityRef)
                ((EntityRef)obj).setParent(null);
        }

        return this;
    }

    public boolean hasChildren()
    {
        if(content == null || content.size() == 0)
            return false;
        for(Iterator i = content.iterator(); i.hasNext();)
            if(i.next() instanceof Element)
                return true;

        return false;
    }

    public List getChildren()
    {
        if(content == null)
            content = new ArrayList(5);
        PartialList elements = new PartialList(content, this);
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof Element)
                elements.addPartial(obj);
        }

        return elements;
    }

    public Element setChildren(List children)
    {
        return setContent(children);
    }

    public List getChildren(String name)
    {
        return getChildren(name, Namespace.NO_NAMESPACE);
    }

    public List getChildren(String name, Namespace ns)
    {
        PartialList children = new PartialList(getChildren(), this);
        if(content != null)
        {
            String uri = ns.getURI();
            for(Iterator i = content.iterator(); i.hasNext();)
            {
                Object obj = i.next();
                if(obj instanceof Element)
                {
                    Element element = (Element)obj;
                    if(element.getNamespaceURI().equals(uri) && element.getName().equals(name))
                        children.addPartial(element);
                }
            }

        }
        return children;
    }

    public Element getChild(String name, Namespace ns)
    {
        if(content == null)
            return null;
        String uri = ns.getURI();
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof Element)
            {
                Element element = (Element)obj;
                if(element.getNamespaceURI().equals(uri) && element.getName().equals(name))
                    return element;
            }
        }

        return null;
    }

    public Element getChild(String name)
    {
        return getChild(name, Namespace.NO_NAMESPACE);
    }

    public Element addContent(String text)
    {
        if(content == null)
            content = new ArrayList(5);
        int size = content.size();
        if(size > 0)
        {
            Object ob = content.get(size - 1);
            if(ob instanceof String)
            {
                text = (String)ob + text;
                content.remove(size - 1);
            }
        }
        content.add(text);
        return this;
    }

    public Element addContent(Element element)
    {
        if(element.isRootElement())
            throw new IllegalAddException(this, element, "The element already has an existing parent (the document root)");
        if(element.getParent() != null)
            throw new IllegalAddException(this, element, "The element already has an existing parent \"" + element.getParent().getQualifiedName() + "\"");
        if(element == this)
            throw new IllegalAddException(this, element, "The element cannot be added to itself");
        if(isAncestor(element))
            throw new IllegalAddException(this, element, "The element cannot be added as a descendent of itself");
        if(content == null)
            content = new ArrayList(5);
        element.setParent(this);
        content.add(element);
        return this;
    }

    private boolean isAncestor(Element e)
    {
        for(Object p = parent; p instanceof Element; p = ((Element)p).getParent())
            if(p == e)
                return true;

        return false;
    }

    public Element addContent(ProcessingInstruction pi)
    {
        if(pi.getParent() != null)
            throw new IllegalAddException(this, pi, "The PI already has an existing parent \"" + pi.getParent().getQualifiedName() + "\"");
        if(pi.getDocument() != null)
            throw new IllegalAddException(this, pi, "The PI already has an existing parent (the document root)");
        if(content == null)
            content = new ArrayList(5);
        content.add(pi);
        pi.setParent(this);
        return this;
    }

    public Element addContent(EntityRef entity)
    {
        if(entity.getParent() != null)
            throw new IllegalAddException(this, entity, "The entity reference already has an existing parent \"" + entity.getParent().getQualifiedName() + "\"");
        if(content == null)
            content = new ArrayList(5);
        content.add(entity);
        entity.setParent(this);
        return this;
    }

    public Element addContent(CDATA cdata)
    {
        if(content == null)
            content = new ArrayList(5);
        content.add(cdata);
        return this;
    }

    public Element addContent(Comment comment)
    {
        if(comment.getParent() != null)
            throw new IllegalAddException(this, comment, "The comment already has an existing parent \"" + comment.getParent().getQualifiedName() + "\"");
        if(comment.getDocument() != null)
            throw new IllegalAddException(this, comment, "The comment already has an existing parent (the document root)");
        if(content == null)
            content = new ArrayList(5);
        content.add(comment);
        comment.setParent(this);
        return this;
    }

    public boolean removeChild(String name)
    {
        return removeChild(name, Namespace.NO_NAMESPACE);
    }

    public boolean removeChild(String name, Namespace ns)
    {
        if(content == null)
            return false;
        String uri = ns.getURI();
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof Element)
            {
                Element element = (Element)obj;
                if(element.getNamespaceURI().equals(uri) && element.getName().equals(name))
                {
                    element.setParent(null);
                    i.remove();
                    return true;
                }
            }
        }

        return false;
    }

    public boolean removeChildren(String name)
    {
        return removeChildren(name, Namespace.NO_NAMESPACE);
    }

    public boolean removeChildren(String name, Namespace ns)
    {
        if(content == null)
            return false;
        String uri = ns.getURI();
        boolean deletedSome = false;
        for(Iterator i = content.iterator(); i.hasNext();)
        {
            Object obj = i.next();
            if(obj instanceof Element)
            {
                Element element = (Element)obj;
                if(element.getNamespaceURI().equals(uri) && element.getName().equals(name))
                {
                    element.setParent(null);
                    i.remove();
                    deletedSome = true;
                }
            }
        }

        return deletedSome;
    }

    public boolean removeChildren()
    {
        boolean deletedSome = false;
        if(content != null)
        {
            for(Iterator i = content.iterator(); i.hasNext();)
            {
                Object obj = i.next();
                if(obj instanceof Element)
                {
                    Element element = (Element)obj;
                    i.remove();
                    element.setParent(null);
                    deletedSome = true;
                }
            }

        }
        return deletedSome;
    }

    public List getAttributes()
    {
        if(attributes == null)
            attributes = new ArrayList(5);
        PartialList atts = new PartialList(attributes, this);
        atts.addAllPartial(attributes);
        return atts;
    }

    public Attribute getAttribute(String name)
    {
        return getAttribute(name, Namespace.NO_NAMESPACE);
    }

    public Attribute getAttribute(String name, Namespace ns)
    {
        if(attributes == null)
            return null;
        String uri = ns.getURI();
        for(Iterator i = attributes.iterator(); i.hasNext();)
        {
            Attribute att = (Attribute)i.next();
            if(att.getNamespaceURI().equals(uri) && att.getName().equals(name))
                return att;
        }

        return null;
    }

    public String getAttributeValue(String name)
    {
        return getAttributeValue(name, Namespace.NO_NAMESPACE);
    }

    public String getAttributeValue(String name, Namespace ns)
    {
        Attribute attrib = getAttribute(name, ns);
        if(attrib == null)
            return null;
        else
            return attrib.getValue();
    }

    public Element setAttributes(List attributes)
    {
        this.attributes = attributes;
        return this;
    }

    public Element setAttribute(String name, String value)
    {
        return setAttribute(new Attribute(name, value));
    }

    public Element setAttribute(String name, String value, Namespace ns)
    {
        return setAttribute(new Attribute(name, value, ns));
    }

    public Element setAttribute(Attribute attribute)
    {
        if(attribute.getParent() != null)
            throw new IllegalAddException(this, attribute, "The attribute already has an existing parent \"" + attribute.getParent().getQualifiedName() + "\"");
        boolean preExisting = false;
        String prefix = attribute.getNamespace().getPrefix();
        String uri = attribute.getNamespace().getURI();
        if(!prefix.equals(""))
        {
            if(prefix.equals(getNamespacePrefix()) && !uri.equals(getNamespaceURI()))
                throw new IllegalAddException(this, attribute, "The attribute namespace prefix \"" + prefix + "\" collides with the element namespace prefix");
            if(additionalNamespaces != null && additionalNamespaces.size() > 0)
            {
                for(Iterator itr = additionalNamespaces.iterator(); itr.hasNext();)
                {
                    Namespace ns = (Namespace)itr.next();
                    if(prefix.equals(ns.getPrefix()) && !uri.equals(ns.getURI()))
                        throw new IllegalAddException(this, attribute, "The attribute namespace prefix \"" + prefix + "\" collides with a namespace declared by the element");
                }

            }
        }
        if(attributes != null && attributes.size() > 0)
        {
            for(Iterator itr = attributes.iterator(); itr.hasNext();)
            {
                Attribute att = (Attribute)itr.next();
                Namespace ns = att.getNamespace();
                if(attribute.getName().equals(att.getName()) && ns.getURI().equals(att.getNamespaceURI()))
                    preExisting = true;
                if(!prefix.equals("") && prefix.equals(ns.getPrefix()) && !uri.equals(ns.getURI()))
                    throw new IllegalAddException(this, attribute, "The attribute namespace prefix \"" + prefix + "\" collides with another attribute namespace on " + "the element");
            }

        }
        if(attributes == null)
            attributes = new ArrayList(5);
        else
        if(preExisting)
            removeAttribute(attribute.getName(), attribute.getNamespace());
        attributes.add(attribute);
        attribute.setParent(this);
        return this;
    }

    /**
     * @deprecated Method addAttribute is deprecated
     */

    public Element addAttribute(String name, String value)
    {
        return addAttribute(new Attribute(name, value));
    }

    /**
     * @deprecated Method removeAttribute is deprecated
     */

    public boolean removeAttribute(String name, String uri)
    {
        for(Iterator i = attributes.iterator(); i.hasNext();)
        {
            Attribute att = (Attribute)i.next();
            if(att.getNamespaceURI().equals(uri) && att.getName().equals(name))
            {
                i.remove();
                att.setParent(null);
                return true;
            }
        }

        return false;
    }

    public boolean removeAttribute(String name)
    {
        return removeAttribute(name, Namespace.NO_NAMESPACE);
    }

    public boolean removeAttribute(String name, Namespace ns)
    {
        if(attributes == null)
            return false;
        String uri = ns.getURI();
        for(Iterator i = attributes.iterator(); i.hasNext();)
        {
            Attribute att = (Attribute)i.next();
            if(att.getNamespaceURI().equals(uri) && att.getName().equals(name))
            {
                i.remove();
                att.setParent(null);
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        StringBuffer stringForm = (new StringBuffer(64)).append("[Element: <").append(getQualifiedName());
        String nsuri = getNamespaceURI();
        if(!nsuri.equals(""))
            stringForm.append(" [Namespace: ").append(nsuri).append("]");
        stringForm.append("/>]");
        return stringForm.toString();
    }

    public final boolean equals(Object ob)
    {
        return this == ob;
    }

    public final int hashCode()
    {
        return super.hashCode();
    }

    public Object clone()
    {
        Element element = null;
        try
        {
            element = (Element)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        element.parent = null;
        element.content = content != null ? ((List) (new ArrayList(5))) : null;
        element.attributes = attributes != null ? ((List) (new ArrayList(5))) : null;
        if(attributes != null)
        {
            Attribute a;
            for(Iterator i = attributes.iterator(); i.hasNext(); element.attributes.add(a))
            {
                a = (Attribute)((Attribute)i.next()).clone();
                a.setParent(element);
            }

        }
        if(content != null)
        {
            for(Iterator i = content.iterator(); i.hasNext();)
            {
                Object obj = i.next();
                if(obj instanceof String)
                    element.content.add(obj);
                else
                if(obj instanceof Element)
                {
                    Element e = (Element)((Element)obj).clone();
                    e.setParent(element);
                    element.content.add(e);
                } else
                if(obj instanceof Comment)
                {
                    Comment c = (Comment)((Comment)obj).clone();
                    c.setParent(element);
                    element.content.add(c);
                } else
                if(obj instanceof CDATA)
                    element.content.add(obj);
                else
                if(obj instanceof ProcessingInstruction)
                {
                    ProcessingInstruction p = (ProcessingInstruction)((ProcessingInstruction)obj).clone();
                    element.content.add(p);
                } else
                if(obj instanceof EntityRef)
                {
                    EntityRef e = (EntityRef)((EntityRef)obj).clone();
                    e.setParent(element);
                    element.content.add(e);
                }
            }

        }
        if(additionalNamespaces != null)
        {
            element.additionalNamespaces = new ArrayList();
            element.additionalNamespaces.addAll(additionalNamespaces);
        }
        return element;
    }

    private void writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        out.writeObject(namespace.getPrefix());
        out.writeObject(namespace.getURI());
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        namespace = Namespace.getNamespace((String)in.readObject(), (String)in.readObject());
    }

    public boolean removeContent(Element element)
    {
        if(content == null)
            return false;
        if(content.remove(element))
        {
            element.setParent(null);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean removeContent(ProcessingInstruction pi)
    {
        if(content == null)
            return false;
        if(content.remove(pi))
        {
            pi.setParent(null);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean removeContent(Comment comment)
    {
        if(content == null)
            return false;
        if(content.remove(comment))
        {
            comment.setParent(null);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean removeContent(CDATA cdata)
    {
        if(content == null)
            return false;
        return content.remove(cdata);
    }

    public boolean removeContent(EntityRef entity)
    {
        if(content == null)
            return false;
        if(content.remove(entity))
        {
            entity.setParent(null);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * @deprecated Method getCopy is deprecated
     */

    public Element getCopy(String name, Namespace ns)
    {
        Element clone = (Element)clone();
        clone.namespace = ns;
        clone.name = name;
        return clone;
    }

    /**
     * @deprecated Method getCopy is deprecated
     */

    public Element getCopy(String name)
    {
        return getCopy(name, Namespace.NO_NAMESPACE);
    }

    /**
     * @deprecated Method addAttribute is deprecated
     */

    public Element addAttribute(Attribute attribute)
    {
        if(getAttribute(attribute.getName(), attribute.getNamespace()) != null)
            throw new IllegalAddException(this, attribute, "Duplicate attributes are not allowed");
        if(attribute.getParent() != null)
            throw new IllegalAddException(this, attribute, "The attribute already has an existing parent \"" + attribute.getParent().getQualifiedName() + "\"");
        if(attributes == null)
            attributes = new ArrayList(5);
        attributes.add(attribute);
        attribute.setParent(this);
        return this;
    }

    /**
     * @deprecated Method getSerializedForm is deprecated
     */

    public final String getSerializedForm()
    {
        throw new RuntimeException("Element.getSerializedForm() is not yet implemented");
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

    public Element setMixedContent(List mixedContent)
    {
        return setContent(mixedContent);
    }

    private static final String CVS_ID = "@(#) $RCSfile: Element.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    private static final int INITIAL_ARRAY_SIZE = 5;
    protected String name;
    protected transient Namespace namespace;
    protected transient List additionalNamespaces;
    protected Object parent;
    protected List attributes;
    protected List content;
}
