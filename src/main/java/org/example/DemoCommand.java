package org.example;

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
