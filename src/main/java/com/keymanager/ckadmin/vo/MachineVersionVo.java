package com.keymanager.ckadmin.vo;
/**
 * 机器版本信息vo类
 */
public class MachineVersionVo implements Comparable {
    private String version;

    private String terminal;

    private String programType;

    private int count;

    private int  versionCount;
    private int versionTotalCount;

    private int terminalCount;
    private int terminalTotalCount;

    private int programTypeCount;
    private int programTypeTotalCount;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getVersionCount() {
        return versionCount;
    }

    public void setVersionCount(int versionCount) {
        this.versionCount = versionCount;
    }

    public int getVersionTotalCount() {
        return versionTotalCount;
    }

    public void setVersionTotalCount(int versionTotalCount) {
        this.versionTotalCount = versionTotalCount;
    }

    public int getTerminalCount() {
        return terminalCount;
    }

    public void setTerminalCount(int terminalCount) {
        this.terminalCount = terminalCount;
    }

    public int getTerminalTotalCount() {
        return terminalTotalCount;
    }

    public void setTerminalTotalCount(int terminalTotalCount) {
        this.terminalTotalCount = terminalTotalCount;
    }

    public int getProgramTypeCount() {
        return programTypeCount;
    }

    public void setProgramTypeCount(int programTypeCount) {
        this.programTypeCount = programTypeCount;
    }

    public int getProgramTypeTotalCount() {
        return programTypeTotalCount;
    }

    public void setProgramTypeTotalCount(int programTypeTotalCount) {
        this.programTypeTotalCount = programTypeTotalCount;
    }

    @Override
    public int compareTo(Object o) {
        int result =0;
        if(this.getVersion() != null && ((MachineVersionVo) o).getVersion() !=null)
          result = this.getVersion().compareTo(((MachineVersionVo) o).getVersion());
        else
            result=-1;
        if (result == 0) {
            if(this.getTerminal() != null && ((MachineVersionVo) o).getTerminal() != null)
                result = this.getTerminal().compareTo(((MachineVersionVo) o).getTerminal());
            else
                result=-1;
            if (result == 0) {
                if (this.getProgramType() != null && ((MachineVersionVo) o).getProgramType() != null) {
                    result = this.getProgramType().compareTo(((MachineVersionVo) o).getProgramType());
                } else {
                    result = -1;
                }
            }
        }
        return result;
    }
}
