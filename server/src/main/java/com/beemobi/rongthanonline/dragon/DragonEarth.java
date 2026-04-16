package com.beemobi.rongthanonline.dragon;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
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
import com.beemobi.rongthanonline.skill.SkillTemplate;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class DragonEarth extends Dragon {
    public static final int DELAY = 300000;
    public static final String[] WISH = new String[]{"Giàu có\n+150\nRuby", "Giàu có\n+300tr\nXu", "Sức mạnh\n+1 tỉ", "Đệ tử\n+150 triệu\nsức mạnh", "Thay\nchiêu 1-2\nĐệ tử", "Thay\nchiêu 2-3\nĐệ tử", "Thay\nchiêu 3-4\nĐệ tử"};
    public long timeSummon;

    public DragonEarth() {
        icons = new int[]{1202, 1203, 1204, 1205};
        items = new int[]{86, 85, 84, 83, 82, 81, 80};
        x = 3100;
        y = 1008;
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
        if (Server.getInstance().dragonTet2024.isActive) {
            player.addInfo(Player.INFO_RED, "Không thể thực hiện khi đang có người gọi rồng");
            return;
        }
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.SUMMON_DRAGON, "Triệu hồi", player, this));
        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
        player.createMenu(NpcName.ME, "Rồng thần Trái đất sẽ bạn cho bạn 1 điều ước", commands);
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
                        case ItemName.NGOC_RONG_1_SAO:
                            items[0] = i;
                            break;
                        case ItemName.NGOC_RONG_2_SAO:
                            items[1] = i;
                            break;
                        case ItemName.NGOC_RONG_3_SAO:
                            items[2] = i;
                            break;
                        case ItemName.NGOC_RONG_4_SAO:
                            items[3] = i;
                            break;
                        case ItemName.NGOC_RONG_5_SAO:
                            items[4] = i;
                            break;
                        case ItemName.NGOC_RONG_6_SAO:
                            items[5] = i;
                            break;
                        case ItemName.NGOC_RONG_7_SAO:
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
            Server.getInstance().service.serverChat(String.format("%s vừa gọi rồng tại %s khu vực %d", player.name, zone.map.template.name, zone.id));
            showWish(player);
            Utils.run(() -> {
                try {
                    int num = 180;
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
            List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER);
            for (Player p : playerList) {
                if (p.taskMain != null && p.taskMain.template.id == 19 && p.taskMain.index == 4) {
                    p.nextTaskIndex();
                }
            }
            player.upPointAchievement(23, 1);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public void showWish(Player player) {
        List<Command> commands = new ArrayList<>();
        Disciple disciple = player.disciple;
        int length = 0;
        if (disciple != null) {
            length = disciple.skills.size();
        }
        for (int i = 0; i < WISH.length; i++) {
            if (i < 3 || (i == 3 && length >= 1) || (i == 4 && length >= 2) || (i == 5 && length >= 3) || (i == 6 && length >= 4)) {
                commands.add(new Command(CommandName.WISH_DRAGON, WISH[i], player, this, i));
            }
        }
        player.createMenu(NpcName.ME, "Ta sẽ ban cho ngươi 1 điều ước, ngươi có 3 phút, hãy suy nghĩ thật kỹ trước khi quyết định", commands);
    }

    @Override
    public void wish(Player player, int index) {
        Command yes = new Command(CommandName.CONFIRM_WISH_DRAGON, "Đồng ý", player, this, index);
        Command no = new Command(CommandName.CALL_DRAGON, "Không", player, this);
        player.startYesNo(String.format("Bạn có chắc chắn muốn ước %s không?", WISH[index].replace("\n", " ")), yes, no);
    }

    @Override
    public void confirm(Player player, int index) {
        if (System.currentTimeMillis() - timeSummon > 180000) {
            player.addInfo(Player.INFO_RED, "Đã hết thời gian chọn điều ước");
            close();
            return;
        }
        switch (index) {
            case 0:
                player.upRuby(150);
                player.addInfo(Player.INFO_YELLOW, "Bạn nhận được 150 Ruby");
                break;
            case 1:
                player.upXu(300000000);
                player.addInfo(Player.INFO_YELLOW, "Bạn nhận được 300tr xu");
                break;
            case 2:
                player.upPower(1000000000);
                break;

            case 3: {
                Disciple disciple = player.disciple;
                if (disciple == null) {
                    player.addInfo(Player.INFO_RED, "Không tìm thấy đệ tử");
                    showWish(player);
                    return;
                }
                disciple.power += 150000000L;
                disciple.potential += 150000000L;
                player.service.discipleInfo(MessageDiscipleInfoName.POWER_INFO);
                player.addInfo(Player.INFO_YELLOW, "Điều ước đã trở thành hiện thực");
                break;
            }

            case 4: {
                Disciple disciple = player.disciple;
                if (disciple == null || disciple.skills.size() < 2) {
                    player.addInfo(Player.INFO_RED, "Không tìm thấy đệ tử hoặc kĩ năng cần thay đổi");
                    showWish(player);
                    return;
                }
                Skill skill1 = new Skill();
                skill1.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_1.next());
                skill1.level = 1;
                skill1.upgrade = 0;
                skill1.point = 0;
                disciple.skills.set(0, skill1);
                Skill skill2 = new Skill();
                skill2.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_2.next());
                skill2.level = 1;
                skill2.upgrade = 0;
                skill2.point = 0;
                disciple.skills.set(1, skill2);
                player.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                player.addInfo(Player.INFO_YELLOW, "Điều ước đã trở thành hiện thực");
                break;
            }

            case 5: {
                Disciple disciple = player.disciple;
                if (disciple == null || disciple.skills.size() < 3) {
                    player.addInfo(Player.INFO_RED, "Không tìm thấy đệ tử hoặc kĩ năng cần thay đổi");
                    showWish(player);
                    return;
                }
                Skill skill3 = new Skill();
                skill3.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_3.next());
                skill3.level = 1;
                skill3.upgrade = 0;
                skill3.point = 0;
                disciple.skills.set(2, skill3);
                Skill skill2 = new Skill();
                skill2.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_2.next());
                skill2.level = 1;
                skill2.upgrade = 0;
                skill2.point = 0;
                disciple.skills.set(1, skill2);
                player.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                player.addInfo(Player.INFO_YELLOW, "Điều ước đã trở thành hiện thực");
                break;
            }

            case 6: {
                Disciple disciple = player.disciple;
                if (disciple == null || disciple.skills.size() < 4) {
                    player.addInfo(Player.INFO_RED, "Không tìm thấy đệ tử hoặc kĩ năng cần thay đổi");
                    showWish(player);
                    return;
                }
                Skill skill3 = new Skill();
                skill3.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_3.next());
                skill3.level = 1;
                skill3.upgrade = 0;
                skill3.point = 0;
                disciple.skills.set(2, skill3);
                Skill skill4 = new Skill();
                skill4.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_4.next());
                skill4.level = 1;
                skill4.upgrade = 0;
                skill4.point = 0;
                disciple.skills.set(3, skill4);
                player.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                player.addInfo(Player.INFO_YELLOW, "Điều ước đã trở thành hiện thực");
                break;
            }

        }
        close();
    }

    @Override
    public void close() {
        timeSummon = System.currentTimeMillis();
        isActive = false;
        masterId = -1;
        Server.getInstance().service.hideDragon(this);
    }

    public boolean isSame(int newId, int oldId) {
        if (oldId == 36 || oldId == 37) {
            return newId == 36 || newId == 37;
        }
        if (oldId == 38 || oldId == 39) {
            return newId == 38 || newId == 39;
        }
        if (oldId == 40 || oldId == 41) {
            return newId == 40 || newId == 41;
        }
        if (oldId == 42 || oldId == 45) {
            return newId == 42 || newId == 45;
        }
        if (oldId == 44 || oldId == 46) {
            return newId == 44 || newId == 46;
        }
        if (oldId == 43 || oldId == 47) {
            return newId == 43 || newId == 47;
        }
        if (oldId == 6 || oldId == 7) {
            return newId == 6 || newId == 7;
        }
        if (oldId == 8 || oldId == 9) {
            return newId == 8 || newId == 9;
        }
        if (oldId == 10 || oldId == 11) {
            return newId == 10 || newId == 11;
        }
        if (oldId == 18 || oldId == 22) {
            return newId == 18 || newId == 22;
        }
        if (oldId == 19 || oldId == 23) {
            return newId == 19 || newId == 23;
        }
        if (oldId == 21) {
            return newId == 21;
        }
        return false;
    }

    public SkillTemplate getSkillTemplate(int[] skill, int current) {
        int id = skill[Utils.nextInt(skill.length)];
        int num = 0;
        while (num < 1000 && isSame(id, current)) {
            id = skill[Utils.nextInt(skill.length)];
            num++;
        }
        return SkillManager.getInstance().skillTemplates.get(id);
    }
}
