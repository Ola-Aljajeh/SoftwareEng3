package com.bankingsystem;

import com.bankingsystem.account.*;
import com.bankingsystem.facade.BankFacade;
import com.bankingsystem.interest.*;
import com.bankingsystem.notification.*;
import com.bankingsystem.transaction.ApprovalStatus;
import com.bankingsystem.transaction.Transaction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * BankingSystemGUI - Interactive demonstration of all 6 design patterns
 * 
 * Showcases:
 * - Composite Pattern: Account hierarchy visualization
 * - Observer Pattern: Real-time notifications panel
 * - Chain of Responsibility: Transaction approval flow
 * - Strategy Pattern: Interest calculation switching
 * - Facade Pattern: Unified BankFacade interface
 * - State Pattern: Account lifecycle management
 */
public class BankingSystemGUI extends JFrame {
    private BankFacade bankFacade;
    private JTree accountTree;
    private DefaultTreeModel treeModel;
    private JTextArea notificationPanel;
    private JLabel totalBalanceLabel;
    private Map<String, Account> accountMap;
    private Map<String, Account> nodeToAccountMap; // Maps node text to account
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    
    // Selected account references
    private Account selectedAccount;
    private Account selectedSourceAccount;
    private JLabel selectedAccountLabel;
    private JLabel selectedSourceLabel;
    
    public BankingSystemGUI() {
        super("Advanced Banking System - Design Patterns Showcase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Initialize banking system
        initializeBankingSystem();
        
        // Build UI
        buildUI();
        
        setVisible(true);
    }
    
    private void initializeBankingSystem() {
        bankFacade = new BankFacade();
        accountMap = new HashMap<>();
        nodeToAccountMap = new HashMap<>();
        
        // Create account hierarchy (demonstrates Composite Pattern)
        Account savingsParent = bankFacade.createSavingsAccount(5000.0);
        Account checkingParent = bankFacade.createCheckingAccount(2000.0);
        Account childSavings = bankFacade.createSavingsAccount(500.0);
        Account childChecking = bankFacade.createCheckingAccount(300.0);
        
        // Create account groups (Composite nodes)
        AccountGroup childrenGroup = bankFacade.createAccountGroup("Children's Accounts");
        childrenGroup.addAccount(childSavings);
        childrenGroup.addAccount(childChecking);
        
        AccountGroup familyGroup = bankFacade.createAccountGroup("Johnson Family");
        familyGroup.addAccount(savingsParent);
        familyGroup.addAccount(checkingParent);
        familyGroup.addAccount(childrenGroup);
        
        // Subscribe to notifications (Observer Pattern)
        NotificationObserver emailObserver = new EmailNotifier("parent@johnson.com");
        NotificationObserver smsObserver = new SMSNotifier("+1-555-0100");
        NotificationObserver inAppObserver = new InAppNotifier("parent_user_123");
        
        bankFacade.subscribeToNotifications(savingsParent, emailObserver);
        bankFacade.subscribeToNotifications(savingsParent, smsObserver);
        bankFacade.subscribeToNotifications(savingsParent, inAppObserver);
        
        // Store accounts for UI
        accountMap.put(savingsParent.getAccountId(), savingsParent);
        accountMap.put(checkingParent.getAccountId(), checkingParent);
        accountMap.put(childSavings.getAccountId(), childSavings);
        accountMap.put(childChecking.getAccountId(), childChecking);
        accountMap.put(childrenGroup.getAccountId(), childrenGroup);
        accountMap.put(familyGroup.getAccountId(), familyGroup);
        
        selectedAccount = savingsParent;
    }
    
    private void buildUI() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left panel: Account hierarchy (Composite)
        mainPanel.add(buildLeftPanel(), BorderLayout.WEST);
        
        // Center panel: Account details and operations
        mainPanel.add(buildCenterPanel(), BorderLayout.CENTER);
        
        // Right panel: Notifications and transactions
        mainPanel.add(buildRightPanel(), BorderLayout.EAST);
        
        // Top panel: Title and balance
        mainPanel.add(buildTopPanel(), BorderLayout.NORTH);
        
        // Bottom panel: Demo buttons
        mainPanel.add(buildBottomPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(33, 150, 243));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Advanced Banking System - Interactive Pattern Showcase");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        totalBalanceLabel = new JLabel("Total Balance: $0.00");
        totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalBalanceLabel.setForeground(Color.WHITE);
        panel.add(Box.createHorizontalStrut(50));
        panel.add(totalBalanceLabel);
        
        return panel;
    }
    
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Account Hierarchy (Composite Pattern)"));
        
        // Build tree (Composite structure)
        buildAccountTree();
        JScrollPane treeScroll = new JScrollPane(accountTree);
        panel.add(treeScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void buildAccountTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Banking System");
        
        DefaultMutableTreeNode johnsons = new DefaultMutableTreeNode("Johnson Family [Group]");
        root.add(johnsons);
        
        nodeToAccountMap.clear();
        
        // Add leaf accounts and nested groups
        Account savingsParent = null;
        for (Account acc : accountMap.values()) {
            if (acc.getAccountType().equals("Savings Account") && acc.getBalance() > 4000) {
                savingsParent = acc;
                break;
            }
        }
        
        if (savingsParent != null) {
            String savingsLabel = String.format("Savings - %s", currencyFormat.format(savingsParent.getBalance()));
            DefaultMutableTreeNode savings = new DefaultMutableTreeNode(savingsLabel);
            nodeToAccountMap.put(savingsLabel, savingsParent);
            johnsons.add(savings);
            
            // Add more accounts...
            for (Account acc : accountMap.values()) {
                if (!acc.equals(savingsParent) && !(acc instanceof AccountGroup)) {
                    String label = String.format("%s - %s", acc.getAccountType(), currencyFormat.format(acc.getBalance()));
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(label);
                    nodeToAccountMap.put(label, acc);
                    johnsons.add(node);
                }
            }
        }
        
        treeModel = new DefaultTreeModel(root);
        accountTree = new JTree(treeModel);
        accountTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) accountTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() != null) {
                String label = (String) node.getUserObject();
                Account acc = nodeToAccountMap.get(label);
                if (acc != null) {
                    selectedAccount = acc;
                    updateAccountDetails();
                }
            }
        });
    }
    
    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Account Operations"));
        
        // Account details
        JPanel detailsPanel = buildDetailsPanel();
        panel.add(detailsPanel, BorderLayout.NORTH);
        
        // Operations
        JPanel opsPanel = buildOperationsPanel();
        panel.add(opsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel buildDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Selected Account Details"));
        
        selectedAccountLabel = new JLabel("Select an account");
        selectedAccountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(new JLabel("Account:"));
        panel.add(selectedAccountLabel);
        
        panel.add(new JLabel("Balance:"));
        JLabel balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(balanceLabel);
        
        panel.add(new JLabel("Type:"));
        JLabel typeLabel = new JLabel("---");
        panel.add(typeLabel);
        
        panel.add(new JLabel("ID:"));
        JLabel idLabel = new JLabel("---");
        idLabel.setFont(new Font("Courier", Font.PLAIN, 10));
        panel.add(idLabel);
        
        panel.add(new JLabel("Interest Strategy:"));
        JComboBox<String> strategyCombo = new JComboBox<>(
            new String[]{"Simple (5%)", "Compound (5%, 12x/year)", "Promotional (8%)"}
        );
        panel.add(strategyCombo);
        
        // Update logic
        accountTree.addTreeSelectionListener(e -> {
            if (selectedAccount != null) {
                selectedAccountLabel.setText(selectedAccount.getDescription().split("\n")[0]);
                balanceLabel.setText(currencyFormat.format(selectedAccount.getBalance()));
                typeLabel.setText(selectedAccount.getAccountType());
                idLabel.setText(selectedAccount.getAccountId());
                updateTotalBalance();
            }
        });
        
        strategyCombo.addActionListener(e -> {
            if (selectedAccount != null) {
                InterestStrategy strategy = switch(strategyCombo.getSelectedIndex()) {
                    case 0 -> new SimpleInterestStrategy(0.05);
                    case 1 -> new CompoundInterestStrategy(0.05, 12);
                    case 2 -> new PromotionalInterestStrategy(0.08, 0.03, 1000);
                    default -> new SimpleInterestStrategy(0.05);
                };
                bankFacade.setInterestStrategy(selectedAccount, strategy);
                appendNotification("Strategy Changed", "Set " + strategyCombo.getSelectedItem() + " for " + selectedAccount.getAccountType());
            }
        });
        
        return panel;
    }
    
    private JPanel buildOperationsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Transaction Operations"));
        
        // Deposit
        JButton depositBtn = new JButton("Deposit $100");
        depositBtn.addActionListener(e -> {
            if (selectedAccount != null) {
                try {
                    bankFacade.deposit(selectedAccount, 100);
                    appendNotification("Deposit", "Deposited $100 to " + selectedAccount.getAccountType());
                    updateAccountDetails();
                } catch (Exception ex) {
                    appendNotification("Error", ex.getMessage());
                }
            }
        });
        panel.add(depositBtn);
        
        // Withdraw
        JButton withdrawBtn = new JButton("Withdraw $50");
        withdrawBtn.addActionListener(e -> {
            if (selectedAccount != null) {
                try {
                    bankFacade.withdraw(selectedAccount, 50);
                    appendNotification("Withdrawal", "Withdrew $50 from " + selectedAccount.getAccountType());
                    updateAccountDetails();
                } catch (Exception ex) {
                    appendNotification("Error", ex.getMessage());
                }
            }
        });
        panel.add(withdrawBtn);
        
        // Apply Interest (Strategy Pattern)
        JButton interestBtn = new JButton("Apply Interest (Strategy)");
        interestBtn.addActionListener(e -> {
            if (selectedAccount != null) {
                double before = selectedAccount.getBalance();
                bankFacade.applyInterest(selectedAccount);
                double after = selectedAccount.getBalance();
                appendNotification("Interest Applied", 
                    String.format("Interest: %s | Balance: %s → %s", 
                        currencyFormat.format(after - before),
                        currencyFormat.format(before),
                        currencyFormat.format(after)));
                updateAccountDetails();
            }
        });
        panel.add(interestBtn);
        
        // Transfer (Chain of Responsibility)
        selectedSourceLabel = new JLabel("Source: Not selected");
        panel.add(selectedSourceLabel);
        
        JButton setSourceBtn = new JButton("Set as Transfer Source");
        setSourceBtn.addActionListener(e -> {
            if (selectedAccount != null) {
                selectedSourceAccount = selectedAccount;
                selectedSourceLabel.setText("Source: " + selectedAccount.getAccountType() + 
                    " (" + currencyFormat.format(selectedAccount.getBalance()) + ")");
            }
        });
        panel.add(setSourceBtn);
        
        JButton transferBtn = new JButton("Transfer $200 (Chain)");
        transferBtn.addActionListener(e -> {
            if (selectedSourceAccount != null && selectedAccount != null && 
                !selectedSourceAccount.equals(selectedAccount)) {
                try {
                    bankFacade.transfer(selectedSourceAccount, selectedAccount, 200);
                    appendNotification("Transfer (Chain of Responsibility)", 
                        "Transferred $200: " + selectedSourceAccount.getAccountType() + 
                        " → " + selectedAccount.getAccountType());
                    updateAccountDetails();
                } catch (Exception ex) {
                    appendNotification("Transfer Failed", ex.getMessage());
                }
            }
        });
        panel.add(transferBtn);
        
        // Refresh
        JButton refreshBtn = new JButton("Refresh Display");
        refreshBtn.addActionListener(e -> {
            buildAccountTree();
            updateAccountDetails();
        });
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Notifications (Observer Pattern)"));
        
        notificationPanel = new JTextArea();
        notificationPanel.setEditable(false);
        notificationPanel.setFont(new Font("Courier", Font.PLAIN, 11));
        notificationPanel.setBackground(new Color(240, 240, 240));
        
        JScrollPane scroll = new JScrollPane(notificationPanel);
        scroll.setPreferredSize(new Dimension(300, 400));
        panel.add(scroll, BorderLayout.CENTER);
        
        // Clear button
        JButton clearBtn = new JButton("Clear Notifications");
        clearBtn.addActionListener(e -> notificationPanel.setText(""));
        panel.add(clearBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Pattern Demonstrations"));
        panel.setBackground(new Color(240, 240, 240));
        
        JButton compositeBtn = new JButton("🎯 Composite: Show Hierarchy");
        compositeBtn.addActionListener(e -> {
            appendNotification("Composite Pattern", 
                "Account groups treat sub-accounts uniformly.\n" +
                "Operations on groups cascade to all children recursively.");
        });
        panel.add(compositeBtn);
        
        JButton observerBtn = new JButton("📢 Observer: Broadcast");
        observerBtn.addActionListener(e -> {
            appendNotification("Observer Pattern", 
                "Multiple observers notified of account events.\n" +
                "See notifications panel for email/SMS/in-app updates.");
        });
        panel.add(observerBtn);
        
        JButton chainBtn = new JButton("🔗 Chain: Approve");
        chainBtn.addActionListener(e -> {
            appendNotification("Chain of Responsibility", 
                "Transactions routed through approval chain:\n" +
                "Auto ($0-$1k) → Manager ($1k-$10k) → Admin (unlimited)");
        });
        panel.add(chainBtn);
        
        JButton strategyBtn = new JButton("⚙️ Strategy: Algorithms");
        strategyBtn.addActionListener(e -> {
            appendNotification("Strategy Pattern", 
                "Interchangeable interest calculation algorithms.\n" +
                "Switch at runtime using dropdown in Operations panel.");
        });
        panel.add(strategyBtn);
        
        JButton facadeBtn = new JButton("🎭 Facade: API");
        facadeBtn.addActionListener(e -> {
            appendNotification("Facade Pattern", 
                "BankFacade simplifies complex subsystem interactions.\n" +
                "All operations route through unified interface.");
        });
        panel.add(facadeBtn);
        
        JButton stateBtn = new JButton("🔄 State: Lifecycle");
        stateBtn.addActionListener(e -> {
            appendNotification("State Pattern", 
                "Accounts have distinct states (Active, Frozen, Suspended, Closed).\n" +
                "Behavior changes based on current state.");
        });
        panel.add(stateBtn);
        
        return panel;
    }
    
    private void updateAccountDetails() {
        if (selectedAccount != null) {
            selectedAccountLabel.setText(selectedAccount.getDescription().split("\n")[0]);
        }
        updateTotalBalance();
    }
    
    private void updateTotalBalance() {
        double total = 0;
        for (Account acc : accountMap.values()) {
            total += acc.getBalance();
        }
        totalBalanceLabel.setText("Total Balance: " + currencyFormat.format(total));
    }
    
    private void appendNotification(String title, String message) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
        notificationPanel.append(String.format("[%s] %s\n%s\n---\n", timestamp, title, message));
        // Auto-scroll to bottom
        notificationPanel.setCaretPosition(notificationPanel.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingSystemGUI());
    }
}
