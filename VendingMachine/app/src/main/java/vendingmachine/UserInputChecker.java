/*
 * UserInterface.java in Presentation Logic Layer
 */
package vendingmachine;

import java.util.*;

public class UserInputChecker {
    public List<String> availableCommands;

    public UserInputChecker(List<String> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public String commandFormatCheck(String[] split) {
        if (split.length < 1) {
            return "Empty command";
        } else if (!availableCommands.contains(split[0])) {
            return "Invalid command";
        }

        // Use equals or equalsIgnoreCase
        if (split[0].equals("exit")) {
            return this.exitFormatCheck(split);
        } else if (split[0].equals("help")) {
            return this.helpFormatCheck(split);
        } else if (split[0].equals("register")) {
            return this.registerFormatCheck(split);
        } else if (split[0].equals("login")) {
            return this.loginFormatCheck(split);
        } else if (split[0].equals("logout")) {
            return this.logoutFormatCheck(split);
        } else if (split[0].equals("list")) {
            return "valid";
        } else if (split[0].equals("buy")) {
            return this.buyFormatCheck(split);
        } else if (split[0].equals("cancel")) {
            return "valid";
        } else if (split[0].equals("add")) {
            return "valid";
        } else if (split[0].equals("del")) {
            return "valid";
        } else if (split[0].equals("update")) {
            return "valid";
        } else {
            return "Invalid command";
        }
    }

    public String exitFormatCheck(String[] split) {
        if (!split[0].equals("exit")) {
            return "Invalid command";
        } else if (split.length < 1) {
            return "Not enough arguments";
        } else if (split.length > 2) {
            return "Too many arguments";
        }

        return "valid";
    }

    public String helpFormatCheck(String[] split) {
        if (!split[0].equals("help")) {
            return "Invalid command";
        } else if (split.length < 1) {
            return "Not enough arguments";
        } else if (split.length > 2) {
            return "Too many arguments";
        } else if (split.length == 2) {
            if (!Arrays.asList("verbose").contains(split[1])) {
                return "Invalid argument";
            }
        }

        return "valid";
    }

    public String registerFormatCheck(String[] split) {
        if (!split[0].equals("register")) {
            return "Invalid command";
        } else if (split.length < 4) {
            return "Not enough arguments";
        } else if (split.length > 4) {
            return "Too many arguments";
        } else if (!split[1].matches(UserInputFormat.Username)) {
            return "Invalid Username format";
        } else if (!split[2].matches(UserInputFormat.Password)) {
            return "Invalid Password format";
        } else if (!split[3].matches(UserInputFormat.Role)) {
            return "Invalid Role format";
        }

        return "valid";
    }

    public String loginFormatCheck(String[] split) {
        if (!split[0].equals("login")) {
            return "Invalid command";
        } else if (split.length < 3) {
            return "Not enough arguments";
        } else if (split.length > 3) {
            return "Too many arguments";
        } else if (!split[1].matches(UserInputFormat.Username)) {
            return "Invalid Username format";
        } else if (!split[2].matches(UserInputFormat.Password)) {
            return "Invalid Password format";
        }

        return "valid";
    }

    public String logoutFormatCheck(String[] split) {
        if (!split[0].equals("logout")) {
            return "Invalid command";
        } else if (split.length < 1) {
            return "Not enough arguments";
        } else if (split.length > 1) {
            return "Too many arguments";
        }

        return "valid";
    }

    public String buyFormatCheck(String[] split) {
        return "valid";
    }

    public String cancelFormatCheck(String[] split) {
        if (!split[0].equals("cancel")) {
            return "Invalid command";
        } else if (split.length < 1) {
            return "Not enough arguments";
        }

        return "valid";
    }
}
