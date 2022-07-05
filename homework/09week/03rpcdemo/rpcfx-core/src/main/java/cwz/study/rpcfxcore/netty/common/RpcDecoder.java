/*
 * <p>文件名称: RpcDecoder</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/5 21:53 </p>
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
package cwz.study.rpcfxcore.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Rpc framework 自定义解码器
 * bytes -> rpcProtocol
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    private int length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("Netty decode run");
        if (in.readableBytes() >= 4) {
            if (length == 0) {
                length = in.readInt();
            }
            if (in.readableBytes() < length) {
                log.info("Readable data is less, wait");
                return;
            }
            byte[] content = new byte[length];
            if (in.readableBytes() >= length) {
                in.readBytes(content);
                RpcProtocol rpcProtocol = new RpcProtocol();
                rpcProtocol.setLen(length);
                rpcProtocol.setContent(content);
                out.add(rpcProtocol);
            }
            length = 0;
        }
    }
}
