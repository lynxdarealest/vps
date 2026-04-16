using UnityEngine;
using Assets.Scripts.GraphicCustoms;
using Assets.Scripts.Games;
using System.Threading;
using Assets.Scripts.IOs;
using Assets.Scripts.Models;
using Assets.Scripts.Entites.Players;
using Assets.Scripts.Screens;
using Assets.Scripts.Networks;
using Assets.Scripts.InputCustoms;

public class Main : MonoBehaviour
{
    public static bool isPC;

    public static string mainThreadName;

    private static MyGraphics myGraphic;

    public static Main main;

    public int count;

    public bool isRun;
    
    private int lastScreenWidth = -1;
    private int lastScreenHeight = -1;

    void Start()
    {
        if (Thread.CurrentThread.Name != "Main")
        {
            Thread.CurrentThread.Name = "Main";
        }
        mainThreadName = Thread.CurrentThread.Name;
        Screen.sleepTimeout = SleepTimeout.NeverSleep;
        Screen.orientation = ScreenOrientation.LandscapeLeft;
        Rms.persistentDataPath = Application.persistentDataPath;
        isPC = !(Application.platform == RuntimePlatform.Android || Application.platform == RuntimePlatform.IPhonePlayer);
        if (!isPC)
        {
            Screen.fullScreen = true;
            int safeHeight = Mathf.Max(1, Screen.height);
            Screen.SetResolution(720 * Screen.width / safeHeight, 720, false);
        }
        else
        {
            // Set resolution to full screen width, maintaining aspect ratio
            int screenWidth = Screen.currentResolution.width;
            int screenHeight = Screen.currentResolution.height;
            
            // Maintain aspect ratio of 1024:600
            float targetRatio = 1024f / 600f;
            
            // Use full width, calculate height from ratio
            int finalWidth = screenWidth;
            int finalHeight = (int)(screenWidth / targetRatio);
            
            // If height exceeds screen, use full height and calculate width
            if (finalHeight > screenHeight) {
                finalHeight = screenHeight;
                finalWidth = (int)(screenHeight * targetRatio);
            }
            
            // Try borderless fullscreen mode - removes window borders but doesn't change resolution
            Screen.fullScreenMode = FullScreenMode.Windowed;
            Screen.SetResolution(finalWidth, finalHeight, false);
        }
    }

    void Update()
    {

    }

    void OnApplicationQuit()
    {
        Debug.Log("Application Quit");
        if (count >= 10)
        {
            ServerManager.instance.session.Close();
        }
    }

    public void Exit()
    {
        OnApplicationQuit();
    }

    void FixedUpdate()
    {
        Rms.update();
        count++;
        if (count >= 10)
        {
            Init();
            RefreshScreenSize();
            GameCanvas.Update();
            Image.Update();
            ServerManager.instance.Update();
            count = 10;
        }
    }

    public void Init()
    {
        if (isRun)
        {
            return;
        }
        isRun = true;
        if ((Application.platform == RuntimePlatform.Android) || (Application.platform == RuntimePlatform.IPhonePlayer))
        {
            isPC = false;
        }
        else
        {
            isPC = true;
        }
        mainThreadName = "Main";
        Screen.orientation = ScreenOrientation.LandscapeLeft;
        Application.targetFrameRate = 60;
        Application.runInBackground = true;
        base.useGUILayout = false;
        if (isPC)
        {
            Screen.fullScreen = false;
        }
        ScreenManager.instance.Init();
        ServerManager.instance = new ServerManager(ScreenManager.instance);
        GameCanvas.LoadData();
        SoundMn.Load();
        myGraphic = new MyGraphics();
        myGraphic.CreateLineMaterial();
        main = this;
        RefreshScreenSize();
    }

    private void RefreshScreenSize()
    {
        int currentWidth = Screen.width;
        int currentHeight = Screen.height;
        if (currentWidth == lastScreenWidth && currentHeight == lastScreenHeight)
        {
            return;
        }

        lastScreenWidth = currentWidth;
        lastScreenHeight = currentHeight;

        GameCanvas.w = currentWidth;
        GameCanvas.h = currentHeight;
        ScreenManager.instance.w = currentWidth;
        ScreenManager.instance.h = currentHeight;

        if (ScreenManager.instance.gameScreen != null)
        {
            ScreenManager.instance.gameScreen.InitButton();
        }
    }

    void OnGUI()
    {
        if (count >= 10)
        {
            CheckInput();
            ServerManager.instance.session.Update();
            if (Event.current.type.Equals(EventType.Repaint))
            {
                GameCanvas.Paint(myGraphic);
                myGraphic.Reset();
            }
        }
    }

    void OnHideUnity(bool isGameShown)
    {
        if (!isGameShown)
        {
            Time.timeScale = 0f;
        }
        else
        {
            Time.timeScale = 1f;
        }
    }

    void SetInit()
    {
        base.enabled = true;
    }

    private void CheckInput()
    {
        if (Input.GetMouseButtonDown(0))
        {
            GameCanvas.PointerClicked((int)Input.mousePosition.x, (int)(((float)Screen.height - Input.mousePosition.y)));
        }
        if (Input.GetMouseButtonUp(0))
        {
            GameCanvas.PointerReleased((int)(Input.mousePosition.x), (int)((float)Screen.height - Input.mousePosition.y));
        }

        GameCanvas.PointerMove((int)Input.mousePosition.x, (int)(((float)Screen.height - Input.mousePosition.y)));

        if (Input.anyKeyDown && Event.current.type == EventType.KeyDown
            && (MyKeyMap.KeyInputs.Contains(Event.current.keyCode) || MyKeyMap.KeyActions.Contains(Event.current.keyCode)))
        {
            KeyCode keyCode = Event.current.keyCode;
            if (Input.GetKey(KeyCode.LeftShift) || Input.GetKey(KeyCode.RightShift))
            {
                switch (keyCode)
                {
                    case KeyCode.BackQuote:
                        keyCode = KeyCode.Tilde;
                        break;
                    case KeyCode.Alpha1:
                        keyCode = KeyCode.Exclaim;
                        break;
                    case KeyCode.Alpha2:
                        keyCode = KeyCode.At;
                        break;
                    case KeyCode.Alpha3:
                        keyCode = KeyCode.Hash;
                        break;
                    case KeyCode.Alpha4:
                        keyCode = KeyCode.Dollar;
                        break;
                    case KeyCode.Alpha5:
                        keyCode = KeyCode.Percent;
                        break;
                    case KeyCode.Alpha6:
                        keyCode = KeyCode.Caret;
                        break;
                    case KeyCode.Alpha7:
                        keyCode = KeyCode.Ampersand;
                        break;
                    case KeyCode.Alpha8:
                        keyCode = KeyCode.Asterisk;
                        break;
                    case KeyCode.Alpha9:
                        keyCode = KeyCode.LeftParen;
                        break;
                    case KeyCode.Alpha0:
                        keyCode = KeyCode.RightParen;
                        break;
                    case KeyCode.Minus:
                        keyCode = KeyCode.Underscore;
                        break;
                    case KeyCode.Equals:
                        keyCode = KeyCode.Plus;
                        break;
                    case KeyCode.Quote:
                        keyCode = KeyCode.DoubleQuote;
                        break;
                    case KeyCode.LeftBracket:
                        keyCode = KeyCode.LeftCurlyBracket;
                        break;
                    case KeyCode.RightBracket:
                        keyCode = KeyCode.RightCurlyBracket;
                        break;
                    case KeyCode.Backslash:
                        keyCode = KeyCode.Pipe;
                        break;
                    case KeyCode.Semicolon:
                        keyCode = KeyCode.Colon;
                        break;
                    case KeyCode.Comma:
                        keyCode = KeyCode.Less;
                        break;
                    case KeyCode.Period:
                        keyCode = KeyCode.Greater;
                        break;
                    case KeyCode.Slash:
                        keyCode = KeyCode.Question;
                        break;
                }
            }
            GameCanvas.KeyPress(keyCode);
        }

        if (Event.current.type == EventType.KeyUp && MyKeyMap.KeyActions.Contains(Event.current.keyCode))
        {
            KeyCode keyCode = Event.current.keyCode;
            switch (keyCode)
            {
                case KeyCode.UpArrow:
                    Player.isMoveUp = false;
                    break;
                case KeyCode.LeftArrow:
                    Player.isMoveLeft = false;
                    break;
                case KeyCode.RightArrow:
                    Player.isMoveRight = false;
                    break;
            }
        }

        GameCanvas.PointerScroll((int)(Input.GetAxis("Mouse ScrollWheel") * 10f));
    }
}
