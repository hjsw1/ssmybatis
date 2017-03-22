package com.crazy.bean;

/**
 * 锟斤拷页Model锟斤拷
 * @author 
 *
 */
public class PageBean {

	private int page; // 当前页
	private int pageSize; // 一页显示的个数
	@SuppressWarnings("unused")
	private int start;	//浠庣鍑犳潯璁板綍寮?濮嬫煡
	private int total;	//数据总条数
	private int count;	//总页数
	
	
	public PageBean(int page, int pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return (page-1)*pageSize;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCount() {
		count=total%pageSize==0 ? total/pageSize : total/pageSize+1;
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
