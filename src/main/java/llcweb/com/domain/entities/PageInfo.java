package llcweb.com.domain.entities;

import java.util.Date;
import java.util.List;

/**
 * @Author ricardo
 * @Description 首页成果信息封装
 * @Date 2018/10/7
 **/

public class PageInfo {

	private Integer id;
	private List<ProductInfo> tPage;
	private int totalElements;

	private int totalPages;

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public PageInfo(Integer id, List<ProductInfo> tPage, int totalElements) {
		this.id = id;
		this.tPage = tPage;
		this.totalElements = totalElements;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ProductInfo> gettPage() {
		return tPage;
	}

	public void settPage(List<ProductInfo> tPage) {
		this.tPage = tPage;
	}

	public PageInfo() {
	}

}