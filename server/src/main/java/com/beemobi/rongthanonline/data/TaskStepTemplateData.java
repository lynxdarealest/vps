package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.task.TaskStepType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_task_step")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStepTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "task_id")
    public Integer taskId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public TaskStepType type;

    @Column(name = "name")
    public String name;

    @Column(name = "map_id")
    public Integer mapId;

    @Column(name = "npc_id")
    public Integer npcId;

    @Column(name = "monster_id")
    public Integer monsterId;

    @Column(name = "boss_id")
    public Integer bossId;

    @Column(name = "item_id")
    public Integer itemId;

    @Column(name = "param")
    public Integer param;

    @Column(name = "description")
    public String description;
}
