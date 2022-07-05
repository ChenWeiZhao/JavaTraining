/*
 * <p>文件名称: RpcServerHandler</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/5 22:58 </p>
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import cwz.study.rpcfxcore.netty.common.RpcProtocol;
import cwz.study.rpcfxcore.protocol.RpcRequest;
import cwz.study.rpcfxcore.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol> {
    private ApplicationContext applicationContext;

    public RpcServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol msg) throws Exception {
        log.info("Netty server receive message:");
        log.info("Message length: " + msg.getLen());
        log.info("Message content: " + new String(msg.getContent(), CharsetUtil.UTF_8));
        // 获取 RpcProtocol中的 RpcRequest内容，反序列化成 RpcRequest 对象
        RpcRequest rpcRequest = JSON.parseObject(new String(msg.getContent(), CharsetUtil.UTF_8), RpcRequest.class);
        log.info("Netty server serializer : " + rpcRequest.toString());
        // 获取相应的bean，反射调用方法，获取结果
        RpcResponse response = invoke(rpcRequest);

        // 返回结果给netty 客户端
        RpcProtocol message = new RpcProtocol();
        String requestJson = JSON.toJSONString(response);
        message.setLen(requestJson.getBytes(CharsetUtil.UTF_8).length);
        message.setContent(requestJson.getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(message).sync();
        log.info("return response to client end");

    }

    /**
     * 获取接口实现对应的bean，反射调用方法，返回结果
     *
     * @param rpcRequest rpc request
     * @return result
     */
    private RpcResponse invoke(RpcRequest rpcRequest) {
        RpcResponse rpcResponse = new RpcResponse();
        String serviceClass = rpcRequest.getServiceClass();
        //改为泛型和反射
        Object bean = applicationContext.getBean(serviceClass);
        try {
            Method method = resolveMethodFromClass(serviceClass.getClass(), rpcRequest.getMethod());
            Object result = method.invoke(serviceClass, rpcRequest.getArgv());
            log.info("Server method invoke result: " + result.toString());
            //两次json序列化能否合并成一个
            rpcResponse.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            rpcResponse.setStatus(true);
            log.info("Server Response serialize to string return");
            return rpcResponse;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            rpcResponse.setException(e);
            rpcResponse.setStatus(false);
            return rpcResponse;
        }
    }

    private Method resolveMethodFromClass(Class<? extends String> aClass, String method) {
        return Arrays.stream(aClass.getMethods())
                .filter(m -> method.equals(m.getName())).findFirst().get();
    }


}
