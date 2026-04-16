package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.model.Friend;
import com.beemobi.rongthanonline.network.Message;
import org.apache.log4j.Logger;

public class FriendAction extends Action {
    private static final Logger logger = Logger.getLogger(FriendAction.class);

    private static final int SHOW_FRIEND = 0;
    private static final int ADD_FRIEND_WITH_PLAYER_ID = 1;
    private static final int REMOVE_FRIEND_WITH_PLAYER_ID = 2;
    private static final int ADD_FRIEND_WITH_PLAYER_NAME = 3;

    public FriendAction(Player player) {
        super(player);
    }

    @Override
    public void action(Message message) {
        try {
            int type = message.reader().readByte();
            switch (type) {
                case SHOW_FRIEND: {
                    player.service.setFriend(0, -1);
                    return;
                }

                case ADD_FRIEND_WITH_PLAYER_ID: {
                    int playerId = message.reader().readInt();
                    if (playerId == player.id) {
                        return;
                    }
                    if (player.friends.size() >= 100) {
                        player.addInfo(Player.INFO_RED, "Danh sách bạn bè đã đầy");
                        return;
                    }
                    Player p = PlayerManager.getInstance().findPlayerById(playerId);
                    if (p == null) {
                        player.addInfo(Player.INFO_RED, "Hiện tại người chơi không online");
                        return;
                    }
                    Friend friend = player.friends.stream().filter(f -> f.playerId == playerId).findFirst().orElse(null);
                    if (friend != null) {
                        player.addInfo(Player.INFO_RED, String.format("%s đã có trong danh sách bạn bè", p.name));
                        return;
                    }
                    player.friends.add(new Friend(p));
                    player.addInfo(Player.INFO_YELLOW, String.format("Kết bạn thành công với %s", p.name));
                    return;
                }

                case REMOVE_FRIEND_WITH_PLAYER_ID: {
                    int playerId = message.reader().readInt();
                    if (playerId == player.id) {
                        return;
                    }
                    Friend friend = player.friends.stream().filter(f -> f.playerId == playerId).findFirst().orElse(null);
                    if (friend == null) {
                        return;
                    }
                    player.friends.remove(friend);
                    player.addInfo(Player.INFO_YELLOW, String.format("Đã xóa %s khỏi danh sách bạn bè", friend.name));
                    player.service.setFriend(2, playerId);
                    return;
                }

                case ADD_FRIEND_WITH_PLAYER_NAME: {
                    String name = message.reader().readUTF();
                    if (name.equals(player.name)) {
                        return;
                    }
                    Player p = PlayerManager.getInstance().findPlayerByName(name);
                    if (p == null) {
                        player.addInfo(Player.INFO_RED, "Hiện tại người chơi không online");
                        return;
                    }
                    Friend friend = player.friends.stream().filter(f -> f.name.equals(name)).findFirst().orElse(null);
                    if (friend != null) {
                        player.addInfo(Player.INFO_RED, String.format("%s đã có trong danh sách bạn bè", p.name));
                        return;
                    }
                    player.friends.add(new Friend(p));
                    player.addInfo(Player.INFO_YELLOW, String.format("Kết bạn thành công với %s", p.name));
                    player.service.setFriend(0, -1);
                    return;
                }
            }
        } catch (Exception ex) {
            logger.error("action", ex);
        }
    }
}
