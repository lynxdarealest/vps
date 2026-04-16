package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.skill.IntrinsicTemplate;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Intrinsic {
    public IntrinsicTemplate template;
    public int param;

    public Intrinsic(IntrinsicTemplate template, int param) {
        this.template = template;
        this.param = Math.min(param, template.max);
    }

    public String[] getInfo() {
        return new String[]{
                template.name,
                String.format("Tối thiểu %d%%, tối đa %d%%", template.min, template.max)
        };
    }

    public String getName() {
        return template.name.replace("#", param + "");
    }

    public int nextParam(int type) {
        RandomCollection<Integer> params = new RandomCollection<>();
        if (type == 1 || (type == 0 && Utils.nextInt(4) == 0)) {
            int percent = 1;
            for (int i = template.max; i >= template.min; i--) {
                params.add(percent, i);
                percent += 10;
            }
        } else {
            int percent = 1;
            int max = template.max - template.max / 3;
            int min = template.min;
            if (min >= max) {
                return min;
            }
            for (int i = max; i >= template.min; i--) {
                params.add(percent, i);
                percent += 10;
            }
        }
        return params.next();
    }

}
