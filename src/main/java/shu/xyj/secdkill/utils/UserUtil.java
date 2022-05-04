package shu.xyj.secdkill.utils;

// 生成用户工具类

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import shu.xyj.secdkill.pojo.User;
import shu.xyj.secdkill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {


    private  static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setNickname("user"+i);
            user.setId(13000000000L + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            users.add(user);
        }

        System.out.println("create user..");
        Connection connection = getConnection();

        String sql = "insert into t_user(id, nickname, password, salt) values(?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getSalt());
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
        connection.close();

        System.out.println("insert to db..");

        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\11515\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToServerPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len=inputStream.read(buff))>=0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userToken = (String) respBean.getObj();
            System.out.println(user.getId() + "create userToken ===> " + userToken);

            String row = user.getId() + "," + userToken;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("The record has been written to file..");
        }
        raf.close();
        System.out.println("over..");
    }


    private static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/secdkill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String driver = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "660201";

        Class.forName(driver);

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }

}
