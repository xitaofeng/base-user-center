package com.shsnc.base.authorization.util.poi;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.Map;

public class LSFreemarker {
	
	/**
	 * 根据文件夹加载配置类
	 * @param file
	 * @return
	 */
	public static Template getTemplate(File file){
		Template template=null;
		try {
			Configuration config=new Configuration(Configuration.VERSION_2_3_23); //模板引擎
			config.setDirectoryForTemplateLoading(file.getParentFile()); //加载模板
			config.setDefaultEncoding("UTF-8"); //文件编码
			config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);	//异常处理方式	
			template=config.getTemplate(file.getName());//根据文件名获取模板
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;
	
	}
	


	public static File generate(File templateFile,File targetFile,Map<String, Object> record){
		Writer writer=null;
		try {
			Template template=getTemplate(templateFile);//根据文件名获取模板
			//文件输出流
			writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile),"UTF-8"));
			template.process(record, writer);//数据渲染在模板，并输出文件
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return targetFile;
		
	}
	

	
	public static void main(String[] args) {

		//Resource resource=new FileSystemResource(getRealPath("/Sce/template/template.ftl"));
		try {
	//		Resource resource=new FileSystemResource(getRealPath("/Sce/template/template.ftl"));
		//	File file=resource.getFile(); //模板
//			if(file.isFile()){
//				System.out.println(file.getName());
//			}
			//输出文件
//			File output=LSFile.getOutputFile(getRealPath("/Sce/temp/")+LSString.getUuid()+".docx");
//			Map<String,Object> map=new HashMap<String, Object>();
//			map.put("name", "lusen");
//			map.put("age", 18);
//			map.put("sex", "男");
//			generate(map,resource.getFile(),output);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
