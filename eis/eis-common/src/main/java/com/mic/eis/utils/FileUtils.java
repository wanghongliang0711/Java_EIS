package com.mic.eis.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author calisto
 * @date 2020-01-19 5:16 下午
 */
public class FileUtils {

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";


    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public static String getAbsoluteFile(String filename) {
        String downloadPath = ToolUtil.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        System.out.println(downloadPath);
        return downloadPath;
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return true;
    }

    /**
     * 下载文件名重新编码
     *
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 获取系统临时目录
     * @return
     */
    public static String getTemp()
    {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 创建临时文件
     * @param filePath
     * @param data
     * @return
     */
    public static File createTempFile(String filePath, byte[] data) throws IOException
    {
        String temp = getTemp() + filePath;
        File file = new File(temp);
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        if (!file.exists())
        {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data, 0, data.length);
        fos.flush();
        fos.close();
        return file;
    }


    /**
     * 替换文本
     * @param find 查找的文本
     * @param rePlace 替换为的文本
     * @param text 文本
     */
    public static String replaceText(String find, String rePlace, String text) throws IOException {
        // 内存流, 作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();
        BufferedReader bufIn = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())));
        String line;
        while ((line = bufIn.readLine()) != null) {
            // 替换每行中, 符合条件的字符串
            line = line.replaceAll(find, rePlace);
            // 将该行写入内存
            tempStream.write(line);
            // 添加换行符
            tempStream.append(System.getProperty("line.separator"));
        }
        // 关闭输入流
        bufIn.close();
        String data = tempStream.toString();
        //关闭临时流
        tempStream.close();
        return data;
    }


    /**
     * 写出文件到response流
     */
    public static void writeTextForResponse(byte[] data, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName="
                + URLEncoder.encode(fileName, "UTF-8"));
        response.getOutputStream().write(data);
    }
}
