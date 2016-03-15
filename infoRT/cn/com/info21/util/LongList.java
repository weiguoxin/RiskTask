// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LongList.java

package cn.com.info21.util;


public final class LongList
{

    public LongList()
    {
        this(50);
    }

    public LongList(int initialCapacity)
    {
        size = 0;
        capacity = initialCapacity;
        elements = new long[capacity];
    }

    public LongList(long longArray[])
    {
        size = longArray.length;
        capacity = longArray.length + 3;
        elements = new long[capacity];
        System.arraycopy(longArray, 0, elements, 0, size);
    }

    public void add(long value)
    {
        elements[size] = value;
        size++;
        if(size == capacity)
        {
            capacity = capacity * 2;
            long newElements[] = new long[capacity];
            for(int i = 0; i < size; i++)
                newElements[i] = elements[i];

            elements = newElements;
        }
    }

    public void add(int index, long value)
    {
        if(index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index " + index + " not valid.");
        for(int i = size; i > index; i--)
            elements[i] = elements[i - 1];

        elements[index] = value;
        size++;
        if(size == capacity)
        {
            capacity = capacity * 2;
            long newElements[] = new long[capacity];
            for(int i = 0; i < size; i++)
                newElements[i] = elements[i];

            elements = newElements;
        }
    }

    public void remove(int index)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index " + index + " not valid.");
        size--;
        for(int i = index; i < size; i++)
            elements[i] = elements[i + 1];

    }

    public void clear()
    {
        size = 0;
    }

    public long get(int index)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index " + index + " not valid.");
        else
            return elements[index];
    }

    public int indexOf(long value)
    {
        for(int i = 0; i < size; i++)
            if(elements[i] == value)
                return i;

        return -1;
    }

    public boolean contains(long value)
    {
        return indexOf(value) != -1;
    }

    public int size()
    {
        return size;
    }

    public long[] toArray()
    {
        int size = this.size;
        long newElements[] = new long[size];
        for(int i = 0; i < size; i++)
            newElements[i] = elements[i];

        return newElements;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < size; i++)
            buf.append(elements[i]).append(" ");

        return buf.toString();
    }

    long elements[];
    int capacity;
    int size;
}
