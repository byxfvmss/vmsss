package cjit.logger;

import java.util.Map;

public abstract interface LogManagerDAO
{
  public static final int RESULT_EXCEED_NUM = 10000;
  public static final int RESULT_EXCEED_CODE = 1;
  public static final int RESULT_NORMAL_CODE = 0;
  public static final String RESULT_KEY = "RESULT_KEY";
  public static final String RESULT_VALUE = "RESULT_VALUE";

  public abstract long insert(LogDO paramLogDO);

  public abstract int deleteByPrimary(long paramLong);

  public abstract int deleteByPrimarys(String[] paramArrayOfString);

  public abstract LogDO getLogByPrimary(long paramLong);

  public abstract PageBox selectByFormWithPaging(LogDO paramLogDO, int paramInt1, int paramInt2);

  public abstract Map selectAllByParams(LogDO paramLogDO);
}