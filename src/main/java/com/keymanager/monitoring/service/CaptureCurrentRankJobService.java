package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CaptureCurrentRankJobDao;
import com.keymanager.monitoring.entity.CaptureCurrentRankJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@Service
public class CaptureCurrentRankJobService  extends ServiceImpl<CaptureCurrentRankJobDao, CaptureCurrentRankJob> {
    @Autowired
    private CaptureCurrentRankJobDao captureCurrentRankJobDao;

    public  List<CaptureCurrentRankJob> searchCaptureCurrentRankJobs()
    {
        List<CaptureCurrentRankJob> captureCurrentRankJobs = captureCurrentRankJobDao.searchCaptureCurrentRankJobs();
        return captureCurrentRankJobs;
    }
}
