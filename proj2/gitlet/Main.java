package gitlet;

import java.util.*;

import static gitlet.Utils.*;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            message("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (!firstArg.equals("init")) {
            if (!validateWorkDir()){
                return;
            }
            Repository.setupPersistance();
        }
        switch (firstArg) {
            case "init" -> {
                if (validateNumArgs("init", args, 1)) return;
                Repository.init();
            }
            case "add" -> {
                if (validateNumArgs("add", args, 2)) {
                    return;
                }
                Repository.add(args[1]);
            }
            case "commit" -> {
                if (validateNumArgs("add", args, 2)) {
                    return;
                }
                Repository.commit(args[1], new Date());
            }
            case "rm" -> {
                if (validateNumArgs("rm", args, 2)) {
                    return;
                }
                Repository.rm(args[1]);
            }
            case "log" -> {
                if (validateNumArgs("log", args, 1)) {
                    return;
                }
                Repository.log();
            }
            case "global-log" -> {
                if (validateNumArgs("global-log", args, 1)) {
                    return;
                }
                Repository.globalLog();
            }
            case "find" -> {
                if (validateNumArgs("find", args, 2)) {
                    return;
                }
                Repository.find(args[1]);
            }
            case "status" -> {
                if (validateNumArgs("status", args, 1)) {
                    return;
                }
                Repository.status();
            }
            case "checkout" -> {
                if (validateNumArgs("checkout", args, 2, 3, 4)) {
                    return;
                }
                if (args.length == 3) Repository.checkoutFileTo(Repository.head.commit(), args[2]);
                if (args.length == 4) Repository.checkoutFileTo(args[1], args[3]);
                if (args.length == 2) Repository.checkoutBranch(args[1]);
            }
            case "branch" -> {
                if (validateNumArgs("branch", args, 2)) {
                    return;
                }
                Repository.createBranch(args[1]);
            }
            case "rm-branch" -> {
                if (validateNumArgs("rm-branch", args, 2)) {
                    return;
                }
                Repository.rmBranch(args[1]);
            }
            case "reset" -> {
                if (validateNumArgs("reset", args, 2)) {
                    return;
                }
                Repository.reset(args[1]);
            }
            case "merge" -> {
                if (validateNumArgs("merge", args, 2)) {
                    return;
                }
                Repository.merge(args[1]);
            }
            case "add-remote" -> {
                if (validateNumArgs("add-remote", args, 3)) {
                    return;
                }
                Repository.addRemote(args[1], args[2]);
            }
            case "rm-remote" -> {
                if (validateNumArgs("rm-remote", args, 2)) {
                    return;
                }
                Repository.rmRemote(args[1]);
            }
            case "push" -> {
                if (validateNumArgs("push", args, 3)) {
                    return;
                }
                Repository.push(args[1], args[2]);
            }
            case "fetch" -> {
                if (validateNumArgs("fetch", args, 3)) {
                    return;
                }
                Repository.fetch(args[1], args[2]);
            }
            case "pull" -> {
                if (validateNumArgs("pull", args, 3)) {
                    return;
                }
                Repository.pull(args[1], args[2]);
            }
            default -> message("No command with that name exists.");
        }
    }

    public static boolean validateNumArgs(String cmd, String[] args, int... n) {
        if (args.length > 4) return true;
        for (int i : n) {
            if (i == 3) {
                if (args.length == i
                        && cmd.equals("checkout")
                        && args[1].equals("--"))  return false;
            }
            if (i == 4) {
                if (args.length == i
                        && cmd.equals("checkout")
                        && args[2].equals("--")) return false;
                else break;
            }
            if (args.length == i) return false;
        }
        message("Incorrect operands.");
        return true;
    }

    public static boolean validateWorkDir() {
        if (!Repository.exists()) {
            message("Not in an initialized Gitlet directory.");
            return false;
        }
        return true;
    }
}
