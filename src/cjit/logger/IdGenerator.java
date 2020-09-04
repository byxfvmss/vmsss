package cjit.logger;

import java.util.HashMap;

import javax.sql.DataSource;

public class IdGenerator
{
  private static HashMap kengens = new HashMap(10);
  private static final int POOL_SIZE = 20;
  private IdGeneratorKey keyinfo;

  private IdGenerator(String keyName, DataSource dataSource)
  {
    this.keyinfo = new IdGeneratorKey(20, keyName, dataSource);
  }

  public static synchronized IdGenerator getInstance(String keyName, DataSource dataSource)
  {
    IdGenerator keygen;
    if (kengens.containsKey(keyName))
      keygen = (IdGenerator)kengens.get(keyName);
    else {
      keygen = new IdGenerator(keyName, dataSource);
      kengens.put(keyName, keygen);
    }
    return keygen;
  }

  public synchronized long getNextKey()
  {
    return this.keyinfo.getNextKey();
  }
}