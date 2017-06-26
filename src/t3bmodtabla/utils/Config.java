/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t3bmodtabla.utils;

/**
 *
 * @author daniel
 */
public class Config {
    
    private String dbHost = "";
    private String dbUser = "";
    private String dbPass = "";
    private String dbPort = "";
    private String dbName = "";
    private String dbClassDriver = "";
    private String dbUrl = "";

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbClassDriver() {
        return dbClassDriver;
    }

    public void setDbClassDriver(String dbClassDriver) {
        this.dbClassDriver = dbClassDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    @Override
    public String toString() {
        return "Config{" + "dbHost=" + dbHost + ", dbUser=" + dbUser + ", dbPass=" + dbPass + ", dbPort=" + dbPort + ", dbName=" + dbName + ", dbClassDriver=" + dbClassDriver + ", dbUrl=" + dbUrl + '}';
    }   
    
}
