using Assets.Scripts.Commands;
using Assets.Scripts.Commons;
using Assets.Scripts.Entites.Players;
using Assets.Scripts.Games;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.Models;
using Assets.Scripts.Services;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;

namespace Assets.Scripts.Dialogs
{
    public class MapViewDialog
    {
        private const int RowHeight = 56;
        private const int FirstResponseTimeoutMs = 1200;
        private const int FallbackResponseTimeoutMs = 1200;
        private const int MapsPerSubGroup = 3;

        private const int LEVEL_AREAS = 0;
        private const int LEVEL_SUBGROUPS = 1;

        public bool isShow;

        private bool isWaitingResponse;

        private int x;
        private int y;
        private int w;
        private int h;

        private int xScroll;
        private int yScroll;
        private int wScroll;
        private int hScroll;

        private int firstVisibleIndex;
        private int maxVisibleRows = 1;
        private int hoverIndex = -1;

        private int xClose;
        private int yClose;
        private int wClose;
        private int hClose;

        private int xBack;
        private int yBack;
        private int wBack;
        private int hBack;

        private long waitStartTime;
        private bool fallbackTried;
        private bool waitFailed;

        private int currentLevel = LEVEL_AREAS;

        private readonly List<CmdMap> allMaps = new List<CmdMap>();
        private readonly List<AreaEntry> areaEntries = new List<AreaEntry>();
        private readonly List<SubGroupEntry> currentSubGroups = new List<SubGroupEntry>();

        private static readonly Regex TrailingNumberPattern = new Regex(@"^(.*?)(\d+)$", RegexOptions.Compiled);

        private class AreaEntry
        {
            public string name;
            public bool isGroup;
            public CmdMap singleMap;
            public List<CmdMap> maps;
        }

        private class SubGroupEntry
        {
            public string label;
            public string info;
            public List<CmdMap> maps;
            public CmdMap defaultMap;
        }

        public void RequestOpen()
        {
            isShow = true;
            isWaitingResponse = true;
            allMaps.Clear();
            areaEntries.Clear();
            currentSubGroups.Clear();
            currentLevel = LEVEL_AREAS;
            firstVisibleIndex = 0;
            hoverIndex = -1;
            waitStartTime = Utils.CurrentTimeMillis();
            fallbackTried = false;
            waitFailed = false;
            RecalcLayout();
        }

        public bool IsWaitingResponse()
        {
            return isWaitingResponse;
        }

        public void SetDestinations(List<CmdMap> maps)
        {
            allMaps.Clear();
            for (int i = 0; i < maps.Count; i++)
            {
                allMaps.Add(maps[i]);
            }

            BuildAreaEntries();

            currentLevel = LEVEL_AREAS;
            firstVisibleIndex = 0;
            hoverIndex = -1;
            isWaitingResponse = false;
            isShow = true;
            waitFailed = false;
            RecalcLayout();
        }

        private void BuildAreaEntries()
        {
            areaEntries.Clear();
            currentSubGroups.Clear();

            Dictionary<string, List<CmdMap>> groups = new Dictionary<string, List<CmdMap>>();
            List<CmdMap> ungrouped = new List<CmdMap>();
            List<string> groupOrder = new List<string>();

            for (int i = 0; i < allMaps.Count; i++)
            {
                CmdMap map = allMaps[i];
                string mapName = map.name == null ? string.Empty : map.name.Trim();

                if (mapName.StartsWith("Chỗ cũ:"))
                {
                    AreaEntry entry = new AreaEntry();
                    entry.name = mapName;
                    entry.isGroup = false;
                    entry.singleMap = map;
                    areaEntries.Add(entry);
                    continue;
                }

                Match matcher = TrailingNumberPattern.Match(mapName);
                if (!matcher.Success)
                {
                    ungrouped.Add(map);
                    continue;
                }

                string baseName = matcher.Groups[1].Value.Trim();
                if (string.IsNullOrEmpty(baseName))
                {
                    ungrouped.Add(map);
                    continue;
                }

                string groupKey = baseName.ToLower();
                if (!groups.ContainsKey(groupKey))
                {
                    groups[groupKey] = new List<CmdMap>();
                    groupOrder.Add(groupKey);
                }
                groups[groupKey].Add(map);
            }

            for (int g = 0; g < groupOrder.Count; g++)
            {
                string groupKey = groupOrder[g];
                List<CmdMap> groupMaps = groups[groupKey];

                if (groupMaps.Count >= 2)
                {
                    groupMaps.Sort((a, b) =>
                    {
                        int numA = ParseTrailingNumber(a.name);
                        int numB = ParseTrailingNumber(b.name);
                        return numA.CompareTo(numB);
                    });

                    string baseName = TrailingNumberPattern.Match(groupMaps[0].name).Groups[1].Value.Trim();
                    AreaEntry entry = new AreaEntry();
                    entry.name = baseName;
                    entry.isGroup = true;
                    entry.maps = groupMaps;
                    areaEntries.Add(entry);
                }
                else
                {
                    ungrouped.Add(groupMaps[0]);
                }
            }

            for (int i = 0; i < ungrouped.Count; i++)
            {
                AreaEntry entry = new AreaEntry();
                entry.name = ungrouped[i].name;
                entry.isGroup = false;
                entry.singleMap = ungrouped[i];
                areaEntries.Add(entry);
            }
        }

        private void BuildSubGroups(AreaEntry area)
        {
            currentSubGroups.Clear();

            if (!area.isGroup || area.maps == null || area.maps.Count == 0)
            {
                return;
            }

            int totalMaps = area.maps.Count;
            int subGroupCount = (totalMaps + MapsPerSubGroup - 1) / MapsPerSubGroup;

            for (int s = 0; s < subGroupCount; s++)
            {
                int fromIdx = s * MapsPerSubGroup;
                int toIdx = Mathf.Min(fromIdx + MapsPerSubGroup, totalMaps);

                SubGroupEntry sub = new SubGroupEntry();
                sub.maps = new List<CmdMap>();
                sub.defaultMap = area.maps[fromIdx];

                for (int m = fromIdx; m < toIdx; m++)
                {
                    sub.maps.Add(area.maps[m]);
                }

                string firstNum = ExtractTrailingNumber(area.maps[fromIdx].name);
                string lastNum = ExtractTrailingNumber(area.maps[toIdx - 1].name);

                if (firstNum == lastNum)
                {
                    sub.label = area.name + " " + firstNum;
                }
                else
                {
                    sub.label = area.name + " " + firstNum + "-" + lastNum;
                }

                sub.info = "Bay den " + sub.defaultMap.name;
                currentSubGroups.Add(sub);
            }
        }

        private string ExtractTrailingNumber(string name)
        {
            if (string.IsNullOrEmpty(name))
            {
                return "";
            }
            Match m = TrailingNumberPattern.Match(name);
            return m.Success ? m.Groups[2].Value : "";
        }

        private int ParseTrailingNumber(string name)
        {
            string numStr = ExtractTrailingNumber(name);
            int result;
            if (int.TryParse(numStr, out result))
            {
                return result;
            }
            return 0;
        }

        public void Paint(MyGraphics g)
        {
            if (!isShow)
            {
                return;
            }
            RecalcLayout();
            g.Reset();
            g.SetColor(0, 0, 0, 0.58f);
            g.FillRect(0, 0, GameCanvas.w, GameCanvas.h);

            g.SetColor(30, 36, 48);
            g.FillRect(x, y, w, h, 8);
            g.SetColor(96, 160, 210);
            g.drawRect(x, y, w, h);

            g.SetColor(48, 65, 84);
            g.FillRect(x + 2, y + 2, w - 4, 66, 6);

            string title = currentLevel == LEVEL_AREAS ? "BAN DO CAPSULE" : "CHON KHU VUC";
            MyFont.text_white.DrawString(g, title, x + 20, y + 10, 0);

            string planetName = Map.template != null ? Map.template.GetPlanetName() : "";
            string currentMapInfo = TrimText(Map.name, 30) + " - " + planetName;
            MyFont.text_mini_white.DrawString(g, "Hanh tinh: " + currentMapInfo, x + 20, y + 34, 0);

            DrawButton(g, xClose, yClose, wClose, hClose, "X", false);

            if (currentLevel == LEVEL_SUBGROUPS)
            {
                DrawButton(g, xBack, yBack, wBack, hBack, "<", false);
            }

            if (isWaitingResponse)
            {
                long now = Utils.CurrentTimeMillis();
                int timeoutMs = fallbackTried ? FallbackResponseTimeoutMs : FirstResponseTimeoutMs;
                if (now - waitStartTime >= timeoutMs)
                {
                    if (!fallbackTried && TryFallbackByCapsule())
                    {
                        fallbackTried = true;
                        waitStartTime = now;
                    }
                    else
                    {
                        isWaitingResponse = false;
                        waitFailed = true;
                    }
                }
            }

            if (isWaitingResponse)
            {
                MyFont.text_white.DrawString(g, "Dang tai danh sach map...", x + w / 2, y + h / 2, 2);
                return;
            }

            if (currentLevel == LEVEL_AREAS)
            {
                PaintAreaList(g);
            }
            else
            {
                PaintSubGroupList(g);
            }
        }

        private void PaintAreaList(MyGraphics g)
        {
            if (areaEntries.Count == 0)
            {
                if (waitFailed)
                {
                    MyFont.text_white.DrawString(g, "Khong nhan duoc danh sach map", x + w / 2, y + h / 2 - 14, 2);
                    MyFont.text_mini_white.DrawString(g, "Bam M de thu lai", x + w / 2, y + h / 2 + 12, 2);
                }
                else
                {
                    MyFont.text_white.DrawString(g, "Khong co map trong hanh tinh nay", x + w / 2, y + h / 2, 2);
                }
                return;
            }

            int from = firstVisibleIndex + 1;
            int to = Mathf.Min(areaEntries.Count, firstVisibleIndex + maxVisibleRows);
            MyFont.text_mini_white.DrawString(g, "Khu vuc: " + from + "-" + to + "/" + areaEntries.Count, x + 20, y + 74, 0);

            g.SetClip(xScroll, yScroll, wScroll, hScroll);
            for (int i = 0; i < maxVisibleRows; i++)
            {
                int index = firstVisibleIndex + i;
                if (index >= areaEntries.Count)
                {
                    break;
                }
                DrawAreaRow(g, areaEntries[index], index, yScroll + i * RowHeight);
            }
            g.Reset();
        }

        private void PaintSubGroupList(MyGraphics g)
        {
            if (currentSubGroups.Count == 0)
            {
                MyFont.text_white.DrawString(g, "Khong co khu vuc nao", x + w / 2, y + h / 2, 2);
                return;
            }

            int from = firstVisibleIndex + 1;
            int to = Mathf.Min(currentSubGroups.Count, firstVisibleIndex + maxVisibleRows);
            MyFont.text_mini_white.DrawString(g, "Nhom: " + from + "-" + to + "/" + currentSubGroups.Count, x + 20, y + 74, 0);

            g.SetClip(xScroll, yScroll, wScroll, hScroll);
            for (int i = 0; i < maxVisibleRows; i++)
            {
                int index = firstVisibleIndex + i;
                if (index >= currentSubGroups.Count)
                {
                    break;
                }
                DrawSubGroupRow(g, currentSubGroups[index], index, yScroll + i * RowHeight);
            }
            g.Reset();
        }

        private void DrawAreaRow(MyGraphics g, AreaEntry entry, int index, int rowY)
        {
            bool isHover = index == hoverIndex;

            if (isHover)
            {
                g.SetColor(60, 84, 112);
            }
            else if (index % 2 == 0)
            {
                g.SetColor(40, 48, 62);
            }
            else
            {
                g.SetColor(34, 42, 56);
            }
            g.FillRect(xScroll, rowY, wScroll, RowHeight - 6, 6);
            g.SetColor(72, 92, 112);
            g.drawRect(xScroll, rowY, wScroll, RowHeight - 6);

            string displayName = TrimText(entry.name, 30);

            if (entry.isGroup)
            {
                string countInfo = entry.maps.Count + " khu vuc";
                MyFont.text_white.DrawString(g, displayName, xScroll + 14, rowY + 7, 0);
                MyFont.text_mini_white.DrawString(g, countInfo, xScroll + 14, rowY + 32, 0);
                MyFont.text_mini_yellow.DrawString(g, "CHON >", xScroll + wScroll - 14, rowY + 18, 1);
            }
            else
            {
                bool isCurrentMap = Map.name != null && entry.singleMap != null && entry.singleMap.name != null
                    && entry.singleMap.name.ToLower() == Map.name.ToLower();
                string mapInfo = entry.singleMap != null ? TrimText(entry.singleMap.info, 40) : "";
                MyFont.text_white.DrawString(g, displayName, xScroll + 14, rowY + 7, 0);
                MyFont.text_mini_white.DrawString(g, mapInfo, xScroll + 14, rowY + 32, 0);

                if (isCurrentMap)
                {
                    MyFont.text_mini_yellow.DrawString(g, "DANG O DAY", xScroll + wScroll - 14, rowY + 18, 1);
                }
                else
                {
                    MyFont.text_mini_yellow.DrawString(g, "BAY", xScroll + wScroll - 14, rowY + 18, 1);
                }
            }
        }

        private void DrawSubGroupRow(MyGraphics g, SubGroupEntry sub, int index, int rowY)
        {
            bool isHover = index == hoverIndex;

            if (isHover)
            {
                g.SetColor(60, 84, 112);
            }
            else if (index % 2 == 0)
            {
                g.SetColor(40, 48, 62);
            }
            else
            {
                g.SetColor(34, 42, 56);
            }
            g.FillRect(xScroll, rowY, wScroll, RowHeight - 6, 6);
            g.SetColor(72, 92, 112);
            g.drawRect(xScroll, rowY, wScroll, RowHeight - 6);

            string label = TrimText(sub.label, 30);
            string info = TrimText(sub.info, 44);
            MyFont.text_white.DrawString(g, label, xScroll + 14, rowY + 7, 0);
            MyFont.text_mini_white.DrawString(g, info, xScroll + 14, rowY + 32, 0);
            MyFont.text_mini_yellow.DrawString(g, "BAY", xScroll + wScroll - 14, rowY + 18, 1);
        }

        private void DrawButton(MyGraphics g, int bx, int by, int bw, int bh, string text, bool isActive)
        {
            if (isActive)
            {
                g.SetColor(55, 115, 170);
            }
            else
            {
                g.SetColor(58, 64, 74);
            }
            g.FillRect(bx, by, bw, bh, 6);
            g.SetColor(122, 168, 210);
            g.drawRect(bx, by, bw, bh);
            MyFont.text_mini_white.DrawString(g, text, bx + bw / 2, by + (bh - MyFont.text_mini_white.GetHeight()) / 2, 2);
        }

        private void RecalcLayout()
        {
            w = Mathf.Min(620, GameCanvas.w - 28);
            h = Mathf.Min(500, GameCanvas.h - 28);
            if (w < 360)
            {
                w = 360;
            }
            if (h < 340)
            {
                h = 340;
            }
            x = (GameCanvas.w - w) / 2;
            y = (GameCanvas.h - h) / 2;

            xClose = x + w - 46;
            yClose = y + 13;
            wClose = 30;
            hClose = 30;

            xBack = x + w - 86;
            yBack = y + 13;
            wBack = 30;
            hBack = 30;

            xScroll = x + 20;
            yScroll = y + 96;
            wScroll = w - 40;
            hScroll = h - 116;
            maxVisibleRows = Mathf.Max(1, hScroll / RowHeight);
            ClampFirstVisible();
        }

        public void KeyPress(KeyCode keyCode)
        {
            if (keyCode == KeyCode.Escape || keyCode == KeyCode.M)
            {
                if (currentLevel == LEVEL_SUBGROUPS)
                {
                    GoBackToAreas();
                }
                else
                {
                    Hide();
                }
            }
            else if (keyCode == KeyCode.UpArrow)
            {
                ScrollBy(-1);
            }
            else if (keyCode == KeyCode.DownArrow)
            {
                ScrollBy(1);
            }
            else if (keyCode == KeyCode.Backspace)
            {
                if (currentLevel == LEVEL_SUBGROUPS)
                {
                    GoBackToAreas();
                }
            }
        }

        public void PointerScroll(int a)
        {
            if (!isShow || isWaitingResponse || a == 0)
            {
                return;
            }
            ScrollBy(a > 0 ? -3 : 3);
        }

        public void OnMouseMove(int px, int py)
        {
            if (!isShow || isWaitingResponse)
            {
                return;
            }
            hoverIndex = GetIndexAt(px, py);
        }

        public void OnMouseClick(int px, int py)
        {
            if (!isShow)
            {
                return;
            }
            if (IsInside(px, py, xClose, yClose, wClose, hClose))
            {
                Hide();
                return;
            }
            if (currentLevel == LEVEL_SUBGROUPS && IsInside(px, py, xBack, yBack, wBack, hBack))
            {
                GoBackToAreas();
                return;
            }
            if (isWaitingResponse)
            {
                return;
            }
            int index = GetIndexAt(px, py);
            if (index < 0)
            {
                return;
            }

            if (currentLevel == LEVEL_AREAS)
            {
                if (index >= areaEntries.Count)
                {
                    return;
                }
                AreaEntry entry = areaEntries[index];
                if (entry.isGroup)
                {
                    BuildSubGroups(entry);
                    currentLevel = LEVEL_SUBGROUPS;
                    firstVisibleIndex = 0;
                    hoverIndex = -1;
                }
                else
                {
                    Service.RequestMapSpaceship(entry.singleMap.index);
                    Hide();
                }
            }
            else
            {
                if (index >= currentSubGroups.Count)
                {
                    return;
                }
                SubGroupEntry sub = currentSubGroups[index];
                Service.RequestMapSpaceship(sub.defaultMap.index);
                Hide();
            }
        }

        private void GoBackToAreas()
        {
            currentLevel = LEVEL_AREAS;
            currentSubGroups.Clear();
            firstVisibleIndex = 0;
            hoverIndex = -1;
        }

        private int GetIndexAt(int px, int py)
        {
            if (px < xScroll || py < yScroll || px > xScroll + wScroll || py > yScroll + hScroll)
            {
                return -1;
            }
            int row = (py - yScroll) / RowHeight;
            if (row < 0 || row >= maxVisibleRows)
            {
                return -1;
            }
            int index = firstVisibleIndex + row;
            int maxCount = currentLevel == LEVEL_AREAS ? areaEntries.Count : currentSubGroups.Count;
            return index < maxCount ? index : -1;
        }

        private void ScrollBy(int delta)
        {
            firstVisibleIndex += delta;
            ClampFirstVisible();
            hoverIndex = -1;
        }

        private void ClampFirstVisible()
        {
            int maxCount = currentLevel == LEVEL_AREAS ? areaEntries.Count : currentSubGroups.Count;
            int maxFirst = Mathf.Max(0, maxCount - maxVisibleRows);
            if (firstVisibleIndex < 0)
            {
                firstVisibleIndex = 0;
            }
            if (firstVisibleIndex > maxFirst)
            {
                firstVisibleIndex = maxFirst;
            }
        }

        private bool TryFallbackByCapsule()
        {
            if (Player.me == null || Player.me.itemsBag == null)
            {
                return false;
            }

            for (int i = 0; i < Player.me.itemsBag.Count; i++)
            {
                var item = Player.me.itemsBag[i];
                if (item != null && (item.template.id == 84 || item.template.id == 85))
                {
                    Service.ItemAction(Service.ACTION_USE_ITEM, i);
                    return true;
                }
            }

            InfoMe.addInfo("Khong tim thay Capsule trong tui do", 0);
            return false;
        }

        private bool IsInside(int px, int py, int bx, int by, int bw, int bh)
        {
            return px >= bx && py >= by && px <= bx + bw && py <= by + bh;
        }

        private string TrimText(string text, int maxLength)
        {
            if (string.IsNullOrEmpty(text))
            {
                return string.Empty;
            }
            if (text.Length <= maxLength)
            {
                return text;
            }
            return text.Substring(0, maxLength - 2) + "..";
        }

        public void Show()
        {
            isShow = true;
            isWaitingResponse = false;
        }

        public void Hide()
        {
            isShow = false;
            isWaitingResponse = false;
            hoverIndex = -1;
            waitFailed = false;
            currentLevel = LEVEL_AREAS;
            currentSubGroups.Clear();
        }

        public bool IsShowing()
        {
            return isShow;
        }
    }
}
