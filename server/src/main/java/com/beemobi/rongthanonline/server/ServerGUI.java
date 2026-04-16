package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.service.ServerService;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

public class ServerGUI extends JFrame {

    private static final Logger logger = Logger.getLogger(ServerGUI.class);
    private JTextArea logArea;
    private JTextField commandField;
    private JButton sendButton;
    private JTable playerTable;
    private DefaultTableModel playerTableModel;
    private JLabel onlineLabel;
    private JLabel uptimeLabel;
    private long startTime;

    public ServerGUI() {
        startTime = System.currentTimeMillis();
        setTitle("Rong Than Online - Bảng Điều Khiển");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Dashboard
        tabbedPane.addTab("Bảng tin", createDashboardPanel());

        // Tab 2: Players
        tabbedPane.addTab("Người chơi", createPlayersPanel());

        // Tab 3: Console
        tabbedPane.addTab("Bảng lệnh", createConsolePanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Window Closing Handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        // Redirect System.out and System.err
        redirectSystemStreams();

        // Start Auto-Refresh Timer (every 5 seconds)
        Timer timer = new Timer(5000, e -> refreshData());
        timer.start();
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Thống kê Server"));
        
        onlineLabel = new JLabel("Trực tuyến: 0");
        onlineLabel.setFont(new Font("Arial", Font.BOLD, 24));
        onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        uptimeLabel = new JLabel("Thời gian chạy: 00:00:00");
        uptimeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        uptimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        statsPanel.add(onlineLabel);
        statsPanel.add(uptimeLabel);
        
        panel.add(statsPanel, BorderLayout.NORTH);

        // Actions Panel
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Thao tác nhanh"));

        JButton stopBtn = new JButton("TẮT SERVER");
        stopBtn.setBackground(Color.RED);
        stopBtn.setForeground(Color.WHITE);
        stopBtn.setFont(new Font("Arial", Font.BOLD, 16));
        stopBtn.addActionListener(e -> confirmExit());

        JButton saveBtn = new JButton("LƯU DỮ LIỆU");
        saveBtn.setBackground(Color.BLUE);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 16));
        saveBtn.addActionListener(e -> {
            try {
                Server.getInstance().saveData();
                JOptionPane.showMessageDialog(this, "Đã lưu dữ liệu thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi lưu dữ liệu: " + ex.getMessage());
            }
        });

        JButton chatBtn = new JButton("THÔNG BÁO SERVER");
        chatBtn.setFont(new Font("Arial", Font.BOLD, 16));
        chatBtn.addActionListener(e -> {
            String msg = JOptionPane.showInputDialog(this, "Nhập nội dung thông báo:");
            if (msg != null && !msg.trim().isEmpty()) {
                if (Server.getInstance().service != null) {
                    Server.getInstance().service.serverNotify(msg);
                    JOptionPane.showMessageDialog(this, "Đã gửi thông báo!");
                }
            }
        });

        JButton maintainBtn = new JButton("BẢO TRÌ");
        maintainBtn.setBackground(Color.ORANGE);
        maintainBtn.setFont(new Font("Arial", Font.BOLD, 16));
        maintainBtn.addActionListener(e -> {
             if (Server.getInstance().service != null) {
                Server.getInstance().service.serverDialog("Máy chủ sẽ bảo trì sau 5 phút. Vui lòng thoát game để lưu dữ liệu.");
                JOptionPane.showMessageDialog(this, "Đã gửi cảnh báo bảo trì!");
            }
        });

        actionsPanel.add(saveBtn);
        actionsPanel.add(chatBtn);
        actionsPanel.add(maintainBtn);
        actionsPanel.add(stopBtn);

        panel.add(actionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPlayersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table Columns
        String[] columns = {"ID", "Tên", "IP", "Vàng", "Ngọc", "Ping"};
        playerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        playerTable = new JTable(playerTableModel);
        playerTable.setRowHeight(25);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Context Menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem kickItem = new JMenuItem("Đuổi (Kick)");
        kickItem.addActionListener(e -> {
            int row = playerTable.getSelectedRow();
            if (row != -1) {
                String name = (String) playerTableModel.getValueAt(row, 1);
                kickPlayer(name);
            }
        });
        popupMenu.add(kickItem);

        playerTable.setComponentPopupMenu(popupMenu);
        playerTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = playerTable.rowAtPoint(e.getPoint());
                    playerTable.setRowSelectionInterval(row, row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(playerTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Làm mới danh sách");
        refreshBtn.addActionListener(e -> refreshPlayerList());
        panel.add(refreshBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createConsolePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Log Area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Command Input
        JPanel inputPanel = new JPanel(new BorderLayout());
        commandField = new JTextField();
        commandField.setFont(new Font("Consolas", Font.PLAIN, 14));
        sendButton = new JButton("Gửi");

        ActionListener sendAction = e -> {
            processCommand(commandField.getText());
            commandField.setText("");
        };

        commandField.addActionListener(sendAction);
        sendButton.addActionListener(sendAction);

        inputPanel.add(commandField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshData() {
        // Update Stats
        int onlineCount = PlayerManager.getInstance().getPlayers().size();
        onlineLabel.setText("Trực tuyến: " + onlineCount);

        long uptime = System.currentTimeMillis() - startTime;
        long seconds = (uptime / 1000) % 60;
        long minutes = (uptime / (1000 * 60)) % 60;
        long hours = (uptime / (1000 * 60 * 60)) % 24;
        uptimeLabel.setText(String.format("Thời gian chạy: %02d:%02d:%02d", hours, minutes, seconds));

        // Only refresh table if Players tab is selected to save performance? 
        // For now, let's just refresh it.
        refreshPlayerList();
    }

    private void refreshPlayerList() {
        playerTableModel.setRowCount(0);
        List<Player> players = PlayerManager.getInstance().getPlayers();
        for (Player p : players) {
            Vector<Object> row = new Vector<>();
            row.add(p.id);
            row.add(p.name);
            row.add(p.session != null ? p.session.ip : "N/A");
            row.add(Utils.formatNumber(p.xu));
            row.add(Utils.formatNumber(p.diamond));
            row.add("0ms"); // Placeholder for ping
            playerTableModel.addRow(row);
        }
    }

    private void kickPlayer(String name) {
        Player player = PlayerManager.getInstance().findPlayerByName(name);
        if (player != null) {
            if (player.session != null) {
                player.session.disconnect();
                System.out.println("Kicked player: " + name);
                JOptionPane.showMessageDialog(this, "Đã đuổi người chơi: " + name);
                refreshPlayerList();
            } else {
                JOptionPane.showMessageDialog(this, "Session người chơi bị null.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy người chơi.");
        }
    }

    private void confirmExit() {
        int confirmed = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn tắt server không?", "Tắt Server",
            JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            Server.getInstance().stop();
            System.exit(0);
        }
    }

    private void updateLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text);
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateLog(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateLog(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));

        // Add Log4j Appender
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        
        // Remove existing ConsoleAppenders to stop duplicate output in console
        java.util.Enumeration appenders = rootLogger.getAllAppenders();
        while (appenders.hasMoreElements()) {
            Object appender = appenders.nextElement();
            if (appender instanceof org.apache.log4j.ConsoleAppender) {
                rootLogger.removeAppender((org.apache.log4j.Appender) appender);
            }
        }

        rootLogger.addAppender(new org.apache.log4j.AppenderSkeleton() {
            @Override
            protected void append(org.apache.log4j.spi.LoggingEvent event) {
                String msg = event.getMessage().toString();
                // Format log message (optional)
                String formattedMsg = String.format("[%s] %s\n", event.getLevel().toString(), msg);
                updateLog(formattedMsg);
            }

            @Override
            public void close() {
            }

            @Override
            public boolean requiresLayout() {
                return false;
            }
        });
    }

    private void processCommand(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            return;
        }
        String cmd = commandLine.trim();
        System.out.println("> " + cmd);
        
        String[] parts = cmd.split("\\s+");
        String command = parts[0].toLowerCase();

        try {
            switch (command) {
                case "stop":
                    confirmExit();
                    break;
                case "save":
                    Server.getInstance().saveData();
                    System.out.println("Server data saved.");
                    break;
                case "online":
                    System.out.println("Online players: " + PlayerManager.getInstance().getPlayers().size());
                    break;
                case "kick":
                    if (parts.length > 1) {
                        kickPlayer(parts[1]);
                    } else {
                        System.out.println("Usage: kick <player_name>");
                    }
                    break;
                case "chat":
                    if (parts.length > 1) {
                        String msg = cmd.substring(5);
                        if (Server.getInstance().service != null) {
                            Server.getInstance().service.serverChat(msg);
                            System.out.println("Broadcasted: " + msg);
                        }
                    }
                    break;
                case "clear":
                    logArea.setText("");
                    break;
                case "help":
                    System.out.println("Available commands:");
                    System.out.println("  stop          - Stop server");
                    System.out.println("  save          - Save data");
                    System.out.println("  online        - Show online count");
                    System.out.println("  kick <name>   - Kick player");
                    System.out.println("  chat <msg>    - Broadcast message");
                    System.out.println("  clear         - Clear console");
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for list.");
            }
        } catch (Exception e) {
            logger.error("Error executing command: " + cmd, e);
        }
    }
}
