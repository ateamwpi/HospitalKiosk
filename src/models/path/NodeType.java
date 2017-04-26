package models.path;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 4/26/2017.
 *
 */
public enum NodeType {
    Location(Color.ORANGE),
    Staircase(Color.DARKBLUE),
    Elevator(Color.DARKBLUE),
    Hallway(Color.GRAY),
    Outside(Color.GRAY),
    Unknown(Color.GRAY);

    private Color nodeColor;
    private static final HashMap<String, NodeType> names = new HashMap<>();

    NodeType(Color nodeColor) {
        this.nodeColor = nodeColor;
    }

    static {
        for (NodeType l : NodeType.values()) {
            names.put(l.name().toUpperCase(), l);
        }
    }

    public Color getNodeColor() {
        return nodeColor;
    }

//    public boolean isInternal() {
//        return equals(Hallway) || equals(Unknown);
//    }

//    public boolean hasNearest() {
//        return equals(Staircase) || equals(Elevator);
//    }

    public static NodeType getType(String s) {
        return names.getOrDefault(s.toUpperCase(), Unknown);
    }

//    /**
//     *
//     * @return The LocationTypes that a user can set a location to through the admin interface
//     */
//    public static NodeType[] userValues() {
//        ArrayList<NodeType> values = new ArrayList<>();
//        for(NodeType locType : values()) {
//            if(!locType.isInternal()) {
//                values.add(locType);
//            }
//        }
//        return values.toArray(new NodeType[values.size()]);
//    }
}
