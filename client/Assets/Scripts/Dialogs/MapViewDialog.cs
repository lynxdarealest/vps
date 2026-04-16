using Assets.Scripts.Commands;
using Assets.Scripts.Games;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.Models;
using Assets.Scripts.Services;
using System.Collections.Generic;
using UnityEngine;

namespace Assets.Scripts.Dialogs
{
    public class MapViewDialog
    {
        private const int RowHeight = 56;

        private readonly List<CmdMap> destinations = new List<CmdMap>();

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

        private int xUp;
        private int yUp;
        private int xDown;
        private int yDown;
        private int wNav;
        private int hNav;

        public void RequestOpen()
        {
            isShow = true;
            isWaitingResponse = true;
            destinations.Clear();
            firstVisibleIndex = 0;
            hoverIndex = -1;
            RecalcLayout();
        }

        public bool IsWaitingResponse()
        {
            return isWaitingResponse;
        }

        public void SetDestinations(List<CmdMap> maps)
        {
            destinations.Clear();
            for (int i = 0; i < maps.Count; i++)
            {
                destinations.Add(maps[i]);
            }

            firstVisibleIndex = 0;
            hoverIndex = -1;
            isWaitingResponse = false;
            isShow = true;
            RecalcLayout();
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
            MyFont.text_white.DrawString(g, "BAN DO CAPSULE", x + 20, y + 10, 0);
            MyFont.text_mini_white.DrawString(g, "Map hien tai: " + TrimText(Map.name, 40), x + 20, y + 34, 0);
            DrawButton(g, xClose, yClose, wClose, hClose, "X", false);

            if (isWaitingResponse)
            {
                MyFont.text_white.DrawString(g, "Dang tai danh sach map...", x + w / 2, y + h / 2, 2);
                return;
            }

            if (destinations.Count == 0)
            {
                MyFont.text_white.DrawString(g, "Khong co map trong hanh tinh nay", x + w / 2, y + h / 2, 2);
                return;
            }

            int from = firstVisibleIndex + 1;
            int to = Mathf.Min(destinations.Count, firstVisibleIndex + maxVisibleRows);
            MyFont.text_mini_white.DrawString(g, "Chon map de bay ngay: " + from + "-" + to + "/" + destinations.Count, x + 20, y + 74, 0);

            g.SetClip(xScroll, yScroll, wScroll, hScroll);
            for (int i = 0; i < maxVisibleRows; i++)
            {
                int index = firstVisibleIndex + i;
                if (index >= destinations.Count)
                {
                    break;
                }
                DrawRow(g, destinations[index], index, yScroll + i * RowHeight);
            }
            g.Reset();

            DrawButton(g, xUp, yUp, wNav, hNav, string.Empty, firstVisibleIndex > 0);
            DrawButton(g, xDown, yDown, wNav, hNav, string.Empty, firstVisibleIndex + maxVisibleRows < destinations.Count);
        }

        private void DrawRow(MyGraphics g, CmdMap map, int index, int rowY)
        {
            bool isHover = index == hoverIndex;
            bool isCurrentMap = Map.name != null && map.name != null && map.name.ToLower() == Map.name.ToLower();

            if (isCurrentMap)
            {
                g.SetColor(56, 92, 72);
            }
            else if (isHover)
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

            string mapName = TrimText(map.name, 34);
            string mapInfo = TrimText(map.info, 54);
            MyFont.text_white.DrawString(g, mapName, xScroll + 14, rowY + 7, 0);
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

            xScroll = x + 20;
            yScroll = y + 96;
            wScroll = w - 40;
            hScroll = h - 158;
            maxVisibleRows = Mathf.Max(1, hScroll / RowHeight);

            hNav = 38;
            wNav = (w - 54) / 2;
            xUp = x + 20;
            yUp = y + h - 50;
            xDown = x + 34 + wNav;
            yDown = yUp;
            ClampFirstVisible();
        }

        public void KeyPress(KeyCode keyCode)
        {
            if (keyCode == KeyCode.M || keyCode == KeyCode.Escape)
            {
                Hide();
            }
            else if (keyCode == KeyCode.UpArrow)
            {
                ScrollBy(-1);
            }
            else if (keyCode == KeyCode.DownArrow)
            {
                ScrollBy(1);
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
            if (isWaitingResponse)
            {
                return;
            }
            if (IsInside(px, py, xUp, yUp, wNav, hNav))
            {
                ScrollBy(-maxVisibleRows);
                return;
            }
            if (IsInside(px, py, xDown, yDown, wNav, hNav))
            {
                ScrollBy(maxVisibleRows);
                return;
            }
            int index = GetIndexAt(px, py);
            if (index < 0 || index >= destinations.Count)
            {
                return;
            }
            Service.RequestMapSpaceship(destinations[index].index);
            Hide();
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
            return index < destinations.Count ? index : -1;
        }

        private void ScrollBy(int delta)
        {
            firstVisibleIndex += delta;
            ClampFirstVisible();
            hoverIndex = -1;
        }

        private void ClampFirstVisible()
        {
            int maxFirst = Mathf.Max(0, destinations.Count - maxVisibleRows);
            if (firstVisibleIndex < 0)
            {
                firstVisibleIndex = 0;
            }
            if (firstVisibleIndex > maxFirst)
            {
                firstVisibleIndex = maxFirst;
            }
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
        }

        public bool IsShowing()
        {
            return isShow;
        }
    }
}
