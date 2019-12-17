package com.visionvera.api.handler.utils;

import com.alibaba.fastjson.JSON;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * HTTP请求工具类
 *
 * @author EricShen
 */
public class HttpUtils {

  /**
   * 错误集合
   */
  public static final List<String> ERROR_LIST = new ArrayList<>(8);

  /**
   * 1.未知错误
   */
  public static final String ERROR = "T1,未知错误:";
  /**
   * 2.协议异常 ProtocolException
   */
  public static final String PROTOCOL_ERROR = "T2,协议错误:";
  /**
   * 3.超时异常(链接、读取) SocketTimeoutException
   */
  public static final String TIMEOUT_ERROR = "T3,超时错误:";
  /**
   * 4.链接错误 ConnectException
   */
  public static final String CONNECT_ERROR = "T4,链接错误:";
  /**
   * 5.io异常 IOException
   */
  public static final String IO_ERROR = "T5,IO错误:";
  /**
   * 6.空指针 NullPointerException
   */
  public static final String NULL_ERROR = "T6,空指针错误:";
  /**
   * 7.url不完整  MalformedURLException
   */
  public static final String URL_ERROR = "T7,URL错误:";
  /**
   * 8.请求失败 非200
   */
  public static final String FAILURE = "T8,请求失败,HTTP Status-Code:";


  static {
    ERROR_LIST.add(ERROR);
    ERROR_LIST.add(PROTOCOL_ERROR);
    ERROR_LIST.add(TIMEOUT_ERROR);
    ERROR_LIST.add(CONNECT_ERROR);
    ERROR_LIST.add(IO_ERROR);
    ERROR_LIST.add(NULL_ERROR);
    ERROR_LIST.add(URL_ERROR);
    ERROR_LIST.add(FAILURE);
  }

  /**
   * 链接超时：毫秒
   */
  private static final int CONNECT_TIMEOUT = 8000;


  /**
   * 读取超时：毫秒
   */
  private static final int READ_TIMEOUT = 50000;

  /**
   * post 请求
   *
   * @param requestUrl url
   * @param paramMap 参数
   */
  public static String sendPostByMap(String requestUrl, Map<String, Object> paramMap) {
    if (null == paramMap) {
      return HttpUtils.sendPost(requestUrl, null);
    }
    StringBuilder request = new StringBuilder();

    //拼接参数
    Set<Map.Entry<String, Object>> set = paramMap.entrySet();
    for (Map.Entry<String, Object> entry : set) {
      if (entry.getValue() != null) {
        request.append("&").append(entry.getKey()).append("=").append(entry.getValue());
      }
    }
    return HttpUtils.sendPost(requestUrl, request.toString());
  }

  /**
   * post 请求
   *
   * @param requestUrl url
   * @param requestbody 参数：name1=value1&name2=value2
   */
  public static String sendPost(String requestUrl, String requestbody) {
    if (null == requestbody) {
      requestbody = "";
    }
    BufferedReader responseReader = null;
    BufferedWriter writer = null;
    try {
      //建立连接
      URL url = new URL(requestUrl);
      // 打开和URL之间的连接
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      //设置连接属性
      //使用URL连接进行输出
      connection.setDoOutput(true);
      //使用URL连接进行输入
      connection.setDoInput(true);
      //忽略缓存
      connection.setUseCaches(false);
      //设置URL请求方法
      connection.setRequestMethod("POST");

      // 维持长连接
      connection.setRequestProperty("Connection", "Keep-Alive");
      connection.setRequestProperty("Charset", "UTF-8");
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

      //链接超时
      connection.setConnectTimeout(CONNECT_TIMEOUT);
      //读取超时
      connection.setReadTimeout(READ_TIMEOUT);

      //建立输出流,并写入数据
      writer = new BufferedWriter(new PrintWriter(connection.getOutputStream()));
      writer.write(requestbody);
      writer.flush();

      //获取响应状态
      int responseCode = connection.getResponseCode();

      //连接成功
      if (HttpURLConnection.HTTP_OK == responseCode) {
        //当正确响应时处理数据
        StringBuffer buffer = new StringBuffer();
        String readLine;
        //处理响应流
        responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((readLine = responseReader.readLine()) != null) {
          buffer.append(readLine).append("\n");
        }
        //成功 200
        return buffer.toString();
      } else {
        //其他
        return FAILURE + responseCode;
      }
    } catch (ProtocolException e) {
      return PROTOCOL_ERROR + e;
    } catch (SocketTimeoutException e) {
      return TIMEOUT_ERROR + e;
    } catch (MalformedURLException e) {
      return URL_ERROR + e;
    } catch (ConnectException e) {
      return CONNECT_ERROR + e;
    } catch (IOException e) {
      return IO_ERROR + e;
    } catch (NullPointerException e) {
      return NULL_ERROR + e;
    } catch (Exception e) {
      return ERROR + e;
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (responseReader != null) {
          responseReader.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * post json请求
   *
   * @param requestUrl url
   * @param object 参数
   */
  public static String sendPostByJson(String requestUrl, Object object) {
    BufferedReader responseReader = null;
    BufferedWriter writer = null;
    try {
      //建立连接
      URL url = new URL(requestUrl);
      // 打开和URL之间的连接
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      //设置连接属性
      //使用URL连接进行输出
      connection.setDoOutput(true);
      //使用URL连接进行输入
      connection.setDoInput(true);
      //忽略缓存
      connection.setUseCaches(false);
      //设置URL请求方法
      connection.setRequestMethod("POST");

      // 维持长连接
      connection.setRequestProperty("Connection", "Keep-Alive");
      //JSON
      connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
      connection.setRequestProperty("Accept", "application/json");

      //链接超时
      connection.setConnectTimeout(CONNECT_TIMEOUT);
      //读取超时
      connection.setReadTimeout(READ_TIMEOUT);

      //建立输出流,并写入数据
      writer = new BufferedWriter(new PrintWriter(connection.getOutputStream()));
      writer.write(JSON.toJSONString(object));
      writer.flush();

      //获取响应状态
      int responseCode = connection.getResponseCode();

      //连接成功
      if (HttpURLConnection.HTTP_OK == responseCode) {
        //当正确响应时处理数据
        StringBuilder buffer = new StringBuilder();
        String readLine;
        //处理响应流
        responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((readLine = responseReader.readLine()) != null) {
          buffer.append(readLine).append("\n");
        }
        //成功 200
        return buffer.toString();
      } else {
        //其他
        return FAILURE + responseCode;
      }
    } catch (ProtocolException e) {
      return PROTOCOL_ERROR + e;
    } catch (SocketTimeoutException e) {
      return TIMEOUT_ERROR + e;
    } catch (MalformedURLException e) {
      return URL_ERROR + e;
    } catch (ConnectException e) {
      return CONNECT_ERROR + e;
    } catch (IOException e) {
      return IO_ERROR + e;
    } catch (NullPointerException e) {
      return NULL_ERROR + e;
    } catch (Exception e) {
      return ERROR + e;
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (responseReader != null) {
          responseReader.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * get请求
   *
   * @param requestUrl url+参数
   */
  public static String sendGet(String requestUrl) {
    BufferedReader responseReader = null;
    try {
      //建立连接
      URL url = new URL(requestUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("GET");
      connection.setDoOutput(false);
      connection.setDoInput(true);

      //链接超时
      connection.setConnectTimeout(CONNECT_TIMEOUT);
      //读取超时
      connection.setReadTimeout(READ_TIMEOUT);

      connection.connect();

      //获取响应状态
      int responseCode = connection.getResponseCode();

      //连接成功
      if (HttpURLConnection.HTTP_OK == responseCode) {
        //当正确响应时处理数据
        StringBuilder buffer = new StringBuilder();
        String readLine;
        //处理响应流
        responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((readLine = responseReader.readLine()) != null) {
          buffer.append(readLine).append("\n");
        }
        //成功 200
        return buffer.toString();
      } else {
        //其他
        return FAILURE + responseCode;
      }
    } catch (ProtocolException e) {
      return PROTOCOL_ERROR + e;
    } catch (SocketTimeoutException e) {
      return TIMEOUT_ERROR + e;
    } catch (MalformedURLException e) {
      return URL_ERROR + e;
    } catch (ConnectException e) {
      return CONNECT_ERROR + e;
    } catch (IOException e) {
      return IO_ERROR + e;
    } catch (NullPointerException e) {
      return NULL_ERROR + e;
    } catch (Exception e) {
      return ERROR + e;
    } finally {
      try {
        if (responseReader != null) {
          responseReader.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 向指定URL发起POST请求 上传文件
   *
   * 发送的文件路径
   *
   * @param sendParam 附加信息
   * @param toUrl POST请求的URL
   * @return 指定URL的响应结果
   */
  public static String sendFilePost(String toUrl, String sendParam, File file) {
    StringBuilder result = new StringBuilder();
    OutputStream out = null;
    BufferedInputStream in = null;
    BufferedReader reader = null;
    try {
      // 换行符
      final String newLine = "\r\n";
      final String boundaryPrefix = "--";
      // 定义数据分隔线
      String boundary = "file-desc-post-boundary-1e4j56df90j4h";
      // 服务器的域名
      URL url = new URL(toUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      // 设置为POST请求
      conn.setRequestMethod("POST");
      // POST请求设置
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setConnectTimeout(6000);
      conn.setReadTimeout(18000);
      conn.setChunkedStreamingMode(1048576);
      // 设置请求头参数
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("Charsert", "UTF-8");
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

      out = new DataOutputStream(conn.getOutputStream());

      // 发送附加信息
      String sbDesc = (boundaryPrefix + boundary + newLine) + "Content-Type: text/plain" + newLine
          + "Content-Disposition: form-data; name=\"sendParam\"" + newLine + newLine + sendParam
          + newLine + boundaryPrefix + boundary + newLine;
      out.write(sbDesc.getBytes(StandardCharsets.UTF_8));
      // 发送文件
      StringBuilder sbFile = new StringBuilder();
      // 文件参数
      String originalFilename = getOriginalFilename(file);
      System.out.println("文件名为：" + originalFilename);
      sbFile.append("Content-Disposition: form-data;name=\"resContent\";filename=\"")
          .append(originalFilename).append("\"").append(newLine);
      sbFile.append("Content-Type:application/octet-stream");
      // 参数头设置完以后需要两个换行，然后才是参数内容
      sbFile.append(newLine);
      sbFile.append(newLine);

      // 将参数头的数据写入到输出流中
      out.write(sbFile.toString().getBytes(StandardCharsets.UTF_8));
      FileInputStream fileInputStream = new FileInputStream(file);
      in = new BufferedInputStream(fileInputStream);
      byte[] bufferOut = new byte[1024 * 10];
      int bytes = 0;
      // 每次读1KB数据,并且将文件数据写入到输出流中
      while ((bytes = in.read(bufferOut)) != -1) {
        out.write(bufferOut, 0, bytes);
      }
      // 最后添加换行
      out.write(newLine.getBytes());
      // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
      byte[] endData = (newLine + boundaryPrefix + boundary + boundaryPrefix + newLine).getBytes();
      // 写上结尾标识
      out.write(endData);
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

      String line = null;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }

      return result.toString();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.close();
        }
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result.toString();
  }

  private static String getOriginalFilename(File file) {
    String filename = file.getName();

    // Check for Unix-style path
    int unixSep = filename.lastIndexOf('/');
    // Check for Windows-style path
    int winSep = filename.lastIndexOf('\\');
    // Cut off at latest possible point
    int pos = (winSep > unixSep ? winSep : unixSep);
    if (pos != -1) {
      // Any sort of path separator found...
      return filename.substring(pos + 1);
    }
    // A plain name
    return filename;
  }

  /**
   * 校验结果(只校验是否请求成功)
   *
   * @param result
   * @return
   */
  public static boolean checkoutResult(String result) {
    for (String error : HttpUtils.ERROR_LIST) {
      boolean contains = result.contains(error);
      if (contains) {
        return false;
      }
    }
    return true;
  }

  /**
   * 获取 url 中的ip
   *
   * @param urlStr url
   * @return
   */
  public static String getIpByUrl(String urlStr) {
    if (StringUtil.isEmpty(urlStr)) {
      return "";
    }
    return urlStr.replaceFirst(RegexConstant.URL_CUT_REGEX, RegexConstant.CUT_IP);
  }

  /**
   * 获取 url 中的端口
   *
   * @param urlStr url
   * @return port
   */
  public static Integer getPortByUrl(String urlStr) {
    if (StringUtil.isEmpty(urlStr)) {
      return null;
    }
    return Integer
        .valueOf(urlStr.replaceFirst(RegexConstant.URL_CUT_REGEX, RegexConstant.CUT_PORT));
  }

  /**
   * 测试
   */
  public static void main(String[] args) {
    String url = "http://127.0.0.1:8080/remoteservice/user/login.do";
    Map<String, Object> paramMap = new HashMap<String, Object>(2);
    paramMap.put("loginName", "admin");
    paramMap.put("password", "e10adc3949ba59abbe56e057f20f883e");
    String result1 = sendPostByMap(url, paramMap);
    String result2 = sendPost(url, "loginName=admin&password=e10adc3949ba59abbe56e057f20f883e");
    String result3 = sendGet(url + "?loginName=admin&password=e10adc3949ba59abbe56e057f20f883e");
    String result4 = sendPostByJson(url, paramMap);
    System.out.println(result1);
    System.out.println(result2);
    System.out.println(result3);
    System.out.println(result4);
    //校验
    final boolean b = checkoutResult(result4);
    System.out.println(b);

  }


}
