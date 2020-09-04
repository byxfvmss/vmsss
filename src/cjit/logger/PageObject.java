package cjit.logger;

public class PageObject
{
  private int pageIndex;
  private int itemAmount;
  private int pageSize;
  private int startNum;
  private int endNum;

  public PageObject()
  {
    this.pageSize = 1;
  }

  public int getPageAmount()
  {
    int remainder = this.itemAmount % this.pageSize;
    if (remainder == 0)
      return (this.itemAmount / this.pageSize);

    return (this.itemAmount / this.pageSize + 1);
  }

  public int getPageIndex()
  {
    return this.pageIndex;
  }

  public void setPageIndex(int pageIndex)
  {
    this.pageIndex = pageIndex;
  }

  public int getItemAmount()
  {
    return this.itemAmount;
  }

  public void setItemAmount(int itemAmount)
  {
    this.itemAmount = itemAmount;
  }

  public int getPageSize()
  {
    return this.pageSize;
  }

  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }

  public int getStartNum()
  {
    return this.startNum;
  }

  public void setStartNum(int startNum)
  {
    this.startNum = startNum;
  }

  public int getEndNum()
  {
    return this.endNum;
  }

  public boolean isFirstPage()
  {
    return (this.pageIndex == 1);
  }

  public boolean isLastPage()
  {
    return (getPageAmount() > this.pageIndex);
  }

  public int getBeginIndex()
  {
    return ((this.pageIndex - 1) * this.pageSize + 1);
  }
}