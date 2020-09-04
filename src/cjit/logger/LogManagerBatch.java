package cjit.logger;

import java.util.List;

public interface LogManagerBatch extends LogManagerDAO{
	public void insertBatch(final List logList);
}
