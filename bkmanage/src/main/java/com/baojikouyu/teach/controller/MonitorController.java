package com.baojikouyu.teach.controller;

import cn.hutool.core.date.DateUtil;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.impl.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MonitorController {

    @Autowired
    private MonitorService monitorService;


    @GetMapping("monitor")
    public ResponseBean moniter() {
        Map<String, Object> resultMap = new LinkedHashMap<>(8);
        SystemInfo info = new SystemInfo();

        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hal = info.getHardware();
        // 系统信息
        resultMap.put("sys", monitorService.getSystemInfo(os));
        /*      List<OSProcess> processes = os.getProcesses();
        resultMap.put("sys_process", processes);*/
        // cpu 信息
        resultMap.put("cpu", monitorService.getCpuInfo(hal.getProcessor()));
        // 内存信息
        resultMap.put("memory", monitorService.getMemoryInfo(hal.getMemory()));
        // 交换区信息
        resultMap.put("swap", monitorService.getSwapInfo(hal.getMemory()));
        // 磁盘
        resultMap.put("disk", monitorService.getDiskInfo(os));
        resultMap.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        return new ResponseBean(200, "成功获取菜单", resultMap);
    }
}
