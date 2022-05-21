import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestFile {
    public static void main(String[] args) throws IOException {
        // 通过简单的程序,把一个文件中的内容独取出来,写入到另一个文件中(相当于文件拷贝功能)
        String srcPath = "D:\\path\\test1.txt";
        String destPath = "D:\\path\\test2.txt";

        // 读写文件之前,一定要打开!
        // 通过这个代码打开了第一个用来读数据的文件
        FileInputStream fileInputStream = new FileInputStream(srcPath);
        // 这个代码打开了第二个用来了写数据的文件
        FileOutputStream fileOutputStream = new FileOutputStream(destPath);

        // 循坏的把第一个文件的内容按照字节来读取出来,依次写到第二个文件中
        while (true) {
            // read 方法依次返回的是一个字节(byte),但是实际上确是用 int 来接收的!!!!
            // 这样做的理由有两个方面:
            // 1. Java 中不存在无符号类型,byte 这样的类型也是有符号的 -128-127
            // 实际上在按照字节读取数据的时候,并不需要让这样的数据进行算术运算,此时正负就没有意义了
            // 期望读到的结果是一个无符号的数字 0-255
            // 2. read 如果读取完毕(文件读取到末尾了),就会返回 EOF(end of file) 用 -1 来表示
            int ch = fileInputStream.read();
            if (ch == -1) {
                break;
            }
            fileOutputStream.write(ch);
        }

        // 文件操作完毕之后,不要忘了关闭文件!!!
        // 如果忘记关闭就可能会引起文件资源泄漏
        fileInputStream.close();
        fileOutputStream.close();
    }
}
