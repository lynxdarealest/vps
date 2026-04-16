using Assets.Scripts.Commons;
using Assets.Scripts.IOs;
using Assets.Scripts.Libs.Jsons;
using Assets.Scripts.Models;
using Assets.Scripts.Networks;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Assets.Scripts.Frames
{
    public class FrameManager
    {
        public static FrameManager instance = new FrameManager();

        public Dictionary<int, Medal> medals;

        public Dictionary<int, Aura> auras;

        public Dictionary<int, MountTemplate> mounts;

        public Dictionary<int, FrameTemplate> frames;

        public Dictionary<int, Bag> bags;

        public int versionMedal;

        public int versionAura;

        public int versionFrame;

        public int versionMount;

        public int versionBag;

        public Dictionary<int, int[]> effectPowers = new Dictionary<int, int[]>();

        public Dictionary<int, int[]> effectEquips = new Dictionary<int, int[]>();

        public FrameManager()
        {
            auras = new Dictionary<int, Aura>();
            medals = new Dictionary<int, Medal>();
            mounts = new Dictionary<int, MountTemplate>();
            frames = new Dictionary<int, FrameTemplate>();
            bags = new Dictionary<int, Bag>();
            versionMedal = -1;
            versionFrame = -1;
            versionAura = -1;
            versionMount = -1;
            versionBag = -1;

            effectPowers.Add(0, new int[] { 600, 601, 602, 603, 604, 605 });
            effectPowers.Add(1, new int[] { 600, 601, 602, 603, 604, 605 });
            effectPowers.Add(2, new int[] { 606, 607, 608, 609, 610, 611 });
            effectPowers.Add(3, new int[] { 612, 613, 614, 615 });
            effectPowers.Add(4, new int[] { 616, 617, 618, 619 });
            effectPowers.Add(5, new int[] { 620, 621, 622, 623 });
            effectPowers.Add(6, new int[] { 624, 625, 626, 627, 628, 629 });
            effectPowers.Add(7, new int[] { 630, 631, 632, 633, 634, 635 });
            effectPowers.Add(8, new int[] { 4900, 4901, 4902, 4903, 4904, 4905 });
            effectPowers.Add(9, new int[] { 4906, 4907, 4908, 4909, 4910, 4911 });
            effectPowers.Add(10, new int[] { 1312, 1313, 1314, 1315, 1316, 1317, 1318 });

            effectEquips.Add(0, new int[] { 3008, 3009, 3010, 3011, 3012, 3013, 3014, 3015 });
            effectEquips.Add(1, new int[] { 3016, 3017, 3018, 3019, 3020, 3021, 3022, 3023 });
            effectEquips.Add(2, new int[] { 3000, 3001, 3002, 3003, 3004, 3005, 3006, 3007 });
            effectEquips.Add(3, new int[] { 3040, 3041, 3042, 3043, 3044, 3045, 3046, 3047 });
            effectEquips.Add(4, new int[] { 3024, 3025, 3026, 3027, 3028, 3029, 3030, 3031 });

        }

        public void Init()
        {
            LoadMedal();
            LoadMount();
            LoadFrame();
            LoadBag();
            LoadAura();
        }

        public void Update()
        {
            long now = Utils.CurrentTimeMillis();
            for (int i = 0; i < medals.Count; i++)
            {
                medals.ElementAt(i).Value.Update(now);
            }
            for (int i = 0; i < mounts.Count; i++)
            {
                mounts.ElementAt(i).Value.Update(now);
            }
            for (int i = 0; i < bags.Count; i++)
            {
                bags.ElementAt(i).Value.Update(now);
            }
            for (int i = 0; i < auras.Count; i++)
            {
                auras.ElementAt(i).Value.Update(now);
            }
        }

        public Frame GetFrame(int id)
        {
            if (frames.ContainsKey(id))
            {
                return new Frame()
                {
                    template = frames[id],
                    icon = -1
                };
            }
            return null;
        }

        public Mount GetMount(int id)
        {
            if (mounts.ContainsKey(id))
            {
                return new Mount()
                {
                    template = mounts[id]
                };
            }
            return null;
        }

        public Bag GetBag(int id)
        {
            if (bags.ContainsKey(id))
            {
                return bags[id];
            }
            return null;
        }

        public void LoadBag()
        {
            try
            {
                MyReader reader = new MyReader(Rms.Load("bag"));
                versionBag = reader.ReadSbyte();
                int count = reader.ReadShort();
                for (int i = 0; i < count; i++)
                {
                    Bag bag = new Bag();
                    bag.id = reader.ReadSbyte();
                    bag.dxFly = reader.ReadShort();
                    bag.dyFly = reader.ReadShort();
                    bag.delay = reader.ReadShort();
                    int count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        bag.icons.Add(reader.ReadShort());
                    }
                    bags.Add(bag.id, bag);
                }
                for (int i = 0; i < count; i++)
                {
                    bags.ElementAt(i).Value.isFly = reader.ReadBool();
                }
            }
            catch
            {
                versionBag = -1;
                bags.Clear();
            }
        }

        public void SaveBag()
        {
            MyWriter writer = new MyWriter();
            writer.WriteSByte(versionBag);
            writer.WriteShort(bags.Count);
            for (int i = 0; i < bags.Count; i++)
            {
                Bag bag = bags.ElementAt(i).Value;
                writer.WriteSByte(bag.id);
                writer.WriteShort(bag.dxFly);
                writer.WriteShort(bag.dyFly);
                writer.WriteShort(bag.delay);
                writer.WriteSByte(bag.icons.Count);
                foreach (int icon in bag.icons)
                {
                    writer.WriteShort(icon);
                }
            }
            for (int i = 0; i < bags.Count; i++)
            {
                Bag bag = bags.ElementAt(i).Value;
                writer.WriteBool(bag.isFly);
            }
            Rms.Save("bag", writer.GetData());
        }

        public void LoadFrame()
        {
            try
            {
                MyReader reader = new MyReader(Rms.Load("frame"));
                versionFrame = reader.ReadSbyte();
                int count = reader.ReadShort();
                for (int i = 0; i < count; i++)
                {
                    FrameTemplate template = new FrameTemplate();
                    template.id = reader.ReadShort();
                    template.hpBar = reader.ReadShort();
                    template.chat = reader.ReadShort();
                    int count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        template.dead.Add(reader.ReadShort());
                    }
                    count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        template.stand.Add(reader.ReadShort());
                    }
                    count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        template.run.Add(reader.ReadShort());
                    }
                    template.fly = reader.ReadShort();
                    template.jump = reader.ReadShort();
                    template.fall = reader.ReadShort();
                    template.injure = reader.ReadShort();
                    count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        template.action.Add(reader.ReadSbyte(), reader.ReadShort());
                    }
                    template.dx = reader.ReadShort();
                    template.dy = reader.ReadShort();
                    template.width = reader.ReadShort();
                    template.height = reader.ReadShort();
                    frames.Add(template.id, template);
                }
            }
            catch
            {
                versionFrame = -1;
                frames.Clear();
            }
        }

        public void SaveFrame()
        {
            MyWriter writer = new MyWriter();
            writer.WriteSByte(versionFrame);
            writer.WriteShort(frames.Count);
            for (int i = 0; i < frames.Count; i++)
            {
                FrameTemplate template = frames.ElementAt(i).Value;
                writer.WriteShort(template.id);
                writer.WriteShort(template.hpBar);
                writer.WriteShort(template.chat);
                writer.WriteSByte(template.dead.Count);
                foreach (int iconId in template.dead)
                {
                    writer.WriteShort(iconId);
                }
                writer.WriteSByte(template.stand.Count);
                foreach (int iconId in template.stand)
                {
                    writer.WriteShort(iconId);
                }
                writer.WriteSByte(template.run.Count);
                foreach (int iconId in template.run)
                {
                    writer.WriteShort(iconId);
                }
                writer.WriteShort(template.fly);
                writer.WriteShort(template.jump);
                writer.WriteShort(template.fall);
                writer.WriteShort(template.injure);
                writer.WriteSByte(template.action.Count);
                foreach (var item in template.action)
                {
                    writer.WriteSByte(item.Key);
                    writer.WriteShort(item.Value);
                }
                writer.WriteShort(template.dx);
                writer.WriteShort(template.dy);
                writer.WriteShort(template.width);
                writer.WriteShort(template.height);
            }
            Rms.Save("frame", writer.GetData());
        }

        private void LoadMount()
        {
            try
            {
                MyReader reader = new MyReader(Rms.Load("mount"));
                versionMount = reader.ReadSbyte();
                int count = reader.ReadShort();
                for (int i = 0; i < count; i++)
                {
                    MountTemplate mount = new MountTemplate();
                    mount.id = reader.ReadSbyte();
                    mount.dx = reader.ReadShort();
                    mount.dy = reader.ReadShort();
                    mount.delay = reader.ReadShort();
                    int count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        mount.icons.Add(reader.ReadShort());
                    }
                    mount.layer = reader.ReadSbyte();
                    mounts.Add(mount.id, mount);
                }
            }
            catch
            {
                versionMount = -1;
                mounts.Clear();
            }
        }

        public void SaveMount()
        {
            MyWriter writer = new MyWriter();
            writer.WriteSByte(versionMount);
            writer.WriteShort(mounts.Count);
            for (int i = 0; i < mounts.Count; i++)
            {
                MountTemplate mount = mounts.ElementAt(i).Value;
                writer.WriteSByte(mount.id);
                writer.WriteShort(mount.dx);
                writer.WriteShort(mount.dy);
                writer.WriteShort(mount.delay);
                writer.WriteSByte(mount.icons.Count);
                foreach (int icon in mount.icons)
                {
                    writer.WriteShort(icon);
                }
                writer.WriteSByte(mount.layer);
            }
            Rms.Save("mount", writer.GetData());
        }

        private void LoadMedal()
        {
            try
            {
                MyReader reader = new MyReader(Rms.Load("medal"));
                versionMedal = reader.ReadSbyte();
                int count = reader.ReadShort();
                for (int i = 0; i < count; i++)
                {
                    Medal medal = new Medal();
                    medal.id = reader.ReadSbyte();
                    medal.dx = reader.ReadShort();
                    medal.dy = reader.ReadShort();
                    medal.delay = reader.ReadShort();
                    int count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        medal.icons.Add(reader.ReadShort());
                    }
                    medals.Add(medal.id, medal);
                }
            }
            catch
            {
                versionMedal = -1;
                medals.Clear();
            }
        }

        public void SaveMedal()
        {
            MyWriter writer = new MyWriter();
            writer.WriteSByte(versionMedal);
            writer.WriteShort(medals.Count);
            for (int i = 0; i < medals.Count; i++)
            {
                Medal medal = medals.ElementAt(i).Value;
                writer.WriteSByte(medal.id);
                writer.WriteShort(medal.dx);
                writer.WriteShort(medal.dy);
                writer.WriteShort(medal.delay);
                writer.WriteSByte(medal.icons.Count);
                foreach (int icon in medal.icons)
                {
                    writer.WriteShort(icon);
                }
            }
            Rms.Save("medal", writer.GetData());
        }

        private void LoadAura()
        {
            try
            {
                MyReader reader = new MyReader(Rms.Load("aura"));
                versionAura = reader.ReadSbyte();
                int count = reader.ReadShort();
                for (int i = 0; i < count; i++)
                {
                    Aura aura = new Aura();
                    aura.id = reader.ReadSbyte();
                    aura.dx = reader.ReadShort();
                    aura.dy = reader.ReadShort();
                    aura.delay = reader.ReadShort();
                    int count_img = reader.ReadSbyte();
                    for (int j = 0; j < count_img; j++)
                    {
                        aura.icons.Add(reader.ReadShort());
                    }
                    auras.Add(aura.id, aura);
                }
            }
            catch
            {
                versionAura = -1;
                auras.Clear();
            }
        }

        public void SaveAura()
        {
            MyWriter writer = new MyWriter();
            writer.WriteSByte(versionAura);
            writer.WriteShort(auras.Count);
            for (int i = 0; i < auras.Count; i++)
            {
                Aura aura = auras.ElementAt(i).Value;
                writer.WriteSByte(aura.id);
                writer.WriteShort(aura.dx);
                writer.WriteShort(aura.dy);
                writer.WriteShort(aura.delay);
                writer.WriteSByte(aura.icons.Count);
                foreach (int icon in aura.icons)
                {
                    writer.WriteShort(icon);
                }
            }
            Rms.Save("aura", writer.GetData());
        }
    }
}
