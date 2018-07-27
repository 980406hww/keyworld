package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.ClientStatus;

import java.util.Set;

public class ClientStatusBatchUpdateCriteria {
    private ClientStatus cs;
    private ClientStatus clientStatus;

    public ClientStatus getCs() {
        return cs;
    }

    public void setCs(ClientStatus cs) {
        this.cs = cs;
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(ClientStatus clientStatus) {
        this.clientStatus = clientStatus;
    }
}
