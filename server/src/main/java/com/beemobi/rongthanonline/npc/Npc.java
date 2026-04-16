package com.beemobi.rongthanonline.npc;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.task.Task;
import org.apache.log4j.Logger;

public class Npc {
    private static final Logger logger = Logger.getLogger(Npc.class);
    public NpcTemplate template;
    public int x;
    public int y;
    public Zone zone;

    public boolean isTask(Player player) {
        Task task = player.taskMain;
        return task != null && (task.template.fromNpcId == template.id || task.template.steps[task.index].npcId == template.id);
    }

    public void chat(Player player, String content) {
        try {
            Message msg = new Message(MessageName.NPC_CHAT);
            msg.writer().writeByte(template.id);
            msg.writer().writeUTF(content);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("chat", ex);
        }
    }
}