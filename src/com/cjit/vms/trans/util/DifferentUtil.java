package com.cjit.vms.trans.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.model.BillItemInfo;

public class DifferentUtil {

	private static int n;
	private static int t;

	public static Map taxlist(List list,BigDecimal diferent) {

		BigDecimal sumDfferent = new BigDecimal(0);
		BillItemInfo comp=new BillItemInfo();
		  //调用排序方法
		 Collections.sort(list,comp);
		for (int i = 0; i < list.size(); i++) {
			BillItemInfo o = (BillItemInfo) list.get(i);
			sumDfferent = sumDfferent.add(o.getDifferent());
		}
		n = getCeil((sumDfferent.divide(diferent).abs()), 0);
		return getDifferentList(list, n, diferent);
	}

	public static Map getDifferentList(List list, int n, BigDecimal diferent) {
		Map map = new HashMap();
		t = 0;
		BigDecimal sumDfferent;
		for (int j = 0; j < n; j++) {
			sumDfferent = new BigDecimal(0);
			List list1 = new ArrayList();
			for (int i = j; i * n < list.size(); i += 2 * n) {
				BillItemInfo o = (BillItemInfo) list.get(i);
				sumDfferent = sumDfferent.add(o.getDifferent());
				list1.add(o);
			}
			for (int i = 2 * n - (j + 1); i < list.size(); i += 2 * n) {
				BillItemInfo o = (BillItemInfo) list.get(i);
				sumDfferent = sumDfferent.add(o.getDifferent());
				list1.add(o);
			}
			if (sumDfferent.divide(diferent).abs().compareTo(new BigDecimal(1)) == -1) {
				t++;
				map.put(Integer.toString(t), list1);
			} else {

				getDifferentList(list, n + 1, diferent);
			}

		}

		if ((t + 1) > n) {

			getDifferentList(list, n + 1, diferent);
		} else {

			return map;
		}
		return map;
	}

	public static int getCeil(BigDecimal d,int n){
		//BigDecimal b = new BigDecimal(String.valueOf(d));
		d = d.divide(BigDecimal.ONE,n,BigDecimal.ROUND_CEILING);
		return d.intValue();
		} 
}