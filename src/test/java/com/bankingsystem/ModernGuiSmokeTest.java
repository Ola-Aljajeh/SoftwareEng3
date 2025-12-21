package com.bankingsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import javax.swing.*;
import java.awt.*;

class ModernGuiSmokeTest {
    @Test
    void constructsWithoutThrowing() throws Exception {
        // Skip test in headless environment
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Headless environment - skipping GUI smoke test");

        SwingUtilities.invokeAndWait(() -> {
            ModernBankingSystemGUI gui = new ModernBankingSystemGUI();
            // simple sanity checks
            assert gui.getTitle().contains("BankingSystem");
            gui.dispose();
        });
    }
}
