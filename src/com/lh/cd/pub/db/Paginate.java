package com.lh.cd.pub.db;

public class Paginate {
	public static int DEFAULT_PAGE_SIZE = 5; // 默认每页显示条数

	private int start;// 分页查询开始记录位置

	private int end;// 分页查看下结束位置

	private int pageSize;// 每页显示记录数

	private int totalCount;// 查询结果总记录数

	private int curPage;// 当前页码

	private int totalPage;// 总共页数

	public Paginate() {
	}

	public Paginate(int curPage, int pageSize) {
		this.curPage = (curPage < 1) ? 1 : curPage;
		this.pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		this.start = (this.curPage - 1) * this.pageSize;
		this.end = this.start + this.pageSize;
		// this.curPage = (int) Math.floor((this.start * 1.0d) / this.pageSize) + 1;
	}

	public Paginate(int curPage) {
		this(curPage, DEFAULT_PAGE_SIZE);
		this.pageSize = DEFAULT_PAGE_SIZE;
	}

	public Paginate(int curPage, int pageSize, int totalCount) {
		this(curPage, pageSize);
		this.totalCount = totalCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
		this.curPage = (int) Math.floor((this.start * 1.0d) / this.pageSize) + 1;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		this.curPage = (int) Math.floor((this.start * 1.0d) / this.pageSize) + 1;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = (int) Math.floor((this.totalCount * 1.0d) / this.pageSize);
		if (this.totalCount % this.pageSize != 0) {
			this.totalPage++;
		}
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getTotalPage() {
		if (totalPage == 0) {
			return 1;
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}