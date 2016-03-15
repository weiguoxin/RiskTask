// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLProperties.java

package cn.com.info21.util;

import cn.com.info21.jdom.Document;
import cn.com.info21.jdom.Element;
import cn.com.info21.jdom.input.SAXBuilder;
import cn.com.info21.jdom.output.XMLOutputter;

import java.io.*;
import java.util.*;

public class XMLProperties
{

    public XMLProperties(String fileName)
        throws IOException
    {
        propertyCache = new HashMap();
        file = new File(fileName);
        if(!file.exists())
        {
            File tempFile = new File(file.getParentFile(), file.getName() + ".tmp");
            if(tempFile.exists())
            {
                //Log.error("WARNING: " + fileName + " was not found, but temp file from " + "previous write operation was. Attempting automatic recovery. Please " + "check file for data consistency.");
                tempFile.renameTo(file);
            } else
            {
                throw new FileNotFoundException("XML properties file does not exist: " + fileName);
            }
        }
        if(!file.canRead())
            throw new IOException("XML properties file must be readable: " + fileName);
        if(!file.canWrite())
            throw new IOException("XML properties file must be writable: " + fileName);
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            SAXBuilder builder = new SAXBuilder();
            DataUnformatFilter format = new DataUnformatFilter();
            builder.setXMLFilter(format);
            doc = builder.build(reader);
        }
        catch(Exception e)
        {
            //Log.error("Error creating XML properties file " + fileName + ".", e);
            throw new IOException(e.getMessage());
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch(Exception exception1) { }
        }
    }

    public synchronized String getProperty(String name)
    {
        String value = (String)propertyCache.get(name);
        
        if(value != null) {
            return value;
        }
        String propName[] = parsePropertyName(name);
        Element element = doc.getRootElement();
        for(int i = 0; i < propName.length; i++)
        {
            element = element.getChild(propName[i]);
            if(element == null)
                return null;
        }

        value = element.getText();
        /*
        try {
            value = new String(value.getBytes("gb2312"),"ISO8859-1");
        } catch (Exception e) {
            Log.error("Encoding error:" + e);
        }
        */
        if("".equals(value))
        {
            return null;
        } else
        {
            value = value.trim();
            propertyCache.put(name, value);
            return value;
        }
    }

    public String[] getChildrenProperties(String parent)
    {
        String propName[] = parsePropertyName(parent);
        Element element = doc.getRootElement();
        for(int i = 0; i < propName.length; i++)
        {
            element = element.getChild(propName[i]);
            if(element == null)
                return new String[0];
        }

        List children = element.getChildren();
        int childCount = children.size();
        String childrenNames[] = new String[childCount];
        for(int i = 0; i < childCount; i++)
            childrenNames[i] = ((Element)children.get(i)).getName();

        return childrenNames;
    }

    public synchronized void setProperty(String name, String value)
    {
        propertyCache.put(name, value);
        String propName[] = parsePropertyName(name);
        Element element = doc.getRootElement();
        for(int i = 0; i < propName.length; i++)
        {
            if(element.getChild(propName[i]) == null)
                element.addContent(new Element(propName[i]));
            element = element.getChild(propName[i]);
        }

        element.setText(value);
        saveProperties();
    }

    public synchronized void deleteProperty(String name)
    {
        propertyCache.remove(name);
        String propName[] = parsePropertyName(name);
        Element element = doc.getRootElement();
        for(int i = 0; i < propName.length - 1; i++)
        {
            element = element.getChild(propName[i]);
            if(element == null)
                return;
        }

        element.removeChild(propName[propName.length - 1]);
        saveProperties();
    }

    private synchronized void saveProperties()
    {
        Writer writer = null;
        boolean error = false;
        File tempFile = null;
        try
        {
            tempFile = new File(file.getParentFile(), file.getName() + ".tmp");
            XMLOutputter outputter = new XMLOutputter("    ", true);
            writer = new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8");
            outputter.output(doc, writer);
        }
        catch(Exception e)
        {
            //Log.error(e);
            error = true;
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch(Exception e)
            {
                //Log.error(e);
                error = true;
            }
        }
        if(!error)
        {
            error = false;
            if(!file.delete())
            {
                //Log.error("Error deleting property file: " + file.getAbsolutePath());
                return;
            }
            try
            {
                XMLOutputter outputter = new XMLOutputter("    ", true);
                writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                outputter.output(doc, writer);
            }
            catch(Exception e)
            {
                //Log.error(e);
                error = true;
            }
            finally
            {
                try
                {
                    writer.close();
                }
                catch(Exception e)
                {
                    //Log.error(e);
                    error = true;
                }
            }
            if(!error)
                tempFile.delete();
        }
    }

    private String[] parsePropertyName(String name)
    {
        List propName = new ArrayList(5);
        for(StringTokenizer tokenizer = new StringTokenizer(name, "."); tokenizer.hasMoreTokens(); propName.add(tokenizer.nextToken()));
        return (String[])propName.toArray(new String[propName.size()]);
    }

    private File file;
    private Document doc;
    private Map propertyCache;
}
