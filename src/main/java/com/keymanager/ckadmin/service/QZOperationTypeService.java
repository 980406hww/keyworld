package com.keymanager.ckadmin.service;

import java.util.Date;

public interface QZOperationTypeService {

    Date getStandardTime(long qzSettingUuid, String terminalType);
}
