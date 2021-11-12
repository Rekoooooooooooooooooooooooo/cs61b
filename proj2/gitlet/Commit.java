package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;
import static gitlet.Repository.*;
public class Commit implements Serializable, Comparable<Commit>{

    public String parentUID; //the first parent
    public String _parentUID; //the second parent, only caused by merge
    public String message;
    public Date timestamp;
    public TreeMap<String, String> metadata; //<pathname, sha1(parentUID, readcontent(file))>
    public String UID;


    Commit() {
        this.parentUID = null;
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.metadata = new TreeMap<>();
        save();
    }

    Commit(String parentUID, String message, Date timestamp) {
        if (message == null) {
            Utils.message("Please enter a commit message.");
            return;
        }
        this.parentUID = parentUID;
        this.message = message;
        this.timestamp = timestamp;
        getParentMetadata(parentUID);
        checkAddition();
        checkDeletion();
        save();
    }
    @SuppressWarnings("unchecked")
    Commit(String first, String second, String message, Date timestamp, Map Maddition) {
        this.parentUID = first;
        this._parentUID = second;
        this.message = message;
        this.timestamp = timestamp;
        getParentMetadata(first);
        this.metadata.putAll(Maddition);
        checkDeletion();
        save();
    }


    private void getParentMetadata(String parentUID) {
        Commit parent = getCommit(parentUID);
        assert parent != null;
        metadata = new TreeMap<>();
        metadata.putAll(parent.metadata);
    }

    private void checkAddition() {
        for (Map.Entry<String, Map.Entry<String, byte[]>> toadd: addition.entrySet()) {
            String filename = toadd.getKey();
            byte[] content = toadd.getValue().getValue();
            String fileUID = toadd.getValue().getKey();
            metadata.put(filename, fileUID);
            //load file to objects
            File file = getFileinObjects(fileUID);
            writeContents(file, content);
        }
        addition.clear();
        writeObject(addition_file, addition);
    }

    private void checkDeletion() {
        if (!deletion.isEmpty()) {
            for (String todele: deletion) {
                metadata.remove(todele);
            }
            deletion.clear();
            writeObject(deletion_file, deletion);
        }
    }

    public static Commit getCommit(String UID) {
        File commitFile= join(commits, UID);
        if (UID.length() < 40) {
            String res = commitUIDs.ceiling(UID);
            String anotherRes = commitUIDs.ceiling(res);
            if (res.contains(UID)) {
                if (anotherRes.equals(res)) {
                    commitFile = join(join(commits, res));
                } else {
                    if (!anotherRes.contains(UID)) {
                        commitFile = join(join(commits, res));
                    }
                }
            }
        }
        if (!commitFile.exists()) {
            Utils.message("No commit with that id exists.");
            return null;
        }
        return readObject(commitFile, Commit.class);
    }

    public Commit parent() {
        if (this.parentUID == null) return null;
        return getCommit(this.parentUID);
    }


    public Commit secParent() {
        if (this._parentUID == null) return null;
        return getCommit(this._parentUID);
    }

    public String getFileUID(String filename) {
        String ans = metadata.get(filename);
        if (ans == null) {
            Utils.message("File does not exist in that commit.");
        }
        return ans;
    }

    public Set<String> files() {
        return this.metadata.keySet();
    }

    public Set<Map.Entry<String, String>> entries() {
        return this.metadata.entrySet();
    }

    private void save() {
        this.UID = sha1(serialize(this));
        commitUIDs.add(this.UID);
        writeObject(UIDs_file, commitUIDs);
        File thisFile = join(commits, this.UID);
        writeObject(thisFile, this);
    }

    public int compareTo(Commit o) {
        return this.timestamp.compareTo(o.timestamp);
    }

}
