package my.trade.vo;

public class PageVO
{
  private int pageSize;
  private int currentPage;
  private int totalPosts;
  private int totalPages;
  private int startPage;
  private int endPage;
  private int startIndex;
  private String searchTerm;
  private final int pageBlockSize = 2;
  
  public int getStartIndex()
  {
    return this.startIndex;
  }
  
  public void setStartIndex(int startIndex)
  {
    this.startIndex = startIndex;
  }
  
  public String getSearchTerm()
  {
    return this.searchTerm;
  }
  
  public void setSearchTerm(String searchTerm)
  {
    this.searchTerm = searchTerm;
  }
  
  public int getPageBlockSize()
  {
    return 2;
  }
  
  public int getPageSize()
  {
    return this.pageSize;
  }
  
  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }
  
  public int getCurrentPage()
  {
    return this.currentPage;
  }
  
  public void setCurrentPage(int currentPage)
  {
    this.currentPage = currentPage;
  }
  
  public int getTotalPosts()
  {
    return this.totalPosts;
  }
  
  public void setTotalPosts(int totalPosts)
  {
    this.totalPosts = totalPosts;
  }
  
  public int getTotalPages()
  {
    return this.totalPages;
  }
  
  public void setTotalPages(int totalPages)
  {
    this.totalPages = totalPages;
  }
  
  public int getStartPage()
  {
    return this.startPage;
  }
  
  public void setStartPage(int startPage)
  {
    this.startPage = startPage;
  }
  
  public int getEndPage()
  {
    return this.endPage;
  }
  
  public void setEndPage(int endPage)
  {
    this.endPage = endPage;
  }
  
  public String toString()
  {
    return 
    
      "PageVO [pageSize=" + this.pageSize + ", currentPage=" + this.currentPage + ", totalPosts=" + this.totalPosts + ", totalPages=" + this.totalPages + ", startPage=" + this.startPage + ", endPage=" + this.endPage + ", startIndex=" + this.startIndex + ", searchTerm=" + this.searchTerm + "]";
  }
  
  public void calculatePages()
  {
    this.totalPages = ((int)Math.ceil(this.totalPosts / this.pageSize));
    int blockStart = (this.currentPage - 1) / 2 * 2 + 1;
    int blockEnd = blockStart + 2 - 1;
    this.startPage = blockStart;
    this.endPage = Math.min(blockEnd, this.totalPages);
    this.startIndex = ((this.currentPage - 1) * this.pageSize);
  }
}
