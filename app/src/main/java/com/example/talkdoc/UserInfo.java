package com.example.talkdoc;

public class UserInfo
{
    private static UserInfo instance;
    private String name;
    private String authority;
    private String patientName = null;

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAuthority(String relationship)
    {
        this.authority = relationship;
    }

    public void setPatientName(String name)
    {
        this.patientName = name;
    }

    public String getName()
    {
        return name;
    }

    public String getAuthority()
    {
        return authority;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public static UserInfo getInstance()
    {
        // 인스턴스가 없는 경우에만 인스턴스를 생성합니다.
        if (instance == null) {
            instance = new UserInfo();
        }

        return instance;
    }
}
