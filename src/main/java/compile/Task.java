package compile;

import common.FileUtil;

import java.io.File;

// 每次的 "编译 + 运行" 这个过程,被称为是一个 compile.Task
public class Task {
    // 创建一组常量来约定临时文件的名字
    // 这个表示所有临时文件所在的目录
    private static final String WORK_DIR = "./tmp/";
    // 约定代码的类名
    private static final String CLASS = "Solution";
    // 约定要编译的代码文件名
    private static final String CODE = WORK_DIR + "Solution.java";
    // 约定存放编译错误信息的文件名
    private static final String COMPILE_ERROR = WORK_DIR + "compileError.txt";
    // 约定存放运行时的标准输出的文件名
    private static final String STDOUT = WORK_DIR + "stdout.txt";
    // 约定存放运行时的标准错误的文件名
    private static final String STDERR = WORK_DIR + "stderr.txt";

    // 这个 compile.Task 类提供的核心方法,就叫做 compileAndRun,编译+运行的意思
    // 参数: 要编译运行的 java 源代码
    // 返回值: 表示编译运行的结果, 编译出错/运行出错/运行正确
    public Answer compileAndRun(Question question) {
        Answer answer = new Answer();
        // 0. 准备好用来存放临时文件的目录
        File workDir = new File(WORK_DIR);
        if (!workDir.exists()) {
            // 创建多级目录
            workDir.mkdirs();
        }
        // 1. 把 question 中的 code 写入到一个 .java 文件中
        FileUtil.writeFile(CODE,question.getCode());
        // 2. 创建子进程,调用 javac 进行编译. 注意! 编译的时候需要有一个 .java 文件,当前是通过 String 的方式来提供的代码
        //       如果编译出错, javac 就会把错误信息给写入到 stderr 里, 就可以用一个专门的文件来保存 compileError.txt
        //    需要先把编译的命令给构造出来
        String compileCmd = String.format("javac -encoding utf8 %s -d %s",CODE,WORK_DIR);
        System.out.println("编译命令 " + compileCmd);
        CommandUtil.run(compileCmd,null,COMPILE_ERROR);
        // 如果编译出错了,错误信息就会被记录到 COMPILE_ERROR 文件中,如果没有编译出错,那么这个文件会是空文件
        String compileError = FileUtil.readFile(COMPILE_ERROR);
        if (!compileError.equals("")) {
            // 编译出错!
            // 直接返回 compile.Answer,让 compile.Answer 里面记录编译的错误信息
            System.out.println("编译出错!");
            answer.setError(1);
            answer.setReason(compileError);
            return answer;
        }
        // 编译正确! 继续往下执行运行的程序
        // 3. 创建子进程,调用 java 命令并执行
        //       运行程序的时候,也会把 java 子进程的标准输出和标准错误获取到 stdout.txt, stderr.txt
        String runCmd = String.format("java -classpath %s %s",WORK_DIR,CLASS);
        System.out.println("运行命令 " + runCmd);
        CommandUtil.run(runCmd,STDOUT,STDERR);
        String runError = FileUtil.readFile(STDERR);
        if (!runError.equals("")) {
            System.out.println("运行出错!");
            answer.setError(2);
            answer.setReason(runError);
            return answer;
        }
        // 4. 父进程获取到刚才编译执行的结果,并打包成 compile.Answer 对象
        //       编译执行的结果,就通过刚才约定的这几个文件来进行获取即可
        answer.setError(0);
        answer.setStdout(FileUtil.readFile(STDOUT));
        return answer;
    }

    public static void main(String[] args) {
        Task task = new Task();
        Question question = new Question();
        question.setCode("public class Solution {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"hello world\");\n" +
                "    }\n" +
                "}\n");
        Answer answer = task.compileAndRun(question);
        System.out.println(answer);
    }
}
