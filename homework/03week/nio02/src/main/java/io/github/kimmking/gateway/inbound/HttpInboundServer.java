package io.github.kimmking.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
public class HttpInboundServer {

    /**
     * 网关入栈服务器绑定端口
     */
    private int port;

    /**
     * 后端服务器地址
     */
    private List<String> proxyServers;

    public HttpInboundServer(int port, List<String> proxyServers) {
        this.port = port;
        this.proxyServers = proxyServers;
    }

    public void run() throws Exception {
        //第一个通常称为“boss ”,接受传入连接。第二个通常称为“worker ”,
        //一旦boss接受连接并将接受的连接注册到worker，它就处理接受的连接的流量。
        //使用了多少线程以及它们如何映射到创建的Channels取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);

        try {
            //引导程序，设置服务器的助手类
            ServerBootstrap bootstrap = new ServerBootstrap();
            //您还可以设置特定于的参数Channel实施。我们正在编写一个TCP/IP服务器，所以我们可以设置套接字选项，例如tcpNoDelay和keepAlive。
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    //option()是为了NioServerSocketChannel它接受传入的连接。
                    //childOption()是为了Channel被父母接受了ServerChannel，也就是NioSocketChannel这种情况下。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.group(bossGroup, workerGroup)
                    //指定使用NioServerSocketChannel用于实例化新的Channel接受传入的连接。
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    //此处指定的处理程序将始终由新接受的Channel。
                    // 这ChannelInitializer是一个特殊的处理程序，旨在帮助用户配置新的Channel。
                    // 很可能您想要配置ChannelPipeline新的Channel通过添加一些处理程序，例如DiscardServerHandler实现您的网络应用程序。
                    // 随着应用程序变得复杂，您可能会向管道中添加更多的处理程序，并最终将这个匿名类提取到一个顶级类中。
                    .childHandler(new HttpInboundInitializer(this.proxyServers));
            //绑定到端口并启动服务器。在这里，绑定到端口8080机器中所有NIC(网络接口卡)的数量
            Channel ch = bootstrap.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
