package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.team.Team;
import com.beemobi.rongthanonline.team.TeamManager;
import com.beemobi.rongthanonline.team.TeamMember;
import com.beemobi.rongthanonline.team.TeamStatus;
import org.apache.log4j.Logger;

public class TeamAction extends Action {
    private static final Logger logger = Logger.getLogger(TeamAction.class);

    private static final int CREATE_TEAM_OR_INVITE_JOIN_TEAM = 0;
    private static final int ACCEPT_JOIN_TEAM = 1;
    private static final int LEAVE_TEAM = 2;
    private static final int CHANGE_STATUS_TEAM = 3;
    private static final int TRUC_XUAT_KHOI_TEAM = 4;
    private static final int NHUONG_CHUC_DOI_TRUONG = 5;
    private static final int FIND_TEAM_IN_AREA = 6;
    private static final int REQUEST_JOIN_TEAM = 7;
    private static final int TEAM_INFO = 8;

    public TeamAction(Player player) {
        super(player);
    }

    @Override
    public void action(Message message) {
        player.lockAction.lock();
        try {
            byte type = message.reader().readByte();
            switch (type) {
                case CREATE_TEAM_OR_INVITE_JOIN_TEAM: {
                    if (player.getTeam() == null) {
                        createTeam();
                    }
                    int playerId = message.reader().readInt();
                    if (playerId == -1) {
                        return;
                    }
                    Player member = PlayerManager.getInstance().findPlayerById(playerId);
                    if (member == null) {
                        player.addInfo(Player.INFO_RED, Language.PLAYER_NOT_ONLINE);
                        return;
                    }
                    if (member.getTeam() != null) {
                        player.addInfo(Player.INFO_RED, "Người chơi đã có tổ đội");
                        return;
                    }
                    if (!player.isCanSendAction(member)) {
                        player.addInfo(Player.INFO_RED, "Người chơi đang chặn người lạ, bạn cần phải kết bạn trước");
                        return;
                    }
                    Team team = player.getTeam();
                    if (team.leaderId != player.id) {
                        player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho đội trưởng");
                        return;
                    }
                    if (team.getMembers().size() >= 6) {
                        player.addInfo(Player.INFO_RED, "Tổ đội đã đủ thành viên");
                        return;
                    }
                    Command yes = new Command(CommandName.ACCEPT_INVITE_JOIN_TEAM, "Đồng ý", member, team);
                    Command no = new Command(CommandName.CANCEL, "Không", member);
                    member.startYesNo(String.format("%s muốn bạn tham gia tổ đội, bạn có đồng ý không?", player.name), yes, no);
                    player.addInfo(Player.INFO_YELLOW, String.format("Đã gửi lời mới đến %s", member.name));
                    return;
                }

                case ACCEPT_JOIN_TEAM:
                    // thêm thành viên vào tổ đội, trả về client
                    return;

                case LEAVE_TEAM:
                    leaveTeam();
                    return;

                case CHANGE_STATUS_TEAM:
                    changeStatusTeam();
                    return;

                case TRUC_XUAT_KHOI_TEAM:
                    expelTeam(message.reader().readInt());
                    return;

                case NHUONG_CHUC_DOI_TRUONG:
                    changeHostTeam(message.reader().readInt());
                    return;

                case FIND_TEAM_IN_AREA:
                    findTeam();
                    return;

                case REQUEST_JOIN_TEAM:
                    requestJoinTeam(message.reader().readInt());
                    return;

                case TEAM_INFO:
                    player.service.teamInfo();
                    return;
            }
        } catch (Exception e) {
            logger.error("action", e);
        } finally {
            player.lockAction.unlock();
        }
    }

    public void acceptJoinTeam(int playerId) {
        Player requester = PlayerManager.getInstance().findPlayerById(playerId);
        if (requester == null) {
            return;
        }
        if (requester.getTeam() != null) {
            player.addInfo(Player.INFO_RED, "Người chơi đang trong tổ đội khác");
            return;
        }
        Team team = player.getTeam();
        if (team == null) {
            return;
        }
        if (player.id != team.leaderId) {
            player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho đội trưởng");
            return;
        }
        if (team.getMembers().size() >= 6) {
            player.addInfo(Player.INFO_RED, "Tổ đội đã đủ thành viên");
            return;
        }
        TeamMember member = team.findMemberByPlayerId(playerId);
        if (member != null) {
            requester.teamId = team.id;
            requester.service.teamInfo();
            return;
        }
        team.addPlayer(requester);
        requester.teamId = team.id;
        requester.service.teamInfo();
        member = team.findMemberByPlayerId(playerId);
        if (member != null) {
            team.service.addMember(member);
        }
    }

    private void requestJoinTeam(int teamId) {
        if (player.getTeam() != null) {
            return;
        }
        Team team = TeamManager.getInstance().findTeamById(teamId);
        if (team == null) {
            return;
        }
        if (team.getMembers().size() >= 6) {
            player.addInfo(Player.INFO_RED, "Tổ đội đã đủ thành viên");
            return;
        }
        if (team.status == TeamStatus.LOCK) {
            player.addInfo(Player.INFO_RED, "Tổ đội đã khóa");
            return;
        }
        if (team.status == TeamStatus.OPEN) {
            team.autoAccept(player);
            return;
        }
        Player leader = PlayerManager.getInstance().findPlayerById(team.leaderId);
        if (leader == null) {
            player.addInfo(Player.INFO_RED, "Đối phương hiện không online");
            return;
        }
        Command yes = new Command(CommandName.ACCEPT_PLAYER_JOIN_TEAM, "Đồng ý", leader, player.id);
        Command no = new Command(CommandName.CANCEL, "Không", leader);
        leader.startYesNo(String.format("%s (lv%d) muốn gia nhập tổ đội, bạn có đồng ý không?", player.name, player.level), yes, no);
    }

    private void findTeam() {
        if (player.zone == null) {
            return;
        }
        Team team = player.getTeam();
        if (team != null) {
            return;
        }
        player.service.findTeam();
    }

    private void changeHostTeam(int playerId) {
        Team team = player.getTeam();
        if (team == null) {
            return;
        }
        if (player.id != team.leaderId) {
            player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho đội trưởng");
            return;
        }
        team.changeHost(playerId);
    }

    private void expelTeam(int playerId) {
        Team team = player.getTeam();
        if (team == null) {
            return;
        }
        if (player.id != team.leaderId) {
            player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho đội trưởng");
            return;
        }
        TeamMember member = team.findMemberByPlayerId(playerId);
        if (member == null) {
            return;
        }
        team.removePlayer(playerId);
        team.service.removeMemberByHost(member);
        Player m = PlayerManager.getInstance().findPlayerById(playerId);
        if (m != null) {
            m.teamId = -1;
            m.service.removePlayerFromTeam(playerId);
            m.addInfo(Player.INFO_RED, String.format("Bạn đã bị %s loại khỏi tổ đội", player.name));
        }
    }

    private void changeStatusTeam() {
        Team team = player.getTeam();
        if (team == null) {
            return;
        }
        if (player.id != team.leaderId) {
            player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho đội trưởng");
            return;
        }
        if (team.status == TeamStatus.NOT_LOCK) {
            team.status = TeamStatus.OPEN;
        } else if (team.status == TeamStatus.OPEN) {
            team.status = TeamStatus.LOCK;
        } else {
            team.status = TeamStatus.NOT_LOCK;
        }
        team.service.changeStatus();
    }

    private void leaveTeam() {
        Team team = player.getTeam();
        if (team == null) {
            return;
        }
        player.teamId = -1;
        team.removePlayer(player.id);
        player.service.removePlayerFromTeam(player.id);
        team.service.removePlayer(player);
        if (team.getMembers().isEmpty()) {
            TeamManager.getInstance().teams.remove(team.id);
            return;
        }
        if (team.leaderId != player.id) {
            return;
        }
        team.changeHost(-1);
    }

    public void acceptInviteJoinTeam(Team team) {
        if (team == null) {
            player.addInfo(Player.INFO_RED, "Không tìm thấy tổ đội");
            return;
        }
        if (team.getMembers().size() >= 6) {
            player.addInfo(Player.INFO_RED, "Tổ đội đã đủ thành viên");
            return;
        }
        team.addPlayer(player);
        player.teamId = team.id;
        player.service.teamInfo();
        TeamMember member = team.findMemberByPlayerId(player.id);
        if (member != null) {
            team.service.addMember(member);
        }
    }

    private void createTeam() {
        if (player.getTeam() != null) {
            player.service.teamInfo();
            return;
        }
        Team team = new Team(player);
        team.addPlayer(player);
        player.teamId = team.id;
        TeamManager.getInstance().addTeam(team);
        player.service.teamInfo();
    }


}
