package com.beemobi.rongthanonline.entity.monster.pet;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.entity.monster.MonsterTypeMove;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.MessagePetInfoName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Pet extends Monster {
    public static final int MAX_LEVEL = 16;
    private static final Logger logger = Logger.getLogger(Pet.class);
    public Player master;

    public List<ItemOption> options;

    public transient int stamina;


    public Pet(Player master, Item item) {
        super();
        this.id = -master.id;
        this.master = master;
        isAutoRefresh = false;
        template = MonsterManager.getInstance().monsterTemplates.get(item.template.mount - 1 + item.getParam(67));
        this.hp = this.maxHp = 1000;
        this.x = master.x;
        this.y = master.y;
        damage = 1000;
        options = new ArrayList<>();
        for (ItemOption itemOption : item.options) {
            options.add(new ItemOption(itemOption.template.id, itemOption.param));
            if (itemOption.template.id == 126) {
                this.hp = this.maxHp = Utils.percentOf(master.maxHp, itemOption.param);
            } else if (itemOption.template.id == 127) {
                this.damage = Utils.percentOf(master.damage, itemOption.param);
            }
        }
    }

    public int getMaxExp() {
        return 500 + 50 * getUpgrade();
    }

    public ItemOption getOption(int id) {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == id) {
                return itemOption;
            }
        }
        return null;
    }

    public void setItemOption(int id, int param) {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == id) {
                itemOption.param = param;
                return;
            }
        }
        options.add(new ItemOption(id, param));
    }

    public int getUpgrade() {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == 19) {
                return itemOption.param;
            }
        }
        return 0;
    }

    public int getExp() {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == 131) {
                return itemOption.param;
            }
        }
        return 0;
    }

    public void setItem(Item item) {
        options.clear();
        for (ItemOption itemOption : item.options) {
            options.add(new ItemOption(itemOption.template.id, itemOption.param));
            if (itemOption.template.id == 126) {
                this.hp = this.maxHp = Utils.percentOf(master.maxHp, itemOption.param);
            } else if (itemOption.template.id == 127) {
                this.damage = Utils.percentOf(master.damage, itemOption.param);
            }
        }
    }

    public int getMaxStamina() {
        return 100;
    }

    public void refreshInfo() {
        for (ItemOption itemOption : this.options) {
            if (itemOption.template.id == 126) {
                this.hp = this.maxHp = Utils.percentOf(master.maxHp, itemOption.param);
            } else if (itemOption.template.id == 127) {
                this.damage = Utils.percentOf(master.damage, itemOption.param);
            }
        }
        master.service.petInfo(MessagePetInfoName.HP_DAMAGE);
        if (zone != null && stamina > 0 && !isDead()) {
            zone.service.refreshHp(this);
        }
    }

    @Override
    public void updateEveryFiveSeconds(long now) {
        super.updateEveryFiveSeconds(now);
        move();
    }

    @Override
    public void updateEveryOneMinutes(long now) {
        super.updateEveryOneMinutes(now);
        if (stamina > 0 && !isDead()) {
            stamina--;
            master.service.petInfo(MessagePetInfoName.STAMINA);
            if (stamina == 0) {
                master.service.setInfo();
                if (zone != null) {
                    zone.leave(this);
                }
            }
        }
    }

    public void move() {
        if (isDead() || zone == null) {
            return;
        }
        followMaster();
    }

    public void followMaster() {
        this.x = master.x + (75 + Utils.nextInt(25)) * (Utils.nextInt(2) == 0 ? 1 : -1);
        if (template.typeMove == MonsterTypeMove.FLY) {
            this.y = master.y - Utils.nextInt(50, 150);
        } else {
            this.y = master.y;
        }
        if (zone != null) {
            zone.service.move(this);
        }
    }

    @Override
    public boolean isCanAttack() {
        return false;
    }

    @Override
    public void updateAttack() {

    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        if (stamina > 0) {
            stamina = 0;
            master.service.petInfo(MessagePetInfoName.STAMINA);
            master.service.setInfo();
        }
        if (zone != null) {
            zone.leave(this);
        }
    }

    public void attack(Entity target) {
        try {
            long damage = this.damage;
            if (target.isMonster()) {
                Monster monster = (Monster) target;
                if (monster.template.id == 107 || monster.template.id == 108) {
                    damage = target.maxHp / 20;
                }
            }
            damage = target.formatDamageInjure(this, damage, false);
            target.injure(master, damage, false, false);
        } catch (Exception ex) {
            logger.error("attack", ex);
        }
    }

    @Override
    public boolean isPet() {
        return true;
    }
}
