using Assets.Scripts.Commands;
using Assets.Scripts.Commons;
using Assets.Scripts.Dialogs;
using Assets.Scripts.Games;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.Models;
using Assets.Scripts.Screens;
using UnityEngine;

namespace Assets.Scripts.Displays
{
    public class Dialog
    {
        public bool isShow;

        private string[] info;

        private Command cmdLeft;

        public Command cmdCenter;

        private Command cmdRight;

        private int w;

        private int h;

        private int x;

        private int y;

        private Image imgBgr;

        private long timeStart;

        private long timeRemaining;

        public Dialog()
        {
            imgBgr = GameCanvas.LoadImage("MainImages/Displays/Dialogs/img_bgr_dialog");
        }

        public void Paint(MyGraphics g)
        {
            if (!isShow)
            {
                return;
            }
            g.Reset();
            g.DrawImage(imgBgr, x, y);
            int hText = MyFont.text_white.GetHeight();
            int num = y + (h - info.Length * hText) / 2 - 10;
            for (int i = 0; i < info.Length; i++)
            {
                string text = info[i];
                if (i == info.Length - 1)
                {
                    text += " (" + timeRemaining + ")";
                }
                MyFont.text_white.DrawString(g, text, ScreenManager.instance.w / 2, num, 2);
                num += hText;
            }
            if (cmdLeft != null)
            {
                cmdLeft.Paint(g);
            }
            if (cmdCenter != null)
            {
                cmdCenter.Paint(g);
            }
            if (cmdRight != null)
            {
                cmdRight.Paint(g);
            }
        }

        public void KeyPress(KeyCode keyCode)
        {
            switch (keyCode)
            {
                case KeyCode.F2:
                    if (cmdRight != null)
                    {
                        cmdRight.PerformAction();
                    }
                    break;
                case KeyCode.F1:
                    if (cmdLeft != null)
                    {
                        cmdLeft.PerformAction();
                    }
                    break;
                case KeyCode.KeypadEnter:
                case KeyCode.Return:
                    if (cmdCenter != null)
                    {
                        cmdCenter.PerformAction();
                    }
                    break;
            }
        }

        public void PointerClicked(int x, int y)
        {
            if (cmdCenter != null && cmdCenter.PointerClicked(x, y))
            {
                return;
            }
            if (cmdLeft != null && cmdLeft.PointerClicked(x, y))
            {
                return;
            }
            if (cmdRight != null && cmdRight.PointerClicked(x, y))
            {
                return;
            }
        }

        public void PointerReleased(int x, int y)
        {
            if (cmdCenter != null && cmdCenter.PointerReleased(x, y))
            {
                return;
            }
            if (cmdLeft != null && cmdLeft.PointerReleased(x, y))
            {
                return;
            }
            if (cmdRight != null && cmdRight.PointerReleased(x, y))
            {
                return;
            }
        }

        public void PointerMove(int x, int y)
        {
            if (cmdCenter != null && cmdCenter.PointerMove(x, y))
            {
                return;
            }
            if (cmdLeft != null && cmdLeft.PointerMove(x, y))
            {
                return;
            }
            if (cmdRight != null && cmdRight.PointerMove(x, y))
            {
                return;
            }
        }

        public void Update()
        {
            if (!isShow)
            {
                return;
            }
            long now = Utils.CurrentTimeMillis();
            timeRemaining = (timeStart + 10000 - now) / 1000;
            if (timeRemaining <= 0)
            {
                Close();
            }
        }

        public void Close()
        {
            isShow = false;
        }

        public void SetInfo(string text, Command left, Command center, Command right)
        {
            SoundMn.OpenDialog();
            w = 800;
            info = MyFont.text_white.SplitFontArray(text, w - 100);
            h = 190;
            cmdLeft = left;
            cmdCenter = center;
            cmdRight = right;
            h = info.Length * MyFont.text_white.GetHeight() + 60;
            if (info.Length < 4)
            {
                h = 4 * MyFont.text_white.GetHeight() + 60;
            }
            y = GameCanvas.h - 30 - h;
            x = GameCanvas.w / 2 - w / 2;
            int buttonY = y + h - Command.ScaleValue(14);
            if (left != null)
            {
                cmdLeft.y = buttonY - cmdLeft.RenderHeight;
            }
            if (right != null)
            {
                cmdRight.y = buttonY - cmdRight.RenderHeight;
            }
            if (center != null)
            {
                cmdCenter.x = x + (w - cmdCenter.RenderWidth) / 2;
                cmdCenter.y = buttonY - cmdCenter.RenderHeight;
            }
            if (left != null && right != null)
            {
                int buttonGap = Command.ScaleValue(18);
                int totalButtonsWidth = cmdLeft.RenderWidth + buttonGap + cmdRight.RenderWidth;
                int startX = x + (w - totalButtonsWidth) / 2;
                cmdLeft.x = startX;
                cmdRight.x = startX + cmdLeft.RenderWidth + buttonGap;
            }
            else if (left != null)
            {
                cmdLeft.x = x + (w - cmdLeft.RenderWidth) / 2;
            }
            else if (right != null)
            {
                cmdRight.x = x + (w - cmdRight.RenderWidth) / 2;
            }
            timeStart = Utils.CurrentTimeMillis();
            isShow = true;
        }
    }
}
