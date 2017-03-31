package models.dir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectoryManager {

    private HashMap<LocationType, Directory> directories;

    public DirectoryManager(HashMap<LocationType, Directory> allLocations) {
        this.directories = allLocations;
    }

}
