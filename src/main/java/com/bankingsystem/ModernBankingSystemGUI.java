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

        JButton refreshBtn = createIconButton("Reload", "/icons/transfer.svg");
        refreshBtn.addActionListener(e -> { rebuildTree(); updateAccountCard(); appendNotification("Refresh", "Refreshed view"); });
        toolbar.add(refreshBtn);

        toolbar.addSeparator(new Dimension(8, 0));

        JButton depositBtn = createIconButton("Deposit", "/icons/deposit.svg");
        depositBtn.addActionListener(e -> doDeposit(100));
        toolbar.add(depositBtn);

        JButton withdrawBtn = createIconButton("Withdraw", "/icons/withdraw.svg");
        withdrawBtn.addActionListener(e -> doWithdraw(50));
        toolbar.add(withdrawBtn);

        JButton interestBtn = createIconButton("Interest", "/icons/interest.svg");
        interestBtn.addActionListener(e -> doApplyInterest());
        toolbar.add(interestBtn);

        toolbar.add(Box.createHorizontalGlue());

        JToggleButton themeToggle = new JToggleButton("Dark");
        themeToggle.addActionListener(this::toggleTheme);
        toolbar.add(themeToggle);

        totalBalanceLabel = new JLabel("Total: $0.00");
        totalBalanceLabel.setFont(totalBalanceLabel.getFont().deriveFont(Font.BOLD, 14f));
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(totalBalanceLabel);

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
        notificationList.setCellRenderer(new NotificationCellRenderer());
        right.add(new JScrollPane(notificationList), BorderLayout.CENTER);

        split.setLeftComponent(left);
        JSplitPane centerRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center, right);
        centerRight.setDividerLocation(620);
        split.setRightComponent(centerRight);

        return split;
    }

    private void buildAccountTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bank");
        DefaultMutableTreeNode family = new DefaultMutableTreeNode("Johnson Family");
        root.add(family);

        nodeToAccountMap.clear();

        for (Account acc : accountMap.values()) {
            if (acc instanceof AccountGroup) {
                DefaultMutableTreeNode g = new DefaultMutableTreeNode(acc.getDescription());
                nodeToAccountMap.put(acc.getDescription(), acc);
                family.add(g);
            } else {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode(acc.getDescription().split("\n")[0]);
                nodeToAccountMap.put(n.getUserObject().toString(), acc);
                family.add(n);
            }
        }

        treeModel = new DefaultTreeModel(root);
        accountTree = new JTree(treeModel);
        accountTree.setRootVisible(false);
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
        card.setBorder(new EmptyBorder(8, 8, 8, 8));
        JPanel info = new JPanel(new GridLayout(0, 1, 4, 4));

        selectedAccountLabel = new JLabel("Select an account");
        selectedAccountLabel.setFont(selectedAccountLabel.getFont().deriveFont(Font.BOLD, 16f));
        info.add(selectedAccountLabel);

        JLabel balanceLabel = new JLabel("Balance: $0.00");
        balanceLabel.setName("balanceLabel");
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(14f));
        info.add(balanceLabel);

        JLabel typeLabel = new JLabel("Type: ---");
        info.add(typeLabel);

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
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                Image img = ImageIO.read(is);
                if (img != null) {
                    b.setIcon(new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                } else {
                    // ImageIO couldn't read resource (e.g., SVG). Fallback to text.
                    b.setText(tooltip);
                }
            } else {
                b.setText(tooltip);
            }
        } catch (IOException ex) {
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

    private static class NotificationCellRenderer extends JLabel implements ListCellRenderer<String> {
        NotificationCellRenderer() { setOpaque(true); setBorder(new EmptyBorder(6,6,6,6)); }
        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernBankingSystemGUI());
    }
}
