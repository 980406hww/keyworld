package com.keymanager.ckadmin.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.annotation.QzStatusMon;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.QZChargeLog;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.service.QzChargeMonService;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QzStatusLogAspect {

    @Resource(name = "qzChargeMonService2")
    QzChargeMonService qzChargeMonService;

    @Resource(name = "qzSettingService2")
    QZSettingService qzSettingService;

    @Pointcut("@annotation(com.keymanager.ckadmin.annotation.QzStatusMon)")
    public void controllerAspect() {}

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
//        System.out.println(joinPoint);
//        Object[] objects = joinPoint.getArgs();
//        for (Object obj : objects) {
//            System.out.println(obj);
//        }
//        System.out.println(joinPoint.getTarget());
//        System.out.println(joinPoint.getKind());
//        System.out.println(joinPoint.getSignature());
//        System.out.println(joinPoint.getSourceLocation());
//        System.out.println(joinPoint.getStaticPart());
//        System.out.println(joinPoint.getThis());
    }

    /**
     * 切点 监控整站收费状态
     *
     * @param joinPoint 信息
     * @param value 返回对象
     */
    @AfterReturning(value = "controllerAspect()", returning = "value")
    public void doAfter(JoinPoint joinPoint, Object value) {
        if (value instanceof ResultBean) {
            ResultBean resultBean = (ResultBean) value;
            if (null != resultBean.getCode() && resultBean.getCode() == 200) { //是否执行成功
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                QzStatusMon qzStatusMon = method.getAnnotation(QzStatusMon.class);
                int type = qzStatusMon.type();
                Object[] obj = joinPoint.getArgs();
                switch (type) {
                    case 1: //添加
                        if (obj[0] instanceof QZSetting) {
                            updQzStatusMonAdd((QZSetting) obj[0]);
                        }
                        break;
                    case 2: //收费
                        if (obj[0] instanceof Map) {
                            Map data = (Map) obj[0];
                            updQzStatusMonMid(data);
                        }
                        break;
                    case 3: //删除
                        if (obj[0] instanceof Long) {
                            updQzStatusMonLow((Long) obj[0]);
                        }
                        break;
                    case 4: //批量删除
                        if (obj[0] instanceof Map) {
                            if (((Map) obj[0]).get("uuids") instanceof List) {
                                updQzStatusMonLows((List) ((Map) obj[0]).get("uuids"));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 添加整站时候的首次收费
     *
     * @param qzSetting 添加的实体对象
     */
    private void updQzStatusMonAdd(QZSetting qzSetting) {
        if (qzSetting.getRenewalStatus() == 4) { //判断整站状态是否为其他
            return;
        }
        QzChargeMon qzChargeMon = new QzChargeMon();
        qzChargeMon.setOperationType(2);
        qzChargeMon.setOperationObj(qzSetting.getUuid().toString());
        qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
        qzChargeMonService.insert(qzChargeMon);
    }

    /**
     * 保存收费方法
     *
     * @param data 收费数据
     */
    private void updQzStatusMonMid(Map data) {
        Map msg = (Map) data.get("msg");
        Long uuid = Long.parseLong((String) msg.get("uuid"));
        switch (Integer.parseInt((String) msg.get("renewalStatus"))) {
            case 0: //暂停收费
                updQzStatusMonStay(uuid);
                break;
            case 1: //续费
                List<QZChargeLog> qzChargeLogs = new ObjectMapper()
                    .convertValue(data.get("data"), new TypeReference<List<QZChargeLog>>() {
                    });
                updQzStatusMonKeep(uuid, qzChargeLogs);
                break;
            case 3: //下架
                updQzStatusMonLow(uuid);
                break;
            default:
                break;
        }
    }

    /**
     * 暂停收费
     *
     * @param uuid 整站id
     */
    private void updQzStatusMonStay(Long uuid) {
        saveQzChargeMon(uuid, 0);
    }

    /**
     * 续费
     *
     * @param uuid 整站id
     * @param qzChargeLogs 收费信息
     */
    private void updQzStatusMonKeep(Long uuid, List<QZChargeLog> qzChargeLogs) {
        if (null == qzChargeLogs || qzChargeLogs.isEmpty()) {
            return;
        }
        QZSetting qzSetting = qzSettingService.selectById(uuid);
        if (qzSetting.getRenewalStatus() == 4) {
            return;
        }
        QzChargeMon qzChargeMon = new QzChargeMon();
        qzChargeMon.setOperationType(1);
        qzChargeMon.setOperationObj(uuid.toString());
        qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
        StringBuilder str = new StringBuilder();
        for (QZChargeLog qzChargeLog : qzChargeLogs) {
            str.append(qzChargeLog.getActualAmount());
            str.append(",");
        }
        qzChargeMon.setOperationAmount(str.substring(0, str.length() - 1));
        qzChargeMonService.insert(qzChargeMon);
    }

    /**
     * 单个下架
     *
     * @param uuid 整站id
     */
    private void updQzStatusMonLow(Long uuid) {
        saveQzChargeMon(uuid, 3);
    }

    /**
     * 批量下架
     *
     * @param uuids 整站ids
     */
    private void updQzStatusMonLows(List uuids) {
        if (null == uuids || uuids.isEmpty()) {
            return;
        }
        List<QZSetting> qzSettings = qzSettingService.selectByUuids(uuids);
        StringBuilder objStr = new StringBuilder();
        Set<String> ses = new HashSet<>();
        for (QZSetting qzSetting : qzSettings) {
            if (qzSetting.getRenewalStatus() == 4) {
                continue;
            }
            objStr.append(qzSetting.getUuid());
            objStr.append(",");
            ses.add(qzSetting.getSearchEngine());
        }
        if (ses.isEmpty()) {
            return;
        }
        StringBuilder amountStr = new StringBuilder();
        for (String se : ses) {
            amountStr.append(se);
            amountStr.append(",");
        }
        QzChargeMon qzChargeMon = new QzChargeMon();
        qzChargeMon.setOperationType(3);
        qzChargeMon.setOperationObj(objStr.substring(0, objStr.length() - 1));
        qzChargeMon.setSearchEngine(amountStr.substring(0, amountStr.length() - 1));
        qzChargeMonService.insert(qzChargeMon);
    }

    private void saveQzChargeMon(Long uuid, Integer operationType) {
        QZSetting qzSetting = qzSettingService.selectById(uuid);
        if (qzSetting.getRenewalStatus() == 4) {
            return;
        }
        QzChargeMon qzChargeMon = new QzChargeMon();
        qzChargeMon.setOperationType(operationType);
        qzChargeMon.setOperationObj(uuid.toString());
        qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
        qzChargeMonService.insert(qzChargeMon);
    }
}
