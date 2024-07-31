package org.codeRunner;

public class Runner implements Runnable{
    String path;
    Runner(String Path){
      this.path=Path;
    }
    @Override
    public void run() {
        Code.compile(this.path);
        final String extension = Code.getExtension(this.path)[0];
        if(extension.equals("c")){
        Code.run(Code.getExtension(path)[1]+".exe");
        }
        if(extension.equals("cpp")){
            Code.run(Code.getExtension(path)[1]+".exe");
        }
        else if(extension.equals("java")){
            Code.run(Code.getExtension(path)[1]+".class");
        }
    }
}
