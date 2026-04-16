package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanManager;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.data.ClanData;
import com.beemobi.rongthanonline.data.ClanMemberData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.entity.player.json.ClanInfo;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.regex.Pattern;

public class ClanAction extends Action {
    private static final Logger logger = Logger.getLogger(ClanAction.class);

    private static final int SHOW_CLAN = -1;
    private static final int SEND_INVITE_JOIN_CLAN = 0;
    private static final int DONATE_COIN = 1;
    private static final int DONATE_DIAMOND = 2;
    private static final int CHANGE_SLOGAN = 3;
    private static final int CHANGE_NOTIFICATION = 4;
    private static final int PHONG_PHO_BANG = 5;
    private static final int BAI_NHIEM_PHO_BANG = 6;
    private static final int LOAI_THANH_VIEN = 7;
    private static final int LEAVE_BANG = 8;
    private static final int PHONG_CHU_BANG = 9;

    public ClanAction(Player player) {
        super(player);
    }

    @Override
    public void action(Message message) {
        try {
            if (Server.getInstance().isInterServer()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            int type = message.reader().readByte();
            if (type == SHOW_CLAN) {
                player.service.setClanInfo(true);
                if (player.clan == null) {
                    player.addInfo(Player.INFO_RED, "Bạn chưa có bang hội");
                }
                return;
            }
            if (player.clan == null) {
                return;
            }
            switch (type) {
                case SEND_INVITE_JOIN_CLAN:
                    sendInviteJoinClan(message.reader().readInt());
                    return;

                case DONATE_COIN:
                    donateMoney(message.reader().readInt());
                    return;
                case DONATE_DIAMOND:
                    return;

                case CHANGE_SLOGAN:
                    changeClanSlogan(message.reader().readUTF());
                    return;

                case CHANGE_NOTIFICATION:
                    changeClanNotification(message.reader().readUTF());
                    return;

                case PHONG_PHO_BANG:
                    changeClanMemberRole(message.reader().readInt(), 1);
                    return;

                case BAI_NHIEM_PHO_BANG:
                    changeClanMemberRole(message.reader().readInt(), 2);
                    return;

                case LOAI_THANH_VIEN:
                    expelClanMember(message.reader().readInt());
                    return;

                case LEAVE_BANG:
                    leaveClan();
                    return;

                case PHONG_CHU_BANG:
                    changeClanMemberRole(message.reader().readInt(), 0);
                    return;
            }
        } catch (Exception ex) {
            logger.error("action", ex);
        }
    }

    public void createClan(Message message) {
        if (Server.getInstance().isInterServer()) {
            player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
            return;
        }
        player.lockAction.lock();
        try {
            if (player.clan != null) {
                return;
            }
            if (player.diamond < 50) {
                player.addInfo(Player.INFO_RED, "Cần ít nhất 50 kim cương để lập bang hội");
                return;
            }
            if (player.pointActive < 200) {
                player.addInfo(Player.INFO_RED, "Điểm năng động từ 200 trở lên mới có thể sử dụng tính năng này");
                return;
            }
            String name = message.reader().readUTF().toLowerCase().trim();
            if (name.length() < 5 || name.length() > 10 || !Pattern.compile("^[a-z0-9]+$").matcher(name).find()) {
                player.service.startDialogOk("Tên bang hội chỉ từ 5 đến 10 kí tự bao gồm chữ và số");
                return;
            }
            if (name.contains("admin")) {
                player.service.startDialogOk("Tên bang hội không được chứa \"admin\"");
                return;
            }
            if (ClanManager.getInstance().findClanByName(name) != null) {
                player.service.startDialogOk("Tên bang hội đã tồn tại!");
                return;
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ClanData data = new ClanData();
            data.server = Server.ID;
            data.name = name;
            data.slogan = "Rồng Thần Online";
            data.notification = "Rồng Thần Online";
            data.level = 1;
            data.bonusSlot = 0;
            data.exp = 0L;
            data.leaderId = player.id;
            data.createTime = now;
            data.resetTime = now;
            data.countManor = 1;
            data.coin = 0L;
            GameRepository.getInstance().clanData.save(data);
            Clan clan = new Clan(data);
            ClanManager.getInstance().addClan(clan);
            ClanMemberData memberData = new ClanMemberData(player, clan.id, 0);
            GameRepository.getInstance().clanMemberData.save(memberData);
            clan.addMember(new ClanMember(memberData));
            player.upDiamond(-50);
            player.setClan(clan);
            player.addInfo(Player.INFO_YELLOW, "Tạo bang hội thành công");
            player.service.setClanInfo(true);
        } catch (Exception ex) {
            logger.error("createClan", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    public void acceptInviteJoinClan(Clan clan) {
        if (player.clan != null) {
            return;
        }
        if (clan == null) {
            return;
        }
        if (clan.members.size() >= clan.getMaxMember()) {
            player.addInfo(Player.INFO_RED, "Bang hội đã đủ thành viên");
            return;
        }
        ClanMemberData memberData = new ClanMemberData(player, clan.id, 2);
        GameRepository.getInstance().clanMemberData.save(memberData);
        long now = System.currentTimeMillis();
        GameRepository.getInstance().playerData.setClanInfo(player.id, Utils.gson.toJson(new long[]{clan.id, player.countManor, now}));
        clan.addMember(new ClanMember(memberData));
        player.setClan(clan);
        player.service.setClanInfo(false);
        if (player.zone != null) {
            player.zone.service.updateClan(player);
        }
        player.clanTime = now;
        clan.service.addInfo(Player.INFO_YELLOW, String.format("%s đã gia nhập bang hội %s", player.name, clan.name));
    }

    public void confirmLeaveClan() {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        ClanMember member = clan.findMemberByPlayerId(player.id);
        if (member == null) {
            return;
        }
        if (member.role == 0) {
            player.addInfo(Player.INFO_RED, "Bạn cần phải nhường chức bang chủ cho người khác trước");
            return;
        }
        clan.lock.writeLock().lock();
        try {
            GameRepository.getInstance().clanMemberData.deleteClanMemberById(member.id);
            player.clanTime = System.currentTimeMillis();
            GameRepository.getInstance().playerData.setClanInfo(member.playerId, Utils.gson.toJson(new long[]{clan.id, player.countManor, player.clanTime}));
            clan.members.remove(member);
            player.setClan(null);
            clan.service.addInfo(Player.INFO_RED, String.format("%s đã rời bang hội", player.name));
            player.service.setClanInfo(false);
            player.addInfo(Player.INFO_RED, String.format("Bạn đã rời bang hội %s", clan.name));
        } finally {
            clan.lock.writeLock().unlock();
        }
    }

    private void leaveClan() {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        ClanMember me = clan.findMemberByPlayerId(player.id);
        if (me == null) {
            return;
        }
        if (me.role == 0) {
            player.addInfo(Player.INFO_RED, "Bạn cần phải nhường chức bang chủ cho người khác trước");
            return;
        }
        Command yes = new Command(CommandName.CONFIRM_LEAVE_CLAN, "Đồng ý", player);
        Command no = new Command(CommandName.CANCEL, "Không", player);
        player.startYesNo(String.format("Bạn có chắc chắn muốn rời bang hội %s không?", clan.name), yes, no);
    }

    public void confirmExpelClanMember(int playerId) {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        if (playerId == player.id) {
            return;
        }
        ClanMember me = clan.findMemberByPlayerId(player.id);
        if (me == null) {
            return;
        }
        if (me.role != 0 && me.role != 1) {
            player.addInfo(Player.INFO_RED, "Chức năng dành cho bang chủ hoặc bang phó");
            return;
        }
        ClanMember member = clan.findMemberByPlayerId(playerId);
        if (member == null) {
            return;
        }
        if (me.role >= member.role) {
            player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
            return;
        }
        clan.lock.writeLock().lock();
        try {
            GameRepository.getInstance().clanMemberData.deleteClanMemberById(member.id);
            long now = System.currentTimeMillis();
            GameRepository.getInstance().playerData.setClanInfo(member.playerId, Utils.gson.toJson(new long[]{clan.id, player.countManor, now}));
            clan.members.remove(member);
            clan.service.addInfo(Player.INFO_YELLOW, String.format("%s đã trục xuất %s khỏi bang hội", player.name, member.name));
            player.service.setClanInfo(false);
            Player mem = PlayerManager.getInstance().findPlayerById(member.playerId);
            if (mem != null) {
                mem.setClan(null);
                mem.service.setClanInfo(false);
                mem.clanTime = now;
                mem.addInfo(Player.INFO_RED, String.format("Bạn bị đuổi khỏi bang hội %s", clan.name));
                if (mem.zone != null) {
                    mem.zone.service.updateClan(mem);
                }
            }
        } finally {
            clan.lock.writeLock().unlock();
        }
    }

    private void expelClanMember(int playerId) {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        if (playerId == player.id) {
            return;
        }
        ClanMember me = clan.findMemberByPlayerId(player.id);
        if (me == null) {
            return;
        }
        if (me.role != 0 && me.role != 1) {
            player.addInfo(Player.INFO_RED, "Chức năng dành cho bang chủ hoặc bang phó");
            return;
        }
        ClanMember member = clan.findMemberByPlayerId(playerId);
        if (member == null) {
            return;
        }
        if (me.role >= member.role) {
            player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
            return;
        }
        Command yes = new Command(CommandName.CONFIRM_EXPEL_CLAN_MEMBER, "Đồng ý", player, playerId);
        Command no = new Command(CommandName.CANCEL, "Không", player);
        player.startYesNo(String.format("Bạn có chắc chắn muốn loại %s ra khỏi bang hội không?", member.name), yes, no);
    }

    private void changeClanMemberRole(int playerId, int role) {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        if (playerId == player.id) {
            return;
        }
        ClanMember me = clan.findMemberByPlayerId(player.id);
        if (me == null) {
            return;
        }
        if (me.role != 0) {
            player.addInfo(Player.INFO_RED, "Chức năng dành cho bang chủ");
            return;
        }
        ClanMember member = clan.findMemberByPlayerId(playerId);
        if (member == null) {
            return;
        }
        if (role == 1) {
            if (!clan.findMemberByRole(1).isEmpty()) {
                player.addInfo(Player.INFO_RED, "Bạn cần bãi nhiệm chức vụ của bang phó hiện tại trước");
                return;
            }
            if (member.role == 1) {
                player.addInfo(Player.INFO_RED, String.format("%s vẫn đang là bang phó", member.name));
                return;
            }
            if (member.role != 2) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            clan.lock.writeLock().lock();
            try {
                member.setRole(1);
                clan.sort();
                clan.service.addInfo(Player.INFO_YELLOW, String.format("%s đã phong %s làm phó bang", player.name, member.name));
                player.service.setClanInfo(false);
            } finally {
                clan.lock.writeLock().unlock();
            }
            return;
        }
        if (role == 2) {
            if (member.role != 1) {
                player.addInfo(Player.INFO_RED, String.format("%s đang không phải bang phó", member.name));
                return;
            }
            clan.lock.writeLock().lock();
            try {
                member.setRole(2);
                clan.sort();
                clan.service.addInfo(Player.INFO_RED, String.format("%s đã bãi nhiệm chức bang phó của %s", player.name, member.name));
                player.service.setClanInfo(false);
            } finally {
                clan.lock.writeLock().unlock();
            }
            return;
        }
        if (role == 0) {
            if (member.role == 0) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            clan.lock.writeLock().lock();
            try {
                member.setRole(0);
                me.setRole(2);
                clan.setLeaderId(member.playerId);
                clan.sort();
                clan.service.addInfo(Player.INFO_YELLOW, String.format("%s đã nhường chức bang chủ cho %s", player.name, member.name));
                player.service.setClanInfo(false);
            } finally {
                clan.lock.writeLock().unlock();
            }
            Top top = TopManager.getInstance().tops.get(TopType.TOP_CLAN);
            if (top != null) {
                TopInfo topInfo = top.findTopInfoByPlayerId(player.id);
                if (topInfo != null) {
                    topInfo.id = member.playerId;
                    topInfo.name = String.format("%s (%s)", clan.name, member.name);
                }
            }
            return;
        }
    }

    private void changeClanNotification(String notification) {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        ClanMember member = clan.findMemberByPlayerId(player.id);
        if (member == null) {
            return;
        }
        if (member.role != 0 && member.role != 1) {
            player.addInfo(Player.INFO_RED, "Chức năng dành cho bang chủ hoặc bang phó");
            return;
        }
        if (notification.length() > 200) {
            notification = notification.trim().substring(0, 200);
        }
        clan.setNotification(notification);
    }

    private void changeClanSlogan(String slogan) {
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        ClanMember member = clan.findMemberByPlayerId(player.id);
        if (member == null) {
            return;
        }
        if (member.role != 0) {
            player.addInfo(Player.INFO_RED, "Chức năng dành cho bang chủ");
            return;
        }
        if (slogan.length() > 50) {
            slogan = slogan.trim().substring(0, 50);
        }
        clan.setSlogan(slogan);
    }

    private void donateMoney(int coin) {
        if (coin <= 0) {
            return;
        }
        Clan clan = player.clan;
        if (clan == null) {
            return;
        }
        if (player.xu < coin) {
            player.addInfo(Player.INFO_RED, "Bạn không đủ xu");
            return;
        }
        player.upXu(-coin);
        clan.upCoin(coin);
        clan.service.addInfo(Player.INFO_YELLOW, String.format("%s đã đóng góp %s xu vào ngân sách bang hội", player.name, Utils.getMoneys(coin)));
    }

    private void sendInviteJoinClan(int playerId) {
        if (playerId == player.id) {
            return;
        }
        Clan clan = player.clan;
        ClanMember member = clan.findMemberByPlayerId(player.id);
        if (member == null) {
            return;
        }
        if (member.role != 0 && member.role != 1) {
            player.addInfo(Player.INFO_RED, "Chức năng dành cho bang chủ hoặc bang phó");
            return;
        }
        if (clan.members.size() >= clan.getMaxMember()) {
            player.addInfo(Player.INFO_RED, "Bang hội đã đủ thành viên");
            return;
        }
        Player p = player.zone.findPlayerById(playerId);
        if (p == null) {
            return;
        }
        if (p.clan != null) {
            player.addInfo(Player.INFO_RED, "Đối phương đã có bang hội");
            return;
        }
        if (player.pointActive < 100) {
            player.addInfo(Player.INFO_RED, "Người chơi không đủ 100 điểm năng động");
            return;
        }
        if (!player.isCanSendAction(p)) {
            player.addInfo(Player.INFO_RED, "Người chơi đang chặn người lạ, bạn cần phải kết bạn trước");
            return;
        }
        long time = p.clanTime + 3600000 - System.currentTimeMillis();
        if (time > 0) {
            player.addInfo(Player.INFO_RED, String.format("Người chơi chỉ có thể tham gia bang hội sau %s", Utils.formatTime(time)));
            return;
        }
        Command yes = new Command(CommandName.ACCEPT_INVITE_JOIN_CLAN, "Đồng ý", p, clan);
        Command no = new Command(CommandName.CANCEL, "Không", p);
        p.startYesNo(String.format("%s mời bạn tham gia bang hội %s, bạn có đồng ý không?", player.name, clan.name), yes, no);
    }
}
