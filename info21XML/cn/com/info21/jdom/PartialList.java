// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PartialList.java

package cn.com.info21.jdom;

import java.util.*;

// Referenced classes of package cn.com.info21.jdom:
//            Element

class PartialList extends LinkedList
{

    public PartialList(List backingList, Element parent)
    {
        this.backingList = backingList;
        this.parent = parent;
    }

    public PartialList(List backingList)
    {
        this(backingList, null);
    }

    public Object removeFirst()
    {
        Object o = super.removeFirst();
        backingList.remove(o);
        if(o instanceof Element)
            ((Element)o).setParent(null);
        return o;
    }

    public Object removeLast()
    {
        Object o = super.removeLast();
        backingList.remove(o);
        if(o instanceof Element)
            ((Element)o).setParent(null);
        return o;
    }

    public void addFirst(Object o)
    {
        if(size() == 0)
        {
            add(o);
            return;
        }
        int index = backingList.indexOf(getFirst());
        super.addFirst(o);
        if(index != -1)
            backingList.add(index, o);
        else
            backingList.add(o);
        if(o instanceof Element)
            ((Element)o).setParent(parent);
    }

    public void addLast(Object o)
    {
        if(size() == 0)
        {
            add(o);
            return;
        }
        int index = backingList.indexOf(getLast());
        super.addLast(o);
        if(index != -1 && index < backingList.size())
            backingList.add(index + 1, o);
        else
            backingList.add(o);
        if(o instanceof Element)
            ((Element)o).setParent(parent);
    }

    public boolean add(Object o)
    {
        backingList.add(o);
        if(o instanceof Element)
            ((Element)o).setParent(parent);
        return super.add(o);
    }

    public boolean remove(Object o)
    {
        backingList.remove(o);
        if(o instanceof Element)
            ((Element)o).setParent(null);
        return super.remove(o);
    }

    public boolean addAll(Collection c)
    {
        Iterator i = c.iterator();
        boolean value;
        for(value = true; i.hasNext() && value; value = add(i.next()));
        return value;
    }

    public boolean addAll(int index, Collection c)
    {
        if(backingList.isEmpty())
            backingList.addAll(c);
        else
            backingList.addAll(index, c);
        for(Iterator i = c.iterator(); i.hasNext();)
        {
            Object o = i.next();
            if(o instanceof Element)
                ((Element)o).setParent(parent);
        }

        return super.addAll(index, c);
    }

    public void clear()
    {
        for(Iterator i = iterator(); i.hasNext();)
        {
            Object o = i.next();
            backingList.remove(o);
            if(o instanceof Element)
                ((Element)o).setParent(null);
        }

        super.clear();
    }

    public Object set(int index, Object current)
    {
        Object old = get(index);
        int backingIndex = backingList.indexOf(old);
        if(backingIndex != -1)
            backingList.set(backingIndex, current);
        if(old instanceof Element)
            ((Element)old).setParent(null);
        if(current instanceof Element)
            ((Element)current).setParent(parent);
        return super.set(index, current);
    }

    public void add(int index, Object current)
    {
        if(index == size())
        {
            addLast(current);
        } else
        {
            backingList.add(backingList.indexOf(get(index)), current);
            if(current instanceof Element)
                ((Element)current).setParent(parent);
            super.add(index, current);
        }
    }

    public Object remove(int index)
    {
        Object o = super.remove(index);
        backingList.remove(o);
        if(o instanceof Element)
            ((Element)o).setParent(null);
        return o;
    }

    protected void addPartial(Object o)
    {
        super.add(o);
    }

    protected boolean addAllPartial(Collection c)
    {
        for(Iterator i = c.iterator(); i.hasNext(); addPartial(i.next()));
        return true;
    }

    private static final String CVS_ID = "@(#) $RCSfile: PartialList.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:26 $ $Name:  $";
    protected List backingList;
    protected Element parent;
}
