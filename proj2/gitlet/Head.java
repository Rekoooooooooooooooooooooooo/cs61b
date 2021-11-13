package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

//import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Head implements Serializable{
    public String UID;
    public String branch;
    public transient Commit headCommit;
    public File file;

    Head() {
        headCommit = new Commit();
        UID = headCommit.UID;
        branch = "master";
        file = Repository.head_file;
        save();
    }

    public Commit commit()  {
        if (headCommit == null) {
            headCommit = Commit.getCommit(UID);
        }
        return headCommit;
    }

    public void moveTo(Commit next) {
        UID = next.UID;
        headCommit = next;
        save();
    }

    public void moveTo(String nextUID) {
        UID = nextUID;
        headCommit = Commit.getCommit(nextUID);
        save();
    }

    public TreeMap<String, String> metadata() {
        if (headCommit == null) return commit().metadata;
        return headCommit.metadata;
    }

    public void save() {
        writeObject(file, this);
    }
}
