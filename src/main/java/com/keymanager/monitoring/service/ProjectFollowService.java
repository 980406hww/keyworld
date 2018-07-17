package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ProjectFollowDao;
import com.keymanager.monitoring.entity.ProjectFollow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectFollowService extends ServiceImpl<ProjectFollowDao, ProjectFollow> {
	private static Logger logger = LoggerFactory.getLogger(ProjectFollowService.class);

	@Autowired
	private ProjectFollowDao projectFollowDao;

	@Autowired
	private CustomerChargeRuleService customerChargeRuleService;

	public List<ProjectFollow> findProjectFollows(Integer customerUuid) {
		return projectFollowDao.findProjectFollows(customerUuid);
	}

	public void deleteProjectFollow(Long uuid) {
		projectFollowDao.deleteById(uuid);
	}

	public void saveProjectFollow(ProjectFollow projectFollow) {
		projectFollow.setCreateTime(new Date());
		projectFollowDao.insert(projectFollow);
	}

	public void deleteProjectFollows(Integer customerUuid) {
		projectFollowDao.deleteProjectFollows(customerUuid);
	}
}
