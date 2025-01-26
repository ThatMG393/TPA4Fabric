package com.thatmg393.tpa4fabric.config.data;

public class ModConfigData {
    public int tpaCooldown = 5; // in seconds
    public int tpaExpireTime = 120; // in seconds;
    public int tpaTeleportTime = 5; // in seconds;

    public int tpaRequestLimit = 99;

    public boolean defaultAllowTPARequests = true;
    public boolean oneTimeTPABack = true;

    public int configVersion = 3; // internal value
}
