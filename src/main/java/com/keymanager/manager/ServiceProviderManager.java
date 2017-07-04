package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.keymanager.db.DBUtil;
import com.keymanager.value.ServiceProviderVO;

public class ServiceProviderManager {

	public ArrayList getAllActiveServiceProvider(String datasourceName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList<ServiceProviderVO> serviceProviders = new ArrayList<ServiceProviderVO>();
		try {
			conn = DBUtil.getConnection(datasourceName);
			sql = " select * FROM t_service_provider where fStatus = 1";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				ServiceProviderVO serviceProviderVO = getServiceProviderVO(datasourceName, rs);
				serviceProviders.add(serviceProviderVO);
			}
			return serviceProviders;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	private ServiceProviderVO getServiceProviderVO(String datasourceName, ResultSet rs) throws SQLException {
		ServiceProviderVO serviceProvider = new ServiceProviderVO();
		serviceProvider.setUuid(rs.getInt("fUuid"));
		serviceProvider.setServiceProviderName(rs.getString("fServiceProviderName"));
		serviceProvider.setStatus(rs.getInt("fStatus"));
		return serviceProvider;
	}
}
