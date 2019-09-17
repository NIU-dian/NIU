package com.yskj.crop.domain.xueqiu;

import java.util.List;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/6/26 9:57
 * @Version 1.0.0
 */
public class QuoteInfoResult {

    private Market market;
    private Quote quote;
    private Others others;
    private List<Tags> tags;

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
