package com.fzs.commons.filelisttomysql;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.fzs.commons.filelisttomysql.bean.FileBean;
import com.fzs.commons.filelisttomysql.utils.FileDataUtils;

public class Main {

	private static Map<String, String> map = new HashMap<String, String>();
	
	private static String fix_path = "/u02/samp/files";
	
	private DruidDataSource dataSource;
	private QueryRunner run ;
	// jdbc config
	private String url = "jdbc:mysql://10.20.1.33:3306/xfolder";
	private String user="root";
	private String password = "admin123321";
	
	public Main() {
		dataSource = new DruidDataSource();
		dataSource.setUrl(this.url);
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		
		run = new QueryRunner(dataSource);
		
		init();
	}
	
	private void init() {
		// 清理数据
		try {
			run.update(truncate_table_t_xfile_file);
			run.update(truncate_table_t_xfile_physical_file);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	

	// sql
	String query_name_path = "select count(1) as num from ";
	
	String truncate_table_t_xfile_file = "truncate table t_xfile_file";
	String truncate_table_t_xfile_physical_file = "truncate table t_xfile_physical_file";
	
	
	String insert_t_xfile_physical_file = "insert into t_xfile_physical_file (file,type,create_time) values (?,?,?)";
	
	// 读取文件
	private  void load(List<String> rows) {
		
		

		for (String row : rows) { // 一行的处理
			List<FileBean> beans = createFiles(row);
			
			// save to db 
			savetodb(beans);
			System.out.println("-----------saved-----------"+row+"-------------------");
		}

	}
	
	
	

	private List<FileBean> createFiles(String row) {
		List<FileBean> list = new ArrayList<FileBean>();
		// 每一行数据
		String[] rowdata = StringUtils.split(row,"|");
		if(rowdata == null || rowdata.length !=3) {
			return list;
		}
		
		String time = rowdata[0];
		long length = NumberUtils.toLong(rowdata[1],4096);
		String srcpath = rowdata[2];
		
		//开始一层层滴建立文件
		String[] paths = StringUtils.split(srcpath.replace(fix_path, StringUtils.EMPTY),"/");
		String currentpath = "";
		String name = "";
		
		System.out.println("----------------------"+srcpath+"-------------------");
		for(int i=0;i<paths.length;i++) {
			currentpath = getCurrentPath(paths, i-1);
			name = paths[i];
			String key =currentpath+"/"+name;
			if(key.startsWith("//")) {
				key = key.replace("//", "/");
			}
			
			
			if(map.containsKey(key)) {// 已经存在了
				continue;
			}else { //不存在
				
				System.out.println("name="+name+",path="+currentpath+",index="+i+",key="+key);
				FileBean bean = null;
				if(srcpath.endsWith(key)) { //文件
					bean = new FileBean(time, length, name, currentpath, false,fix_path);
				}else {//文件夹
					bean = new FileBean(time, length, name, currentpath, true,fix_path);
				}
				
				map.put(key, key);
				
				if(bean != null) {
					list.add(bean);
				}
				
			}
		}
		
		
		
		
		return list;
	}
	
	private String getCurrentPath(String[] paths,int index) {
		if(index<0) {
			index =0;
			return "/";// root
		}
		if(index > paths.length) {
			index = paths.length;
		}
		
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<=index;j++) {
			sb.append("/").append(paths[j]);
		}
		
		// 
		
		return sb.toString();
	}
	
	

	private  void savetodb(List<FileBean> files) {

		for (FileBean fileBean : files) {
			System.out.println("======================"+fileBean+"==========================");
			// 01 保存文件
			saveFile(fileBean);
			if (fileBean.getIsDir() == '0') { // 是文件
				
				// 02 保存物理文件
				savePyFile(fileBean);
			}
		}
	}
	
	String insert_t_xfile_file = "insert into t_xfile_file (PARENT_ID,NAME,length,type,cate,	  path,lastMdf,isDir,isHidden,typeknow,   create_time,f_oper_user_id,status,file) "
			+ "values (0,?,?,?,?,  ?,?,'%s','%s','%s',  ?,?,'1',?)";
	private void saveFile(FileBean bean) {
		try {
			String sql = String.format(insert_t_xfile_file, new Object[]{bean.getIsDir(),bean.getIsHidden(),bean.getTypeknow()});
			run.update(sql,new Object[]{bean.getName(),bean.getLength(),bean.getType(),bean.getCate(),
					bean.getPath(),bean.getLastMdf(),
					bean.getCreate_time(),bean.getF_oper_user_id(),bean.getFile()});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void savePyFile(FileBean bean) {
		try {
			run.update(insert_t_xfile_physical_file,new Object[]{bean.getFile(),bean.getType(),bean.getCreate_time()});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) throws Exception {
		// 读取文件
		File file = new File("file.txt");

		List<String> list = FileUtils.readLines(file);

		Main main = new Main();
		
		// 一行滴读取文件
		main.load(list);

	}

}
