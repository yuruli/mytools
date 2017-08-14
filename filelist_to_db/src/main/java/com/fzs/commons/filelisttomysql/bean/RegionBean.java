package com.fzs.commons.filelisttomysql.bean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * CREATE TABLE `cnarea` (
  `id` mediumint(7) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` mediumint(7) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `region_level` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '层级',
  `area_code` bigint(12) unsigned NOT NULL DEFAULT '0' COMMENT '行政代码',
  `zip_code` mediumint(6) unsigned zerofill NOT NULL DEFAULT '000000' COMMENT '邮政编码',
  `city_code` char(6) NOT NULL DEFAULT '' COMMENT '区号',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `short_name` varchar(50) NOT NULL DEFAULT '' COMMENT '简称',
  `merger_name` varchar(50) NOT NULL DEFAULT '' COMMENT '组合名',
  `pinyin` varchar(30) NOT NULL DEFAULT '' COMMENT '拼音',
  `lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '维度',
  PRIMARY KEY (`id`),
  KEY `idx_lev` (`level`,`parent_id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='中国行政地区表';

 * @author rlyu
 *
 */
public class RegionBean {
	
	
	
	//1,0,0,"","","110000000000","北京市","北京","北京","BeiJing","116.40752600","39.90403000"
	//3,2,2,"100000","010","110101000000","东城区","东城","北京,东城","DongCheng","116.41635700","39.92835300"
	public RegionBean(String row) {
		String[] ss = row.split("','");
//		System.out.println("ss:"+ToStringBuilder.reflectionToString(ss,ToStringStyle.MULTI_LINE_STYLE));
		String[] ss_1 = StringUtils.split(ss[0],",");
//		System.out.println("ss_1:"+ToStringBuilder.reflectionToString(ss_1));
		this.id = NumberUtils.toLong(ss_1[0]);
		this.parent_id = NumberUtils.toLong(ss_1[1]);
		this.region_level = NumberUtils.toInt(ss_1[2]);
		
		if(!"'".equals(ss_1[3])) {
			this.zip_code = NumberUtils.toInt(StringUtils.replace(ss_1[3], "'", StringUtils.EMPTY)) ;
		}
		
		if(StringUtils.isNotBlank(ss[1])) {
			this.city_code = ss[1];		
		}
		
		if(StringUtils.isNotBlank(ss[2])) {
			this.area_code = NumberUtils.toLong(ss[2]);
		}
		
		this.name = ss[3];
		this.short_name = ss[4];
		this.merger_name = ss[5];
		this.pinyin = ss[6];
		
		this.lng = NumberUtils.toDouble(ss[7]);
		this.lat = NumberUtils.toDouble(StringUtils.replace(ss[8], "'", StringUtils.EMPTY));
		
	}
	
	

	
	private long id;
	private long parent_id;
	private int region_level;
	
	private long area_code = 0; //行政代码
	private int zip_code = 0; //邮政编码
	private String city_code = "-"; //区号
	
	private String name;
	private String short_name;
	private String merger_name;
	private String pinyin;
	
	
	private double lng;
	private double lat;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	public int getRegion_level() {
		return region_level;
	}
	public void setRegion_level(int region_level) {
		this.region_level = region_level;
	}
	public long getArea_code() {
		return area_code;
	}
	public void setArea_code(long area_code) {
		this.area_code = area_code;
	}
	public int getZip_code() {
		return zip_code;
	}
	public void setZip_code(int zip_code) {
		this.zip_code = zip_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String getMerger_name() {
		return merger_name;
	}
	public void setMerger_name(String merger_name) {
		this.merger_name = merger_name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
	
	
	
}
