package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.email.NegativeStandardSettingMailService;
import com.keymanager.monitoring.criteria.NegativeStandardSettingCriteria;
import com.keymanager.monitoring.dao.NegativeStandardSettingDao;
import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.monitoring.entity.NegativeStandardSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by ljj on 2018/6/28.
 */

@Service
public class NegativeStandardSettingService extends ServiceImpl<NegativeStandardSettingDao, NegativeStandardSetting> {

    @Autowired
    private NegativeStandardSettingDao negativeStandardSettingDao;

    @Autowired
    private NegativeRankService negativeRankService;

    @Autowired
    private NegativeStandardSettingMailService negativeStandardSettingMailService;

    public Page<NegativeStandardSetting> searchNegaStandardSetting( Page<NegativeStandardSetting> page,NegativeStandardSettingCriteria negativeStandardSettingCriteria){
        page.setRecords(negativeStandardSettingDao.searchNegativeStandardSetting(page,negativeStandardSettingCriteria));
       return page;
    }

    public void deleteNegativeStandardSettings(List<String> uuids){
            negativeStandardSettingDao.deleteBatchIds(uuids);
    }

    public void deleteNegativeStandardSetting(Long uuid){
        negativeStandardSettingDao.deleteById(uuid);
    }

    public void saveNegativeStandardSetting(NegativeStandardSetting negativeStandardSetting){
        negativeStandardSetting.setUpdateTime(new Date());
        if(negativeStandardSetting.getUuid()==null){
            negativeStandardSetting.setReachStandard(false);
            negativeStandardSetting.setCreateTime(new Date());
            negativeStandardSettingDao.insert(negativeStandardSetting);
        }else{
            negativeStandardSettingDao.updateById(negativeStandardSetting);
        }
    }

    public int findNegativeStandardSetting(Long customerUuid,String keyword,String searchEngine){
        return negativeStandardSettingDao.findNegativeStandardSetting(customerUuid,keyword,searchEngine);
    }

    public Page<NegativeStandardSetting> allNegativeStandardSetting(Page<NegativeStandardSetting> page,NegativeStandardSettingCriteria negativeStandardSettingCriteria){
        page.setRecords(negativeStandardSettingDao.allNegativeStandardSetting(page,negativeStandardSettingCriteria));
        return page;
    }

    public void negativeStandardSettingCountSchedule() throws Exception {
        List<NegativeRank> negativeRanks = negativeRankService.getTodayNegativeRanks();
        if(negativeRanks.size()>0){
            Map<String,Object> negativeRankMap = new HashMap<String, Object>();
            for(NegativeRank negativeRank : negativeRanks){
                negativeRankMap.put(negativeRank.getKeyword()+"_"+negativeRank.getSearchEngine(),negativeRank);
            }
            List<Map> userInfos = negativeStandardSettingDao.findUsers();
            for(Map<String,Object> userInfo : userInfos){
                List<NegativeStandardSetting> negativeStandardSettingList = new ArrayList<NegativeStandardSetting>();
                List<NegativeRank> negativeRankList = new ArrayList<NegativeRank>();
                List<NegativeStandardSetting> negativeStandardSettings = negativeStandardSettingDao.getNegativeStandardSetting((String) userInfo.get("loginName"));
                for(NegativeStandardSetting negativeStandardSetting : negativeStandardSettings){
                    NegativeRank negativeRank = (NegativeRank) negativeRankMap.get(negativeStandardSetting.getKeyword()+"_"+negativeStandardSetting.getSearchEngine());
                    if(negativeRank!=null){
                        Integer[] negativeStandardSettingData = new Integer[]{
                                negativeStandardSetting.getTopOnePageNegativeCount(),
                                negativeStandardSetting.getTopTwoPageNegativeCount(),
                                negativeStandardSetting.getTopThreePageNegativeCount(),
                                negativeStandardSetting.getTopFourPageNegativeCount(),
                                negativeStandardSetting.getTopFivePageNegativeCount()
                        };
                        String[] negativeRankData = new String[]{
                                negativeRank.getFirstPageRanks(),
                                negativeRank.getSecondPageRanks(),
                                negativeRank.getThirdPageRanks(),
                                negativeRank.getFourthPageRanks(),
                                negativeRank.getFifthPageRanks()
                        };
                        int negativeRankCount = 0;
                        boolean standard = true;
                        if(negativeStandardSettingData.length==negativeRankData.length){
                            for (int i=0;i<negativeRankData.length;i++){
                                if(!negativeRankData[i].equals("")){
                                    negativeRankCount = negativeRankCount+negativeRankData[i].split(",").length;
                                    if(negativeRankCount>negativeStandardSettingData[i]){
                                        standard = false;
                                        break;
                                    }
                                }
                            }
                            if (standard&&negativeStandardSetting.getReachStandard() == false) {
                                negativeStandardSetting.setStandardTime(new Date());
                                negativeStandardSetting.setReachStandard(true);
                                negativeStandardSettingDao.updateById(negativeStandardSetting);
                                negativeStandardSettingList.add(negativeStandardSetting);
                                negativeRankList.add(negativeRank);
                            }
                            if (!standard&&negativeStandardSetting.getReachStandard() == true) {
                                negativeStandardSetting.setReachStandard(false);
                                negativeStandardSettingDao.updateById(negativeStandardSetting);
                                negativeStandardSettingList.add(negativeStandardSetting);
                                negativeRankList.add(negativeRank);
                            }
                        }
                    }
                }
                if(negativeStandardSettingList.size()==negativeRankList.size()&&negativeRankList.size()>0){
                    negativeStandardSettingMailService.sendNegativeStandardSettingMail((String) userInfo.get("email"), negativeStandardSettingList, negativeRankList,(String) userInfo.get("userName"));
                }
            }
        }
    }

    public  List<String>  findContactPersons(NegativeStandardSettingCriteria negativeStandardSettingCriteria){
        return  negativeStandardSettingDao.findContactPersons(negativeStandardSettingCriteria);
    }
}
