package com.fzs.commons.filelisttomysql.bean;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateUtils;

import com.fzs.commons.filelisttomysql.utils.FileDataUtils;

public class FileBean {

	public FileBean(String time, long length, String name, String path, boolean dir,String fix_path) {

		this.name = name;
		this.length = length;
		this.path = path;
		this.isHidden = '0';
		this.f_oper_user_id = "yuruli";
		this.file = FileDataUtils.getFile(name, path,fix_path);
		try {
			this.create_time = DateUtils.parseDate(time, new String[] { "yyyy-MM-dd HH:mm:ss" });
		} catch (ParseException e) {
			e.printStackTrace();
			this.create_time = new Date();
		}
		this.lastMdf = create_time.getTime();
		if (dir) { // 文件夹
			this.cate = "uk";
			this.type = "uk";
			this.length = 4096;
			this.isDir = '1';
			this.typeknow = '0';
		} else {
			this.type = FileDataUtils.getType(name);
			this.cate = this.type;
			this.isDir = '0';
			this.typeknow = FileDataUtils.getTypeknow(this.type);
		}
	}

	

	private String name;

	private long length;

	private String type;

	private String cate;

	private String path;

	private long lastMdf;

	private char isDir;

	private char isHidden;

	private char typeknow;

	private Date create_time;

	private String f_oper_user_id = "yuruli";

	private String file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getLastMdf() {
		return lastMdf;
	}

	public void setLastMdf(long lastMdf) {
		this.lastMdf = lastMdf;
	}

	public char getIsDir() {
		return isDir;
	}

	public void setIsDir(char isDir) {
		this.isDir = isDir;
	}

	public char getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(char isHidden) {
		this.isHidden = isHidden;
	}

	public char getTypeknow() {
		return typeknow;
	}

	public void setTypeknow(char typeknow) {
		this.typeknow = typeknow;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getF_oper_user_id() {
		return f_oper_user_id;
	}

	public void setF_oper_user_id(String f_oper_user_id) {
		this.f_oper_user_id = f_oper_user_id;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
