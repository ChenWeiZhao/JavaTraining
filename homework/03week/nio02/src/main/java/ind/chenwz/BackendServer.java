package ind.chenwz;

import io.netty.util.CharsetUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackendServer {
    // 后台服务端口
    public static final int BACKEND_PORT = 8088;
    // 运行标志位
    public static final AtomicBoolean RunningFlag = new AtomicBoolean(true);

    public static void main(String[] args) throws IOException {
        // 根据系统配置创建了一个固定大小的线程池处理请求
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
        final ServerSocket serverSocket = new ServerSocket(BACKEND_PORT);
        while (RunningFlag.get()) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //循环退出之后关闭服务
        serverSocket.close();
        executorService.shutdown();
    }

    private static void service(Socket socket) {
        //接收报文-解析Http头
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[8 * 2048];
            int length = inputStream.read(buffer);
            byteArrayOutputStream.write(buffer, 0, length);
            String inputContent = byteArrayOutputStream.toString(CharsetUtil.UTF_8.name());
            System.out.println("后端服务接到请求：\n" + inputContent);
            byteArrayOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回信息
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,this is cwz-nio3!";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}