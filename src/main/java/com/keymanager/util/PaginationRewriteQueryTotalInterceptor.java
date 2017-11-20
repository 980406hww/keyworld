package com.keymanager.util;

import com.baomidou.mybatisplus.MybatisDefaultParameterHandler;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author LiuJie
 * @create 2017-11-20 18:15
 * @desc
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class PaginationRewriteQueryTotalInterceptor extends PaginationInterceptor{
    private static final Log logger = LogFactory.getLog(PaginationRewriteQueryTotalInterceptor.class);

    protected void queryTotal(boolean overflowCurrent, String sql, MappedStatement mappedStatement, BoundSql boundSql, Pagination page, Connection connection) {
        try {
            String sqlRemoveTOTAL = sql.replace(") TOTAL","");
            String alias = sqlRemoveTOTAL.substring(sqlRemoveTOTAL.indexOf("FROM"),sqlRemoveTOTAL.lastIndexOf("FROM"));
            String sqlTmp = sqlRemoveTOTAL.replace(alias,"");
            PreparedStatement statement = connection.prepareStatement(sqlTmp);
            Throwable var8 = null;

            try {
                DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
                parameterHandler.setParameters(statement);
                int total = 0;
                ResultSet resultSet = statement.executeQuery();
                Throwable var12 = null;

                try {
                    if(resultSet.next()) {
                        total = resultSet.getInt(1);
                    }
                } catch (Throwable var37) {
                    var12 = var37;
                    throw var37;
                } finally {
                    if(resultSet != null) {
                        if(var12 != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var36) {
                                var12.addSuppressed(var36);
                            }
                        } else {
                            resultSet.close();
                        }
                    }

                }

                page.setTotal(total);
                int pages = page.getPages();
                if(overflowCurrent && page.getCurrent() > pages) {
                    page.setCurrent(1);
                }
            } catch (Throwable var39) {
                var8 = var39;
                throw var39;
            } finally {
                if(statement != null) {
                    if(var8 != null) {
                        try {
                            statement.close();
                        } catch (Throwable var35) {
                            var8.addSuppressed(var35);
                        }
                    } else {
                        statement.close();
                    }
                }

            }
        } catch (Throwable var41) {
            logger.error("Error: Method queryTotal execution error !", var41);
        }
    }

}
