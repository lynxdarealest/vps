package com.beemobi.rongthanonline.entity.monster.big;

import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TuyetLinh extends BigMonster {

    public static final int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
            MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
            MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU};

    public TuyetLinh() {
        super(105);
        hp = maxHp = baseHp = template.hp;
        percentDamage = 20;
        isHasPoint = false;
    }

    @Override
    public long delayAttack() {
        return 2000;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.max(Math.min(damage, hp / 100), 1);
    }

    @Override
    public void updateEveryTenSeconds(long now) {
        super.updateEveryTenSeconds(now);
        List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
        if (!playerList.isEmpty()) {
            for (Player player : playerList) {
                if (!player.isDead()) {
                    player.addEffect(new Effect(player, EffectName.DONG_BANG, 2000));
                }
            }
        }
    }

    @Override
    public void throwItemWhenLeaveMap(Entity killer) {
        if (killer == null || (!killer.isPlayer() && !killer.isPet() && !killer.isDisciple())) {
            return;
        }
        List<ItemMap> itemMaps = new ArrayList<>();
        int length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(20000, 50000), -1));
        }
        length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOI_SAO_VANG, 1, -1));
        }
        length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.QUA_CAU_DO, 1, -1));
        }
        length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGUOI_TUYET, 1, -1));
        }
        length = Utils.nextInt(3, 5);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        }
        length = Utils.nextInt(3, 5);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.THE_XU, 1, -1));
        }
        if (!itemMaps.isEmpty()) {
            zone.addItemMap(itemMaps, this.x);
        }
    }
}
