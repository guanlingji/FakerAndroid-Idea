package com.faker.idea.packer;

import java.io.File;
import java.io.IOException;

class Main {
    private static final int a = 0x12345678;
    private static final int b = 0x78563412;
    public static void test(){
        char ch = (char)a;
        if(0x12==ch){
            System.out.println("小端!");
        }else{
            System.out.println("大端!");
        }
    }

    public static void main(String[] args) throws IOException {
        Packer packer = new Packer(new File("C:\\Users\\Yang\\Desktop\\AppShell\\native\\app\\src\\main\\assets\\dex"));
//        packer.addFile("smali", new File("C:\\Users\\HxBreak\\Desktop\\apkshell\\ApkShellUtil\\lib\\smali-2.2.5-fat.jar"));
        //packer.addFile("你好json", new File("C:\\Users\\HxBreak\\Desktop\\apkshell\\ApkShellUtil\\lib\\org.json.jar"));
        //packer.addFile("ccasdas", new File("C:\\Users\\HxBreak\\Desktop\\apkshell\\ApkShellUtil\\lib\\org.json.jar"));
        //packer.addFile("kl", new File("C:\\Users\\Yang\\Desktop\\xkp\\t\\classes.dex"));
        //packer.saveToPath();
        packer.addFile("kl.dex", new File("C:\\Users\\Yang\\Desktop\\xkp\\kl"));
        packer.saveToPath();
        //packer.unpackFromFile(new File("C:\\Users\\Yang\\Desktop\\xkp\\"));
        




        
        
    }
}
