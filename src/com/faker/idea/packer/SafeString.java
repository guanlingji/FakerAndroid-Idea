package com.faker.idea.packer;

import com.google.common.io.LittleEndianDataInputStream;
import com.faker.idea.patch.LittleEndianDataOutputStream;

import java.io.IOException;

class SafeString {


    public SafeString(){
    }

    public static void write(LittleEndianDataOutputStream stream, String s) throws IOException {
        byte bt[] = s.getBytes();
        stream.writeInt(bt.length);
        stream.write(bt);
    }

    public static String read(LittleEndianDataInputStream stream) throws IOException {
         int len = stream.readInt();
         byte[] buf = new byte[len];
         int retLen = stream.read(buf, 0, len);
         if (retLen != len){
             throw new IOException("File Length Not Enougth");
         }
         return new String(buf);
    }
}
