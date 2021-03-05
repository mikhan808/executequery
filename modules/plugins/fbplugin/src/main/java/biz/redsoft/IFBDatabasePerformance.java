package biz.redsoft;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vasiliy on 4/6/2017.
 */
public interface IFBDatabasePerformance {

    void setConnection(Connection connection) throws SQLException;

    IFBPerformanceInfo getPerformanceInfo() throws SQLException;

    String getLastExecutedPlan(ResultSet rs) throws SQLException;

    String getLastExecutedPlan(Statement st) throws SQLException;
}
