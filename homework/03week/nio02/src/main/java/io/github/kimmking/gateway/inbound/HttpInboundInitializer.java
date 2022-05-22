package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private List<String> proxyServer;

    public HttpInboundInitializer(List<String> proxyServer) {
        this.proxyServer = proxyServer;
    }

    /**
     * 初始化管道
     *
     * @param ch
     */
    @Override
    public void initChannel(SocketChannel ch) {
        //数据处理管道，就是事件处理器链
        ChannelPipeline pipeline = ch.pipeline();
//		if (sslCtx != null) {
//			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
//		}
        pipeline.addLast(new HttpServerCodec());
        //pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        //添加自定义的入站处理事件
        pipeline.addLast(new HttpInboundHandler(this.proxyServer));
    }
}
