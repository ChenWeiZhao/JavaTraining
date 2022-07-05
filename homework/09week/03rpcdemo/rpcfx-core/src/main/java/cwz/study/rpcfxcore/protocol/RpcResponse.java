/*
 * <p>文件名称: RpcResponse</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/5 21:44 </p>
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
package cwz.study.rpcfxcore.protocol;

import lombok.Data;

/**
 * Rpc 自定义响应结果
 */
@Data
public class RpcResponse {
    /**
     * 响应结果
     */
    private Object result;

    /**
     * 函数是否执行成功
     */
    private Boolean status;

    /**
     * 执行失败的异常信息
     */
    private Exception exception;
}
