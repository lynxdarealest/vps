package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.mission.IMission;
import com.beemobi.rongthanonline.mission.MissionType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MissionAction extends Action {
    private static final Logger logger = Logger.getLogger(MissionAction.class);

    private static final int SHOW_MENU = -1;
    private static final int MISSION_WEEK = 0;
    private static final int MISSION_DAILY = 1;
    private static final int MISSION_RECHARGE = 2;
    private static final int MISSION_EVENT = 3;

    public MissionAction(Player player) {
        super(player);
    }

    @Override
    public void action(Message message) {
        try {
            if (Server.getInstance().isInterServer()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            int action = message.reader().readByte();
            switch (action) {
                case -2:
                    player.service.startDialogOk("Phần thưởng sẽ được trao vào 0h thứ 2 hàng tuần nếu bạn nằm trong danh sách top 10");
                    return;

                case SHOW_MENU: {
                    List<Command> commandList = new ArrayList<>();
                    commandList.add(new Command(CommandName.MENU_NAP_THE, "Nạp\ntích lũy", player));
                    commandList.add(new Command(CommandName.NAP_KIM_CUONG_HANG_NGAY, "Nạp 7 ngày", player));
                    commandList.add(new Command(CommandName.BAO_DANH_HANG_NGAY, "Online hàng ngày", player));
                    commandList.add(new Command(CommandName.SHOW_ACHIEVEMENT, "Thành tựu", player));
                    commandList.add(new Command(CommandName.NHAP_GIFT_CODE, "Nhập mã quà tặng", player));
                    commandList.add(new Command(CommandName.QUA_CUA_TOI, "Kho quà", player));
                    player.createMenu(NpcName.ME, "", commandList);
                    return;
                }

                case MISSION_WEEK: {
                    reward(message.reader().readByte(), player.missionWeeks, 0);
                    return;
                }

                case MISSION_DAILY: {
                    reward(message.reader().readByte(), player.missionDailies, 1);
                    return;
                }

                case MISSION_RECHARGE: {
                    reward(message.reader().readByte(), player.missionRecharges, 2);
                    return;
                }

                case MISSION_EVENT: {
                    List<IMission> missions = player.missionEvents;
                    if (missions.stream().allMatch(m -> m.getType() == MissionType.DA_XONG)) {
                        player.resetMissionEvent();
                    }
                    reward(message.reader().readByte(), player.missionEvents, 3);
                    player.service.startDialogOk("Khi nhận xong tất cả phần thưởng, nhiệm vụ sẽ được đặt lại");
                    return;
                }

            }
        } catch (Exception ex) {
            logger.error("action", ex);
        }
    }

    public void reward(int index, List<IMission> missions, int typeShow) {
        if (index < 0 || index >= missions.size()) {
            return;
        }
        IMission mission = missions.get(index);
        if (mission.getType() == MissionType.CHUA_HOAN_THANH) {
            player.addInfo(Player.INFO_RED, "Bạn chưa hoàn thành nhiệm vụ");
            return;
        }
        if (mission.getType() == MissionType.DA_XONG) {
            player.addInfo(Player.INFO_RED, "Bạn đã nhận phần thưởng này rồi");
            return;
        }
        player.lockAction.lock();
        try {
            List<Item> items = mission.getItems();
            if (items.isEmpty()) {
                return;
            }
            if (player.getCountItemBagEmpty() < items.size()) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất %d ô trống trong túi đồ", items.size()));
                return;
            }
            mission.setType(MissionType.DA_XONG);
            for (Item item : items) {
                player.addItem(item.cloneItem(), true);
            }
            player.service.showMission(typeShow, missions);
        } finally {
            player.lockAction.unlock();
        }
    }
}
