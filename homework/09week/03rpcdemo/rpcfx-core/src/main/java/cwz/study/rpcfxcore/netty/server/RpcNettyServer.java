/*
 * <p>文件名称: RpcNettyServer</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/5 22:19 </p>
 * <p>完成日期: </p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 *
 * @version 1.0
 * @author chenwz
 */
package cwz.study.rpcfxcore.netty.server;

import cwz.study.rpcfxcore.netty.common.RpcDecoder;
import cwz.study.rpcfxcore.netty.common.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Netty Server 启动类
 */
@Slf4j
@Component
public class RpcNettyServer {

    private final ApplicationContext context;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public RpcNettyServer(ApplicationContext context) {
        this.context = context;
    }

    public void run() throws Exception {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast("Message Encoder", new RpcEncoder());
                                pipeline.addLast("Message Decoder", new RpcDecoder());
                                pipeline.addLast("Message Handler", new RpcServerHandler(context));
                            }
                        }
                );
        int port = 8080;
        Channel channel = serverBootstrap.bind(port).sync().channel();
        log.info("开启netty http服务器，端口为 " + port);
        channel.closeFuture().sync();
    }

    public void destroy() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }

}
