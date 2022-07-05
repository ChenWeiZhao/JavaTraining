/*
 * <p>文件名称: RpcNettyClientSync</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/5 23:29 </p>
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
package cwz.study.rpcfxcore.netty.client;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cwz.study.rpcfxcore.netty.common.RpcProtocol;
import cwz.study.rpcfxcore.protocol.RpcRequest;
import cwz.study.rpcfxcore.protocol.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class RpcNettyClientSync {
    private RpcNettyClientSync() {
    }

    /**
     * 使用Map来保存用过的Channel，看下次相同的后台服务是否能够重用，起一个类似缓存的作用
     */
    private ConcurrentHashMap<String, Channel> channelPool = new ConcurrentHashMap<>();
    private EventLoopGroup clientGroup = new NioEventLoopGroup(new ThreadFactoryBuilder().setNameFormat("client work-%d").build());

    /**
     * 调用channel发送请求，从handler中获取响应结果
     *
     * @return 响应
     */
    public RpcResponse getReponse(RpcRequest rpcRequest, String url) throws URISyntaxException, InterruptedException {
        RpcProtocol request = convertNettyRequest(rpcRequest);
        URI uri = new URI(url);
        String cacheKey = uri.getHost() + ":" + uri.getPort();
        // 查看缓存池中是否有可重用的channel
        if (channelPool.containsKey(cacheKey)) {
            Channel channel = channelPool.get(cacheKey);
            if (!channel.isActive() || !channel.isWritable() || !channel.isOpen()) {
                log.debug("Channel can't reuse");
            } else {
                try {
                    RpcClientSyncHandler rpcClientSyncHandler = new RpcClientSyncHandler();
                    rpcClientSyncHandler.setLatch(new CountDownLatch(1));
                    channel.pipeline().replace("clientHandler", "clientHandler", rpcClientSyncHandler);
                    channel.writeAndFlush(request).sync();
                    return rpcClientSyncHandler.getResponse();
                } catch (InterruptedException e) {
                    log.debug("channel reuse send msg failed!");
                    channel.close();
                    channelPool.remove(cacheKey);
                }
                log.debug("Handler is busy, please user new channel");
            }
        }
        // 没有或者不可用则新建
        // 并将最终的handler添加到pipeline中，拿到结果后返回
        RpcClientSyncHandler rpcClientSyncHandler = new RpcClientSyncHandler();
        rpcClientSyncHandler.setLatch(new CountDownLatch(1));
        Channel channel = createChannel(uri.getHost(), uri.getPort());
        channel.pipeline().replace("clientHandler", "clientHandler", rpcClientSyncHandler);
        channelPool.put(cacheKey, channel);
        channel.writeAndFlush(request).sync();
        return rpcClientSyncHandler.getResponse();

    }

    /**
     * 返回新的Channel
     *
     * @param host ip地址
     * @param port 端口
     * @return channel
     * @throws InterruptedException exception
     */
    private Channel createChannel(String host, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.AUTO_CLOSE, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
        return bootstrap.connect(host, port).sync().channel();
    }

    /**
     * 将 {@RpcRequest} 转成 netty 自定义的通信格式 {@RpcProtocol}
     */
    private RpcProtocol convertNettyRequest(RpcRequest rpcRequest) {
        RpcProtocol rpcProtocol = new RpcProtocol();
        String requestJson = JSON.toJSONString(rpcRequest);
        rpcProtocol.setLen(requestJson.getBytes(StandardCharsets.UTF_8).length);
        rpcProtocol.setContent(requestJson.getBytes(StandardCharsets.UTF_8));
        return rpcProtocol;
    }


    private enum EnumSingleton {
        /**
         * 懒汉枚举单例
         */
        INSTANCE;
        private RpcNettyClientSync instance;

        EnumSingleton() {
            instance = new RpcNettyClientSync();
        }

        public RpcNettyClientSync getSingletonInstance() {
            return instance;
        }

    }

    public RpcNettyClientSync getInstance() {
        return EnumSingleton.INSTANCE.getSingletonInstance();
    }
}
