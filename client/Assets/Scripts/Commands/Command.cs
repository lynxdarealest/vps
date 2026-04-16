using Assets.Scripts.Actions;
using Assets.Scripts.Games;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.Models;
using Assets.Scripts.Screens;
using System;
using UnityEngine;

namespace Assets.Scripts.Commands
{
    public class Command
    {
        private const float BASE_UI_WIDTH = 1024f;

        private const float BASE_UI_HEIGHT = 600f;

        public string caption;

        public string[] subCaption;

        public IActionListener actionListener;

        public int actionId;

        public int x;

        public int y;

        public int w;

        public int h;

        public bool isFocus;

        public object _object;

        public bool isShow;

        public Image image;

        public Image imageFocus;

        public Image imageClick;

        public bool isClick;

        public int anchor = StaticObj.TOP_LEFT;

        public bool isFollowWithCamera = false;

        public static Image imgButtonMini;

        public static Image imgButtonMiniClicked;

        public static Image imgButtonMiniFocus;

        private static Image imgButton;

        private static Image imgButtonClicked;

        private static Image imgButtonFocus;

        public bool isZoomText = true;

        public float uiScaleMultiplier = 1f;

        public int imageTransform = 0;

        static Command()
        {
            imgButtonMini = GameCanvas.LoadImage("MainImages/GameCanvas/btn_cmd_mini");
            imgButtonMiniClicked = GameCanvas.LoadImage("MainImages/GameCanvas/btn_cmd_mini_click");
            imgButtonMiniFocus = GameCanvas.LoadImage("MainImages/GameCanvas/btn_cmd_mini_focus");
            imgButton = GameCanvas.LoadImage("MainImages/GameCanvas/btn_cmd");
            imgButtonClicked = GameCanvas.LoadImage("MainImages/GameCanvas/btn_cmd_click");
            imgButtonFocus = GameCanvas.LoadImage("MainImages/GameCanvas/btn_cmd_focus");
        }

        public Command()
        {
            isShow = true;
            anchor = StaticObj.TOP_LEFT;
        }

        public Command(Image image, Image imageFocus, Image imageClick, IActionListener actionListener, int action, object p)
        {
            this.image = image;
            this.imageFocus = imageFocus;
            this.imageClick = imageClick;
            actionId = action;
            this.actionListener = actionListener;
            this._object = p;
            if (image != null)
            {
                w = image.GetWidth();
                h = image.GetHeight();
            }
            else
            {
                w = 90;
                h = 90;
            }
            isShow = true;
            anchor = StaticObj.TOP_LEFT;
        }

        public Command(string image, string imageFocus, string imageClick, string caption, IActionListener actionListener, int action, object p)
        {
            this.caption = caption;
            this.image = GameCanvas.LoadImage(image);
            this.imageFocus = GameCanvas.LoadImage(imageFocus);
            this.imageClick = GameCanvas.LoadImage(imageClick);
            actionId = action;
            this.actionListener = actionListener;
            this._object = p;
            if (this.image != null)
            {
                w = this.image.GetWidth();
                h = this.image.GetHeight();
            }
            else
            {
                w = 90;
                h = 90;
            }
            isShow = true;
            anchor = StaticObj.TOP_LEFT;
        }

        public Command(string image, string imageFocus, string imageClick, IActionListener actionListener, int action, object p)
        {
            this.image = GameCanvas.LoadImage(image);
            this.imageFocus = GameCanvas.LoadImage(imageFocus);
            this.imageClick = GameCanvas.LoadImage(imageClick);
            actionId = action;
            this.actionListener = actionListener;
            this._object = p;
            if (this.image != null)
            {
                w = this.image.GetWidth();
                h = this.image.GetHeight();
            }
            else
            {
                w = 90;
                h = 90;
            }
            isShow = true;
            anchor = StaticObj.TOP_LEFT;
        }

        public Command(string caption, IActionListener actionListener, int action, object p)
        {
            image = imgButton;
            imageFocus = imgButtonFocus;
            imageClick = imgButtonClicked;
            this.caption = caption;
            actionId = action;
            this.actionListener = actionListener;
            _object = p;
            w = 220;
            h = 65;
            isShow = true;
        }

        public Command(string caption, IActionListener actionListener, int action)
        {
            image = imgButton;
            imageFocus = imgButtonFocus;
            imageClick = imgButtonClicked;
            this.caption = caption;
            actionId = action;
            this.actionListener = actionListener;
            _object = null;
            w = 220;
            h = 65;
            isShow = true;
        }

        public virtual void Paint(MyGraphics g)
        {
            if (!isShow)
            {
                return;
            }
            if (image != null && imageFocus != null)
            {
                int renderW = GetRenderWidth();
                int renderH = GetRenderHeight();
                Image currentImage = isClick ? imageClick : (!isFocus ? image : imageFocus);
                bool useTransformedImage = imageTransform != 0;
                if (useTransformedImage)
                {
                    if (anchor == StaticObj.TOP_LEFT)
                    {
                        g.DrawImage(x, y, currentImage, imageTransform, StaticObj.TOP_LEFT);
                    }
                    else if (anchor == StaticObj.VCENTER_HCENTER)
                    {
                        g.DrawImage(x, y, currentImage, imageTransform, StaticObj.VCENTER_HCENTER);
                    }
                }
                else
                {
                    if (anchor == StaticObj.TOP_LEFT)
                    {
                        g.drawImageScale(currentImage, x, y, renderW, renderH, 0);
                    }
                    else if (anchor == StaticObj.VCENTER_HCENTER)
                    {
                        g.drawImageScale(currentImage, x - renderW / 2, y - renderH / 2, renderW, renderH, 0);
                    }
                }
                if (isClick && isZoomText)
                {
                    MyFont.text_mini_white.DrawString(g, caption, x + renderW / 2, y + (renderH - MyFont.text_mini_white.GetHeight()) / 2 - 1, 2);
                }
                else if (isFocus)
                {
                    MyFont.text_white.DrawString(g, caption, x + renderW / 2, y + (renderH - MyFont.text_white.GetHeight()) / 2 - 1, 2);
                }
                else
                {
                    MyFont.text_white.DrawString(g, caption, x + renderW / 2, y + (renderH - MyFont.text_white.GetHeight()) / 2 - 1, 2);
                }
            }
        }

        public virtual bool PointerClicked(int x, int y)
        {
            if (!isShow)
            {
                isClick = false;
                isFocus = false;
                return false;
            }
            int xBonus = (isFollowWithCamera ? (-ScreenManager.instance.gameScreen.cmx) : 0);
            int yBonus = (isFollowWithCamera ? (-ScreenManager.instance.gameScreen.cmy) : 0);
            int renderW = GetRenderWidth();
            int renderH = GetRenderHeight();
            if (anchor == StaticObj.VCENTER_HCENTER)
            {
                xBonus -= renderW / 2;
                yBonus -= renderH / 2;
            }
            if (x >= this.x + xBonus && x <= this.x + renderW + xBonus
                && y >= this.y + yBonus && y <= this.y + renderH + yBonus)
            {
                isClick = true;
                return true;
            }
            else
            {
                isClick = false;
            }
            return false;
        }

        public virtual bool PointerReleased(int x, int y)
        {
            if (!isShow)
            {
                isClick = false;
                isFocus = false;
                return false;
            }
            int xBonus = (isFollowWithCamera ? (-ScreenManager.instance.gameScreen.cmx) : 0);
            int yBonus = (isFollowWithCamera ? (-ScreenManager.instance.gameScreen.cmy) : 0);
            int renderW = GetRenderWidth();
            int renderH = GetRenderHeight();
            if (anchor == StaticObj.VCENTER_HCENTER)
            {
                xBonus -= renderW / 2;
                yBonus -= renderH / 2;
            }
            if (!isClick)
            {
                isClick = false;
                return false;
            }
            isClick = false;
            if (x >= this.x + xBonus && x <= this.x + renderW + xBonus
                && y >= this.y + yBonus && y <= this.y + renderH + yBonus)
            {
                int clickThreshold = ScaleValue(40);
                if (Math.Abs(GameCanvas.PointerReleaseX - GameCanvas.PointerClickX) < clickThreshold
                    && Math.Abs(GameCanvas.PointerReleaseY - GameCanvas.PointerClickY) < clickThreshold)
                {
                    PerformAction();
                }

                return true;
            }
            return false;
        }

        public virtual bool PointerMove(int x, int y)
        {
            if (!isShow)
            {
                isClick = false;
                isFocus = false;
                return false;
            }
            int xBonus = (isFollowWithCamera ? (-ScreenManager.instance.gameScreen.cmx) : 0);
            int yBonus = (isFollowWithCamera ? (-ScreenManager.instance.gameScreen.cmy) : 0);
            int renderW = GetRenderWidth();
            int renderH = GetRenderHeight();
            if (anchor == StaticObj.VCENTER_HCENTER)
            {
                xBonus -= renderW / 2;
                yBonus -= renderH / 2;
            }
            if (x >= this.x + xBonus && x <= this.x + renderW + xBonus
                && y >= this.y + yBonus && y <= this.y + renderH + yBonus)
            {
                isFocus = true;
                return true;
            }
            else
            {
                isFocus = false;
            }
            return false;
        }

        public virtual void PerformAction()
        {
            if (actionId > 0 && actionListener != null)
            {
                SoundMn.buttonClick();
                isClick = false;
                isFocus = false;
                actionListener.Perform(actionId, _object);
            }
        }

        public static float GetUIScale()
        {
            float scaleW = GameCanvas.w / BASE_UI_WIDTH;
            float scaleH = GameCanvas.h / BASE_UI_HEIGHT;
            return Mathf.Clamp(Mathf.Min(scaleW, scaleH), 0.8f, 1.35f);
        }

        public static int ScaleValue(int value)
        {
            return Mathf.Max(1, Mathf.RoundToInt(value * GetUIScale()));
        }

        protected int GetRenderWidth()
        {
            return Mathf.Max(1, Mathf.RoundToInt(w * GetUIScale() * uiScaleMultiplier));
        }

        protected int GetRenderHeight()
        {
            return Mathf.Max(1, Mathf.RoundToInt(h * GetUIScale() * uiScaleMultiplier));
        }

        public int RenderWidth => GetRenderWidth();

        public int RenderHeight => GetRenderHeight();
    }
}
