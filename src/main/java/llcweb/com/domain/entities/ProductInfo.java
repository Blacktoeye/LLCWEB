package llcweb.com.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author ricardo
 * @Description 首页成果信息封装
 * @Date 2018/10/7
 **/

public class ProductInfo {

	private Integer id;
	private String title;
	private Date date;
	private String introduction;
	private String authorList;
	private String state;
	private int totalPages;

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public ProductInfo(Integer id, String title, Date date, String introduction, String authorList, String state) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.introduction = introduction;
		this.authorList = authorList;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}



	public String getAuthorList() {
		return authorList;
	}

	public void setAuthorList(String authorList) {
		this.authorList = authorList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ProductInfo() {
	}

}