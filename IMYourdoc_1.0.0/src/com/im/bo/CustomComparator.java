package com.im.bo;

import java.util.Comparator;

public class CustomComparator implements Comparator<RosterVCardBo> {
    @Override
    public int compare(RosterVCardBo o1, RosterVCardBo o2) {
        return o1.getName().compareTo(o2.getName());
    }
}