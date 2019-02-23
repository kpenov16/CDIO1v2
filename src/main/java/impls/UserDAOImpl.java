package impls;

import entities.UserDTO;
import interfaces.IExtendedUserDAO;
import interfaces.IUserDAO;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IExtendedUserDAO, Serializable{
    private static final long serialVersionUID = 4545864587995944260L;
    // JDBC driver name and database URL
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967";

    //  Database credentials
    final String USER = "s133967";
    final String PASS = "8JPOJuQcgUpUVIVHY4S2H";

    @Override
    public UserDTO getUser(int userId) throws IUserDAO.DALException {
        UserDTO user = new UserDTO();

        String sql = "SELECT user.id AS id, user.first_name AS name, user.ini AS ini, user.password AS password, user.cpr AS cpr" +
                " FROM user" +
                " WHERE user.id = ?";
        UserDTO returnedUser = querryUserWithoutRoles(sql, user, userId);

        sql = "SELECT role_user.user_role AS role" +
              " FROM role_user" +
              " WHERE role_user.user_id = ?";
        user = querryUserRoles(sql, user);
        return user;
    }

    @Override
    public List<UserDTO> getUserList() throws IUserDAO.DALException {
        String sql = "SELECT user.id AS id" +
                " FROM user";
        List<Integer> ids = querryUserIds(sql);
        List<UserDTO> users = new ArrayList<>();
        for(Integer id : ids){
            try {
                UserDTO user = getUser(id);
                users.add( user );
            } catch (IUserDAO.DALException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public void createUser(UserDTO user) throws IUserDAO.DALException {
        String sql = "INSERT INTO user (id, first_name, ini, password, cpr)" +
                "VALUES ( ? , ? , ? , ? , ?)";
        updateIndhold(sql, user.getUserId(), user.getUserName(), user.getIni(), user.getPassword(), user.getCpr());

        String sql4 = "INSERT INTO role_user (user_id, user_role)" +
                "VALUES ( ? , ? )";
        for (String role : user.getRoles()){
            updateIndhold(sql4, user.getUserId(), role);
        }
    }

    @Override
    public void updateUser(UserDTO user) throws IUserDAO.DALException {
        String sql = "UPDATE user" +
                    " SET first_name = ?" +
                    " WHERE id = ?";
        updateIndhold(sql, user.getUserName(), user.getUserId());

        String sqlPassword = "UPDATE user" +
                " SET password = ?" +
                " WHERE id = ?";
        updateIndhold(sqlPassword, user.getPassword(), user.getUserId());

        //DELETE FROM role_user WHERE  user_id = 1;
        String sql2 = "DELETE FROM role_user WHERE user_id = ?";
        updateIndhold(sql2, user.getUserId());

        String sql3 = "INSERT INTO role_user (user_id, user_role)" +
                "VALUES ( ? , ? )";
        for (String role : user.getRoles()){
            updateIndhold(sql3, user.getUserId(), role);
        }
    }

    @Override
    public void deleteUser(int userId) throws IUserDAO.DALException {
        //DELETE FROM role_user WHERE  user_id = 1;
        String sql = "DELETE FROM role_user WHERE user_id = ?";
        updateIndhold(sql, userId);

        //DELETE FROM user WHERE id = 1
        String sql3 = "DELETE FROM user WHERE id = ?";
        updateIndhold(sql3, userId);
    }


    private UserDTO querryUserWithoutRoles(String sql, UserDTO userDTO, int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int id  = rs.getInt("id");
                String firstName = rs.getString("name");
                String ini = rs.getString("ini");
                String password = rs.getString("password");
                String cpr = rs.getString("cpr");

                userDTO.setUserId(id);
                userDTO.setUserName(firstName);
                userDTO.setIni(ini);
                userDTO.setPassword(password);
                userDTO.setCpr(cpr);
            }
            rs.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return userDTO;
    }

    private UserDTO querryUserRoles(String sql, UserDTO userDTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userDTO.getUserId());
            ResultSet rs = pstmt.executeQuery();

            List<String> roles = new ArrayList<>();
            while(rs.next()){
                String role = rs.getString("role");
                roles.add(role);
            }
            userDTO.setRoles(roles);
            rs.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return userDTO;
    }

    private void updateIndhold(String sql, int param01, String param02, String param03, String param04, String param05) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, param01);
            pstmt.setString(2, param02);
            pstmt.setString(3, param03);
            pstmt.setString(4, param04);
            pstmt.setString(5, param05);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }
    private void updateIndhold(String sql, int param01, String param02) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, param01);
            pstmt.setString(2, param02);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }
    private void updateIndhold(String sql, String param01, int param02) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, param01);
            pstmt.setInt(2, param02);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }

    private void updateIndhold(String sql, int param01) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, param01);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }

    public void addRolesToRoleTable(List<String> roles) {
        for(String role : roles){
            String sql = "INSERT INTO role (name)" +
                    "VALUES ( ? )";
            updateIndhold(sql, role);
        }
    }

    private void updateIndhold(String sql, String param01) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, param01);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(pstmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }

    private List<Integer> querryUserIds(String sql) {
        Connection conn = null;
        Statement stmt = null;
        List<Integer> ids = new ArrayList<>();
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id  = rs.getInt("id");
                ids.add(id);
            }
            rs.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return ids;
    }

    @Override
    public boolean isInUse(String ini) {
        String sql = "SELECT ini FROM user";
        List<String> inits = queryStringParams(sql, "ini");
        return inits.contains(ini);
    }

    private List<String> queryStringParams(String sql, String key) {
        Connection conn = null;
        Statement stmt = null;
        List<String> params = new ArrayList<>();
        try{
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String param = rs.getString(key);
                params.add(param);
            }
            rs.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return params;
    }

    @Override
    public List<String> queryAllRoles() {
        String sql = "SELECT name FROM role";
        return queryStringParams(sql, "name");
    }
}