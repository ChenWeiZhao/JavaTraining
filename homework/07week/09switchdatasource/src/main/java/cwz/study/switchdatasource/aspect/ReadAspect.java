/*
 * <p>文件名称: ReadAspect</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/21 22:09 </p>
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
package cwz.study.switchdatasource.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
public class ReadAspect {

    @Pointcut("@annotation(cwz.study.switchdatasource.annotation.SwitchReadSlave)")
    public void read() {

    }

    /**
     * 将查询改为读节点
     */
    @Around("read()")
    public List<Map<String, Object>> switchDataSource(ProceedingJoinPoint point) throws Throwable {
        System.out.println("data source change");
        Object[] args = point.getArgs();
        //args[0] =
        return (List<Map<String, Object>>) point.proceed(args);
    }


}
