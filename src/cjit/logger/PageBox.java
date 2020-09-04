package cjit.logger;

import java.util.ArrayList;
import java.util.List;

public class PageBox
{
  private List pageList;
  private PageObject pageObject;

  public List getPageList()
  {
    if (this.pageList == null) new ArrayList(); return this.pageList;
  }

  public void setPageList(List pageList)
  {
    this.pageList = pageList;
  }

  public PageObject getPageObject()
  {
    return this.pageObject;
  }

  public void setPageObject(PageObject pageObject)
  {
    this.pageObject = pageObject;
  }
}