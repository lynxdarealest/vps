package com.beemobi.rongthanonline.npc;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.NpcTreeInfo;

public class NpcTree extends Npc {
    public int time;
    public Player master;

    public NpcTree(Player player, int[] info) {
        template = NpcManager.getInstance().npcTemplates.get(info[0]);
        time = info[1];
        master = player;
    }

    public NpcTree(Player player, int id, int time) {
        template = NpcManager.getInstance().npcTemplates.get(id);
        this.time = time;
        master = player;
    }
}
