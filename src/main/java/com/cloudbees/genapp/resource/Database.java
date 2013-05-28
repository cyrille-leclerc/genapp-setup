package com.cloudbees.genapp.resource;

import java.util.*;

public class Database extends Resource {

    public static final String URL_PROPERTY = "DATABASE_URL";
    public static final String USERNAME_PROPERTY = "DATABASE_USERNAME";
    public static final String PASSWORD_PROPERTY = "DATABASE_PASSWORD";
    public static final List<String> TYPES = Arrays.asList("datasource", "database");

    public static String getDriver(String url) {
        if (url.matches("^mysql://.*$"))
            return "mysql";
        else if (url.matches("^sqlserver://.*$"))
            return "sql";
        else if (url.matches("^postgresql://.*$"))
            return "postgres";
        else if (url.matches("^oracle:.*$"))
            return "oracle";
        return null;
    }

    public static String getJavaDriver(String driver) {
        if (driver.equals("mysql"))
            return "com.mysql.jdbc.Driver";
        else if (driver.equals("sql"))
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        else if (driver.equals("postgres"))
            return "org.postgresql.Driver";
        else if (driver.equals("oracle"))
            return "oracle.jdbc.OracleDriver";
        return null;
    }

    public static String getDataSourceClassName(String driver) {
        if (driver.equals("mysql"))
            return "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
        else if (driver.equals("sql"))
            return "com.microsoft.sqlserver.jdbc.SQLServerDataSource";
        else if (driver.equals("postgres"))
            return "org.postgresql.ds.PGSimpleDataSource";
        else if (driver.equals("oracle"))
            return "oracle.jdbc.pool.OracleDataSource";
        return null;
    }

    static boolean checkResource(Resource resource) {
        boolean isValid;
        if (isValid = resource != null) {
            isValid = TYPES.contains(resource.getType());
            isValid = isValid && resource.getProperty(URL_PROPERTY) != null;
            isValid = isValid && resource.getProperty(USERNAME_PROPERTY) != null;
            isValid = isValid && resource.getProperty(PASSWORD_PROPERTY) != null;
            isValid = isValid && getDriver(resource.getProperty(URL_PROPERTY)) != null;
        }
        return isValid;
    }

    Database (Resource resource) {
        super(resource.getProperties(), resource.getDescriptors());
        if (!checkResource(resource))
            throw new IllegalArgumentException("Incorrect database resource definition.");
    }

    public String getUrl() {
        return getProperty(URL_PROPERTY);
    }

    public String getUsername() {
        return getProperty(USERNAME_PROPERTY);
    }

    public String getPassword() {
        return getProperty(PASSWORD_PROPERTY);
    }

    public String getDriver() {
        return getDriver(getProperty(URL_PROPERTY));
    }

    public String getJavaDriver() {
        return getJavaDriver(getDriver(getProperty(URL_PROPERTY)));
    }
}