using Assets.Scripts.Commands;
using Assets.Scripts.Commons;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.IOs;
using Assets.Scripts.Models;
using Assets.Scripts.Screens;
using System.Collections.Generic;
using UnityEngine;

namespace Assets.Scripts.Items
{
    public class Item : Command
    {
        public static int TYPE_GANG = 0;

        public static int TYPE_BOI = 1;

        public static int TYPE_GIAY = 2;

        public static int TYPE_NHAN = 3;

        public static int TYPE_QUAN = 4;

        public static int TYPE_DAY_CHUYEN = 5;

        public static int TYPE_AO = 6;

        public static int TYPE_RADAR = 7;

        public static int TYPE_AVATAR = 8;

        public static int TYPE_BONG_TAI = 9;

        public static int TYPE_THU_CUOI = 10;

        public static int TYPE_CLAN = 11;

        public static int TYPE_CHUA_MO_1 = 12;

        public static int TYPE_CHUA_MO_2 = 13;

        public static int TYPE_THUC_AN = 14;

        public static int TYPE_YEN = 15;

        public static int TYPE_DIAMOND = 16;

        public ItemTemplate template;

        public List<ItemOption> options = new List<ItemOption>();

        public int quantity;

        public bool isLock;

        public int indexUI;

        public int id;

        public int typePrice;

        public int price;

        public string expiryInfo;

        public string sellerName;

        public int status;

        public string strFrom;

        public string strExpiry;

        public string strInfo;

        public Item()
        {
            w = 55;
            h = 55;
        }

        public Item(Message message)
        {
            w = 55;
            h = 55;
            this.template = ItemManager.instance.itemTemplates[message.ReadShort()];
            this.quantity = message.ReadInt();
            this.isLock = message.ReadBool();
            int count_option = message.ReadSByte();
            for (int j = 0; j < count_option; j++)
            {
                ItemOption option = new ItemOption();
                option.template = ItemManager.instance.itemOptionTemplates[message.ReadShort()];
                option.param = message.ReadInt();
                this.options.Add(option);
            }
        }

        public Item(int id, Message message)
        {
            w = 55;
            h = 55;
            this.template = ItemManager.instance.itemTemplates[id];
            this.quantity = message.ReadInt();
            this.isLock = message.ReadBool();
            int count_option = message.ReadSByte();
            for (int j = 0; j < count_option; j++)
            {
                ItemOption option = new ItemOption();
                option.template = ItemManager.instance.itemOptionTemplates[message.ReadShort()];
                option.param = message.ReadInt();
                this.options.Add(option);
            }
        }

        public void Paint(MyGraphics g, int x, int y, int w, int h)
        {
            int upgrade = GetUpgrade();
            if (upgrade > 0)
            {
                PaintEffectFront(g, x + 2, y + 2, upgrade);
            }
            GraphicManager.instance.Draw(g, template.iconId, x + w / 2, y + h / 2, 0, 3);
            if (upgrade > 0)
            {
                PaintEffectBehind(g, x, y, upgrade);
            }
            if (quantity > 1)
            {
                MyFont.text_mini_yellow.DrawString(g, Utils.FormatNumber(quantity), x + w - 5, y + 30, 1, MyFont.text_mini_grey);
            }
        }

        public void Paint(MyGraphics g, int x, int y, int w, int h, int upgrade)
        {
            if (upgrade > 0)
            {
                PaintEffectFront(g, x + 2, y + 2, upgrade);
            }
            GraphicManager.instance.Draw(g, template.iconId, x + w / 2, y + h / 2, 0, 3);
            if (upgrade > 0)
            {
                PaintEffectBehind(g, x, y, upgrade);
            }
            if (quantity > 1)
            {
                MyFont.text_mini_yellow.DrawString(g, Utils.FormatNumber(quantity), x + w - 5, y + 30, 1, MyFont.text_mini_grey);
            }
        }

        public static void PaintEffectFront(MyGraphics g, int x, int y, int upgrade)
        {
            int index = 0;
            if (upgrade == 19)
            {
                index = 8;
            }
            else if (upgrade == 18)
            {
                index = 7;
            }
            else if (upgrade == 17)
            {
                index = 6;
            }
            else if (upgrade == 16)
            {
                index = 5;
            }
            else if (upgrade >= 14)
            {
                index = 4;
            }
            else if (upgrade >= 12)
            {
                index = 3;
            }
            else if (upgrade >= 8)
            {
                index = 2;
            }
            else if (upgrade >= 4)
            {
                index = 1;
            }
            GraphicManager.instance.Draw(g, fronts[index, indexFront], x, y, 0, StaticObj.TOP_LEFT);
        }

        static int[,] fronts =
        {
            {1622, 1623, 1624, 1625, 1626, 1627, 1628, 1629 },
            {1630, 1631, 1632, 1633, 1634, 1635, 1636, 1637 },
            {1638, 1639, 1640, 1641, 1642, 1643, 1644, 1645 },
            {1646, 1647, 1648, 1649, 1650, 1651, 1652, 1653 },
            {1654, 1655, 1656, 1657, 1658, 1659, 1660, 1661 },
            {1662, 1663, 1664, 1665, 1666, 1667, 1668, 1669 },
            {1670, 1671, 1672, 1673, 1674, 1675, 1676, 1677 },
            {1678, 1679, 1680, 1681, 1682, 1683, 1684, 1685 },
            {1686, 1687, 1688, 1689, 1690, 1691, 1692, 1693 }
        };

        static int[,] behinds =
        {
            {1516, 1517, 1518, 1519, 1520, 1521, 1522, 1523 },
            {1524, 1525, 1526, 1527, 1528, 1529, 1530, 1531 },
            {1532, 1533, 1534, 1535, 1536, 1537, 1538, 1539 },
            {1540, 1541, 1542, 1543, 1544, 1545, 1546, 1547 },
            {1548, 1549, 1550, 1551, 1552, 1553, 1554, 1555 },
            {1556, 1557, 1558, 1559, 1560, 1561, 1562, 1563 },
            {1564, 1565, 1566, 1567, 1568, 1569, 1570, 1571 },
            {1572, 1573, 1574, 1575, 1576, 1577, 1578, 1579 },
            {1580, 1581, 1582, 1583, 1584, 1585, 1586, 1587 }
        };

        public static void PaintEffectBehind(MyGraphics g, int x, int y, int upgrade)
        {
            int index = 0;
            if (upgrade == 19)
            {
                index = 8;
            }
            else if (upgrade == 18)
            {
                index = 7;
            }
            else if (upgrade == 17)
            {
                index = 6;
            }
            else if (upgrade == 16)
            {
                index = 5;
            }
            else if (upgrade >= 14)
            {
                index = 4;
            }
            else if (upgrade >= 12)
            {
                index = 3;
            }
            else if (upgrade >= 8)
            {
                index = 2;
            }
            else if (upgrade >= 4)
            {
                index = 1;
            }
            GraphicManager.instance.Draw(g, behinds[index, indexBehind], x, y, 0, StaticObj.TOP_LEFT);
        }

        public static int indexBehind;
        public static int indexFront;
        public static long tTimeUpdate;

        public static void Update()
        {
            long now = Utils.CurrentTimeMillis();
            if (now - tTimeUpdate > 50)
            {
                tTimeUpdate = now;
                if (indexBehind < 7)
                {
                    indexBehind++;
                }
                else
                {
                    indexBehind = 0;
                }
                if (indexFront < 7)
                {
                    indexFront++;
                }
                else
                {
                    indexFront = 0;
                }
            }
        }

        public void PaintShop(MyGraphics g)
        {
            int xBonus = 0;
            if (isClick)
            {
                xBonus += 10;
                g.DrawImage(Panel.imgItemShopClick, x, y);
            }
            else
            {
                g.DrawImage(!isFocus ? Panel.imgItemShop : Panel.imgItemShopFocus, x, y);
            }
            //g.DrawImage(Panel.imgBgrItem, x + xBonus + 15, y + 15);
            int upgrade = GetUpgrade();
            if (upgrade == 0)
            {
                g.SetColor(Color.black, 0.5f);
                g.FillRect(x + 15 + xBonus, y + 15, 54, 54, 8);
            }
            Paint(g, x + 15 + xBonus, y + 15, Panel.imgBgrItem.GetWidth(), Panel.imgBgrItem.GetHeight(), upgrade);
            int dis = 5;
            string namePrice = " Xu";
            if (typePrice == 1)
            {
                namePrice = " Kim cương";
            }
            else if (typePrice == 2)
            {
                namePrice = " Xu khóa";
            }
            else if (typePrice == 3)
            {
                namePrice = " Ruby";
            }
            else if (typePrice > 3)
            {
                namePrice = " Điểm";
            }
            string str = string.Empty;
            if (upgrade > 0)
            {
                str = " [+" + upgrade + "]";
            }
            string name = template.name + str;
            MyFont myFont = MyFont.text_white;
            if (isClick || myFont.GetWidth(name) > w - Panel.imgBgrItem.GetWidth() - xBonus - 30)
            {
                myFont = MyFont.text_mini_white;
            }
            int hText = myFont.GetHeight();
            int yText = y + (h - hText * 2 - dis) / 2;
            myFont.DrawString(g, name, x + xBonus + 30 + Panel.imgBgrItem.GetWidth(), yText, 0);
            myFont.DrawString(g, "Giá: " + Utils.NumberToString(price) + namePrice, x + xBonus + 30 + Panel.imgBgrItem.GetWidth(), yText + hText + dis, 0);
        }

        public void PaintTrade(MyGraphics g)
        {
            int xBonus = 0;
            if (isClick)
            {
                xBonus += 10;
                g.DrawImage(Panel.imgItemTradeClick, x, y);
            }
            else
            {
                g.DrawImage(!isFocus ? Panel.imgItemTrade : Panel.imgItemTradeFocus, x, y);
            }
            int upgrade = GetUpgrade();
            if (upgrade == 0)
            {
                g.SetColor(Color.black, 0.5f);
                g.FillRect(x + 15 + xBonus, y + 15, 54, 54, 8);
            }
            Paint(g, x + 15 + xBonus, y + 15, Panel.imgBgrItem.GetWidth(), Panel.imgBgrItem.GetHeight(), upgrade);
            int dis = 5;
            string str = string.Empty;
            if (upgrade > 0)
            {
                str = " [+" + upgrade + "]";
            }
            string name = template.name + str;
            MyFont myFont = MyFont.text_white;
            if (isClick || myFont.GetWidth(name) > w - Panel.imgBgrItem.GetWidth() - xBonus - 30)
            {
                myFont = MyFont.text_mini_white;
            }
            int hText = myFont.GetHeight();
            int yText = y + (h - hText * 2 - dis) / 2;
            myFont.DrawString(g, name, x + xBonus + 30 + Panel.imgBgrItem.GetWidth(), yText, 0);
            string text_des = template.description;
            if (quantity > 1)
            {
                text_des = "Số lượng: " + Utils.GetMoneys(quantity);
            }
            else if (options.Count > 0)
            {
                text_des = options[0].GetStrOption();
            }
            myFont.DrawString(g, text_des, x + xBonus + 30 + Panel.imgBgrItem.GetWidth(), yText + hText + dis, 0);
        }

        public Item Clone()
        {
            Item item = new Item();
            item.template = this.template;
            item.quantity = this.quantity;
            item.indexUI = this.indexUI;
            item.typePrice = this.typePrice;
            item.price = this.price;
            foreach (ItemOption i in this.options)
            {
                ItemOption itemOption = new ItemOption();
                itemOption.template = i.template;
                itemOption.param = i.param;
                item.options.Add(itemOption);
            }
            return item;
        }

        public Item NextUpgrade()
        {
            Item item = new Item();
            item.template = this.template;
            item.quantity = this.quantity;
            int upgrade = GetUpgrade();
            if (upgrade == 0)
            {
                this.options.Add(new ItemOption(19, 0));
            }
            foreach (ItemOption i in this.options)
            {
                ItemOption itemOption = new ItemOption();
                itemOption.template = i.template;
                itemOption.param = i.param;
                if (itemOption.template.id == 19)
                {
                    itemOption.param += 1;
                }
                if (itemOption.template.type == 0)
                {
                    int per = 10;
                    if (itemOption.template.id == 0 || itemOption.template.id == 3 || itemOption.template.id == 4)
                    {
                        if (upgrade >= 14)
                        {
                            per = 30;
                        }
                        else if (upgrade >= 12)
                        {
                            per = 25;
                        }
                        else if (upgrade >= 8)
                        {
                            per = 20;
                        }
                        else if (upgrade >= 4)
                        {
                            per = 15;
                        }
                    }
                    int min = itemOption.param * per / 100;
                    if (min < 1)
                    {
                        min = 1;
                    }
                    itemOption.param += min;
                }
                if (itemOption.template.type == 2)
                {
                    itemOption.param += 10;
                }
                item.options.Add(itemOption);
            }

            return item;
        }

        public int GetUpgrade()
        {
            foreach (ItemOption i in this.options)
            {
                int id = i.template.id;
                if (id == 33 || id == 34 || id == 35 || id == 78 || id == 110)
                {
                    return 19;
                }
                if (id == 19)
                {
                    return i.param;
                }
            }
            return 0;
        }

        public int GetParam(int optionId)
        {
            foreach (ItemOption itemOption in options)
            {
                if (itemOption.template.id == optionId)
                {
                    return itemOption.param;
                }
            }
            return 0;
        }

        public bool IsLengendary()
        {
            foreach (ItemOption itemOption in options)
            {
                int id = itemOption.template.id;
                if (id == 33 || id == 34 || id == 35 || id == 78 || id == 110)
                {
                    return true;
                }
            }
            return false;
        }
    }
}
