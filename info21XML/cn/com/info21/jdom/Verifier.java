// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Verifier.java

package cn.com.info21.jdom;

public final class Verifier
{

    private Verifier()
    {
    }

    public static final String checkElementName(String name)
    {
        String reason;
        if((reason = checkXMLName(name)) != null)
            return reason;
        if(name.indexOf(":") != -1)
            return "Element names cannot contain colons";
        else
            return null;
    }

    public static final String checkAttributeName(String name)
    {
        String reason;
        if((reason = checkXMLName(name)) != null)
            return reason;
        if(name.equals("xml:space") || name.equals("xml:lang"))
            return null;
        if(name.indexOf(":") != -1)
            return "Attribute names cannot contain colons";
        if(name.equals("xmlns"))
            return "An Attribute name may not be \"xmlns\"; use the Namespace class to manage namespaces";
        else
            return null;
    }

    public static final String checkCharacterData(String text)
    {
        if(text == null)
            return "A null is not a legal XML value";
        int i = 0;
        for(int len = text.length(); i < len; i++)
            if(!isXMLCharacter(text.charAt(i)))
                return "0x" + Integer.toHexString(text.charAt(i)) + " is not a legal XML character";

        return null;
    }

    public static final String checkCDATASection(String data)
    {
        String reason = null;
        if((reason = checkCharacterData(data)) != null)
            return reason;
        if(data.indexOf("]]>") != -1)
            return "CDATA cannot internally contain a CDATA ending delimiter (]]>)";
        else
            return null;
    }

    public static final String checkNamespacePrefix(String prefix)
    {
        if(prefix == null || prefix.equals(""))
            return null;
        char first = prefix.charAt(0);
        if(isXMLDigit(first))
            return "Namespace prefixes cannot begin with a number";
        if(first == '$')
            return "Namespace prefixes cannot begin with a dollar sign ($)";
        if(first == '-')
            return "Namespace prefixes cannot begin with a hyphen (-)";
        if(first == '.')
            return "Namespace prefixes cannot begin with a period (.)";
        if(prefix.toLowerCase().startsWith("xml"))
            return "Namespace prefixes cannot begin with \"xml\" in any combination of case";
        int i = 0;
        for(int len = prefix.length(); i < len; i++)
        {
            char c = prefix.charAt(i);
            if(!isXMLNameCharacter(c))
                return "Namespace prefixes cannot contain the character \"" + c + "\"";
        }

        if(prefix.indexOf(":") != -1)
            return "Namespace prefixes cannot contain colons";
        else
            return null;
    }

    public static final String checkNamespaceURI(String uri)
    {
        if(uri == null || uri.equals(""))
            return null;
        char first = uri.charAt(0);
        if(Character.isDigit(first))
            return "Namespace URIs cannot begin with a number";
        if(first == '$')
            return "Namespace URIs cannot begin with a dollar sign ($)";
        if(first == '-')
            return "Namespace URIs cannot begin with a hyphen (-)";
        else
            return null;
    }

    public static final String checkProcessingInstructionTarget(String target)
    {
        String reason;
        if((reason = checkXMLName(target)) != null)
            return reason;
        if(target.indexOf(":") != -1)
            return "Processing instruction targets cannot contain colons";
        if(target.equalsIgnoreCase("xml"))
            return "Processing instructions cannot have a target of \"xml\" in any combination of case";
        else
            return null;
    }

    public static final String checkCommentData(String data)
    {
        String reason = null;
        if((reason = checkCharacterData(data)) != null)
            return reason;
        if(data.indexOf("--") != -1)
            return "Comments cannot contain double hyphens (--)";
        else
            return null;
    }

    private static String checkXMLName(String name)
    {
        if(name == null || name.length() == 0 || name.trim().equals(""))
            return "XML names cannot be null or empty";
        char first = name.charAt(0);
        if(!isXMLNameStartCharacter(first))
            return "XML names cannot begin with the character \"" + first + "\"";
        int i = 0;
        for(int len = name.length(); i < len; i++)
        {
            char c = name.charAt(i);
            if(!isXMLNameCharacter(c))
                return "XML names cannot contain the character \"" + c + "\"";
        }

        return null;
    }

    public static boolean isXMLCharacter(char c)
    {
        if(c == '\n')
            return true;
        if(c == '\r')
            return true;
        if(c == '\t')
            return true;
        if(c < ' ')
            return false;
        if(c <= '\uD7FF')
            return true;
        if(c < '\uE000')
            return false;
        if(c <= '\uFFFD')
            return true;
        if(c < '\0')
            return false;
        return c <= '\0';
    }

    public static boolean isXMLNameCharacter(char c)
    {
        return isXMLLetter(c) || isXMLDigit(c) || c == '.' || c == '-' || c == '_' || c == ':' || isXMLCombiningChar(c) || isXMLExtender(c);
    }

    public static boolean isXMLNameStartCharacter(char c)
    {
        return isXMLLetter(c) || c == '_' || c == ':';
    }

    public static boolean isXMLLetterOrDigit(char c)
    {
        return isXMLLetter(c) || isXMLDigit(c);
    }

    public static boolean isXMLLetter(char c)
    {
        if(c < 'A')
            return false;
        if(c <= 'Z')
            return true;
        if(c < 'a')
            return false;
        if(c <= 'z')
            return true;
        if(c < '\300')
            return false;
        if(c <= '\326')
            return true;
        if(c < '\330')
            return false;
        if(c <= '\366')
            return true;
        if(c < '\370')
            return false;
        if(c <= '\377')
            return true;
        if(c < '\u0100')
            return false;
        if(c <= '\u0131')
            return true;
        if(c < '\u0134')
            return false;
        if(c <= '\u013E')
            return true;
        if(c < '\u0141')
            return false;
        if(c <= '\u0148')
            return true;
        if(c < '\u014A')
            return false;
        if(c <= '\u017E')
            return true;
        if(c < '\u0180')
            return false;
        if(c <= '\u01C3')
            return true;
        if(c < '\u01CD')
            return false;
        if(c <= '\u01F0')
            return true;
        if(c < '\u01F4')
            return false;
        if(c <= '\u01F5')
            return true;
        if(c < '\u01FA')
            return false;
        if(c <= '\u0217')
            return true;
        if(c < '\u0250')
            return false;
        if(c <= '\u02A8')
            return true;
        if(c < '\u02BB')
            return false;
        if(c <= '\u02C1')
            return true;
        if(c == '\u0386')
            return true;
        if(c < '\u0388')
            return false;
        if(c <= '\u038A')
            return true;
        if(c == '\u038C')
            return true;
        if(c < '\u038E')
            return false;
        if(c <= '\u03A1')
            return true;
        if(c < '\u03A3')
            return false;
        if(c <= '\u03CE')
            return true;
        if(c < '\u03D0')
            return false;
        if(c <= '\u03D6')
            return true;
        if(c == '\u03DA')
            return true;
        if(c == '\u03DC')
            return true;
        if(c == '\u03DE')
            return true;
        if(c == '\u03E0')
            return true;
        if(c < '\u03E2')
            return false;
        if(c <= '\u03F3')
            return true;
        if(c < '\u0401')
            return false;
        if(c <= '\u040C')
            return true;
        if(c < '\u040E')
            return false;
        if(c <= '\u044F')
            return true;
        if(c < '\u0451')
            return false;
        if(c <= '\u045C')
            return true;
        if(c < '\u045E')
            return false;
        if(c <= '\u0481')
            return true;
        if(c < '\u0490')
            return false;
        if(c <= '\u04C4')
            return true;
        if(c < '\u04C7')
            return false;
        if(c <= '\u04C8')
            return true;
        if(c < '\u04CB')
            return false;
        if(c <= '\u04CC')
            return true;
        if(c < '\u04D0')
            return false;
        if(c <= '\u04EB')
            return true;
        if(c < '\u04EE')
            return false;
        if(c <= '\u04F5')
            return true;
        if(c < '\u04F8')
            return false;
        if(c <= '\u04F9')
            return true;
        if(c < '\u0531')
            return false;
        if(c <= '\u0556')
            return true;
        if(c == '\u0559')
            return true;
        if(c < '\u0561')
            return false;
        if(c <= '\u0586')
            return true;
        if(c < '\u05D0')
            return false;
        if(c <= '\u05EA')
            return true;
        if(c < '\u05F0')
            return false;
        if(c <= '\u05F2')
            return true;
        if(c < '\u0621')
            return false;
        if(c <= '\u063A')
            return true;
        if(c < '\u0641')
            return false;
        if(c <= '\u064A')
            return true;
        if(c < '\u0671')
            return false;
        if(c <= '\u06B7')
            return true;
        if(c < '\u06BA')
            return false;
        if(c <= '\u06BE')
            return true;
        if(c < '\u06C0')
            return false;
        if(c <= '\u06CE')
            return true;
        if(c < '\u06D0')
            return false;
        if(c <= '\u06D3')
            return true;
        if(c == '\u06D5')
            return true;
        if(c < '\u06E5')
            return false;
        if(c <= '\u06E6')
            return true;
        if(c < '\u0905')
            return false;
        if(c <= '\u0939')
            return true;
        if(c == '\u093D')
            return true;
        if(c < '\u0958')
            return false;
        if(c <= '\u0961')
            return true;
        if(c < '\u0985')
            return false;
        if(c <= '\u098C')
            return true;
        if(c < '\u098F')
            return false;
        if(c <= '\u0990')
            return true;
        if(c < '\u0993')
            return false;
        if(c <= '\u09A8')
            return true;
        if(c < '\u09AA')
            return false;
        if(c <= '\u09B0')
            return true;
        if(c == '\u09B2')
            return true;
        if(c < '\u09B6')
            return false;
        if(c <= '\u09B9')
            return true;
        if(c < '\u09DC')
            return false;
        if(c <= '\u09DD')
            return true;
        if(c < '\u09DF')
            return false;
        if(c <= '\u09E1')
            return true;
        if(c < '\u09F0')
            return false;
        if(c <= '\u09F1')
            return true;
        if(c < '\u0A05')
            return false;
        if(c <= '\u0A0A')
            return true;
        if(c < '\u0A0F')
            return false;
        if(c <= '\u0A10')
            return true;
        if(c < '\u0A13')
            return false;
        if(c <= '\u0A28')
            return true;
        if(c < '\u0A2A')
            return false;
        if(c <= '\u0A30')
            return true;
        if(c < '\u0A32')
            return false;
        if(c <= '\u0A33')
            return true;
        if(c < '\u0A35')
            return false;
        if(c <= '\u0A36')
            return true;
        if(c < '\u0A38')
            return false;
        if(c <= '\u0A39')
            return true;
        if(c < '\u0A59')
            return false;
        if(c <= '\u0A5C')
            return true;
        if(c == '\u0A5E')
            return true;
        if(c < '\u0A72')
            return false;
        if(c <= '\u0A74')
            return true;
        if(c < '\u0A85')
            return false;
        if(c <= '\u0A8B')
            return true;
        if(c == '\u0A8D')
            return true;
        if(c < '\u0A8F')
            return false;
        if(c <= '\u0A91')
            return true;
        if(c < '\u0A93')
            return false;
        if(c <= '\u0AA8')
            return true;
        if(c < '\u0AAA')
            return false;
        if(c <= '\u0AB0')
            return true;
        if(c < '\u0AB2')
            return false;
        if(c <= '\u0AB3')
            return true;
        if(c < '\u0AB5')
            return false;
        if(c <= '\u0AB9')
            return true;
        if(c == '\u0ABD')
            return true;
        if(c == '\u0AE0')
            return true;
        if(c < '\u0B05')
            return false;
        if(c <= '\u0B0C')
            return true;
        if(c < '\u0B0F')
            return false;
        if(c <= '\u0B10')
            return true;
        if(c < '\u0B13')
            return false;
        if(c <= '\u0B28')
            return true;
        if(c < '\u0B2A')
            return false;
        if(c <= '\u0B30')
            return true;
        if(c < '\u0B32')
            return false;
        if(c <= '\u0B33')
            return true;
        if(c < '\u0B36')
            return false;
        if(c <= '\u0B39')
            return true;
        if(c == '\u0B3D')
            return true;
        if(c < '\u0B5C')
            return false;
        if(c <= '\u0B5D')
            return true;
        if(c < '\u0B5F')
            return false;
        if(c <= '\u0B61')
            return true;
        if(c < '\u0B85')
            return false;
        if(c <= '\u0B8A')
            return true;
        if(c < '\u0B8E')
            return false;
        if(c <= '\u0B90')
            return true;
        if(c < '\u0B92')
            return false;
        if(c <= '\u0B95')
            return true;
        if(c < '\u0B99')
            return false;
        if(c <= '\u0B9A')
            return true;
        if(c == '\u0B9C')
            return true;
        if(c < '\u0B9E')
            return false;
        if(c <= '\u0B9F')
            return true;
        if(c < '\u0BA3')
            return false;
        if(c <= '\u0BA4')
            return true;
        if(c < '\u0BA8')
            return false;
        if(c <= '\u0BAA')
            return true;
        if(c < '\u0BAE')
            return false;
        if(c <= '\u0BB5')
            return true;
        if(c < '\u0BB7')
            return false;
        if(c <= '\u0BB9')
            return true;
        if(c < '\u0C05')
            return false;
        if(c <= '\u0C0C')
            return true;
        if(c < '\u0C0E')
            return false;
        if(c <= '\u0C10')
            return true;
        if(c < '\u0C12')
            return false;
        if(c <= '\u0C28')
            return true;
        if(c < '\u0C2A')
            return false;
        if(c <= '\u0C33')
            return true;
        if(c < '\u0C35')
            return false;
        if(c <= '\u0C39')
            return true;
        if(c < '\u0C60')
            return false;
        if(c <= '\u0C61')
            return true;
        if(c < '\u0C85')
            return false;
        if(c <= '\u0C8C')
            return true;
        if(c < '\u0C8E')
            return false;
        if(c <= '\u0C90')
            return true;
        if(c < '\u0C92')
            return false;
        if(c <= '\u0CA8')
            return true;
        if(c < '\u0CAA')
            return false;
        if(c <= '\u0CB3')
            return true;
        if(c < '\u0CB5')
            return false;
        if(c <= '\u0CB9')
            return true;
        if(c == '\u0CDE')
            return true;
        if(c < '\u0CE0')
            return false;
        if(c <= '\u0CE1')
            return true;
        if(c < '\u0D05')
            return false;
        if(c <= '\u0D0C')
            return true;
        if(c < '\u0D0E')
            return false;
        if(c <= '\u0D10')
            return true;
        if(c < '\u0D12')
            return false;
        if(c <= '\u0D28')
            return true;
        if(c < '\u0D2A')
            return false;
        if(c <= '\u0D39')
            return true;
        if(c < '\u0D60')
            return false;
        if(c <= '\u0D61')
            return true;
        if(c < '\u0E01')
            return false;
        if(c <= '\u0E2E')
            return true;
        if(c == '\u0E30')
            return true;
        if(c < '\u0E32')
            return false;
        if(c <= '\u0E33')
            return true;
        if(c < '\u0E40')
            return false;
        if(c <= '\u0E45')
            return true;
        if(c < '\u0E81')
            return false;
        if(c <= '\u0E82')
            return true;
        if(c == '\u0E84')
            return true;
        if(c < '\u0E87')
            return false;
        if(c <= '\u0E88')
            return true;
        if(c == '\u0E8A')
            return true;
        if(c == '\u0E8D')
            return true;
        if(c < '\u0E94')
            return false;
        if(c <= '\u0E97')
            return true;
        if(c < '\u0E99')
            return false;
        if(c <= '\u0E9F')
            return true;
        if(c < '\u0EA1')
            return false;
        if(c <= '\u0EA3')
            return true;
        if(c == '\u0EA5')
            return true;
        if(c == '\u0EA7')
            return true;
        if(c < '\u0EAA')
            return false;
        if(c <= '\u0EAB')
            return true;
        if(c < '\u0EAD')
            return false;
        if(c <= '\u0EAE')
            return true;
        if(c == '\u0EB0')
            return true;
        if(c < '\u0EB2')
            return false;
        if(c <= '\u0EB3')
            return true;
        if(c == '\u0EBD')
            return true;
        if(c < '\u0EC0')
            return false;
        if(c <= '\u0EC4')
            return true;
        if(c < '\u0F40')
            return false;
        if(c <= '\u0F47')
            return true;
        if(c < '\u0F49')
            return false;
        if(c <= '\u0F69')
            return true;
        if(c < '\u10A0')
            return false;
        if(c <= '\u10C5')
            return true;
        if(c < '\u10D0')
            return false;
        if(c <= '\u10F6')
            return true;
        if(c == '\u1100')
            return true;
        if(c < '\u1102')
            return false;
        if(c <= '\u1103')
            return true;
        if(c < '\u1105')
            return false;
        if(c <= '\u1107')
            return true;
        if(c == '\u1109')
            return true;
        if(c < '\u110B')
            return false;
        if(c <= '\u110C')
            return true;
        if(c < '\u110E')
            return false;
        if(c <= '\u1112')
            return true;
        if(c == '\u113C')
            return true;
        if(c == '\u113E')
            return true;
        if(c == '\u1140')
            return true;
        if(c == '\u114C')
            return true;
        if(c == '\u114E')
            return true;
        if(c == '\u1150')
            return true;
        if(c < '\u1154')
            return false;
        if(c <= '\u1155')
            return true;
        if(c == '\u1159')
            return true;
        if(c < '\u115F')
            return false;
        if(c <= '\u1161')
            return true;
        if(c == '\u1163')
            return true;
        if(c == '\u1165')
            return true;
        if(c == '\u1167')
            return true;
        if(c == '\u1169')
            return true;
        if(c < '\u116D')
            return false;
        if(c <= '\u116E')
            return true;
        if(c < '\u1172')
            return false;
        if(c <= '\u1173')
            return true;
        if(c == '\u1175')
            return true;
        if(c == '\u119E')
            return true;
        if(c == '\u11A8')
            return true;
        if(c == '\u11AB')
            return true;
        if(c < '\u11AE')
            return false;
        if(c <= '\u11AF')
            return true;
        if(c < '\u11B7')
            return false;
        if(c <= '\u11B8')
            return true;
        if(c == '\u11BA')
            return true;
        if(c < '\u11BC')
            return false;
        if(c <= '\u11C2')
            return true;
        if(c == '\u11EB')
            return true;
        if(c == '\u11F0')
            return true;
        if(c == '\u11F9')
            return true;
        if(c < '\u1E00')
            return false;
        if(c <= '\u1E9B')
            return true;
        if(c < '\u1EA0')
            return false;
        if(c <= '\u1EF9')
            return true;
        if(c < '\u1F00')
            return false;
        if(c <= '\u1F15')
            return true;
        if(c < '\u1F18')
            return false;
        if(c <= '\u1F1D')
            return true;
        if(c < '\u1F20')
            return false;
        if(c <= '\u1F45')
            return true;
        if(c < '\u1F48')
            return false;
        if(c <= '\u1F4D')
            return true;
        if(c < '\u1F50')
            return false;
        if(c <= '\u1F57')
            return true;
        if(c == '\u1F59')
            return true;
        if(c == '\u1F5B')
            return true;
        if(c == '\u1F5D')
            return true;
        if(c < '\u1F5F')
            return false;
        if(c <= '\u1F7D')
            return true;
        if(c < '\u1F80')
            return false;
        if(c <= '\u1FB4')
            return true;
        if(c < '\u1FB6')
            return false;
        if(c <= '\u1FBC')
            return true;
        if(c == '\u1FBE')
            return true;
        if(c < '\u1FC2')
            return false;
        if(c <= '\u1FC4')
            return true;
        if(c < '\u1FC6')
            return false;
        if(c <= '\u1FCC')
            return true;
        if(c < '\u1FD0')
            return false;
        if(c <= '\u1FD3')
            return true;
        if(c < '\u1FD6')
            return false;
        if(c <= '\u1FDB')
            return true;
        if(c < '\u1FE0')
            return false;
        if(c <= '\u1FEC')
            return true;
        if(c < '\u1FF2')
            return false;
        if(c <= '\u1FF4')
            return true;
        if(c < '\u1FF6')
            return false;
        if(c <= '\u1FFC')
            return true;
        if(c == '\u2126')
            return true;
        if(c < '\u212A')
            return false;
        if(c <= '\u212B')
            return true;
        if(c == '\u212E')
            return true;
        if(c < '\u2180')
            return false;
        if(c <= '\u2182')
            return true;
        if(c == '\u3007')
            return true;
        if(c < '\u3021')
            return false;
        if(c <= '\u3029')
            return true;
        if(c < '\u3041')
            return false;
        if(c <= '\u3094')
            return true;
        if(c < '\u30A1')
            return false;
        if(c <= '\u30FA')
            return true;
        if(c < '\u3105')
            return false;
        if(c <= '\u312C')
            return true;
        if(c < '\u4E00')
            return false;
        if(c <= '\u9FA5')
            return true;
        if(c < '\uAC00')
            return false;
        return c <= '\uD7A3';
    }

    public static boolean isXMLCombiningChar(char c)
    {
        if(c < '\u0300')
            return false;
        if(c <= '\u0345')
            return true;
        if(c < '\u0360')
            return false;
        if(c <= '\u0361')
            return true;
        if(c < '\u0483')
            return false;
        if(c <= '\u0486')
            return true;
        if(c < '\u0591')
            return false;
        if(c <= '\u05A1')
            return true;
        if(c < '\u05A3')
            return false;
        if(c <= '\u05B9')
            return true;
        if(c < '\u05BB')
            return false;
        if(c <= '\u05BD')
            return true;
        if(c == '\u05BF')
            return true;
        if(c < '\u05C1')
            return false;
        if(c <= '\u05C2')
            return true;
        if(c == '\u05C4')
            return true;
        if(c < '\u064B')
            return false;
        if(c <= '\u0652')
            return true;
        if(c == '\u0670')
            return true;
        if(c < '\u06D6')
            return false;
        if(c <= '\u06DC')
            return true;
        if(c < '\u06DD')
            return false;
        if(c <= '\u06DF')
            return true;
        if(c < '\u06E0')
            return false;
        if(c <= '\u06E4')
            return true;
        if(c < '\u06E7')
            return false;
        if(c <= '\u06E8')
            return true;
        if(c < '\u06EA')
            return false;
        if(c <= '\u06ED')
            return true;
        if(c < '\u0901')
            return false;
        if(c <= '\u0903')
            return true;
        if(c == '\u093C')
            return true;
        if(c < '\u093E')
            return false;
        if(c <= '\u094C')
            return true;
        if(c == '\u094D')
            return true;
        if(c < '\u0951')
            return false;
        if(c <= '\u0954')
            return true;
        if(c < '\u0962')
            return false;
        if(c <= '\u0963')
            return true;
        if(c < '\u0981')
            return false;
        if(c <= '\u0983')
            return true;
        if(c == '\u09BC')
            return true;
        if(c == '\u09BE')
            return true;
        if(c == '\u09BF')
            return true;
        if(c < '\u09C0')
            return false;
        if(c <= '\u09C4')
            return true;
        if(c < '\u09C7')
            return false;
        if(c <= '\u09C8')
            return true;
        if(c < '\u09CB')
            return false;
        if(c <= '\u09CD')
            return true;
        if(c == '\u09D7')
            return true;
        if(c < '\u09E2')
            return false;
        if(c <= '\u09E3')
            return true;
        if(c == '\u0A02')
            return true;
        if(c == '\u0A3C')
            return true;
        if(c == '\u0A3E')
            return true;
        if(c == '\u0A3F')
            return true;
        if(c < '\u0A40')
            return false;
        if(c <= '\u0A42')
            return true;
        if(c < '\u0A47')
            return false;
        if(c <= '\u0A48')
            return true;
        if(c < '\u0A4B')
            return false;
        if(c <= '\u0A4D')
            return true;
        if(c < '\u0A70')
            return false;
        if(c <= '\u0A71')
            return true;
        if(c < '\u0A81')
            return false;
        if(c <= '\u0A83')
            return true;
        if(c == '\u0ABC')
            return true;
        if(c < '\u0ABE')
            return false;
        if(c <= '\u0AC5')
            return true;
        if(c < '\u0AC7')
            return false;
        if(c <= '\u0AC9')
            return true;
        if(c < '\u0ACB')
            return false;
        if(c <= '\u0ACD')
            return true;
        if(c < '\u0B01')
            return false;
        if(c <= '\u0B03')
            return true;
        if(c == '\u0B3C')
            return true;
        if(c < '\u0B3E')
            return false;
        if(c <= '\u0B43')
            return true;
        if(c < '\u0B47')
            return false;
        if(c <= '\u0B48')
            return true;
        if(c < '\u0B4B')
            return false;
        if(c <= '\u0B4D')
            return true;
        if(c < '\u0B56')
            return false;
        if(c <= '\u0B57')
            return true;
        if(c < '\u0B82')
            return false;
        if(c <= '\u0B83')
            return true;
        if(c < '\u0BBE')
            return false;
        if(c <= '\u0BC2')
            return true;
        if(c < '\u0BC6')
            return false;
        if(c <= '\u0BC8')
            return true;
        if(c < '\u0BCA')
            return false;
        if(c <= '\u0BCD')
            return true;
        if(c == '\u0BD7')
            return true;
        if(c < '\u0C01')
            return false;
        if(c <= '\u0C03')
            return true;
        if(c < '\u0C3E')
            return false;
        if(c <= '\u0C44')
            return true;
        if(c < '\u0C46')
            return false;
        if(c <= '\u0C48')
            return true;
        if(c < '\u0C4A')
            return false;
        if(c <= '\u0C4D')
            return true;
        if(c < '\u0C55')
            return false;
        if(c <= '\u0C56')
            return true;
        if(c < '\u0C82')
            return false;
        if(c <= '\u0C83')
            return true;
        if(c < '\u0CBE')
            return false;
        if(c <= '\u0CC4')
            return true;
        if(c < '\u0CC6')
            return false;
        if(c <= '\u0CC8')
            return true;
        if(c < '\u0CCA')
            return false;
        if(c <= '\u0CCD')
            return true;
        if(c < '\u0CD5')
            return false;
        if(c <= '\u0CD6')
            return true;
        if(c < '\u0D02')
            return false;
        if(c <= '\u0D03')
            return true;
        if(c < '\u0D3E')
            return false;
        if(c <= '\u0D43')
            return true;
        if(c < '\u0D46')
            return false;
        if(c <= '\u0D48')
            return true;
        if(c < '\u0D4A')
            return false;
        if(c <= '\u0D4D')
            return true;
        if(c == '\u0D57')
            return true;
        if(c == '\u0E31')
            return true;
        if(c < '\u0E34')
            return false;
        if(c <= '\u0E3A')
            return true;
        if(c < '\u0E47')
            return false;
        if(c <= '\u0E4E')
            return true;
        if(c == '\u0EB1')
            return true;
        if(c < '\u0EB4')
            return false;
        if(c <= '\u0EB9')
            return true;
        if(c < '\u0EBB')
            return false;
        if(c <= '\u0EBC')
            return true;
        if(c < '\u0EC8')
            return false;
        if(c <= '\u0ECD')
            return true;
        if(c < '\u0F18')
            return false;
        if(c <= '\u0F19')
            return true;
        if(c == '\u0F35')
            return true;
        if(c == '\u0F37')
            return true;
        if(c == '\u0F39')
            return true;
        if(c == '\u0F3E')
            return true;
        if(c == '\u0F3F')
            return true;
        if(c < '\u0F71')
            return false;
        if(c <= '\u0F84')
            return true;
        if(c < '\u0F86')
            return false;
        if(c <= '\u0F8B')
            return true;
        if(c < '\u0F90')
            return false;
        if(c <= '\u0F95')
            return true;
        if(c == '\u0F97')
            return true;
        if(c < '\u0F99')
            return false;
        if(c <= '\u0FAD')
            return true;
        if(c < '\u0FB1')
            return false;
        if(c <= '\u0FB7')
            return true;
        if(c == '\u0FB9')
            return true;
        if(c < '\u20D0')
            return false;
        if(c <= '\u20DC')
            return true;
        if(c == '\u20E1')
            return true;
        if(c < '\u302A')
            return false;
        if(c <= '\u302F')
            return true;
        if(c == '\u3099')
            return true;
        return c == '\u309A';
    }

    public static boolean isXMLExtender(char c)
    {
        if(c < '\266')
            return false;
        if(c == '\267')
            return true;
        if(c == '\u02D0')
            return true;
        if(c == '\u02D1')
            return true;
        if(c == '\u0387')
            return true;
        if(c == '\u0640')
            return true;
        if(c == '\u0E46')
            return true;
        if(c == '\u0EC6')
            return true;
        if(c == '\u3005')
            return true;
        if(c < '\u3031')
            return false;
        if(c <= '\u3035')
            return true;
        if(c < '\u309D')
            return false;
        if(c <= '\u309E')
            return true;
        if(c < '\u30FC')
            return false;
        return c <= '\u30FE';
    }

    public static boolean isXMLDigit(char c)
    {
        if(c < '0')
            return false;
        if(c <= '9')
            return true;
        if(c < '\u0660')
            return false;
        if(c <= '\u0669')
            return true;
        if(c < '\u06F0')
            return false;
        if(c <= '\u06F9')
            return true;
        if(c < '\u0966')
            return false;
        if(c <= '\u096F')
            return true;
        if(c < '\u09E6')
            return false;
        if(c <= '\u09EF')
            return true;
        if(c < '\u0A66')
            return false;
        if(c <= '\u0A6F')
            return true;
        if(c < '\u0AE6')
            return false;
        if(c <= '\u0AEF')
            return true;
        if(c < '\u0B66')
            return false;
        if(c <= '\u0B6F')
            return true;
        if(c < '\u0BE7')
            return false;
        if(c <= '\u0BEF')
            return true;
        if(c < '\u0C66')
            return false;
        if(c <= '\u0C6F')
            return true;
        if(c < '\u0CE6')
            return false;
        if(c <= '\u0CEF')
            return true;
        if(c < '\u0D66')
            return false;
        if(c <= '\u0D6F')
            return true;
        if(c < '\u0E50')
            return false;
        if(c <= '\u0E59')
            return true;
        if(c < '\u0ED0')
            return false;
        if(c <= '\u0ED9')
            return true;
        if(c < '\u0F20')
            return false;
        return c <= '\u0F29';
    }

    private static boolean isXMLLetterOld(char c)
    {
        if(c >= 'A' && c <= 'Z')
            return true;
        if(c >= 'a' && c <= 'z')
            return true;
        if(c >= '\300' && c <= '\326')
            return true;
        if(c >= '\330' && c <= '\366')
            return true;
        if(c >= '\370' && c <= '\377')
            return true;
        if(c >= '\u0100' && c <= '\u0131')
            return true;
        if(c >= '\u0134' && c <= '\u013E')
            return true;
        if(c >= '\u0141' && c <= '\u0148')
            return true;
        if(c >= '\u014A' && c <= '\u017E')
            return true;
        if(c >= '\u0180' && c <= '\u01C3')
            return true;
        if(c >= '\u01CD' && c <= '\u01F0')
            return true;
        if(c >= '\u01F4' && c <= '\u01F5')
            return true;
        if(c >= '\u01FA' && c <= '\u0217')
            return true;
        if(c >= '\u0250' && c <= '\u02A8')
            return true;
        if(c >= '\u02BB' && c <= '\u02C1')
            return true;
        if(c >= '\u0388' && c <= '\u038A')
            return true;
        if(c == '\u0386')
            return true;
        if(c == '\u038C')
            return true;
        if(c >= '\u038E' && c <= '\u03A1')
            return true;
        if(c >= '\u03A3' && c <= '\u03CE')
            return true;
        if(c >= '\u03D0' && c <= '\u03D6')
            return true;
        if(c == '\u03DA')
            return true;
        if(c == '\u03DC')
            return true;
        if(c == '\u03DE')
            return true;
        if(c == '\u03E0')
            return true;
        if(c >= '\u03E2' && c <= '\u03F3')
            return true;
        if(c >= '\u0401' && c <= '\u040C')
            return true;
        if(c >= '\u040E' && c <= '\u044F')
            return true;
        if(c >= '\u0451' && c <= '\u045C')
            return true;
        if(c >= '\u045E' && c <= '\u0481')
            return true;
        if(c >= '\u0490' && c <= '\u04C4')
            return true;
        if(c >= '\u04C7' && c <= '\u04C8')
            return true;
        if(c >= '\u04CB' && c <= '\u04CC')
            return true;
        if(c >= '\u04D0' && c <= '\u04EB')
            return true;
        if(c >= '\u04EE' && c <= '\u04F5')
            return true;
        if(c >= '\u04F8' && c <= '\u04F9')
            return true;
        if(c >= '\u0531' && c <= '\u0556')
            return true;
        if(c == '\u0559')
            return true;
        if(c >= '\u0561' && c <= '\u0586')
            return true;
        if(c >= '\u05D0' && c <= '\u05EA')
            return true;
        if(c >= '\u05F0' && c <= '\u05F2')
            return true;
        if(c >= '\u0621' && c <= '\u063A')
            return true;
        if(c >= '\u0641' && c <= '\u064A')
            return true;
        if(c >= '\u0671' && c <= '\u06B7')
            return true;
        if(c >= '\u06BA' && c <= '\u06BE')
            return true;
        if(c >= '\u06C0' && c <= '\u06CE')
            return true;
        if(c >= '\u06D0' && c <= '\u06D3')
            return true;
        if(c == '\u06D5')
            return true;
        if(c >= '\u06E5' && c <= '\u06E6')
            return true;
        if(c >= '\u0905' && c <= '\u0939')
            return true;
        if(c == '\u093D')
            return true;
        if(c >= '\u0958' && c <= '\u0961')
            return true;
        if(c >= '\u0985' && c <= '\u098C')
            return true;
        if(c >= '\u098F' && c <= '\u0990')
            return true;
        if(c >= '\u0993' && c <= '\u09A8')
            return true;
        if(c >= '\u09AA' && c <= '\u09B0')
            return true;
        if(c == '\u09B2')
            return true;
        if(c >= '\u09B6' && c <= '\u09B9')
            return true;
        if(c >= '\u09DC' && c <= '\u09DD')
            return true;
        if(c >= '\u09DF' && c <= '\u09E1')
            return true;
        if(c >= '\u09F0' && c <= '\u09F1')
            return true;
        if(c >= '\u0A05' && c <= '\u0A0A')
            return true;
        if(c >= '\u0A0F' && c <= '\u0A10')
            return true;
        if(c >= '\u0A13' && c <= '\u0A28')
            return true;
        if(c >= '\u0A2A' && c <= '\u0A30')
            return true;
        if(c >= '\u0A32' && c <= '\u0A33')
            return true;
        if(c >= '\u0A35' && c <= '\u0A36')
            return true;
        if(c >= '\u0A38' && c <= '\u0A39')
            return true;
        if(c >= '\u0A59' && c <= '\u0A5C')
            return true;
        if(c == '\u0A5E')
            return true;
        if(c >= '\u0A72' && c <= '\u0A74')
            return true;
        if(c >= '\u0A85' && c <= '\u0A8B')
            return true;
        if(c == '\u0A8D')
            return true;
        if(c >= '\u0A8F' && c <= '\u0A91')
            return true;
        if(c >= '\u0A93' && c <= '\u0AA8')
            return true;
        if(c >= '\u0AAA' && c <= '\u0AB0')
            return true;
        if(c >= '\u0AB2' && c <= '\u0AB3')
            return true;
        if(c >= '\u0AB5' && c <= '\u0AB9')
            return true;
        if(c == '\u0ABD')
            return true;
        if(c == '\u0AE0')
            return true;
        if(c >= '\u0B05' && c <= '\u0B0C')
            return true;
        if(c >= '\u0B0F' && c <= '\u0B10')
            return true;
        if(c >= '\u0B13' && c <= '\u0B28')
            return true;
        if(c >= '\u0B2A' && c <= '\u0B30')
            return true;
        if(c >= '\u0B32' && c <= '\u0B33')
            return true;
        if(c >= '\u0B36' && c <= '\u0B39')
            return true;
        if(c == '\u0B3D')
            return true;
        if(c >= '\u0B5C' && c <= '\u0B5D')
            return true;
        if(c >= '\u0B5F' && c <= '\u0B61')
            return true;
        if(c >= '\u0B85' && c <= '\u0B8A')
            return true;
        if(c >= '\u0B8E' && c <= '\u0B90')
            return true;
        if(c >= '\u0B92' && c <= '\u0B95')
            return true;
        if(c >= '\u0B99' && c <= '\u0B9A')
            return true;
        if(c == '\u0B9C')
            return true;
        if(c >= '\u0B9E' && c <= '\u0B9F')
            return true;
        if(c >= '\u0BA3' && c <= '\u0BA4')
            return true;
        if(c >= '\u0BA8' && c <= '\u0BAA')
            return true;
        if(c >= '\u0BAE' && c <= '\u0BB5')
            return true;
        if(c >= '\u0BB7' && c <= '\u0BB9')
            return true;
        if(c >= '\u0C05' && c <= '\u0C0C')
            return true;
        if(c >= '\u0C0E' && c <= '\u0C10')
            return true;
        if(c >= '\u0C12' && c <= '\u0C28')
            return true;
        if(c >= '\u0C2A' && c <= '\u0C33')
            return true;
        if(c >= '\u0C35' && c <= '\u0C39')
            return true;
        if(c >= '\u0C60' && c <= '\u0C61')
            return true;
        if(c >= '\u0C85' && c <= '\u0C8C')
            return true;
        if(c >= '\u0C8E' && c <= '\u0C90')
            return true;
        if(c >= '\u0C92' && c <= '\u0CA8')
            return true;
        if(c >= '\u0CAA' && c <= '\u0CB3')
            return true;
        if(c >= '\u0CB5' && c <= '\u0CB9')
            return true;
        if(c == '\u0CDE')
            return true;
        if(c >= '\u0CE0' && c <= '\u0CE1')
            return true;
        if(c >= '\u0D05' && c <= '\u0D0C')
            return true;
        if(c >= '\u0D0E' && c <= '\u0D10')
            return true;
        if(c >= '\u0D12' && c <= '\u0D28')
            return true;
        if(c >= '\u0D2A' && c <= '\u0D39')
            return true;
        if(c >= '\u0D60' && c <= '\u0D61')
            return true;
        if(c >= '\u0E01' && c <= '\u0E2E')
            return true;
        if(c == '\u0E30')
            return true;
        if(c >= '\u0E32' && c <= '\u0E33')
            return true;
        if(c >= '\u0E40' && c <= '\u0E45')
            return true;
        if(c >= '\u0E81' && c <= '\u0E82')
            return true;
        if(c == '\u0E84')
            return true;
        if(c >= '\u0E87' && c <= '\u0E88')
            return true;
        if(c == '\u0E8A')
            return true;
        if(c == '\u0E8D')
            return true;
        if(c >= '\u0E94' && c <= '\u0E97')
            return true;
        if(c >= '\u0E99' && c <= '\u0E9F')
            return true;
        if(c >= '\u0EA1' && c <= '\u0EA3')
            return true;
        if(c == '\u0EA5')
            return true;
        if(c == '\u0EA7')
            return true;
        if(c >= '\u0EAA' && c <= '\u0EAB')
            return true;
        if(c >= '\u0EAD' && c <= '\u0EAE')
            return true;
        if(c == '\u0EB0')
            return true;
        if(c >= '\u0EB2' && c <= '\u0EB3')
            return true;
        if(c == '\u0EBD')
            return true;
        if(c >= '\u0EC0' && c <= '\u0EC4')
            return true;
        if(c >= '\u0F40' && c <= '\u0F47')
            return true;
        if(c >= '\u0F49' && c <= '\u0F69')
            return true;
        if(c >= '\u10A0' && c <= '\u10C5')
            return true;
        if(c >= '\u10D0' && c <= '\u10F6')
            return true;
        if(c == '\u1100')
            return true;
        if(c >= '\u1102' && c <= '\u1103')
            return true;
        if(c >= '\u1105' && c <= '\u1107')
            return true;
        if(c == '\u1109')
            return true;
        if(c >= '\u110B' && c <= '\u110C')
            return true;
        if(c >= '\u110E' && c <= '\u1112')
            return true;
        if(c == '\u113C')
            return true;
        if(c == '\u113E')
            return true;
        if(c == '\u1140')
            return true;
        if(c == '\u114C')
            return true;
        if(c == '\u114E')
            return true;
        if(c == '\u1150')
            return true;
        if(c >= '\u1154' && c <= '\u1155')
            return true;
        if(c == '\u1159')
            return true;
        if(c >= '\u115F' && c <= '\u1161')
            return true;
        if(c == '\u1163')
            return true;
        if(c == '\u1165')
            return true;
        if(c == '\u1167')
            return true;
        if(c == '\u1169')
            return true;
        if(c >= '\u116D' && c <= '\u116E')
            return true;
        if(c >= '\u1172' && c <= '\u1173')
            return true;
        if(c == '\u1175')
            return true;
        if(c == '\u119E')
            return true;
        if(c == '\u11A8')
            return true;
        if(c == '\u11AB')
            return true;
        if(c >= '\u11AE' && c <= '\u11AF')
            return true;
        if(c >= '\u11B7' && c <= '\u11B8')
            return true;
        if(c == '\u11BA')
            return true;
        if(c >= '\u11BC' && c <= '\u11C2')
            return true;
        if(c == '\u11EB')
            return true;
        if(c == '\u11F0')
            return true;
        if(c == '\u11F9')
            return true;
        if(c >= '\u1E00' && c <= '\u1E9B')
            return true;
        if(c >= '\u1EA0' && c <= '\u1EF9')
            return true;
        if(c >= '\u1F00' && c <= '\u1F15')
            return true;
        if(c >= '\u1F18' && c <= '\u1F1D')
            return true;
        if(c >= '\u1F20' && c <= '\u1F45')
            return true;
        if(c >= '\u1F48' && c <= '\u1F4D')
            return true;
        if(c >= '\u1F50' && c <= '\u1F57')
            return true;
        if(c == '\u1F59')
            return true;
        if(c == '\u1F5B')
            return true;
        if(c == '\u1F5D')
            return true;
        if(c >= '\u1F5F' && c <= '\u1F7D')
            return true;
        if(c >= '\u1F80' && c <= '\u1FB4')
            return true;
        if(c >= '\u1FB6' && c <= '\u1FBC')
            return true;
        if(c == '\u1FBE')
            return true;
        if(c >= '\u1FC2' && c <= '\u1FC4')
            return true;
        if(c >= '\u1FC6' && c <= '\u1FCC')
            return true;
        if(c >= '\u1FD0' && c <= '\u1FD3')
            return true;
        if(c >= '\u1FD6' && c <= '\u1FDB')
            return true;
        if(c >= '\u1FE0' && c <= '\u1FEC')
            return true;
        if(c >= '\u1FF2' && c <= '\u1FF4')
            return true;
        if(c >= '\u1FF6' && c <= '\u1FFC')
            return true;
        if(c == '\u2126')
            return true;
        if(c >= '\u212A' && c <= '\u212B')
            return true;
        if(c == '\u212E')
            return true;
        if(c >= '\u2180' && c <= '\u2182')
            return true;
        if(c >= '\u3041' && c <= '\u3094')
            return true;
        if(c >= '\u30A1' && c <= '\u30FA')
            return true;
        if(c >= '\u3105' && c <= '\u312C')
            return true;
        if(c >= '\uAC00' && c <= '\uD7A3')
            return true;
        if(c >= '\u4E00' && c <= '\u9FA5')
            return true;
        if(c == '\u3007')
            return true;
        return c >= '\u3021' && c <= '\u3029';
    }

    private static boolean isXMLDigitOld(char c)
    {
        if(c >= '0' && c <= '9')
            return true;
        if(c >= '\u0660' && c <= '\u0669')
            return true;
        if(c >= '\u06F0' && c <= '\u06F9')
            return true;
        if(c >= '\u0966' && c <= '\u096F')
            return true;
        if(c >= '\u09E6' && c <= '\u09EF')
            return true;
        if(c >= '\u0A66' && c <= '\u0A6F')
            return true;
        if(c >= '\u0AE6' && c <= '\u0AEF')
            return true;
        if(c >= '\u0B66' && c <= '\u0B6F')
            return true;
        if(c >= '\u0BE7' && c <= '\u0BEF')
            return true;
        if(c >= '\u0C66' && c <= '\u0C6F')
            return true;
        if(c >= '\u0CE6' && c <= '\u0CEF')
            return true;
        if(c >= '\u0D66' && c <= '\u0D6F')
            return true;
        if(c >= '\u0E50' && c <= '\u0E59')
            return true;
        if(c >= '\u0ED0' && c <= '\u0ED9')
            return true;
        return c >= '\u0F20' && c <= '\u0F29';
    }

    private static boolean isXMLCombiningCharOld(char c)
    {
        if(c >= '\u0300' && c <= '\u0345')
            return true;
        if(c >= '\u0360' && c <= '\u0361')
            return true;
        if(c >= '\u0483' && c <= '\u0486')
            return true;
        if(c >= '\u0591' && c <= '\u05A1')
            return true;
        if(c >= '\u05A3' && c <= '\u05B9')
            return true;
        if(c >= '\u05BB' && c <= '\u05BD')
            return true;
        if(c == '\u05BF')
            return true;
        if(c >= '\u05C1' && c <= '\u05C2')
            return true;
        if(c == '\u05C4')
            return true;
        if(c >= '\u064B' && c <= '\u0652')
            return true;
        if(c == '\u0670')
            return true;
        if(c >= '\u06D6' && c <= '\u06DC')
            return true;
        if(c >= '\u06DD' && c <= '\u06DF')
            return true;
        if(c >= '\u06E0' && c <= '\u06E4')
            return true;
        if(c >= '\u06E7' && c <= '\u06E8')
            return true;
        if(c >= '\u06EA' && c <= '\u06ED')
            return true;
        if(c >= '\u0901' && c <= '\u0903')
            return true;
        if(c == '\u093C')
            return true;
        if(c >= '\u093E' && c <= '\u094C')
            return true;
        if(c == '\u094D')
            return true;
        if(c >= '\u0951' && c <= '\u0954')
            return true;
        if(c >= '\u0962' && c <= '\u0963')
            return true;
        if(c >= '\u0981' && c <= '\u0983')
            return true;
        if(c == '\u09BC')
            return true;
        if(c == '\u09BE')
            return true;
        if(c == '\u09BF')
            return true;
        if(c >= '\u09C0' && c <= '\u09C4')
            return true;
        if(c >= '\u09C7' && c <= '\u09C8')
            return true;
        if(c >= '\u09CB' && c <= '\u09CD')
            return true;
        if(c == '\u09D7')
            return true;
        if(c >= '\u09E2' && c <= '\u09E3')
            return true;
        if(c == '\u0A02')
            return true;
        if(c == '\u0A3C')
            return true;
        if(c == '\u0A3E')
            return true;
        if(c == '\u0A3F')
            return true;
        if(c >= '\u0A40' && c <= '\u0A42')
            return true;
        if(c >= '\u0A47' && c <= '\u0A48')
            return true;
        if(c >= '\u0A4B' && c <= '\u0A4D')
            return true;
        if(c >= '\u0A70' && c <= '\u0A71')
            return true;
        if(c >= '\u0A81' && c <= '\u0A83')
            return true;
        if(c == '\u0ABC')
            return true;
        if(c >= '\u0ABE' && c <= '\u0AC5')
            return true;
        if(c >= '\u0AC7' && c <= '\u0AC9')
            return true;
        if(c >= '\u0ACB' && c <= '\u0ACD')
            return true;
        if(c >= '\u0B01' && c <= '\u0B03')
            return true;
        if(c == '\u0B3C')
            return true;
        if(c >= '\u0B3E' && c <= '\u0B43')
            return true;
        if(c >= '\u0B47' && c <= '\u0B48')
            return true;
        if(c >= '\u0B4B' && c <= '\u0B4D')
            return true;
        if(c >= '\u0B56' && c <= '\u0B57')
            return true;
        if(c >= '\u0B82' && c <= '\u0B83')
            return true;
        if(c >= '\u0BBE' && c <= '\u0BC2')
            return true;
        if(c >= '\u0BC6' && c <= '\u0BC8')
            return true;
        if(c >= '\u0BCA' && c <= '\u0BCD')
            return true;
        if(c == '\u0BD7')
            return true;
        if(c >= '\u0C01' && c <= '\u0C03')
            return true;
        if(c >= '\u0C3E' && c <= '\u0C44')
            return true;
        if(c >= '\u0C46' && c <= '\u0C48')
            return true;
        if(c >= '\u0C4A' && c <= '\u0C4D')
            return true;
        if(c >= '\u0C55' && c <= '\u0C56')
            return true;
        if(c >= '\u0C82' && c <= '\u0C83')
            return true;
        if(c >= '\u0CBE' && c <= '\u0CC4')
            return true;
        if(c >= '\u0CC6' && c <= '\u0CC8')
            return true;
        if(c >= '\u0CCA' && c <= '\u0CCD')
            return true;
        if(c >= '\u0CD5' && c <= '\u0CD6')
            return true;
        if(c >= '\u0D02' && c <= '\u0D03')
            return true;
        if(c >= '\u0D3E' && c <= '\u0D43')
            return true;
        if(c >= '\u0D46' && c <= '\u0D48')
            return true;
        if(c >= '\u0D4A' && c <= '\u0D4D')
            return true;
        if(c == '\u0D57')
            return true;
        if(c == '\u0E31')
            return true;
        if(c >= '\u0E34' && c <= '\u0E3A')
            return true;
        if(c >= '\u0E47' && c <= '\u0E4E')
            return true;
        if(c == '\u0EB1')
            return true;
        if(c >= '\u0EB4' && c <= '\u0EB9')
            return true;
        if(c >= '\u0EBB' && c <= '\u0EBC')
            return true;
        if(c >= '\u0EC8' && c <= '\u0ECD')
            return true;
        if(c >= '\u0F18' && c <= '\u0F19')
            return true;
        if(c == '\u0F35')
            return true;
        if(c == '\u0F37')
            return true;
        if(c == '\u0F39')
            return true;
        if(c == '\u0F3E')
            return true;
        if(c == '\u0F3F')
            return true;
        if(c >= '\u0F71' && c <= '\u0F84')
            return true;
        if(c >= '\u0F86' && c <= '\u0F8B')
            return true;
        if(c >= '\u0F90' && c <= '\u0F95')
            return true;
        if(c == '\u0F97')
            return true;
        if(c >= '\u0F99' && c <= '\u0FAD')
            return true;
        if(c >= '\u0FB1' && c <= '\u0FB7')
            return true;
        if(c == '\u0FB9')
            return true;
        if(c >= '\u20D0' && c <= '\u20DC')
            return true;
        if(c == '\u20E1')
            return true;
        if(c >= '\u302A' && c <= '\u302F')
            return true;
        if(c == '\u3099')
            return true;
        return c == '\u309A';
    }

    private static boolean isXMLExtenderOld(char c)
    {
        if(c == '\267')
            return true;
        if(c == '\u02D0')
            return true;
        if(c == '\u02D1')
            return true;
        if(c == '\u0387')
            return true;
        if(c == '\u0640')
            return true;
        if(c == '\u0E46')
            return true;
        if(c == '\u0EC6')
            return true;
        if(c == '\u3005')
            return true;
        if(c >= '\u3031' && c <= '\u3035')
            return true;
        if(c >= '\u309D' && c <= '\u309E')
            return true;
        return c >= '\u30FC' && c <= '\u30FE';
    }

    private static boolean isXMLCharacterOld(char c)
    {
        if(c >= ' ' && c <= '\uD7FF')
            return true;
        if(c >= '\uE000' && c <= '\uFFFD')
            return true;
        if(c >= '\0' && c <= '\0')
            return true;
        if(c == '\n')
            return true;
        if(c == '\r')
            return true;
        return c == '\t';
    }

    public static void main(String args[])
    {
        for(int i = 0; i < 0x10000; i++)
            if(isXMLLetter((char)i) != isXMLLetterOld((char)i)){}
                //Log.info("isXMLLetter mismatch: " + i + " hex: " + Integer.toHexString(i));

        for(int i = 0; i < 0x10000; i++)
            if(isXMLDigit((char)i) != isXMLDigitOld((char)i)){}
                //Log.info("isXMLDigit mismatch: " + i + " hex: " + Integer.toHexString(i));

        for(int i = 0; i < 0x10000; i++)
            if(isXMLCombiningChar((char)i) != isXMLCombiningCharOld((char)i)){}
                //Log.info("isXMLCombiningChar mismatch: " + i + " hex: " + Integer.toHexString(i));

        for(int i = 0; i < 0x10000; i++)
            if(isXMLExtender((char)i) != isXMLExtenderOld((char)i)){}
                //Log.info("isXMLExtender mismatch: " + i + " hex: " + Integer.toHexString(i));

        for(int i = 0; i < 0x10000; i++)
            if(isXMLCharacter((char)i) != isXMLCharacterOld((char)i)){}
                //Log.info("isXMLCharacter mismatch: " + i + " hex: " + Integer.toHexString(i));

    }

    private static final String CVS_ID = "@(#) $RCSfile: Verifier.java,v $ $Revision: 1.1 $ $Date: 2006/02/28 01:59:25 $ $Name:  $";
}
