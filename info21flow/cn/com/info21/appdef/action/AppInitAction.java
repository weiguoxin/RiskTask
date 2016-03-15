/*
 * 创建日期 2005-9-16
 */

package cn.com.info21.appdef.action;
import cn.com.info21.appdef.*;

/**
 * @author lkh
 */

public class AppInitAction {
    public AppInitAction() {
    }
    public static String run(int actid) {
        String xmlstr = null;
        try {
            String tmpstr1 = null;
            String tmpstr2 = null;
            String tmpstr3 = null;
            BAMappingIterator bi = BAMappingHome.findByCondition(" actid = " + actid);
            Button btn = null;
            while (bi.hasNext()) {
                btn = ButtonHome.findById(((BAMapping)bi.next()).getButtonId());
                if (null != btn) {
                    if (null == tmpstr1) {
                        tmpstr1 = btn.getName();
                    } else {
                        tmpstr1 = tmpstr1 + "," + btn.getName();
                    }
                }
            }
            
            FAMappingIterator fi = FAMappingHome.findByCondition(" actid = " + actid);
            Field field = null;
            while (fi.hasNext()) {
                field = FieldHome.findById(((FAMapping)fi.next()).getFieldId());
                if (null != field) {
                    if (null == tmpstr2) {
                        tmpstr2 = field.getFieldName();
                    } else {
                        tmpstr2 = tmpstr2 + "," + field.getFieldName();
                    }
                }
            }
            
            IAMappingIterator ii = IAMappingHome.findByCondition(" actid = " + actid);
            Idea idea = null;
            while (ii.hasNext()) {
                idea = IdeaHome.findById(((IAMapping)ii.next()).getIdeaId());
                if ( null != idea) {
                    if (null == tmpstr3) {
                        tmpstr3 = idea.getIdeaName();
                    } else {
                        tmpstr3 = tmpstr3 + "," + idea.getIdeaName();
                    }
                }
            }
            
            if (null != tmpstr1) {
                xmlstr = "<actionlist>" + tmpstr1 + "</actionlist>";
            }
            
            if (null != tmpstr2) {
                if (null != xmlstr) {
                    xmlstr = xmlstr + "<fieldlist>" + tmpstr2 + "</fieldlist>";
                } else {
                    xmlstr = "<fieldlist>" + tmpstr2 + "</fieldlist>";
                }
            }
            
            if (null != tmpstr3) {
                if (null != xmlstr) {
                    xmlstr = xmlstr + "<idealist>" + tmpstr3 + "</idealist>";
                } else {
                    xmlstr = "<idealist>" + tmpstr3 + "</idealist>";
                }
            }
            
            if (xmlstr != null) {
                xmlstr = "<param>" + xmlstr + "</param>";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlstr;
    }
}
