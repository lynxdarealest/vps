package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.Enemy;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EnemyAction extends Action {
    private static final Logger logger = Logger.getLogger(FriendAction.class);

    private static final int SHOW_ENEMY = 0;
    private static final int REMOVE_ENEMY = 2;
    private static final int REVENGE = 1;

    public int targetId;
    public long lastTimeRevenge;

    public EnemyAction(Player player) {
        super(player);
    }

    @Override
    public void action(Message message) {
        try {
            int type = message.reader().readByte();
            switch (type) {
                case SHOW_ENEMY: {
                    player.service.setEnemy(0, -1);
                    return;
                }

                case REMOVE_ENEMY: {
                    int playerId = message.reader().readInt();
                    if (playerId == player.id) {
                        return;
                    }
                    Enemy enemy = player.enemies.stream().filter(e -> e.playerId == playerId).findFirst().orElse(null);
                    if (enemy == null) {
                        return;
                    }
                    player.enemies.remove(enemy);
                    player.addInfo(Player.INFO_YELLOW, String.format("Đã xóa %s khỏi danh sách kẻ thù", enemy.name));
                    player.service.setEnemy(2, playerId);
                    return;
                }

                case REVENGE: {
                    if (player.level < 5) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu đạt cấp 5 trở lên");
                        return;
                    }
                    int playerId = message.reader().readInt();
                    if (playerId == player.id) {
                        return;
                    }
                    Enemy enemy = player.enemies.stream().filter(e -> e.playerId == playerId).findFirst().orElse(null);
                    if (enemy == null) {
                        return;
                    }
                    Player p = PlayerManager.getInstance().findPlayerById(enemy.playerId);
                    if (p == null) {
                        player.addInfo(Player.INFO_RED, String.format("%s đang offline", enemy.name));
                        return;
                    }
                    if (p.zone == null) {
                        player.addInfo(Player.INFO_RED, String.format("Không tìm thấy vị trí của %s", enemy.name));
                        return;
                    }
                    if (p.typePk == 1) {
                        player.addInfo(Player.INFO_RED, String.format("%s đang thách đấu với chiến binh khác", enemy.name));
                        return;
                    }
                    Map map = p.zone.map;
                    if (!player.isCanJoinMap(map) || map.template.id == MapName.DAI_HOI_VO_THUAT || map.template.id == MapName.DAU_TRUONG
                            || map.isExpansion() || map.template.id == MapName.LANH_DIA_BANG_HOI || map.template.id == MapName.NHA_GO_HAN) {
                        player.addInfo(Player.INFO_RED, "Không thể đến khu vực này");
                        return;
                    }
                    long now = System.currentTimeMillis();
                    if (now - lastTimeRevenge < 300000 && targetId == p.id) {
                        revenge(p, false);
                    } else {
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.REVENGE, "OK", player, p.id));
                        commandList.add(new Command(CommandName.CANCEL, "Đóng", player));
                        player.createMenu(NpcName.ME, String.format("Bạn muốn đến ngay chỗ %s, phí là 1 kim cương và được tìm thoải mái trong 5 phút", p.name), commandList);
                    }
                    return;
                }
            }
        } catch (Exception ex) {
            logger.error("action", ex);
        }
    }

    public void revenge(Player enemy, boolean isMoney) {
        player.lockAction.lock();
        try {
            if (enemy.zone == null || !enemy.zone.getPlayers(Zone.TYPE_BOSS).isEmpty()) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            long now = System.currentTimeMillis();
            if (now - lastTimeRevenge < 5000) {
                player.addInfo(Player.INFO_RED, "Thao tác quá nhanh");
                return;
            }
            if (isMoney) {
                if (!player.isEnoughMoney(TypePrice.RUBY, 1)) {
                    return;
                }
                player.downMoney(TypePrice.RUBY, 1);
            }
            lastTimeRevenge = now;
            targetId = enemy.id;
            player.teleport(enemy.zone, true);
            Utils.setTimeout(() -> {
                if (player.zone != null && !player.isDead() && !enemy.isDead() && player.zone == enemy.zone) {
                    player.chat("Mau đền tội");
                    enemy.addInfo(Player.INFO_RED, "Có người đang đến tìm bạn để trả thù");
                    player.testPlayerId = enemy.id;
                    enemy.testPlayerId = player.id;
                    player.setTypePk(1);
                    enemy.setTypePk(1);
                    player.zone.service.playerFlight(player, enemy);
                }
            }, 3000);
        } finally {
            player.lockAction.unlock();
        }
    }

}
