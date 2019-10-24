package com.company;

import java.util.LinkedList;
import java.util.List;

public class ShiedStore {

    List<Shield> shieldList = new LinkedList<>();

    public ShiedStore(List<Shield> shieldList) {
        this.shieldList = shieldList;
    }

    public List<Shield> getShieldList() {
        return shieldList;
    }
}
