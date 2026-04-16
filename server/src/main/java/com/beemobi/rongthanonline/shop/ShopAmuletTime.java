package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopAmuletTime extends Shop {
    private static final Logger logger = Logger.getLogger(ShopAmuletTime.class);

    private final Object[][][] items = new Object[][][]{
            {
                    {ItemName.BUA_TRI_TUE, 1, TypePrice.RUBY, 5},
                    {ItemName.BUA_MANH_ME, 1, TypePrice.RUBY, 5},
                    {ItemName.BUA_CUNG_CAP, 1, TypePrice.RUBY, 3},
                    {ItemName.BUA_THU_HUT, 1, TypePrice.RUBY, 2},
                    {ItemName.BUA_MAY_MAN, 1, TypePrice.RUBY, 5},
                    {ItemName.BUA_NGUNG_DONG, 1, TypePrice.RUBY, 1},
                    {ItemName.BUA_DE_TU, 1, TypePrice.RUBY, 10},
                    {ItemName.BUA_SIEU_TRI_TUE, 1, TypePrice.DIAMOND, 10},
                    {ItemName.BUA_SIEU_CAP_TRI_TUE, 1, TypePrice.DIAMOND, 15},
                    {ItemName.BUA_SIEU_DE_TU, 1, TypePrice.DIAMOND, 10},
                    {ItemName.BUA_SIEU_CAP_DE_TU, 1, TypePrice.DIAMOND, 15},
            }
    };

    public ShopAmuletTime(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        String[] tabs = new String[]{"Bùa 1 giờ", "Bùa 8 giờ", "Bùa 1 tháng"};
        for (String tab : tabs) {
            itemShops.put(tab, new ArrayList<>());
        }
        List<Effect> effectList = player.getEffects();
        HashMap<Integer, Integer> effects = new HashMap<>();
        for (Effect effect : effectList) {
            effects.put(effect.template.id, (int) (effect.time / 60000));
        }
        for (int i = 0; i < tabs.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                ItemShop itemShop = new ItemShop((int) items[0][j][0], (int) items[0][j][1], (TypePrice) items[0][j][2], (int) items[0][j][3]);
                int mul = 1;
                if (i == 1) {
                    itemShop.price *= 4;
                    mul = 8;
                } else if (i == 2) {
                    itemShop.price *= 100;
                    mul = 720;
                }
                if (mul > 1) {
                    for (ItemOption option : itemShop.options) {
                        if (option.template.id == 23) {
                            option.param *= mul;
                            break;
                        }
                    }
                }
                itemShop.options.add(new ItemOption(24, effects.getOrDefault(itemShop.getEffectTemplateId(), 0)));
                itemShops.get(tabs[i]).add(itemShop);
            }
        }
        return itemShops;
    }
}
