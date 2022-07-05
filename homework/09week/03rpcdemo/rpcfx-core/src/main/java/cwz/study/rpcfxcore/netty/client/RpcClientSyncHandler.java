/*
 * <p>文件名称: RpcClientSyncHandler</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/5 23:21 </p>
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
import cwz.study.rpcfxcore.netty.common.RpcProtocol;
import cwz.study.rpcfxcore.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class RpcClientSyncHandler extends SimpleChannelInboundHandler<RpcProtocol> {
    private CountDownLatch latch;
    private RpcResponse response;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol msg) throws Exception {
        log.info("Netty client receive message:");
        log.info("Message length: " + msg.getLen());
        log.info("Message content: " + new String(msg.getContent(), CharsetUtil.UTF_8));

        // 将 RpcResponse字符串 反序列化成 RpcResponse对象
        RpcResponse rpcResponse = JSON.parseObject(new String(msg.getContent(), CharsetUtil.UTF_8), RpcResponse.class);
        log.info("Netty client serializer : " + rpcResponse.toString());
        response = rpcResponse;
        latch.countDown();
    }

    /**
     * 锁的初始化
     *
     * @param latch CountDownLatch
     */
    void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * 阻塞等待结果后返回
     *
     * @return 后台服务器响应
     * @throws InterruptedException exception
     */
    RpcResponse getResponse() throws InterruptedException {
        latch.await();
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
