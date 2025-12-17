package com.bankingsystem;

import com.bankingsystem.account.*;
import com.bankingsystem.facade.BankFacade;
import com.bankingsystem.interest.*;
import com.bankingsystem.notification.*;

/**
 * BankingSystemDemo - Demonstrates all patterns working together
 * 
 * Run this to see the complete banking system in action.
 */
public class BankingSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     Advanced Banking System - Design Patterns Showcase         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Initialize the facade
        BankFacade bank = new BankFacade();
        
        // ========== COMPOSITE PATTERN: Create Account Hierarchy ==========
        System.out.println("1. COMPOSITE PATTERN - Account Hierarchy");
        System.out.println("────────────────────────────────────────");
        
        Account parentSavings = bank.createSavingsAccount(5000.0);
        Account parentChecking = bank.createCheckingAccount(2000.0);
        Account childSavings = bank.createSavingsAccount(500.0);
        Account childChecking = bank.createCheckingAccount(300.0);
        
        AccountGroup familyAccounts = bank.createAccountGroup("Johnson Family");
        familyAccounts.addAccount(parentSavings);
        familyAccounts.addAccount(parentChecking);
        
        AccountGroup childAccounts = bank.createAccountGroup("Children's Accounts");
        childAccounts.addAccount(childSavings);
        childAccounts.addAccount(childChecking);
        
        familyAccounts.addAccount(childAccounts); // Nested composite!
        
        System.out.println("Created family account hierarchy");
        System.out.println("Total family balance: $" + String.format("%.2f", bank.getBalance(familyAccounts)));
        System.out.println(bank.getAccountDescription(familyAccounts));
        
        // ========== OBSERVER PATTERN: Subscribe to Notifications ==========
        System.out.println("\n2. OBSERVER PATTERN - Event Notifications");
        System.out.println("─────────────────────────────────────────");
        
        NotificationObserver emailNotifier = new EmailNotifier("parent@johnson.com");
        NotificationObserver smsNotifier = new SMSNotifier("+1-555-0100");
        NotificationObserver inAppNotifier = new InAppNotifier("parent_user_123");
        
        bank.subscribeToNotifications(parentSavings, emailNotifier);
        bank.subscribeToNotifications(parentSavings, smsNotifier);
        bank.subscribeToNotifications(parentSavings, inAppNotifier);
        
        System.out.println("Subscribed to email, SMS, and in-app notifications\n");
        
        // ========== CHAIN OF RESPONSIBILITY: Approval Workflow ==========
        System.out.println("3. CHAIN OF RESPONSIBILITY - Transaction Approval");
        System.out.println("────────────────────────────────────────────────");
        
        System.out.println("\nPerforming small transfer ($500 - auto-approved)...");
        boolean success1 = bank.transfer(parentSavings, childSavings, 500);
        System.out.println("Transfer successful: " + success1);
        System.out.println("Parent savings new balance: $" + String.format("%.2f", bank.getBalance(parentSavings)));
        System.out.println("Child savings new balance: $" + String.format("%.2f", bank.getBalance(childSavings)));
        
        System.out.println("\nPerforming medium transfer ($5,000 - manager-approved)...");
        boolean success2 = bank.transfer(parentSavings, parentChecking, 3000);
        System.out.println("Transfer successful: " + success2);
        
        System.out.println("\nPerforming large transfer ($15,000 - admin-approved)...");
        boolean success3 = bank.transfer(parentSavings, childSavings, 4000);
        System.out.println("Transfer successful: " + success3);
        
        // ========== STRATEGY PATTERN: Interest Calculation ==========
        System.out.println("\n4. STRATEGY PATTERN - Interest Calculation");
        System.out.println("──────────────────────────────────────────");
        
        System.out.println("\nApplying simple interest (5% annually)...");
        bank.setInterestStrategy(parentSavings, new SimpleInterestStrategy(0.05));
        double balanceBeforeSimple = bank.getBalance(parentSavings);
        bank.applyInterest(parentSavings);
        System.out.println("Balance before: $" + String.format("%.2f", balanceBeforeSimple));
        System.out.println("Balance after: $" + String.format("%.2f", bank.getBalance(parentSavings)));
        
        System.out.println("\nSwitching to compound interest (5% annually, compounded 12 times)...");
        bank.setInterestStrategy(parentSavings, new CompoundInterestStrategy(0.05, 12));
        double balanceBeforeCompound = bank.getBalance(parentSavings);
        bank.applyInterest(parentSavings);
        System.out.println("Balance before: $" + String.format("%.2f", balanceBeforeCompound));
        System.out.println("Balance after: $" + String.format("%.2f", bank.getBalance(parentSavings)));
        
        System.out.println("\nApplying promotional interest (8% for qualifying accounts)...");
        bank.setInterestStrategy(childSavings, new PromotionalInterestStrategy(0.08, 0.02, 500));
        double balanceBeforePromo = bank.getBalance(childSavings);
        bank.applyInterest(childSavings);
        System.out.println("Balance before: $" + String.format("%.2f", balanceBeforePromo));
        System.out.println("Balance after: $" + String.format("%.2f", bank.getBalance(childSavings)));
        
        // ========== COMPOSITE + STRATEGY: Group Interest ==========
        System.out.println("\n5. COMPOSITE + STRATEGY - Applying Interest to Group");
        System.out.println("──────────────────────────────────────────────────────");
        
        System.out.println("\nApplying 4% compound interest to entire family...");
        bank.setInterestStrategyForGroup(familyAccounts, new CompoundInterestStrategy(0.04, 4));
        double balanceBeforeGroupInterest = bank.getBalance(familyAccounts);
        bank.applyInterestToGroup(familyAccounts);
        System.out.println("Family balance before: $" + String.format("%.2f", balanceBeforeGroupInterest));
        System.out.println("Family balance after: $" + String.format("%.2f", bank.getBalance(familyAccounts)));
        
        // ========== FINAL STATE ==========
        System.out.println("\n6. FINAL STATE");
        System.out.println("──────────────");
        System.out.println(bank.getAccountDescription(familyAccounts));
        
        // ========== SUMMARY ==========
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        PATTERNS SUMMARY                        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║ ✓ Composite Pattern     : Account hierarchy with recursion   ║");
        System.out.println("║ ✓ Observer Pattern      : Multi-channel notifications        ║");
        System.out.println("║ ✓ Chain of Responsibility: Flexible transaction approval     ║");
        System.out.println("║ ✓ Strategy Pattern      : Runtime-switchable algorithms    ║");
        System.out.println("║ ✓ Facade Pattern        : Simplified client interface       ║");
        System.out.println("║ ✓ State Pattern         : Account lifecycle management      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
    }
}
