package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

public class Repository {
    /**
     * Dirs:
     * -CWD: The current working directory.
     * -GITLET_DIR: The .gitlet directory.
     * -objects: all saved files, named by hash of their content.
     * -commits: all commits, named by hash of their content.
     * <p>
     * Files:
     * -addition_file: an area of staging file content for addition, a TreeMap of filename and a map of its hash of its content and its content(byte[]).
     * -deletion_file: an area of staging filename for deletion.
     * -head_file: which contains all properties of head pointer, includes the hash of head commit and in which branch.
     * -branches_files: a TreeMap of the name of branch and its tail.
     * <p>
     * Fields:  (fileHash=sha1(readcontent(file)))
     * -addition: TreeMap<file's name, Map.Entry<files hash, readcontent(file)>>
     * -deletion: TreeSet<file's name>
     * -branches: TreeMap<<branch's name, tail's hash>>
     */

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static File objects = join(GITLET_DIR, "objects");
    public static File commits = join(GITLET_DIR, "commits");

    public static File branches_file = join(GITLET_DIR, "branches");
    public static File addition_file = join(GITLET_DIR, "addition");
    public static File deletion_file = join(GITLET_DIR, "deletion");
    public static File head_file = join(GITLET_DIR, "head");
    public static File UIDs_file = join(GITLET_DIR, "commitUIDs");
    public static File remotes_file = join(GITLET_DIR, "remotes");


    public static TreeSet<String> commitUIDs;
    public static TreeMap<String, Map.Entry<String, byte[]>> addition;
    public static TreeSet<String> deletion;
    public static Head head;
    public static TreeMap<String, String> branches;
    public static HashMap<String, String> remotes;

    public static void init(){
        if (!GITLET_DIR.mkdir()) {
            message("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        commits.mkdir();
        objects.mkdir();

        commitUIDs = new TreeSet<>();
        writeObject(UIDs_file, commitUIDs);
        addition = new TreeMap<>();
        writeObject(addition_file, addition);
        deletion = new TreeSet<>();
        writeObject(deletion_file, deletion);
        remotes = new HashMap<>();
        writeObject(remotes_file, remotes);
        branches = new TreeMap<>();

        head = new Head();         //Creat a head
        branches.put(head.branch, head.UID);  //head.branch = "master"
        writeObject(branches_file, branches);
    }

    @SuppressWarnings("unchecked")
    public static void setupPersistance() {
        commitUIDs = readObject(UIDs_file, TreeSet.class);
        addition = readObject(addition_file, TreeMap.class);
        deletion = readObject(deletion_file, TreeSet.class);
        branches = readObject(branches_file, TreeMap.class);
        remotes = readObject(remotes_file, HashMap.class);
        head = readObject(head_file, Head.class);
        head.file = head_file;
    }

    public static void add(String filename) {
        if (!plainFilenamesIn(CWD).contains(filename)) {
            message("File does not exist.");
            return;
        }
        if (deletion.contains(filename)) {
            deletion.remove(filename);
            writeObject(deletion_file, deletion);
            return;
        }
        File file = getFileinCWD(filename);
        byte[] content = readContents(file);
        String fileUID = sha1(content);
        if (head.commit().files().contains(filename)) {
            String fileinHead = head.commit().metadata.get(filename);
            if (fileinHead.equals(fileUID)) {
                addition.remove(filename);
                return;
            }
        }
        Map.Entry<String, byte[]> newfile = new AbstractMap.SimpleEntry<>(fileUID, content);
        addition.put(filename, newfile);
        writeObject(addition_file, addition);
    }

    public static void commit(String message, Date timestamp) {
        if (addition.isEmpty() && deletion.isEmpty()) {
            message("No changes added to the commit.");
            return;
        }
        if (message.equals("")) {
            message("Please enter a commit message.");
            return;
        }
        Commit newcommit = new Commit(head.UID, message, timestamp);
        head.moveTo(newcommit);
        branches.put(head.branch, head.UID);
        writeObject(branches_file, branches);
    }

    public static void rm(String filename) {
        boolean staged = addition.containsKey(filename);
        boolean tracked = head.commit().files().contains(filename);
        if (staged) {
            addition.remove(filename);
            writeObject(addition_file, addition);
        }
        if (tracked) {
            deletion.add(filename);
            restrictedDelete(filename);
            writeObject(deletion_file, deletion);
        }
        if (!staged && !tracked) {
            message("No reason to remove the file.");
        }
    }

    public static void log() {
        for (Commit ptr = head.commit(); ptr != null; ptr = ptr.parent()) {
            printCommit(ptr);
        }
    }

    public static void globalLog() {
        List<String> allCommitsUID = plainFilenamesIn(commits);
        for (String UID : allCommitsUID) {
            printCommit(Commit.getCommit(UID));
        }
    }

    private static void printCommit(Commit commit) {
        if (commit._parentUID == null) {
            System.out.println(String.format(Locale.ENGLISH,
                    "===\ncommit %1$2s\nDate: %2$ta %2$tb %2$te %2$tT %2$tY %2$tz\n%3$2s\n",
                    commit.UID, commit.timestamp, commit.message));
        } else {
            System.out.println(String.format(Locale.ENGLISH,
                    "===\ncommit %1$2s\nMerge: %2$2s %3$2s\nDate: %4$ta %4$tb %4$te %4$tT %4$tY %4$tz\n%5$2s\n",
                    commit.UID, commit.parentUID.substring(0, 7), commit._parentUID.substring(0, 7), commit.timestamp, commit.message));
        }
    }

    public static void find(String message) {
        List<String> allCommitsUID = plainFilenamesIn(commits);
        boolean hasfind = false;
        for (String UID : allCommitsUID) {
            Commit commit = Commit.getCommit(UID);
            if (commit.message.equals(message)) {
                hasfind = true;
                System.out.println(UID);
            }
        }
        if (!hasfind) {
            message("Found no commit with that message.");
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        for (String branch : branches.keySet()) {
            if (branch.equals(head.branch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String filename : addition.keySet()) {
            System.out.println(filename);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String filename : deletion) {
            System.out.println(filename);
        }
        System.out.println();

        TreeSet<String> MNSFC = new TreeSet<>();
        TreeSet<String> UF = new TreeSet<>();

        TreeMap<String, String> allFiles = new TreeMap<>(); //<pathname, content>
        for (String filename : plainFilenamesIn(CWD)) {
            File file = getFileinCWD(filename);
            allFiles.put(filename, sha1(readContents(file)));
        }

        TreeMap<String, String> additionCopy = new TreeMap<>();
        for (Map.Entry<String, Map.Entry<String, byte[]>> entry : addition.entrySet()) {
            String filename = entry.getKey();
            String fileUID = entry.getValue().getKey();
            additionCopy.put(filename, fileUID);
        }

        TreeMap<String, String> headCopy = new TreeMap<>();
        headCopy.putAll(head.commit().metadata);

        for (Map.Entry<String, String> file : allFiles.entrySet()) {
            String filename = file.getKey();
            String UIDofCWD = file.getValue();
            String UIDofHead = headCopy.remove(filename);
            String UIDofAdd = additionCopy.remove(filename);

            //Tracked in the current commit, changed in the working directory, but not staged
            if (UIDofHead != null && UIDofAdd == null && !UIDofHead.equals(UIDofCWD)) {
                MNSFC.add(filename + " (modified)");
                continue;
            }
            //Staged for addition, but with different contents than in the working directory
            if (UIDofAdd != null && !UIDofAdd.equals(UIDofCWD)) {
                MNSFC.add(filename + " (modified)");
                continue;
            }
            if (UIDofHead == null && UIDofAdd == null) {
                UF.add(filename);
            }
        }
        //Staged for addition, but deleted in the working directory
        for (String filename : additionCopy.keySet()) MNSFC.add(filename + " (deleted)");
        //Not staged for removal, but tracked in the current commit and deleted from the working directory
        for (String filename : headCopy.keySet()) {
            if (!deletion.contains(filename)) MNSFC.add(filename + " (deleted)");
        }

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String file : MNSFC) System.out.println(file);
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String file : UF) System.out.println(file);
        System.out.println();
    }

    public static void checkoutFileTo(String UID, String filename) {
        Commit commit = Commit.getCommit(UID);
        if (commit == null) return;
        checkoutFileTo(commit, filename);
    }

    public static void checkoutFileTo(Commit commit, String filename) {
        byte[] content = getContentinObjects(commit.getFileUID(filename));
        if (content == null) return;
        File file = getFileinCWD(filename);
        writeContents(file, content);
    }

    public static void checkoutBranch(String branch) {
        if (!branches.containsKey(branch)) {
            message("No such branch exists.");
            return;
        }
        if (head.branch.equals(branch)) {
            message("No need to checkout the current branch.");
            return;
        }
        Commit destination = getBranch(branch);
        checkoutFiles(destination);
        head.branch = branch;
        head.moveTo(destination);
    }

    public static void createBranch(String branch) {
        if (branches.containsKey(branch)) {
            message("A branch with that name already exists.");
            return;
        }
        branches.put(branch, head.UID);
        writeObject(branches_file, branches);
    }

    public static void rmBranch(String branch) {
        if (branch.equals(head.branch)) {
            message("Cannot remove the current branch.");
            return;
        }
        String res = branches.remove(branch);
        if (res == null) {
            message("A branch with that name does not exist.");
            return;
        }
        writeObject(branches_file, branches);
    }

    public static void reset(String UID) {
        Commit destination = Commit.getCommit(UID);
        if (destination == null) return;
        checkoutFiles(destination);
        head.moveTo(destination);
        branches.put(head.branch, head.UID);
        writeObject(branches_file, branches);
    }

    private static void checkoutFiles(Commit destination) {
        if (!destination.UID.equals(head.UID)) {
            Set<Map.Entry<String, String>> filesinDest = destination.entries();
            Set<String> filesinHead = head.commit().files();
            List<String> allFiles = plainFilenamesIn(CWD);
            for (Map.Entry<String, String> fileEntries : filesinDest) {
                String filename = fileEntries.getKey();
                if (!filesinHead.contains(filename) && allFiles.contains(filename)) {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
            }
            for (String filename : filesinHead) {
                if (!filesinDest.contains(filename)) {
                    restrictedDelete(getFileinCWD(filename));
                }
            }
            for (Map.Entry<String, String> fileEntries : filesinDest) {
                String filename = fileEntries.getKey();
                String fileUID = fileEntries.getValue();
                byte[] content = getContentinObjects(fileUID);
                File file = getFileinCWD(filename);
                writeContents(file, content);
            }
        }
        addition.clear();
        writeObject(addition_file, addition);
    }

    public static void merge(String branch) {
        if (!addition.isEmpty() || !deletion.isEmpty()) {
            message("You have uncommitted changes.");
            return;
        }
        Commit other = getBranch(branch);
        if (other == null) {
            return;
        }
        if (other.UID.equals(head.UID)) {
            message("Cannot merge a branch with itself.");
            return;
        }

        Commit split = latestCommonAncestor(head.commit(), other);
        if (other.UID.equals(split.UID)) {
            message("Given branch is an ancestor of the current branch.");
            return;
        }
        if (head.UID.equals(split.UID)) {
            checkoutBranch(branch);
            message("Current branch fast-forwarded.");
            return;
        }

        HashMap<String, String> headCopy = new HashMap<>();
        headCopy.putAll(head.commit().metadata);
        // if there are unstaged changes to a file that would be changed by a merge.
        TreeSet<String> changedButNotStaged = new TreeSet<>();
        for (Map.Entry<String, String> file : headCopy.entrySet()) {
            String filename = file.getKey();
            String UIDofHead = file.getValue();
            File fileinCWD = getFileinCWD(filename);
            if (!fileinCWD.exists()) continue;
            String UIDofCWD = sha1(readContents(fileinCWD));
            if (!UIDofHead.equals(UIDofCWD)) {
                Map.Entry<String, byte[]> fileinAdd = addition.get(filename);
                if (fileinAdd == null || !fileinAdd.getKey().equals(UIDofCWD)) {
                    changedButNotStaged.add(filename);
                }
            }
        }
        if (!changedButNotStaged.isEmpty()) {
            message("Your local changes to the following files would be overwritten by merge:");
            for (String file : changedButNotStaged) {
                System.out.println(file);
            }
        }

        List<String> allFiles = plainFilenamesIn(CWD);
        TreeMap<String, String> splitCopy = new TreeMap<>();
        splitCopy.putAll(split.metadata);
        TreeMap<String, String> otherCopy = new TreeMap<>();
        otherCopy.putAll(other.metadata);
        HashMap<String, String> Maddition = new HashMap<>();

        /**
         * for each file in the given branch:  (g represent the file in the given branch , h represent the file in the head, s represent the file in the split point)
         *   if it not in the split point:
         *       if it not in the head:
         *           if it in CWD, goto WARNING;                         1
         *           if it not in CWD, stage it for addition.            2
         *       if it in the head:
         *           g == h, merge do nothing;
         *           g != j, goto CONFLICT;                              3
         *   if it in the split point:
         *       if it not in the head:
         *           g == s, keep untracked;
         *           g != s, goto CONFLICT.                              4
         *       if it in the head:
         *           g == h == s, merge do nothing;
         *           g == h, g != s, merge do nothing;
         *           g != h, g == s, merge do nothing;
         *           g != h, g != s, h != s, goto CONFLICT;              5
         *           g != h, h == s, stage g for addition.               6
         *
         * for the rest files in the split point:
         *      if this files in the head:
         *           if they are modified, goto CONFLICT;                7
         *           if they are unmodified, stage them for removal;     8
         *      if this files not in the head, merge do nothing;
         */
        for (Map.Entry<String, String> otherFile : otherCopy.entrySet()){
            String filename = otherFile.getKey();
            String OT = otherFile.getValue();
            String HD = headCopy.remove(filename);
            String SP = splitCopy.remove(filename);
            if (SP == null) {
                if (HD == null) {
                    if (allFiles.contains(filename)) {
                        message("There is an untracked file in the way; delete it, or add and commit it first.");             // 1
                        return;
                    } else {                                                                                                       // 2
                        Maddition.put(filename, OT);
                        writeContents(getFileinCWD(filename), (Object) getContentinObjects(OT));
                    }
                } else {
                    if (!OT.equals(HD)) {
                        confilct(Maddition, filename, HD, OT);                                                                     // 3
                    }
                }
            } else {
                if (HD == null) {
                    if (!OT.equals(SP)){
                        confilct(Maddition, filename, HD, OT);                                                                // 4
                    }
                } else {
                    if (!OT.equals(HD)) {
                        if (HD.equals(SP)) {                                                                                        // 6
                            Maddition.put(filename, OT);
                            writeContents(getFileinCWD(filename), (Object) getContentinObjects(OT));
                        } else {
                            if (!OT.equals(SP)) {
                                confilct(Maddition, filename, HD, OT);                                                                  // 5
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, String> splitFiles : splitCopy.entrySet()) {
            String filename = splitFiles.getKey();
            String SP = splitFiles.getValue();
            String HD = headCopy.remove(filename);
            if (SP.equals(HD)) {                                                                                                    // 8
                deletion.add(filename);
                restrictedDelete(filename);
            } else if (HD != null) {
                confilct(Maddition, filename, HD, null);                                                                        // 7
            }
        }

        if (Maddition.isEmpty() && deletion.isEmpty()) {
            message("No changes added to the commit.");
            return;
        }

        //Commit mergeCommit = new Commit(head.UID, other.UID, String.format("Merged %s into %s.", branch, head.branch), new Date(), Maddition);
        head.moveTo(new Commit(head.UID, other.UID, String.format("Merged %s into %s.", branch, head.branch), new Date(), Maddition));
        branches.put(head.branch, head.UID);
        writeObject(branches_file, branches);
    }

    private static void confilct(HashMap<String, String> Maddition, String filename, String HD, String OT) {
        message("Encountered a merge conflict.");
        String HDcontent = "";
        String OTcontent = "";
        if (HD != null && getFileinObjects(HD).exists()) HDcontent = readContentsAsString(getFileinObjects(HD));
        if (OT != null && getFileinObjects(OT).exists()) OTcontent = readContentsAsString(getFileinObjects(OT));
        String content = String.format("<<<<<<< HEAD\n%s=======\n%s>>>>>>>\n", HDcontent, OTcontent);
        writeContents(getFileinCWD(filename), content);
        String fileUID = sha1(content);
        writeContents(getFileinObjects(fileUID), content);
        Maddition.put(filename, fileUID);
    }

    private static Commit latestCommonAncestor(Commit a, Commit b) {
        Set<String> cache = new HashSet<>();
        TreeSet<Commit> ancestors = new TreeSet<>();
        lcaHelper(cache, ancestors, a, b);
        return ancestors.last();
    }

    @SuppressWarnings("unchecked")
    private static void lcaHelper(Set cache, Set ancs, Commit a, Commit b) {
        if (a.UID.equals(b.UID)) {
            ancs.add(a);
            return;
        }
        if (!cache.add(sha1(a.UID, b.UID))) return;
        if (a.timestamp.after(b.timestamp)) {
            Commit aFirstPar = a.parent();
            lcaHelper(cache, ancs, aFirstPar, b);
            if (a._parentUID != null) {
                Commit aSecondPar = a.secParent();
                lcaHelper(cache, ancs, aSecondPar, b);
            }
        } else {
            Commit bFirstPar = b.parent();
            lcaHelper(cache, ancs, a, bFirstPar);
            if (b._parentUID != null) {
                Commit bSecondPar = b.secParent();
                lcaHelper(cache, ancs, a, bSecondPar);
            }
        }
    }

    public static void addRemote(String remote, String address){
        if (remotes.containsKey(remote)) {
            message("A remote with that name already exists.");
            return;
        }
        address = address.replace("/",File.separator);
        remotes.put(remote, address);
        writeObject(remotes_file, remotes);
    }

    public static void rmRemote(String remote){
        String address = remotes.remove(remote);
        if (address == null) {
            message("A remote with that name does not exist.");
        }
        writeObject(remotes_file, remotes);
    }

    public static void push(String remote, String branch){
        String address = remotes.get(remote);
        File remoteDir = join(address);
        if (!remoteDir.exists()) {
            message("Remote directory not found.");
            return;
        }

        Commit branchHD = getBranch(branch);
        if (branchHD == null) return;

        File rBranches_file = join(remoteDir,  "branches");
        @SuppressWarnings("unchecked") TreeMap<String, String > rBranches = readObject(rBranches_file, TreeMap.class);
        File rObejects = join(remoteDir, "objects");
        File rCommits = join(remoteDir, "commits");

        String rBranchHead = rBranches.get(branch);
        if (rBranchHead == null) {
            copyCommitToRm(branchHD, rCommits, rObejects);
            rBranches.put(branch, branchHD.UID);
            writeObject(rBranches_file, rBranches);
            return;
        }
        boolean isancestor = false;
        String ptr = branchHD.UID;
        while(true){
            if (ptr.equals(rBranchHead) ) {
                isancestor = true;
                break;
            }
            Commit a = Commit.getCommit(ptr);
            if (a.parentUID == null) {
                break;
            } else {
                ptr = a.parent().UID;
            }
        }
        if (!isancestor) {
            message("Please pull down remote changes before pushing.");
            return;
        }
        String pptr = branchHD.UID;
        while (!pptr.equals(ptr)) {
            Commit c = Commit.getCommit(pptr);
            copyCommitToRm(c, rCommits, rObejects);
            pptr = c.parent().UID;
        }
        rBranches.put(branch, branchHD.UID);
        writeObject(rBranches_file, rBranches);

        File rHeadfile = join(remoteDir, "head");
        Head rHead = readObject(rHeadfile, Head.class);
        if (rHead.branch.equals(branch)) {
            rHead.moveTo(branchHD);
        }
    }

    private static void copyCommitToRm(Commit c, File rCommits, File rObjects) {
        File x = join(rCommits, c.UID);
        writeObject(x, c);
        for (Map.Entry<String, String> file : c.entries()) {
            String fileUID = file.getValue();
            File blob = join(rObjects, fileUID);
            writeContents(blob, (Object) getContentinObjects(fileUID));
        }
    }

    private static void copyCommitFrRm(Commit c, File rCommits, File rObjects) {
        File b = join(commits, c.UID);
        writeObject(b, c);
        for (Map.Entry<String, String> file : c.entries()) {
            String fileUID = file.getValue();
            File blob = join(objects, fileUID);
            writeContents(blob, (Object) readContents(join(rObjects, fileUID)));
        }
    }

    public static void fetch(String remote, String branch){
        String address = remotes.get(remote);
        File remoteDir = join(address);
        if (!remoteDir.exists()) {
            message("Remote directory not found.");
            return;
        }
        File rBranches_file = join(remoteDir,  "branches");

        @SuppressWarnings("unchecked") TreeMap<String, String > rBranches = readObject(rBranches_file, TreeMap.class);
        String rBranchHead = rBranches.get(branch);

        File rObejects = join(remoteDir, "objects");
        File rCommits = join(remoteDir, "commits");
        if (rBranchHead == null) {
            message("That remote does not have that branch.");
            return;
        }
        Commit c = readObject(join(rCommits, rBranchHead), Commit.class);
        copyCommitFrRm(c, rCommits, rObejects);
        branches.put(remote + "/" + branch, rBranchHead);
        writeObject(branches_file, branches);
    }

    public static void pull(String remote, String branch){
        fetch(remote, branch);
        merge(remote + "/" + branch);
    }

    public static File getFileinCWD(String filename) {
        return join(CWD, filename);
    }

    public static File getFileinObjects(String UID) {
        return join(objects, UID);
    }

    public static byte[] getContentinObjects(String UID) {
        if (UID == null) return null;
        File file = getFileinObjects(UID);
        if (!file.exists()) return null;
        return readContents(file);
    }

    public static Commit getBranch(String branch) {
        String UID = branches.get(branch);
        if (UID != null) {
            return Commit.getCommit(UID);
        } else {
            message("A branch with that name does not exist.");
            return null;
        }
    }

    public static boolean exists() {
        return GITLET_DIR.exists();
    }

}
