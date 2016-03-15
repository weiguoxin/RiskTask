/*
 * 创建日期 2005-9-1
 */

package cn.com.info21.appdef;

/**
 * @author lkh
 */

public class AppInit {
    public static String run(int actid) {
        String xmlstr = null;
        try {
            String tmpstr = null;
            ButtonIterator bi = App.getButtonByActId(actid);
            Button bn = null;
            while (bi.hasNext()) {
               bn = (Button)bi.next();
               if (null == tmpstr) {
                   tmpstr = bn.getName();
               } else {
                   tmpstr += "," + bn.getName();
               }
            }
            if (null != tmpstr) {
                xmlstr = "<actionlist>" + tmpstr + "</actionlist>";
                tmpstr = null;
            }
            FieldIterator fi = App.getFieldByActid(actid);
            Field fd = null;
            while(fi.hasNext()) {
                fd = (Field)fi.next();
                if (null == tmpstr) {
                    tmpstr = fd.getFieldName();
                } else {
                    tmpstr += "," + fd.getFieldName();
                }
            }
            if (null != tmpstr) {
                if (null == xmlstr) {
                    xmlstr = "<fieldlist>" + tmpstr + "</fieldlist>";
                } else {
                    xmlstr += "<fieldlist>" + tmpstr + "</fieldlist>";
                }
                tmpstr = null;
            }
            IdeaIterator ii = App.getIdeaByActId(actid);
            Idea idea = null;
            while (ii.hasNext()) {
                idea = (Idea)ii.next();
                if (null == tmpstr) {
                    tmpstr = idea.getIdeaName();
                } else {
                    tmpstr += "," + idea.getIdeaName();
                }
            }
            if (null != tmpstr) {
                if (null == xmlstr) {
                    xmlstr = "<idealist>" + tmpstr + "</idealist>";
                } else {
                    xmlstr += "<idealist>" + tmpstr + "</idealist>";
                }
                tmpstr = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlstr;
    }
}