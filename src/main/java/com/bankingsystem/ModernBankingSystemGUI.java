package com.bankingsystem;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.AccountGroup;
import com.bankingsystem.facade.BankFacade;
import com.bankingsystem.interest.CompoundInterestStrategy;
import com.bankingsystem.interest.InterestStrategy;
import com.bankingsystem.interest.PromotionalInterestStrategy;
import com.bankingsystem.interest.SimpleInterestStrategy;
import com.bankingsystem.notification.NotificationObserver;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ModernBankingSystemGUI extends JFrame {
    private BankFacade bankFacade;
    private JTree accountTree;
    private DefaultTreeModel treeModel;
    private DefaultListModel<String> notificationListModel;
    private JList<String> notificationList;
    private JLabel totalBalanceLabel;
    private Map<String, Account> accountMap;
    private Map<String, Account> nodeToAccountMap;
    private Account selectedAccount;
    private Account selectedSourceAccount;
    private JLabel selectedAccountLabel;
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private boolean darkMode = false;

    public ModernBankingSystemGUI() {
        super("BankingSystem — Modern UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // Apply FlatLaf look & feel
        try {
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf: " + e.getMessage());
        }

        initializeBankingSystem();
        buildUI();

        setVisible(true);
    }

    private void initializeBankingSystem() {
        bankFacade = new BankFacade();
        accountMap = new HashMap<>();
        nodeToAccountMap = new HashMap<>();

        Account savingsParent = bankFacade.createSavingsAccount(5000.0);
        Account checkingParent = bankFacade.createCheckingAccount(2000.0);
        Account childSavings = bankFacade.createSavingsAccount(500.0);
        Account childChecking = bankFacade.createCheckingAccount(300.0);

        AccountGroup childrenGroup = bankFacade.createAccountGroup("Children's Accounts");
        childrenGroup.addAccount(childSavings);
        childrenGroup.addAccount(childChecking);

        AccountGroup familyGroup = bankFacade.createAccountGroup("Johnson Family");
        familyGroup.addAccount(savingsParent);
        familyGroup.addAccount(checkingParent);
        familyGroup.addAccount(childrenGroup);

        NotificationObserver inAppObserver = new com.bankingsystem.notification.InAppNotifier("parent_user_123");
        bankFacade.subscribeToNotifications(savingsParent, inAppObserver);

        accountMap.put(savingsParent.getAccountId(), savingsParent);
        accountMap.put(checkingParent.getAccountId(), checkingParent);
        accountMap.put(childSavings.getAccountId(), childSavings);
        accountMap.put(childChecking.getAccountId(), childChecking);
        accountMap.put(childrenGroup.getAccountId(), childrenGroup);
        accountMap.put(familyGroup.getAccountId(), familyGroup);

        selectedAccount = savingsParent;
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildMainSplit(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JComponent buildTopBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // Search/filter box
        JTextField searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(240, 28));
        searchField.setToolTipText("Search accounts by name/type/balance");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterAccounts(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterAccounts(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterAccounts(); }
            private void filterAccounts() { String q = searchField.getText().trim().toLowerCase(); rebuildTree(q); }
        });
        toolbar.add(searchField);

        toolbar.addSeparator(new Dimension(8, 0));

        JButton refreshBtn = createIconButton("Reload", "/icons/transfer.svg");
        refreshBtn.addActionListener(e -> { rebuildTree(); updateAccountCard(); appendNotification("Refresh", "Refreshed view"); });
        toolbar.add(refreshBtn);

        toolbar.addSeparator(new Dimension(8, 0));

        JButton depositBtn = createIconButton("Deposit", "/icons/deposit.svg");
        depositBtn.setToolTipText("Deposit (Ctrl+D)");
        depositBtn.addActionListener(e -> doDeposit(100));
        toolbar.add(depositBtn);

        JButton withdrawBtn = createIconButton("Withdraw", "/icons/withdraw.svg");
        withdrawBtn.setToolTipText("Withdraw (Ctrl+W)");
        withdrawBtn.addActionListener(e -> doWithdraw(50));
        toolbar.add(withdrawBtn);

        JButton interestBtn = createIconButton("Interest", "/icons/interest.svg");
        interestBtn.setToolTipText("Apply Interest (Ctrl+I)");
        interestBtn.addActionListener(e -> doApplyInterest());
        toolbar.add(interestBtn);

        toolbar.add(Box.createHorizontalGlue());

        JToggleButton themeToggle = new JToggleButton();
        themeToggle.setToolTipText("Toggle dark/light theme");
        // use an SVG icon if available
        themeToggle.setIcon(loadSvgIcon("/icons/theme-toggle.svg", 18));
        themeToggle.addActionListener(this::toggleTheme);
        toolbar.add(themeToggle);

        totalBalanceLabel = new JLabel("Total: $0.00");
        totalBalanceLabel.setFont(totalBalanceLabel.getFont().deriveFont(Font.BOLD, 14f));
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(totalBalanceLabel);

        // Keyboard shortcuts
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();
        im.put(KeyStroke.getKeyStroke("control D"), "deposit");
        am.put("deposit", new AbstractAction() { public void actionPerformed(ActionEvent e) { doDeposit(100); } });
        im.put(KeyStroke.getKeyStroke("control W"), "withdraw");
        am.put("withdraw", new AbstractAction() { public void actionPerformed(ActionEvent e) { doWithdraw(50); } });
        im.put(KeyStroke.getKeyStroke("control I"), "interest");
        am.put("interest", new AbstractAction() { public void actionPerformed(ActionEvent e) { doApplyInterest(); } });

        return toolbar;
    }

    private JComponent buildMainSplit() {
        JSplitPane split = new JSplitPane();
        split.setDividerLocation(280);

        // Left: tree
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createTitledBorder("Accounts"));
        buildAccountTree();
        left.add(new JScrollPane(accountTree), BorderLayout.CENTER);

        // Center: account card and operations
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createTitledBorder("Account Details"));
        center.add(buildAccountCard(), BorderLayout.NORTH);
        center.add(buildOperationsPanel(), BorderLayout.CENTER);

        // Right: notifications
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Notifications"));
        notificationListModel = new DefaultListModel<>();
        notificationList = new JList<>(notificationListModel);
        notificationList.setCellRenderer(new RichNotificationRenderer());
        // Right-click to copy
        notificationList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    int idx = notificationList.locationToIndex(e.getPoint());
                    if (idx >= 0) {
                        String val = notificationListModel.get(idx);
                        java.awt.datatransfer.StringSelection sel = new java.awt.datatransfer.StringSelection(val);
                        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
                        appendNotification("Clipboard", "Copied notification to clipboard");
                    }
                }
            }
        });
        right.add(new JScrollPane(notificationList), BorderLayout.CENTER);

        split.setLeftComponent(left);
        JSplitPane centerRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center, right);
        centerRight.setDividerLocation(620);
        split.setRightComponent(centerRight);

        return split;
    }

    private void buildAccountTree() { buildAccountTree(""); }

    private void buildAccountTree(String query) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bank");
        DefaultMutableTreeNode family = new DefaultMutableTreeNode("Johnson Family");
        root.add(family);

        nodeToAccountMap.clear();

        String q = query == null ? "" : query.trim().toLowerCase();
        for (Account acc : accountMap.values()) {
            String label = acc.getDescription().split("\n")[0];
            String searchable = (label + " " + acc.getAccountType() + " " + String.format("%f", acc.getBalance())).toLowerCase();
            if (!q.isEmpty() && !searchable.contains(q)) continue;

            DefaultMutableTreeNode n;
            if (acc instanceof AccountGroup) {
                n = new DefaultMutableTreeNode(acc.getDescription());
            } else {
                n = new DefaultMutableTreeNode(label);
            }
            nodeToAccountMap.put(n.getUserObject().toString(), acc);
            family.add(n);
        }

        treeModel = new DefaultTreeModel(root);
        accountTree = new JTree(treeModel);
        accountTree.setRootVisible(false);
        accountTree.setCellRenderer(new AccountTreeCellRenderer());
        accountTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) accountTree.getLastSelectedPathComponent();
                    if (node != null) {
                        Account acc = nodeToAccountMap.get(node.getUserObject().toString());
                        if (acc != null) {
                            // Quick action: apply interest on double-click
                            bankFacade.applyInterest(acc);
                            appendNotification("Interest Applied", "Applied to " + acc.getAccountType());
                            updateAccountCard();
                        }
                    }
                }
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) showTreePopup(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) showTreePopup(e);
            }
        });
        accountTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) accountTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() != null) {
                String label = node.getUserObject().toString();
                Account acc = nodeToAccountMap.get(label);
                if (acc != null) {
                    selectedAccount = acc;
                    updateAccountCard();
                }
            }
        });
    }

    private JPanel buildAccountCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 12, 12, 12));
        card.setBackground(new Color(250, 250, 250));

        JPanel info = new JPanel(new GridLayout(0, 1, 6, 6));
        info.setOpaque(false);

        selectedAccountLabel = new JLabel("Select an account");
        selectedAccountLabel.setFont(selectedAccountLabel.getFont().deriveFont(Font.BOLD, 18f));
        info.add(selectedAccountLabel);

        JLabel balanceLabel = new JLabel("$0.00");
        balanceLabel.setName("balanceLabel");
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 24f));
        balanceLabel.setForeground(new Color(33, 150, 243));
        info.add(balanceLabel);

        JLabel typeLabel = new JLabel("Type: ---");
        info.add(typeLabel);

        JPanel quick = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        quick.setOpaque(false);
        JButton depositQuick = createIconButton("Deposit", "/icons/deposit.svg");
        depositQuick.addActionListener(e -> doDeposit(100));
        depositQuick.setToolTipText("Quick deposit $100");
        quick.add(depositQuick);

        JButton withdrawQuick = createIconButton("Withdraw", "/icons/withdraw.svg");
        withdrawQuick.addActionListener(e -> doWithdraw(50));
        withdrawQuick.setToolTipText("Quick withdraw $50");
        quick.add(withdrawQuick);

        JButton transferQuick = createIconButton("Transfer", "/icons/transfer.svg");
        transferQuick.addActionListener(e -> doTransfer(200));
        transferQuick.setToolTipText("Quick transfer $200 (use Set Source first)");
        quick.add(transferQuick);

        info.add(quick);

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildOperationsPanel() {
        JPanel ops = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton setSource = new JButton("Set Source");
        setSource.addActionListener(e -> {
            if (selectedAccount != null) {
                selectedSourceAccount = selectedAccount;
                appendNotification("Source Set", "Source set to " + selectedAccount.getAccountType());
            }
        });
        ops.add(setSource);

        JButton transfer = new JButton("Transfer $200");
        transfer.addActionListener(e -> doTransfer(200));
        ops.add(transfer);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> { rebuildTree(); updateAccountCard(); });
        ops.add(refresh);

        return ops;
    }

    private JComponent buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clear = new JButton("Clear Notifications");
        clear.addActionListener(e -> notificationListModel.clear());
        p.add(clear);
        return p;
    }

    private JButton createIconButton(String tooltip, String resourcePath) {
        JButton b = new JButton();
        b.setToolTipText(tooltip);
        // Try FlatSVGIcon first (provided by flatlaf-extras)
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                try {
                    Class<?> flatSvgClass = Class.forName("com.formdev.flatlaf.extras.FlatSVGIcon");
                    javax.swing.Icon ic = (javax.swing.Icon) flatSvgClass.getConstructor(java.net.URL.class, float.class).newInstance(url, 20f);
                    b.setIcon(ic);
                } catch (Exception inner) {
                    // fallback to ImageIO
                    try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                        if (is != null) {
                            Image img = ImageIO.read(is);
                            if (img != null) b.setIcon(new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                            else b.setText(tooltip);
                        } else b.setText(tooltip);
                    }
                }
            } else {
                b.setText(tooltip);
            }
        } catch (Exception e) {
            b.setText(tooltip);
        }
        return b;
    }

    private void doDeposit(double amount) {
        if (selectedAccount != null) {
            try {
                bankFacade.deposit(selectedAccount, amount);
                appendNotification("Deposit", "Deposited " + currencyFormat.format(amount) + " to " + selectedAccount.getAccountType());
                updateAccountCard();
            } catch (Exception e) {
                appendNotification("Error", e.getMessage());
            }
        }
    }

    private void rebuildTree(String query) {
        buildAccountTree(query);
        SwingUtilities.invokeLater(() -> { accountTree.updateUI(); updateTotalBalance(); });
    }

    private javax.swing.Icon loadSvgIcon(String resourcePath, float size) {
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                try {
                    Class<?> flatSvgClass = Class.forName("com.formdev.flatlaf.extras.FlatSVGIcon");
                    return (javax.swing.Icon) flatSvgClass.getConstructor(java.net.URL.class, float.class).newInstance(url, size);
                } catch (Exception inner) {
                    // fallback
                    try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                        if (is != null) {
                            Image img = ImageIO.read(is);
                            if (img != null) return new ImageIcon(img.getScaledInstance((int)size, (int)size, Image.SCALE_SMOOTH));
                        }
                    }
                }
            }
        } catch (Exception e) {
            // swallow
        }
        return null;
    }

    private void showTreePopup(java.awt.event.MouseEvent e) {
        javax.swing.tree.TreePath path = accountTree.getPathForLocation(e.getX(), e.getY());
        if (path == null) return;
        accountTree.setSelectionPath(path);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        Account acc = nodeToAccountMap.get(node.getUserObject().toString());
        if (acc == null) return;

        JPopupMenu menu = new JPopupMenu();
        JMenuItem deposit = new JMenuItem("Deposit $100");
        deposit.addActionListener(ae -> { bankFacade.deposit(acc, 100); appendNotification("Deposit", "Deposited $100 to " + acc.getAccountType()); updateAccountCard(); });
        menu.add(deposit);

        JMenuItem withdraw = new JMenuItem("Withdraw $50");
        withdraw.addActionListener(ae -> { bankFacade.withdraw(acc, 50); appendNotification("Withdraw", "Withdrew $50 from " + acc.getAccountType()); updateAccountCard(); });
        menu.add(withdraw);

        JMenuItem setSource = new JMenuItem("Set as Source");
        setSource.addActionListener(ae -> { selectedSourceAccount = acc; appendNotification("Source Set", "Set source to " + acc.getAccountType()); updateAccountCard(); });
        menu.add(setSource);

        JMenuItem applyInterest = new JMenuItem("Apply Interest");
        applyInterest.addActionListener(ae -> { bankFacade.applyInterest(acc); appendNotification("Interest", "Applied interest to " + acc.getAccountType()); updateAccountCard(); });
        menu.add(applyInterest);

        menu.show(accountTree, e.getX(), e.getY());
    }

    private void doWithdraw(double amount) {
        if (selectedAccount != null) {
            try {
                bankFacade.withdraw(selectedAccount, amount);
                appendNotification("Withdraw", "Withdrew " + currencyFormat.format(amount) + " from " + selectedAccount.getAccountType());
                updateAccountCard();
            } catch (Exception e) {
                appendNotification("Error", e.getMessage());
            }
        }
    }

    private void doTransfer(double amount) {
        if (selectedSourceAccount != null && selectedAccount != null && !selectedSourceAccount.equals(selectedAccount)) {
            try {
                bankFacade.transfer(selectedSourceAccount, selectedAccount, amount);
                appendNotification("Transfer", "Transferred " + currencyFormat.format(amount) + " from " + selectedSourceAccount.getAccountType() + " to " + selectedAccount.getAccountType());
                updateAccountCard();
            } catch (Exception e) {
                appendNotification("Transfer Failed", e.getMessage());
            }
        }
    }

    private void doApplyInterest() {
        if (selectedAccount != null) {
            double before = selectedAccount.getBalance();
            bankFacade.applyInterest(selectedAccount);
            double after = selectedAccount.getBalance();
            appendNotification("Interest", "Applied interest: " + currencyFormat.format(after - before));
            updateAccountCard();
        }
    }

    private void rebuildTree() {
        buildAccountTree();
        SwingUtilities.invokeLater(() -> { accountTree.updateUI(); updateTotalBalance(); });
    }

    private void updateAccountCard() {
        SwingUtilities.invokeLater(() -> {
            if (selectedAccount != null) {
                selectedAccountLabel.setText(selectedAccount.getDescription().split("\n")[0]);
            }
            updateTotalBalance();
        });
    }

    private void updateTotalBalance() {
        double total = 0;
        for (Account acc : accountMap.values()) total += acc.getBalance();
        totalBalanceLabel.setText("Total: " + currencyFormat.format(total));
    }

    private void appendNotification(String title, String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        notificationListModel.addElement(String.format("[%s] %s - %s", timestamp, title, message));
        notificationList.ensureIndexIsVisible(notificationListModel.size()-1);
    }

    private void toggleTheme(ActionEvent e) {
        darkMode = !darkMode;
        try {
            if (darkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            appendNotification("Theme", "Failed to switch theme: " + ex.getMessage());
        }
    }

    private static class RichNotificationRenderer extends JPanel implements ListCellRenderer<String> {
        private JLabel title = new JLabel();
        private JLabel time = new JLabel();
        RichNotificationRenderer() {
            setLayout(new BorderLayout(6,6));
            setBorder(new EmptyBorder(6,6,6,6));
            title.setFont(title.getFont().deriveFont(Font.BOLD, 12f));
            time.setFont(time.getFont().deriveFont(Font.PLAIN, 11f));
            add(title, BorderLayout.CENTER);
            add(time, BorderLayout.EAST);
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            // Split by ' - ' to heuristically get short title/time
            String display = value;
            String[] parts = value.split(" - ", 2);
            title.setText(parts.length>1?parts[1]:value);
            time.setText(parts.length>0?parts[0].replaceAll("\[|\]","") : "");
            if (isSelected) setBackground(list.getSelectionBackground()); else setBackground(list.getBackground());
            setOpaque(true);
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernBankingSystemGUI());
    }
}
