package com.fzs.commons.filelisttomysql;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.druid.pool.DruidDataSource;
import com.fzs.commons.filelisttomysql.bean.RegionBean;

/**
 * jdbc.driver=oracle.jdbc.OracleDriver
jdbc.url=
#jdbc.username=samp_dev
#jdbc.password=lxkj
jdbc.username=samp
jdbc.password=lxkj_info
 * @author rlyu
 *
 */
public class ToRegionMain {

	
	
	private DruidDataSource dataSource;
	private QueryRunner run ;
	// jdbc config
	private String url = "jdbc:oracle:thin:@(DESCRIPTION =(FAILOVER=ON)(LOAD_BALANCE=ON)(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = fzs-rac-scan.fzsdomain)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = fzsdb)))";
	private String user="samp";
	private String password = "lxkj_info";
	
	
	
	
	public ToRegionMain() {
		dataSource = new DruidDataSource();
		dataSource.setUrl(this.url);
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		
		run = new QueryRunner(dataSource);
		
		init();
	}
	
	private void init() {
		// 清理数据
		try {
			
			run.update("truncate table t_region ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	String insert_sql = "insert into t_region (id,parent_id,region_level,  area_code,zip_code,city_code,   name,short_name,merger_name,pinyin,  lng,lat) "
			+ " values (?,?,?, ?,?,?, ?,?,?,?, ?,? ) ";
	private void saveBean(RegionBean bean) throws Exception {
		
		
		run.update(insert_sql, new Object[]{
				bean.getId(),bean.getParent_id(),bean.getRegion_level(),
				bean.getArea_code(),bean.getZip_code(),bean.getCity_code(),
				bean.getName(),bean.getShort_name(),bean.getMerger_name(),bean.getPinyin(),
				bean.getLng(),bean.getLat()
		});
	}
	
	
	public static void main(String[] args) throws Exception {
		
		File file = new File("cnarea20160320-2.sql");
		List<String> rows = FileUtils.readLines(file);
		
		ToRegionMain main = new ToRegionMain();
		
		
		RegionBean bean = null;
		int i = 0;
		for (String row : rows) {
			if(i < 10) {
				
				bean = new RegionBean(row);
				
				System.out.println(ToStringBuilder.reflectionToString(bean,ToStringStyle.MULTI_LINE_STYLE));
			//	i++;
				if(bean.getRegion_level()<3) {
					main.saveBean(bean);
				}
				
			}
			
		
		}
		
		
	}
}
