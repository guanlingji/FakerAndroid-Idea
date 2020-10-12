package com.faker.idea.packer;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class FileUtil {

    /**
     * 循环删除目录
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
//            dir.delete();
        }
        return dir.delete();
    }
    
    public static void coverFile(String filePath, String str) throws IOException {
		 FileOutputStream fos=new FileOutputStream(filePath,false);
	     fos.write(str.getBytes());
	     fos.close();      
	}
    
    public static void writeFile(String path,String str) throws IOException {
        File fout = new File(path);
        FileOutputStream fos = new FileOutputStream(fout,true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(str);
        bw.newLine();
        bw.close();
    }

    public static long calcFileCRC32(File file) throws IOException {
        FileInputStream fi = new FileInputStream(file);
        CheckedInputStream checksum = new CheckedInputStream(fi, new CRC32());
        while (checksum.read() != -1) { }
        long temp = checksum.getChecksum().getValue();
        fi.close();
        checksum.close();
        return temp;
    }
}
