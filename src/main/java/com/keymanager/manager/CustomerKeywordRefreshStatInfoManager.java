package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keymanager.db.DBUtil;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.ClientStatusVO;
import com.keymanager.value.ConfigVO;
import com.keymanager.value.CustomerKeywordRefreshStatInfoVO;

public class CustomerKeywordRefreshStatInfoManager {
	private int recordCount;
	private int currPage = 0;
	private int pageCount = 0;

	public int getRecordCount() {
		return recordCount;
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	private List<CustomerKeywordRefreshStatInfoVO> getCustomerKeywordStatInfos(Connection conn, String type, String groupName, String customerName)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = " SELECT                                                                                            "
					+ "   ck.fOptimizeGroupName,                                                                          "
					+ "   COUNT(1)                 total_keyword_count,                                                   "
					+ "   SUM(IF(ck.fOptimizePlanCount > ck.fOptimizedCount, 1, 0))    need_optimize_keyword_count,       "
					+ "   SUM(IF(ck.fInvalidRefreshCount > (? - 1), 1, 0)) invalid_keyword_count,                               "
					+ "   SUM(ck.fOptimizePlanCount)    total_optimize_count,                                             "
					+ "   SUM(ck.fOptimizedCount)    total_optimized_count,                                               "
					+ "   SUM(ck.fOptimizePlanCount - ck.fOptimizedCount)    need_optimize_count,                         "
					+ "   SUM(ck.fQueryCount)    query_count                                                              "
					+ " FROM t_customer_keyword ck, t_customer c                                                          "
					+ " WHERE ck.fType = ?                                                                                "
					+ " AND ck.fCustomerUuid = c.fUuid                                                                    ";
			if (StringUtil.isNotNullNorEmpty(groupName)) {
				sql = sql + " AND ck.fOptimizeGroupName like '%" + groupName.trim() + "%' ";
			}
			if (StringUtil.isNotNullNorEmpty(customerName)) {
				sql = sql + " AND c.fContactPerson like '%" + customerName.trim() + "%' ";
			}
			sql = sql + " GROUP BY ck.fOptimizeGroupName                                                                    "
					+ " ORDER BY ck.fOptimizeGroupName                                                                    ";
			ps = conn.prepareStatement(sql, 1003, 1007);
			
			ConfigManager configManager = new ConfigManager();
			ConfigVO configVO = configManager.getConfig(conn, ConfigManager.CONFIG_KEY_MAX_INVALID_COUNT, type);
			
			ps.setString(1, configVO.getValue());
			ps.setString(2, type);
			rs = ps.executeQuery();
			List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOs = new ArrayList<CustomerKeywordRefreshStatInfoVO>();
			while (rs.next()) {
				CustomerKeywordRefreshStatInfoVO refreshStatInfoVO = new CustomerKeywordRefreshStatInfoVO();
				refreshStatInfoVO.setGroup(rs.getString("fOptimizeGroupName"));
				refreshStatInfoVO.setTotalKeywordCount(rs.getInt("total_keyword_count"));
				refreshStatInfoVO.setNeedOptimizeKeywordCount(rs.getInt("need_optimize_keyword_count"));
				refreshStatInfoVO.setInvalidKeywordCount(rs.getInt("invalid_keyword_count"));
				refreshStatInfoVO.setTotalOptimizeCount(rs.getInt("total_optimize_count"));
				refreshStatInfoVO.setTotalOptimizedCount(rs.getInt("total_optimized_count"));
				refreshStatInfoVO.setNeedOptimizeCount(rs.getInt("need_optimize_count"));
				refreshStatInfoVO.setQueryCount(rs.getInt("query_count"));
				refreshStatInfoVO.setMaxInvalidCount(Integer.parseInt(configVO.getValue()));
				customerKeywordRefreshStatInfoVOs.add(refreshStatInfoVO);
			}
			return customerKeywordRefreshStatInfoVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("generateCustomerKeywordStatInfo");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}
  
	public List<CustomerKeywordRefreshStatInfoVO> generateCustomerKeywordStatInfo(String datasourceName, String type, String groupName,
			String customerName) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);

			List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOs = getCustomerKeywordStatInfos(conn, type, groupName, customerName);
			Map<String, CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOMap = new HashMap<String, CustomerKeywordRefreshStatInfoVO>();
			for (CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
				customerKeywordRefreshStatInfoVOMap.put(customerKeywordRefreshStatInfoVO.getGroup(), customerKeywordRefreshStatInfoVO);
			}
			ClientStatusManager clientStatusManager = new ClientStatusManager();
			List<ClientStatusVO> clientStatusVOs = clientStatusManager.getClientStatusVOs(conn, type, groupName, customerName);
			for (ClientStatusVO clientStatusVO : clientStatusVOs) {
				CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO = customerKeywordRefreshStatInfoVOMap.get(clientStatusVO.getGroup());
				if (customerKeywordRefreshStatInfoVO != null) {
					customerKeywordRefreshStatInfoVO.setTotalMachineCount(customerKeywordRefreshStatInfoVO.getTotalMachineCount() + 1);
					if (clientStatusVO.isRed()) {
						customerKeywordRefreshStatInfoVO.setUnworkMachineCount(customerKeywordRefreshStatInfoVO.getUnworkMachineCount() + 1);
					}
				}
			}
			CustomerKeywordRefreshStatInfoVO total = new CustomerKeywordRefreshStatInfoVO();
			total.setGroup("总计");
			for (CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
				total.setInvalidKeywordCount(total.getInvalidKeywordCount() + customerKeywordRefreshStatInfoVO.getInvalidKeywordCount());
				total.setNeedOptimizeCount(total.getNeedOptimizeCount() + customerKeywordRefreshStatInfoVO.getNeedOptimizeCount());
				total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + customerKeywordRefreshStatInfoVO.getNeedOptimizeKeywordCount());
				total.setQueryCount(total.getQueryCount() + customerKeywordRefreshStatInfoVO.getQueryCount());
				total.setTotalKeywordCount(total.getTotalKeywordCount() + customerKeywordRefreshStatInfoVO.getTotalKeywordCount());
				total.setTotalMachineCount(total.getTotalMachineCount() + customerKeywordRefreshStatInfoVO.getTotalMachineCount());
				total.setTotalOptimizeCount(total.getTotalOptimizeCount() + customerKeywordRefreshStatInfoVO.getTotalOptimizeCount());
				total.setTotalOptimizedCount(total.getTotalOptimizedCount() + customerKeywordRefreshStatInfoVO.getTotalOptimizedCount());
				total.setUnworkMachineCount(total.getUnworkMachineCount() + customerKeywordRefreshStatInfoVO.getUnworkMachineCount());
				total.setMaxInvalidCount(customerKeywordRefreshStatInfoVO.getMaxInvalidCount());
			}
			customerKeywordRefreshStatInfoVOs.add(0, total);

			return customerKeywordRefreshStatInfoVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("generateCustomerKeywordStatInfo");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
}