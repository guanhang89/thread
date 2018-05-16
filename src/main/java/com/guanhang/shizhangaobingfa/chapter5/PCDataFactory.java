package com.guanhang.shizhangaobingfa.chapter5;

import com.lmax.disruptor.EventFactory;

public class PCDataFactory implements EventFactory<PCData> {

    @Override
    public PCData newInstance() {
        return new PCData();
    }
}
