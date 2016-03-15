// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IllegalAddException.java

package cn.com.info21.jdom;


// Referenced classes of package cn.com.info21.jdom:
//            Attribute, Element, ProcessingInstruction, Comment, 
//            EntityRef, Namespace, DocType, Document

public class IllegalAddException extends IllegalArgumentException
{

    public IllegalAddException(Element base, Attribute added, String reason)
    {
        super("The attribute \"" + added.getQualifiedName() + "\" could not be added to the element \"" + base.getQualifiedName() + "\": " + reason);
    }

    public IllegalAddException(Element base, Element added, String reason)
    {
        super("The element \"" + added.getQualifiedName() + "\" could not be added as a child of \"" + base.getQualifiedName() + "\": " + reason);
    }

    public IllegalAddException(Document base, Element added, String reason)
    {
        super("The element \"" + added.getQualifiedName() + "\" could not be added as the root of the document: " + reason);
    }

    public IllegalAddException(Element base, ProcessingInstruction added, String reason)
    {
        super("The PI \"" + added.getTarget() + "\" could not be added as content to \"" + base.getQualifiedName() + "\": " + reason);
    }

    public IllegalAddException(Document base, ProcessingInstruction added, String reason)
    {
        super("The PI \"" + added.getTarget() + "\" could not be added to the top level of the document: " + reason);
    }

    public IllegalAddException(Element base, Comment added, String reason)
    {
        super("The comment \"" + added.getText() + "\" could not be added as content to \"" + base.getQualifiedName() + "\": " + reason);
    }

    public IllegalAddException(Document base, Comment added, String reason)
    {
        super("The comment \"" + added.getText() + "\" could not be added to the top level of the document: " + reason);
    }

    public IllegalAddException(Element base, EntityRef added, String reason)
    {
        super("The entity reference\"" + added.getName() + "\" could not be added as content to \"" + base.getQualifiedName() + "\": " + reason);
    }

    public IllegalAddException(Element base, Namespace added, String reason)
    {
        super("The namespace xmlns" + (added.getPrefix() != null ? ":" + added.getPrefix() + "=" : "=") + "\"" + added.getURI() + "\" could not be added as content to \"" + base.getQualifiedName() + "\": " + reason);
    }

    public IllegalAddException(Document base, DocType added, String reason)
    {
        super("The DOCTYPE " + added.getSystemID() + " could not be added to the document: " + reason);
    }

    public IllegalAddException(String reason)
    {
        super(reason);
    }

    private static final String CVS_ID = "@(#) $RCSfile: IllegalAddException.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
}
