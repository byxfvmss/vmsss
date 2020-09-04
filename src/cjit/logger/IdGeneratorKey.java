package cjit.logger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class IdGeneratorKey extends JdbcDaoSupport {
	public final static String TABLE_NAME = "U_BASE_FORM_NO";
	public final static String FORM_TYPE = "FORM_TYPE";
	public final static String FORM_ID = "CURRENT_ID";
	public final static String BLANK = " ";

	private long keyMax;
	private long keyMin;
	private long nextKey;
	private int poolSize;
	private String keyName;

	/**
	 * ���캯��
	 */
	public IdGeneratorKey() {
	};

	/**
	 * ���캯��
	 */
	public IdGeneratorKey(int poolSize, String keyName, DataSource dataSource) {
		this.poolSize = poolSize;
		this.keyName = keyName;
		this.setDataSource(dataSource);
		retrieveFromDB();
	}

	/**
	 * ȡֵ�������ṩ������ֵ
	 */
	public long getKeyMax() {
		return keyMax;
	}

	/**
	 * ȡֵ�������ṩ�����Сֵ
	 */
	public long getKeyMin() {
		return keyMin;
	}

	/**
	 * ȡֵ�������ṩ��ĵ�ǰֵ
	 */
	public long getNextKey() {
		if (nextKey > keyMax) {
			retrieveFromDB();
		}
		return nextKey++;
	}

	/**
	 * �ڲ�����������ݿ���ȡ��ĵ�ǰֵ
	 */
	private void retrieveFromDB() {

		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
				this.getDataSource());
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager
				.getTransaction(definition);
		Object[] params = new Object[] { keyName };
		try {
			String update = "UPDATE" + BLANK + TABLE_NAME + BLANK + "SET"
					+ BLANK + FORM_ID + "=" + BLANK + FORM_ID +"+"+poolSize
					+ BLANK + "WHERE" + BLANK + FORM_TYPE + "=?";
			this.getJdbcTemplate().update(update, params);
			String select = "SELECT" + BLANK + FORM_ID + BLANK + "FROM" + BLANK
					+ TABLE_NAME + BLANK + "WHERE" + BLANK + FORM_TYPE + BLANK
					+ "=?";
			long keyFromDB = this.getJdbcTemplate()
					.queryForLong(select, params);
			transactionManager.commit(status);
			keyMax = keyFromDB;
			keyMin = keyFromDB - poolSize + 1;
			nextKey = keyMin;
		} catch (Exception e) {
			transactionManager.rollback(status);
		}
		;
	}
}