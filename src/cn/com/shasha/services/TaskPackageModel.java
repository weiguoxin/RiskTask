package cn.com.shasha.services;

import java.util.Date;
/**
 * ÈÎÎñ°ü
 * @author _wgx
 *
 */
public class TaskPackageModel {


	private int id ;
	private String name;
	private String swjgdm;
	private String  swjgmc;
	private String xfrdm;
	private String xfrmc;
	private Date xfdate;
	private int all_count;
	private int get_count;
	private String beizhu;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSwjgdm() {
		return swjgdm;
	}
	public void setSwjgdm(String swjgdm) {
		this.swjgdm = swjgdm;
	}
	public String getSwjgmc() {
		return swjgmc;
	}
	public void setSwjgmc(String swjgmc) {
		this.swjgmc = swjgmc;
	}
	public String getXfrdm() {
		return xfrdm;
	}
	public void setXfrdm(String xfrdm) {
		this.xfrdm = xfrdm;
	}
	public String getXfrmc() {
		return xfrmc;
	}
	public void setXfrmc(String xfrmc) {
		this.xfrmc = xfrmc;
	}
	public Date getXfdate() {
		return xfdate;
	}
	public void setXfdate(Date xfdate) {
		this.xfdate = xfdate;
	}
	public int getAll_count() {
		return all_count;
	}
	public void setAll_count(int all_count) {
		this.all_count = all_count;
	}
	public int getGet_count() {
		return get_count;
	}
	public void setGet_count(int get_count) {
		this.get_count = get_count;
	}
	public String getBeizhu() {
		return beizhu;
	}
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

}
