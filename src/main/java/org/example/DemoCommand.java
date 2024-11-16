package org.example;

// Leaf class for running demo version
public class DemoCommand implements Command {
    @Override
    public void execute() {
        BettingAgency.main(new String[]{});
    }

    @Override
    public String name() {
        return "Run Demo Version";
    }
}
