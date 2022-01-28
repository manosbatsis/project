package com.topideal.order.tools.pdf;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

public class FreemakerUtils {

	/**
	 * 按模版和传入参数生成html字符串
	 * @param dataMap
	 * @param templateString
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	@SuppressWarnings("deprecation")
	public static String genHtmlStrWithTemplate(Map<String,Object> dataMap,String templateString) throws Exception{
        StringWriter stringWriter = new StringWriter();
        
        //xml 特殊字符转换
        for (String key : dataMap.keySet()) {
        	
        	Object val = dataMap.get(key);
			
        	if(val instanceof String) {
        		String tempVal = String.valueOf(val) ;
        		
        		if(tempVal.contains("&")) {
        			tempVal = tempVal.replaceAll("&", "&amp;") ;
        		}
        		
        		if(tempVal.contains("<")) {
        			tempVal = tempVal.replaceAll("< ", "&lt;") ;
        		}
        		
        		if(tempVal.contains(">")) {
        			tempVal = tempVal.replaceAll(">", "&gt;") ;
        		}
        		
        		if(tempVal.contains("'")) {
        			tempVal = tempVal.replaceAll("'", "&apos;") ;
        		}
        		
        		if(tempVal.contains("\"")) {
        			tempVal = tempVal.replaceAll("\"", "&quot;") ;
        		}
        		
        		dataMap.put(key, tempVal) ;
        	}
        	
		}
        
        //使用一个模板加载器变成模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateString);
        //在配置中设置模板加载器
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(stringTemplateLoader);

        Template template = configuration.getTemplate("template","utf-8");
        template.process(dataMap, stringWriter);
            
        stringWriter.flush();
        
        return stringWriter.toString();
    }
	
	/**
	 * 格式替换html
	 */
	public static String  formatReplacementHtml(String sourceHtml) {
		
		if(sourceHtml.contains("<!--#liststart-->")
				&& sourceHtml.contains("<!--#listend-->")) {
			sourceHtml = sourceHtml.replace("<!--#liststart-->", "<#list goodsList as item>") ;
			sourceHtml = sourceHtml.replace("<!--#listend-->", "</#list>") ;
		}

		if(sourceHtml.contains("<!--#costListstart-->")
				&& sourceHtml.contains("<!--#costListend-->")) {
			sourceHtml = sourceHtml.replace("<!--#costListstart-->", "<#list costList as costItem>") ;
			sourceHtml = sourceHtml.replace("<!--#costListend-->", "</#list>") ;
		}
		
		if(!sourceHtml.contains("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + 
				"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"utf-8\"/></head><body style = \"font-family: SimSun;\">")) {
			StringBuffer htmlSb = new StringBuffer() ;
			
			htmlSb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + 
					"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">") ;
			htmlSb.append("<head><meta charset=\"utf-8\"/>") ;
			htmlSb.append("<style>\n table, tr, td, th, tbody, thead, tfoot {page-break-inside: avoid !important;} \n </style>") ;
			htmlSb.append("</head>") ;
			htmlSb.append("<body style = \"font-family: SimSun;\">") ;
			htmlSb.append(sourceHtml) ;
			htmlSb.append("</body></html>") ;
			
			sourceHtml = htmlSb.toString() ;
			
		}
		
		return sourceHtml ;
	}
	
	/**
	 * 格式替换html
	 */
	public static String  formatReplacementHorizontalHtml(String sourceHtml) {
		
		if(sourceHtml.contains("<!--#liststart-->")
				&& sourceHtml.contains("<!--#listend-->")) {
			sourceHtml = sourceHtml.replace("<!--#liststart-->", "<#list goodsList as item>") ;
			sourceHtml = sourceHtml.replace("<!--#listend-->", "</#list>") ;
		}

		if(sourceHtml.contains("<!--#costListstart-->")
				&& sourceHtml.contains("<!--#costListend-->")) {
			sourceHtml = sourceHtml.replace("<!--#costListstart-->", "<#list costList as costItem>") ;
			sourceHtml = sourceHtml.replace("<!--#costListend-->", "</#list>") ;
		}
		
		if(!sourceHtml.contains("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + 
				"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"utf-8\"/></head><body style = \"font-family: SimSun;\">")) {
			StringBuffer htmlSb = new StringBuffer() ;
			
			htmlSb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + 
					"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">") ;
			htmlSb.append("<head><meta charset=\"utf-8\"/>") ;
			htmlSb.append("</head>") ;
			htmlSb.append("<body style = \"font-family: SimSun;\">") ;
			htmlSb.append(sourceHtml) ;
			htmlSb.append("</body></html>") ;
			
			sourceHtml = htmlSb.toString() ;
			
		}
		
		return sourceHtml ;
	}
	
	/**
	 * 转换换行符为<br/>
	 * @param html
	 * @return
	 */
	public static final String ConvertLineBreaks(String html) {
		if(html.contains("\r\n")) {
			html = html.replaceAll("\r\n", "<br/>") ;
		}else if(html.contains("\r")) {
			html = html.replaceAll("\r", "<br/>") ;
		}
		/*else if(html.contains("\n")) {
			html = html.replaceAll("\n", "<br/>") ;
		}*/
		
		return html ;
	}
}
