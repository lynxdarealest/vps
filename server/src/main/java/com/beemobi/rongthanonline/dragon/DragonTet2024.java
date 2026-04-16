package com.beemobi.rongthanonline.dragon;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.network.MessageDiscipleInfoName;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerRandom;
import com.beemobi.rongthanonline.skill.Skill;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class DragonTet2024 extends Dragon {
    public static final int DELAY = 60000;
    public long timeSummon;

    public DragonTet2024() {
        icons = new int[]{6017, 6018, 6019, 6020};
        items = new int[]{6016, 6015, 6014, 6013, 6012, 6011, 6010};
        x = 1050;
        y = 936;
    }

    @Override
    public void showMenu(Player player) {
        if (isActive) {
            if (masterId != player.id) {
                player.addInfo(Player.INFO_RED, "Không thể thực hiện khi đang có người gọi rồng");
                return;
            }
            showWish(player);
            return;
        }
        if (player.pointActive < 50) {
            player.addInfo(Player.INFO_RED, "Điểm năng động từ 50 trở lên mới có thể sử dụng tính năng này");
            return;
        }
        if (player.level < 10) {
            player.addInfo(Player.INFO_RED, "Yêu cầu trình độ cấp 10 trở lên");
            return;
        }
        if (Server.getInstance().dragon.isActive) {
            player.addInfo(Player.INFO_RED, "Không thể thực hiện khi đang có người gọi rồng");
            return;
        }
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.SUMMON_DRAGON, "Triệu hồi", player, this));
        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
        player.createMenu(NpcName.ME, "Rồng thần Nguyên đán sẽ bạn cho bạn 1 điều ước", commands);
    }

    @Override
    public void summonDragon(Player player) {
        player.lockAction.lock();
        try {
            if (isActive) {
                return;
            }
            if (player.pointActive < 50 || player.level < 10 || player.zone == null) {
                return;
            }
            if (player.isTrading()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            long now = System.currentTimeMillis();
            long time = DELAY + timeSummon - now;
            if (time > 0) {
                player.addInfo(Player.INFO_RED, String.format("Vui lòng chờ %s nữa", Utils.formatTime(time)));
                return;
            }
            int[] items = new int[]{-1, -1, -1, -1, -1, -1, -1};
            for (int i = 0; i < player.itemsBag.length; i++) {
                Item item = player.itemsBag[i];
                if (item != null) {
                    switch (item.template.id) {
                        case ItemName.NGOC_RONG_NGUYEN_DAN_1_SAO:
                            items[0] = i;
                            break;
                        case ItemName.NGOC_RONG_NGUYEN_DAN_2_SAO:
                            items[1] = i;
                            break;
                        case ItemName.NGOC_RONG_NGUYEN_DAN_3_SAO:
                            items[2] = i;
                            break;
                        case ItemName.NGOC_RONG_NGUYEN_DAN_4_SAO:
                            items[3] = i;
                            break;
                        case ItemName.NGOC_RONG_NGUYEN_DAN_5_SAO:
                            items[4] = i;
                            break;
                        case ItemName.NGOC_RONG_NGUYEN_DAN_6_SAO:
                            items[5] = i;
                            break;
                        case ItemName.NGOC_RONG_NGUYEN_DAN_7_SAO:
                            items[6] = i;
                            break;
                    }
                }
            }
            for (int index : items) {
                if (index == -1) {
                    player.addInfo(Player.INFO_RED, "Vui lòng thu thập đủ 7 viên Ngọc rồng");
                    return;
                }
            }
            for (int index : items) {
                Item item = player.itemsBag[index];
                if (item != null) {
                    item.quantity--;
                    if (item.quantity <= 0) {
                        player.itemsBag[index] = null;
                    }
                }
            }
            player.service.setItemBag();
            isActive = true;
            masterId = player.id;
            timeSummon = now;
            zone = player.zone;
            zone.service.summonDragon(this);
            showWish(player);
            Utils.run(() -> {
                try {
                    int num = 60;
                    while (isActive && num > 0) {
                        num--;
                        if (num == 0) {
                            close();
                            break;
                        }
                        Thread.sleep(1000);
                    }
                } catch (Exception ignored) {
                }
            });
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public void showWish(Player player) {
        List<Command> commands = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        content.append("Ngươi có 1 phút để chọn 1 trong các điều ước:").append("\n");
        content.append("1) Tăng 100% sức mạnh tiềm năng nhận được khi đánh quái trong 60 phút").append("\n");
        content.append("2) Tăng 100% sức mạnh tiềm năng cho đệ tử khi đánh quái trong 60 phút").append("\n");
        content.append("3) Tăng 30% Sức đánh trong 60 phút").append("\n");
        content.append("4) Tăng 50% HP, KI trong 60 phút").append("\n");
        for (int i = 0; i < 4; i++) {
            commands.add(new Command(CommandName.CONFIRM_WISH_DRAGON, String.valueOf(i + 1), player, this, i));
        }
        player.createMenu(NpcName.ME, content.toString(), commands);
    }

    @Override
    public void wish(Player player, int index) {
        /*Command yes = new Command(CommandName.CONFIRM_WISH_DRAGON, "Đồng ý", player, this, index);
        Command no = new Command(CommandName.CALL_DRAGON, "Không", player, this);
        player.startYesNo(String.format("Bạn có chắc chắn muốn ước %s không?", WISH[index].replace("\n", " ")), yes, no);*/
    }

    @Override
    public void confirm(Player player, int index) {
        if (System.currentTimeMillis() - timeSummon > 60000) {
            player.addInfo(Player.INFO_RED, "Đã hết thời gian chọn điều ước");
            close();
            return;
        }
        switch (index) {
            case 0: {
                player.addTimeEffect(new Effect(player, EffectName.TANG_TNSM_TU_NR_NGUYEN_DAN, 3600000));
                break;
            }

            case 1: {
                player.addTimeEffect(new Effect(player, EffectName.TANG_TNSN_DE_TU_TU_NR_NGUYEN_DAN, 3600000));
                break;
            }

            case 2: {
                player.addTimeEffect(new Effect(player, EffectName.TANG_SUC_DANH_TU_NR_NGUYEN_DAN, 3600000));
                break;
            }

            case 3: {
                player.addTimeEffect(new Effect(player, EffectName.TANG_HP_KI_TU_NR_NGUYEN_DAN, 3600000));
                break;
            }

        }
        player.addInfo(Player.INFO_YELLOW, "Điều ước đã thành hiện thực");
        close();
    }

    @Override
    public void close() {
        timeSummon = System.currentTimeMillis();
        isActive = false;
        masterId = -1;
        zone.service.hideDragon(this);
    }

}
