package com.faker.idea;

import brut.androlib.AndrolibException;
import brut.androlib.meta.MetaInfo;
import brut.androlib.src.SmaliBuilder;
import brut.directory.ExtFile;
import com.faker.idea.packer.Packer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.BackgroundTaskQueue;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class FakeSmali extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        new BackgroundTaskQueue(project, "Facker Smali Builder").run(new SmailPackerThread(project,"facker smali building"));
    }
    class SmailPackerThread extends Task.Backgroundable {

        Project project;
        public SmailPackerThread(@Nullable Project project, @Nls(capitalization = Nls.Capitalization.Title) @NotNull String title) {
            super(project, title);
            this.project = project;
        }

        private void log(File file, String log, boolean append){
            FileWriter writer;
            try {
                writer = new FileWriter(file,append);
                writer.write(log);
                writer.write("\r\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(@NotNull ProgressIndicator progressIndicator) {

            String projectPath = project.getBasePath();
            File file = new File(projectPath,"app");
            File srcMain = new File(file,"src\\main");
            File javaSrcMain = new File(srcMain,"java");
            File smalis = new File(srcMain,"smalis");
            File assets = new File(srcMain,"assets");
            File apktoolYaml = new File(srcMain,"apktool.yml");
            File logFile = new File(smalis,"build.txt");
            if(!logFile.exists()){
                try {
                    logFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            log(logFile,"get the project dir "+projectPath,false);
            progressIndicator.setText("get the project dir"+projectPath);

            MetaInfo metaInfo = null;
            try {
                metaInfo = MetaInfo.load(new FileInputStream(apktoolYaml));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            Packer packer = new Packer(new File(assets,"target.dfk"));
            File smaliDirs[] = smalis.listFiles();
            int index = 1;
            for ( int i=0;i<smaliDirs.length;i++) {
                File f = smaliDirs[i];
                if(f.isDirectory()&&f.getName().startsWith("smali")&&!f.getName().endsWith("assets")){
                    ExtFile extFile = new ExtFile(f);
                    try {
                        String endStuff = null;
                        if(index==1){
                            endStuff = "_fk.dex";
                        }else {
                            endStuff = index+"_fk.dex";
                        }
                        int targetSdkVersion = Integer.parseInt(metaInfo.sdkInfo.get("minSdkVersion"));
                        System.out.println("targetSdkVersion "+targetSdkVersion);
                        File dexFile = new File(assets,"classes"+endStuff);
                        log(logFile,"building dex:"+dexFile.getName(),true);
                        progressIndicator.setText("building dex:"+dexFile.getName());

                        MSmaliBuilder.build(extFile,dexFile,targetSdkVersion,javaSrcMain);
                        index++;
                        try {
                            packer.addFile(dexFile.getName(),dexFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (AndrolibException ex) {
                        log(logFile,"building dex failed:"+i+"----"+ex.getLocalizedMessage() ,true);
                        progressIndicator.setText("building dex:"+ex.getLocalizedMessage());
                        ex.printStackTrace();
                    }
                }
            }
            //pcaker then del oringal
            try {
                log(logFile,"packing dexes----" ,true);
                progressIndicator.setText("packing dexes----");
                packer.saveToPath();
                //packer.delOriginalDex();
                log(logFile,"dex packer task finished----" ,true);
                progressIndicator.setText("dex packer task finished----");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
