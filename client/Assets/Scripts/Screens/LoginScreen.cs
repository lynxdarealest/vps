using UnityEngine;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.Actions;
using Assets.Scripts.InputCustoms;
using Assets.Scripts.Models;
using Assets.Scripts.Commons;
using Assets.Scripts.Games;
using Assets.Scripts.Services;
using Assets.Scripts.IOs;
using Assets.Scripts.Displays;
using Assets.Scripts.Commands;
using Assets.Scripts.Networks;

namespace Assets.Scripts.Screens
{
    public class LoginScreen : MyScreen, IActionListener
    {
        public TextField tfUser;

        public TextField tfPass;

        public Command cmdLogin;

        public Command cmdRegister;

        public Image imgPopupLogin;

        public Command cmdClearData;

        public int w;

        public int h;

        public int x;

        public int y;

        private int lastLayoutW = -1;

        private int lastLayoutH = -1;

        public LoginScreen(ScreenManager screenManager) : base(screenManager)
        {
            imgPopupLogin = GameCanvas.LoadImage("MainImages/Logins/popup_login");
            cmdClearData = new Command("MainImages/Logins/btn_clear_data", "MainImages/Logins/btn_clear_data_focus", "MainImages/Logins/btn_clear_data_click", "Xóa dữ liệu", this, 7, null);

            int width = 400;
            tfUser = new TextField("MainImages/Logins/input_login");
            tfUser.name = PlayerText.username;
            tfUser.x = screenManager.w / 2 - tfUser.w / 2;
            tfUser.type = TextField.TYPE_USERNAME;

            tfPass = new TextField("MainImages/Logins/input_login");
            tfPass.name = PlayerText.password;
            tfPass.x = screenManager.w / 2 - tfPass.w / 2;

            tfPass.type = TextField.TYPE_PASSWORD;

            cmdLogin = new Command("MainImages/Logins/btn_login", "MainImages/Logins/btn_login_focus", "MainImages/Logins/btn_login_click", PlayerText.login, this, 1, null);

            cmdRegister = new Command("MainImages/Logins/btn_login", "MainImages/Logins/btn_login_focus", "MainImages/Logins/btn_login_click", PlayerText.register, this, 2, null);

            w = width + 120;
            h = 250;

            string username = Rms.LoadString("username");
            if (username != null)
            {
                tfUser.SetText(username);
            }
            string password = Rms.LoadString("password");
            if (password != null)
            {
                tfPass.SetText(password);
            }

            Relayout();
        }

        private void Relayout()
        {
            if (lastLayoutW == screenManager.w && lastLayoutH == screenManager.h)
            {
                return;
            }

            lastLayoutW = screenManager.w;
            lastLayoutH = screenManager.h;

            int margin = Command.ScaleValue(10);
            int gap = Command.ScaleValue(30);
            int centerGap = Command.ScaleValue(10);
            int panelOffsetY = Command.ScaleValue(40);

            w = Mathf.RoundToInt((400 + 120) * Command.GetUIScale());
            h = Mathf.RoundToInt(250 * Command.GetUIScale());
            x = screenManager.w / 2 - w / 2;
            y = screenManager.h / 2 - h / 2 + panelOffsetY;

            tfUser.x = screenManager.w / 2 - tfUser.w / 2;
            tfPass.x = screenManager.w / 2 - tfPass.w / 2;
            tfUser.y = y + Command.ScaleValue(35);
            tfPass.y = tfUser.y + tfPass.h + gap;

            cmdLogin.x = screenManager.w / 2 - cmdLogin.RenderWidth - centerGap;
            cmdRegister.x = screenManager.w / 2 + centerGap;
            cmdLogin.y = y + h + Command.ScaleValue(25);
            cmdRegister.y = cmdLogin.y;

            cmdClearData.x = screenManager.w - cmdClearData.RenderWidth - margin;
            cmdClearData.y = screenManager.h - cmdClearData.RenderHeight - margin;

        }

        public override void Paint(MyGraphics g)
        {
            Relayout();
            screenManager.PaintBackground(g);
            if (!DisplayManager.instance.dialog.isShow && !GameCanvas.menu.isShow)
            {
                g.DrawImage(screenManager.imgLogo, screenManager.w / 2, y - 30, StaticObj.BOTTOM_HCENTER);

                g.DrawImage(imgPopupLogin, screenManager.w / 2, y - 30, StaticObj.TOP_CENTER);
                cmdLogin.Paint(g);
                cmdRegister.Paint(g);

                tfUser.Paint(g);
                tfPass.Paint(g);

                cmdClearData.Paint(g);
                if (!ServerManager.instance.IsUpdateCompleted())
                {
                    screenManager.PaintLoading(g);
                }
            }
        }

        public override void SwitchToMe()
        {
            tfUser.isFocus = false;
            tfPass.isFocus = false;
            base.SwitchToMe();
            SoundMn.playSoundBgr();
        }

        public override void Update()
        {
            Relayout();
            tfUser.Update();
            tfPass.Update();
            if (screenManager.gameScreen.isAutoLogin && !DisplayManager.instance.dialog.isShow && !GameCanvas.menu.isShow && ServerManager.instance.IsUpdateCompleted() && GameCanvas.gameTick % 200 == 0)
            {
                DoLogin();
            }
        }

        public override void PointerClicked(int x, int y)
        {
            if (!ServerManager.instance.IsUpdateCompleted())
            {
                return;
            }
            if (cmdLogin != null)
            {
                cmdLogin.PointerClicked(x, y);
            }
            if (cmdRegister != null)
            {
                cmdRegister.PointerClicked(x, y);
            }
            if (tfUser != null)
            {
                tfUser.PointerClicked(x, y);
            }
            if (tfPass != null)
            {
                tfPass.PointerClicked(x, y);
            }
            if (cmdClearData.PointerClicked(x, y))
            {
                return;
            }
        }

        public override void PointerReleased(int x, int y)
        {
            if (!ServerManager.instance.IsUpdateCompleted())
            {
                return;
            }
            if (cmdLogin != null)
            {
                cmdLogin.PointerReleased(x, y);
            }
            if (cmdRegister != null)
            {
                cmdRegister.PointerReleased(x, y);
            }
            if (cmdClearData.PointerReleased(x, y))
            {
                return;
            }
        }

        public override void PointerMove(int x, int y)
        {
            if (!ServerManager.instance.IsUpdateCompleted())
            {
                return;
            }
            if (cmdLogin != null)
            {
                cmdLogin.PointerMove(x, y);
            }
            if (cmdRegister != null)
            {
                cmdRegister.PointerMove(x, y);
            }
            if (cmdClearData.PointerMove(x, y))
            {
                return;
            }
        }

        public override void KeyPress(KeyCode keyCode)
        {
            if (!ServerManager.instance.IsUpdateCompleted())
            {
                return;
            }
            if (keyCode == KeyCode.Tab && !DisplayManager.instance.dialog.isShow && !GameCanvas.menu.isShow)
            {
                if (tfUser.isFocus)
                {
                    tfUser.isFocus = false;
                    tfPass.isFocus = true;
                }
                else
                {
                    tfUser.isFocus = true;
                    tfPass.isFocus = false;
                }
            }
            if (keyCode == KeyCode.Return && !DisplayManager.instance.dialog.isShow && !GameCanvas.menu.isShow)
            {
                DoLogin();
            }
            if (tfUser.isFocus)
            {
                tfUser.KeyPress(keyCode);
            }
            else if (tfPass.isFocus)
            {
                tfPass.KeyPress(keyCode);
            }
        }

        public void Perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1:
                    DoLogin();
                    break;
                case 2:
                    {
                        Command yes = new Command("Có", this, 5);
                        Command no = new Command("Không", this, 6);
                        DisplayManager.instance.StartDialogYesNo("Bạn có muốn đăng ký tài khoản tại đây không?\nChọn không để đi đến website đăng ký", yes, no);
                        break;
                    }

                case 4:
                    break;
                case 5:
                    DisplayManager.instance.dialog.Close();
                    if (screenManager.registerScreen == null)
                    {
                        screenManager.registerScreen = new RegisterScreen(screenManager);
                    }
                    screenManager.registerScreen.SwitchToMe();
                    break;
                case 6:
                    DisplayManager.instance.dialog.Close();
                    Application.OpenURL(ServerManager.LINKWEB);
                    break;
                case 7:
                    {
                        Command yes = new Command("Có", this, 8);
                        Command no = new Command("Không", this, 9);
                        DisplayManager.instance.StartDialogYesNo("Bạn có muốn xóa dữ liệu tạm thời của trò chơi không?", yes, no);
                        break;
                    }
                case 8:
                    {
                        Rms.ClearAll();
                        DisplayManager.instance.StartDialogOk("Vui lòng khởi động lại trò chơi", new Command("OK", this, 10));
                        break;
                    }
                case 9:
                    {
                        DisplayManager.instance.dialog.Close();
                        break;
                    }
                case 10:
                    {
                        Main.main.Exit();
                        break;
                    }

                default:
                    break;
            }
        }

        public void ShowServers()
        {
            if (screenManager.serverListScreen == null)
            {
                screenManager.serverListScreen = new ServerListScreen(ScreenManager.instance);
            }
            screenManager.serverListScreen.SwitchToMe();
        }

        public void DoLogin()
        {
            if (tfUser.GetText().Equals(string.Empty))
            {
                GameCanvas.StartDialogOk(PlayerText.userBlank);
                return;
            }
            if (tfPass.GetText().Equals(string.Empty))
            {
                GameCanvas.StartDialogOk(PlayerText.passwordBlank);
                return;
            }
            ServerManager.instance.Connect();
            Service.Login(tfUser.GetText(), tfPass.GetText());
            GameCanvas.startWaitDlg();
            Rms.SaveString("username", tfUser.GetText());
            Rms.SaveString("password", tfPass.GetText());
        }
    }
}
