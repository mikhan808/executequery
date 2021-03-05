package biz.redsoft;

import javax.resource.ResourceException;
import java.sql.Connection;
import java.sql.SQLException;

public interface IFBDataSource {

    void setUserName(String userName);

    void setPassword(String password);

    void setURL(String url);

    void setCharset(String charset);

    Connection getConnection() throws SQLException;

    void close() throws ResourceException;

    void setCertificate(String certificate);

    void setNonStandardProperty(String key, String value);
}
